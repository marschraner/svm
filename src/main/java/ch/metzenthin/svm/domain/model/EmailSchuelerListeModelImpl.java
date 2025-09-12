package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import ch.metzenthin.svm.common.datatypes.Elternmithilfe;
import ch.metzenthin.svm.common.datatypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.MaercheneinteilungenSorter;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import java.util.*;

/**
 * @author Martin Schraner
 */
public class EmailSchuelerListeModelImpl extends AbstractModel implements EmailSchuelerListeModel {

  private final MaercheneinteilungenSorter maercheneinteilungenSorter =
      new MaercheneinteilungenSorter();
  private EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe;
  private boolean blindkopien;
  private Set<String> fehlendeEmailAdressen;
  private Set<String> ungueltigeEmailAdressen;

  @Override
  public EmailSchuelerListeEmpfaengerGruppe getEmailSchuelerListeEmpfaengerGruppe() {
    return emailSchuelerListeEmpfaengerGruppe;
  }

  @Override
  public void setEmailSchuelerListeEmpfaengerGruppe(
      EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe) {
    EmailSchuelerListeEmpfaengerGruppe oldValue = this.emailSchuelerListeEmpfaengerGruppe;
    this.emailSchuelerListeEmpfaengerGruppe = emailSchuelerListeEmpfaengerGruppe;
    firePropertyChange(
        Field.EMAIL_SCHUELER_LISTE_EMPFAENGER_GRUPPE,
        oldValue,
        this.emailSchuelerListeEmpfaengerGruppe);
  }

  @Override
  public boolean isBlindkopien() {
    return blindkopien;
  }

  @Override
  public void setBlindkopien(boolean isSelected) {
    boolean oldValue = blindkopien;
    blindkopien = isSelected;
    firePropertyChange(Field.BLINDKOPIEN, oldValue, blindkopien);
  }

  @Override
  public EmailSchuelerListeEmpfaengerGruppe[] getSelectableEmailSchuelerListeEmpfaengerGruppen(
      SchuelerSuchenTableModel schuelerSuchenTableModel) {
    List<EmailSchuelerListeEmpfaengerGruppe> selectableSchuelerListeEmpfaengerGruppe =
        new ArrayList<>();
    for (EmailSchuelerListeEmpfaengerGruppe gruppe : EmailSchuelerListeEmpfaengerGruppe.values()) {
      // Keine Rollenliste oder Eltermithilfe, falls kein Märchen
      if (schuelerSuchenTableModel.getMaerchen() == null
          && (gruppe == EmailSchuelerListeEmpfaengerGruppe.ROLLENLISTE
              || gruppe == EmailSchuelerListeEmpfaengerGruppe.ELTERNMITHILFE)) {
        continue;
      }
      selectableSchuelerListeEmpfaengerGruppe.add(gruppe);
    }
    return selectableSchuelerListeEmpfaengerGruppe.toArray(
        new EmailSchuelerListeEmpfaengerGruppe[0]);
  }

  @SuppressWarnings({"DuplicatedCode", "java:S3776", "java:S6541"})
  @Override
  public CallDefaultEmailClientCommand.Result callEmailClient(
      SchuelerSuchenTableModel schuelerSuchenTableModel) {

    Set<String> emailAdressen = new LinkedHashSet<>();
    fehlendeEmailAdressen = new LinkedHashSet<>();

    switch (emailSchuelerListeEmpfaengerGruppe) {
      case MUTTER_UND_ODER_VATER -> {
        // Wenn vorhanden E-Mail(s) von Mutter und/oder Vater, sonst des Schülers
        for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
          // Mutter und/oder Vater
          Set<String> emailAdressenForSchueler = new LinkedHashSet<>();
          addEmailOfMutterUndOderVater(schueler, emailAdressenForSchueler);

          // Schüler
          if (emailAdressenForSchueler.isEmpty() && checkNotEmpty(schueler.getEmail())) {
            emailAdressenForSchueler.add(schueler.getEmail());
          }

          if (!emailAdressenForSchueler.isEmpty()) {
            emailAdressen.addAll(emailAdressenForSchueler);
          } else {
            fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
          }
        }
      }
      case SCHUELER -> {
        // Wenn vorhanden E-Mail des Schülers, sonst Mutter und/oder Vater
        for (Schueler schueler : schuelerSuchenTableModel.getSelektierteSchuelerList()) {
          // Schüler
          Set<String> emailAdressenForSchueler = new LinkedHashSet<>();
          if (checkNotEmpty(schueler.getEmail())) {
            emailAdressenForSchueler.add(schueler.getEmail());
          }

          // Mutter und/oder Vater
          if (emailAdressenForSchueler.isEmpty()) {
            addEmailOfMutterUndOderVater(schueler, emailAdressenForSchueler);
          }

          if (!emailAdressenForSchueler.isEmpty()) {
            emailAdressen.addAll(emailAdressenForSchueler);
          } else {
            fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
          }
        }
      }
      case ROLLENLISTE -> {
        // Wenn vorhanden E-Mail des Schülers, sonst der Elternmithilfe (falls nicht Dritt-
        // person), sonst der Mutter, sonst des Vaters, sonst Elternmithilfe Drittperson
        Map<Schueler, Maercheneinteilung> maercheneinteilungen =
            schuelerSuchenTableModel.getMaercheneinteilungen();

        // Wenn nach Rollen gesucht wurde, muss nach Rollen sortiert werden, sonst nach Schülern
        Set<Schueler> keys;
        if (schuelerSuchenTableModel.isNachRollenGesucht()) {
          // Wenn nach Rollen gesucht wurde, gibt es keine Keys mit leeren Values
          maercheneinteilungen =
              maercheneinteilungenSorter.sortMaercheneinteilungenByGruppeAndRolle(
                  maercheneinteilungen);
          keys = maercheneinteilungen.keySet();
        } else {
          // Sortierung nach Keys
          keys = new TreeSet<>(maercheneinteilungen.keySet());
        }

        for (Schueler schueler : keys) {
          Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
          if (maercheneinteilung == null || !schueler.isSelektiert()) {
            continue;
          }
          String email = null;
          Angehoeriger elternmithilfe = null;
          if (maercheneinteilung.getElternmithilfe() != null
              && maercheneinteilung.getElternmithilfe() != Elternmithilfe.DRITTPERSON) {
            elternmithilfe =
                (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER
                    ? schueler.getMutter()
                    : schueler.getVater());
          }
          if (checkNotEmpty(schueler.getEmail())) {
            email = schueler.getEmail();
          } else if (elternmithilfe != null && checkNotEmpty(elternmithilfe.getEmail())) {
            email = elternmithilfe.getEmail();
          } else if (schueler.getMutter() != null
              && checkNotEmpty(schueler.getMutter().getEmail())) {
            email = schueler.getMutter().getEmail();
          } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
            email = schueler.getVater().getEmail();
          } else if (maercheneinteilung.getElternmithilfeDrittperson() != null
              && checkNotEmpty(maercheneinteilung.getElternmithilfeDrittperson().getEmail())) {
            email = maercheneinteilung.getElternmithilfeDrittperson().getEmail();
          } else {
            fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
          }
          if (email != null) {
            emailAdressen.add(email);
          }
        }
      }
      case ELTERNMITHILFE -> {
        Map<Schueler, Maercheneinteilung> maercheneinteilungenElternmithilfe =
            schuelerSuchenTableModel.getMaercheneinteilungen();
        List<Person> elternmithilfen = new ArrayList<>();
        for (Schueler schueler : schuelerSuchenTableModel.getMaercheneinteilungen().keySet()) {
          Maercheneinteilung maercheneinteilung = maercheneinteilungenElternmithilfe.get(schueler);
          if (maercheneinteilung == null || !schueler.isSelektiert()) {
            continue;
          }
          Person elternmithilfe;
          if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER) {
            elternmithilfe = schueler.getMutter();
          } else if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.VATER) {
            elternmithilfe = schueler.getVater();
          } else {
            elternmithilfe = maercheneinteilung.getElternmithilfeDrittperson();
          }
          // Falls Elternteil nach Erfassen der Elternmithilfe gelöscht wurde, kann Elternmithilfe
          // null sein.
          if (elternmithilfe != null) {
            elternmithilfen.add(elternmithilfe);
          }
        }
        Collections.sort(elternmithilfen);
        for (Person elternmithilfe : elternmithilfen) {
          String email = null;
          if (checkNotEmpty(elternmithilfe.getEmail())) {
            email = elternmithilfe.getEmail();
          } else {
            fehlendeEmailAdressen.add(
                elternmithilfe.getNachname() + " " + elternmithilfe.getVorname());
          }
          if (email != null) {
            emailAdressen.add(email);
          }
        }
      }
    }

    CommandInvoker commandInvoker = getCommandInvoker();
    CallDefaultEmailClientCommand callDefaultEmailClientCommand =
        new CallDefaultEmailClientCommand(emailAdressen, blindkopien);
    commandInvoker.executeCommand(callDefaultEmailClientCommand);
    ungueltigeEmailAdressen = callDefaultEmailClientCommand.getUngueltigeEmailAdressen();

    return callDefaultEmailClientCommand.getResult();
  }

  @SuppressWarnings("DuplicatedCode")
  private void addEmailOfMutterUndOderVater(
      Schueler schueler, Set<String> emailAdressenForSchueler) {

    // Mutter und/oder Vater mit wuenschtEmails selektiert
    if (schueler.getMutter() != null
        && checkNotEmpty(schueler.getMutter().getEmail())
        && (schueler.getMutter().getWuenschtEmails() != null
            && schueler.getMutter().getWuenschtEmails())) {
      emailAdressenForSchueler.add(schueler.getMutter().getEmail());
    }
    if (schueler.getVater() != null
        && checkNotEmpty(schueler.getVater().getEmail())
        && (schueler.getVater().getWuenschtEmails() != null
            && schueler.getVater().getWuenschtEmails())) {
      emailAdressenForSchueler.add(schueler.getVater().getEmail());
    }

    // Falls keine gefunden wuenschtEmails ignorieren (sollte nicht auftreten)
    if (emailAdressenForSchueler.isEmpty()) {
      if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
        emailAdressenForSchueler.add(schueler.getMutter().getEmail());
      } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
        emailAdressenForSchueler.add(schueler.getVater().getEmail());
      }
    }
  }

  @Override
  public Set<String> getFehlendeEmailAdressen() {
    return fehlendeEmailAdressen;
  }

  @Override
  public Set<String> getUngueltigeEmailAdressen() {
    return ungueltigeEmailAdressen;
  }

  @Override
  public boolean isCompleted() {
    return true;
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }
}
