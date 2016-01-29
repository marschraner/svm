package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.utils.StringNumberComparator;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.util.*;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class EmailSchuelerListeModelImpl extends AbstractModel implements EmailSchuelerListeModel {

    private EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe;
    private boolean blindkopien;
    private List<String> fehlendeEmailAdressen;
    private List<String> ungueltigeEmailAdressen;

    public EmailSchuelerListeModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public EmailSchuelerListeEmpfaengerGruppe getEmailSchuelerListeEmpfaengerGruppe() {
        return emailSchuelerListeEmpfaengerGruppe;
    }

    @Override
    public void setEmailSchuelerListeEmpfaengerGruppe(EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe) {
        EmailSchuelerListeEmpfaengerGruppe oldValue = this.emailSchuelerListeEmpfaengerGruppe;
        this.emailSchuelerListeEmpfaengerGruppe = emailSchuelerListeEmpfaengerGruppe;
        firePropertyChange(Field.EMAIL_SCHUELER_LISTE_EMPFAENGER_GRUPPE, oldValue, this.emailSchuelerListeEmpfaengerGruppe);
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
    public EmailSchuelerListeEmpfaengerGruppe[] getSelectableEmailSchuelerListeEmpfaengerGruppen(SchuelerSuchenTableModel schuelerSuchenTableModel) {
        List<EmailSchuelerListeEmpfaengerGruppe> selectableSchuelerListeEmpfaengerGruppe = new ArrayList<>();
        for (EmailSchuelerListeEmpfaengerGruppe gruppe : EmailSchuelerListeEmpfaengerGruppe.values()) {
            // Keine Rollenliste oder Eltermithilfe, falls kein Märchen
            if (schuelerSuchenTableModel.getMaerchen() == null &&
                    (gruppe == EmailSchuelerListeEmpfaengerGruppe.ROLLENLISTE || gruppe == EmailSchuelerListeEmpfaengerGruppe.ELTERNMITHILFE)) {
                continue;
            }
            selectableSchuelerListeEmpfaengerGruppe.add(gruppe);
        }
        return selectableSchuelerListeEmpfaengerGruppe.toArray(new EmailSchuelerListeEmpfaengerGruppe[selectableSchuelerListeEmpfaengerGruppe.size()]);
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient(SchuelerSuchenTableModel schuelerSuchenTableModel) {

        List<String> emailAdressen = new ArrayList<>();
        fehlendeEmailAdressen = new ArrayList<>();

        switch (emailSchuelerListeEmpfaengerGruppe) {

            case MUTTER_ODER_VATER:
                // Wenn vorhanden Email der Mutter, sonst des Vaters, sonst des Rechnungsempfängers
                for (Schueler schueler : schuelerSuchenTableModel.getZuExportierendeSchuelerList()) {
                    String email = null;
                    if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        email = schueler.getMutter().getEmail();
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        email = schueler.getVater().getEmail();
                    } else if (checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
                        email = schueler.getRechnungsempfaenger().getEmail();
                    } else {
                        fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
                    }
                    if (email != null) {
                        emailAdressen.add(email);
                    }
                }
                break;

            case SCHUELER:
                // Wenn vorhanden Email des Schülers, sonst der Mutter, sonst des Vaters, sonst des Rechnungsempfängers
                for (Schueler schueler : schuelerSuchenTableModel.getZuExportierendeSchuelerList()) {
                    String email = null;
                    if (checkNotEmpty(schueler.getEmail())) {
                        email = schueler.getEmail();
                    } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        email = schueler.getMutter().getEmail();
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        email = schueler.getVater().getEmail();
                    } else if (checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
                        email = schueler.getRechnungsempfaenger().getEmail();
                    } else {
                        fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
                    }
                    if (email != null) {
                        emailAdressen.add(email);
                    }
                }
                break;

            case ROLLENLISTE:
                // Wenn vorhanden Email des Schülers, sonst der Elternmithilfe (falls nicht Drittperson), sonst der Mutter, sonst des Vaters, sonst Elternmithilfe Drittperson
                Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();
                // Wenn nach Rollen gesucht wurde, muss nach Rollen sortiert werden, sonst nach Schülern
                Set<Schueler> keys;
                if (schuelerSuchenTableModel.isNachRollenGesucht()) {
                    // Wenn nach Rollen gesucht wurde, gibt es keine Keys mit leeren Values
                    maercheneinteilungen = sortMaercheneinteilungenByRollen(maercheneinteilungen);
                    keys = maercheneinteilungen.keySet();
                } else {
                    // Sortierung nach Keys
                    keys = new TreeSet<>(maercheneinteilungen.keySet());
                }
                for (Schueler schueler : keys) {
                    Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
                    if (maercheneinteilung == null || !schueler.isZuExportieren()) {
                        continue;
                    }
                    String email = null;
                    Angehoeriger elternmithilfe = null;
                    if (maercheneinteilung.getElternmithilfe() != null && maercheneinteilung.getElternmithilfe() != Elternmithilfe.DRITTPERSON) {
                        elternmithilfe = (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER ? schueler.getMutter() : schueler.getVater());
                    }
                    if (checkNotEmpty(schueler.getEmail())) {
                        email = schueler.getEmail();
                    } else if (elternmithilfe != null && checkNotEmpty(elternmithilfe.getEmail())) {
                        email = elternmithilfe.getEmail();
                    } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        email = schueler.getMutter().getEmail();
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        email = schueler.getVater().getEmail();
                    } else if (maercheneinteilung.getElternmithilfeDrittperson() != null && checkNotEmpty(maercheneinteilung.getElternmithilfeDrittperson().getEmail())) {
                        email = maercheneinteilung.getElternmithilfeDrittperson().getEmail();
                    } else {
                        fehlendeEmailAdressen.add(schueler.getNachname() + " " + schueler.getVorname());
                    }
                    if (email != null) {
                        emailAdressen.add(email);
                    }
                }

                break;

            case ELTERNMITHILFE:
                Map<Schueler, Maercheneinteilung> maercheneinteilungenElternmithilfe = schuelerSuchenTableModel.getMaercheneinteilungen();
                List<Person> elternmithilfen = new ArrayList<>();
                for (Schueler schueler : schuelerSuchenTableModel.getMaercheneinteilungen().keySet()) {
                    Maercheneinteilung maercheneinteilung = maercheneinteilungenElternmithilfe.get(schueler);
                    if (maercheneinteilung == null || !schueler.isZuExportieren()) {
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
                    // Falls Elternteil nach Erfassen der Eltern-Mithilfe gelöscht wurde, kann Elternmithilfe null sein.
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
                        fehlendeEmailAdressen.add(elternmithilfe.getNachname() + " " + elternmithilfe.getVorname());
                    }
                    if (email != null) {
                        emailAdressen.add(email);
                    }
                }
                break;
        }

        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdressen, blindkopien);
        commandInvoker.executeCommand(callDefaultEmailClientCommand);
        ungueltigeEmailAdressen = callDefaultEmailClientCommand.getUngueltigeEmailAdressen();

        return callDefaultEmailClientCommand.getResult();
    }

    private Map<Schueler, Maercheneinteilung> sortMaercheneinteilungenByRollen(Map<Schueler, Maercheneinteilung> maercheneinteilungen) {
        List<Map.Entry<Schueler, Maercheneinteilung>> list = new LinkedList<>(maercheneinteilungen.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Schueler, Maercheneinteilung>>() {
            @Override
            public int compare(Map.Entry<Schueler, Maercheneinteilung> o1, Map.Entry<Schueler, Maercheneinteilung> o2) {
                Comparator<String> stringNumberComparator = new StringNumberComparator();
                return stringNumberComparator.compare(o1.getValue().getRolle1(), o2.getValue().getRolle1());
            }
        });

        Map<Schueler, Maercheneinteilung> result = new LinkedHashMap<>();
        for (Map.Entry<Schueler, Maercheneinteilung> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public List<String> getFehlendeEmailAdressen() {
        return fehlendeEmailAdressen;
    }

    @Override
    public List<String> getUngueltigeEmailAdressen() {
        return ungueltigeEmailAdressen;
    }

    @Override
    public void initializeCompleted() {}

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {}
}
