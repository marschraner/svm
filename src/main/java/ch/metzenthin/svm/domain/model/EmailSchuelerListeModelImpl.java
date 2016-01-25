package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.EmailSchuelerListeEmpfaengerGruppe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class EmailSchuelerListeModelImpl extends AbstractModel implements EmailSchuelerListeModel {

    private EmailSchuelerListeEmpfaengerGruppe emailSchuelerListeEmpfaengerGruppe;
    private boolean blindkopien;

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

        StringBuilder emailAdressenSb = new StringBuilder();

        switch (emailSchuelerListeEmpfaengerGruppe) {

            case MUTTER_ODER_VATER:
                // Wenn vorhanden Email der Mutter, sonst des Vaters, sonst des Rechnungsempfängers
                for (Schueler schueler : schuelerSuchenTableModel.getSchuelerList()) {
                    String email = null;
                    if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        email = schueler.getMutter().getEmail();
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        email = schueler.getVater().getEmail();
                    } else if (checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
                        email = schueler.getRechnungsempfaenger().getEmail();
                    }
                    if (email != null) {
                        emailAdressenSb.append(email);
                        emailAdressenSb.append(";");
                    }
                }
                break;

            case SCHUELER:
                // Wenn vorhanden Email des Schülers, sonst der Mutter, sonst des Vaters, sonst des Rechnungsempfängers
                for (Schueler schueler : schuelerSuchenTableModel.getSchuelerList()) {
                    String email = null;
                    if (checkNotEmpty(schueler.getEmail())) {
                        email = schueler.getEmail();
                    } else if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
                        email = schueler.getMutter().getEmail();
                    } else if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
                        email = schueler.getVater().getEmail();
                    } else if (checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
                        email = schueler.getRechnungsempfaenger().getEmail();
                    }
                    if (email != null) {
                        emailAdressenSb.append(email);
                        emailAdressenSb.append(";");
                    }
                }
                break;

            case ROLLENLISTE:
                // Wenn vorhanden Email des Schülers, sonst der Elternmithilfe (falls nicht Drittperson), sonst der Mutter, sonst des Vaters, sonst Elternmithilfe Drittperson
                Map<Schueler, Maercheneinteilung> maercheneinteilungen = schuelerSuchenTableModel.getMaercheneinteilungen();
                for (Schueler schueler : schuelerSuchenTableModel.getMaercheneinteilungen().keySet()) {
                    Maercheneinteilung maercheneinteilung = maercheneinteilungen.get(schueler);
                    if (maercheneinteilung == null) {
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
                    }
                    if (email != null) {
                        emailAdressenSb.append(email);
                        emailAdressenSb.append(";");
                    }
                }

                break;

            case ELTERNMITHILFE:
                Map<Schueler, Maercheneinteilung> maercheneinteilungenElternmithilfe = schuelerSuchenTableModel.getMaercheneinteilungen();
                for (Schueler schueler : schuelerSuchenTableModel.getMaercheneinteilungen().keySet()) {
                    Maercheneinteilung maercheneinteilung = maercheneinteilungenElternmithilfe.get(schueler);
                    if (maercheneinteilung == null) {
                        continue;
                    }
                    String email = null;
                    Person elternmithilfe;
                    if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.MUTTER) {
                        elternmithilfe = schueler.getMutter();
                    } else if (maercheneinteilung.getElternmithilfe() == Elternmithilfe.VATER) {
                        elternmithilfe = schueler.getVater();
                    } else {
                        elternmithilfe = maercheneinteilung.getElternmithilfeDrittperson();
                    }
                    // Falls Elternteil nach Erfassen der Eltern-Mithilfe gelöscht wurde, kann Elternmithilfe null sein.
                    if (elternmithilfe != null && checkNotEmpty(elternmithilfe.getEmail())) {
                        email = elternmithilfe.getEmail();
                    }
                    if (email != null) {
                        emailAdressenSb.append(email);
                        emailAdressenSb.append(";");
                    }
                }
                break;
        }

        // Letzten ; löschen
        emailAdressenSb.setLength(emailAdressenSb.length() - 1);

        if (emailAdressenSb.length() == 0) {
            return CallDefaultEmailClientCommand.Result.EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN;
        }

        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdressenSb.toString(), blindkopien);
        commandInvoker.executeCommand(callDefaultEmailClientCommand);
        return callDefaultEmailClientCommand.getResult();
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
