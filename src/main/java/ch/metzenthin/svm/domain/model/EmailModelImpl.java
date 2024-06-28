package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.EmailEmpfaenger;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class EmailModelImpl extends AbstractModel implements EmailModel {

    private EmailEmpfaenger emailEmpfaenger;

    @Override
    public EmailEmpfaenger getEmailEmpfaenger() {
        return emailEmpfaenger;
    }

    @Override
    public void setEmailEmpfaenger(EmailEmpfaenger emailEmpfaenger) {
        EmailEmpfaenger oldValue = this.emailEmpfaenger;
        this.emailEmpfaenger = emailEmpfaenger;
        firePropertyChange(Field.EMAIL_EMPFAENGER, oldValue, this.emailEmpfaenger);
    }

    @Override
    public EmailEmpfaenger[] getSelectableEmailEmpfaengers(SchuelerDatenblattModel schuelerDatenblattModel) {
        List<EmailEmpfaenger> selectableEmailEmpfaengerList = new ArrayList<>();
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.MUTTER);
        }
        if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())
                && schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.MUTTER_UND_VATER);
        }
        if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.VATER);
        }
        if (!(schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger()))
                && !(schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger()))
                && checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.RECHNUNGSEMPFAENGER);
        }
        if (checkNotEmpty(schueler.getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.SCHUELER);
        }
        return selectableEmailEmpfaengerList.toArray(new EmailEmpfaenger[0]);
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient(SchuelerDatenblattModel schuelerDatenblattModel) {

        Schueler schueler = schuelerDatenblattModel.getSchueler();
        String emailAdresse;

        // Separator für mehrere E-Mails
        Properties svmProperties = SvmProperties.getSvmProperties();
        String emailClientMultipleMailsSeparator = svmProperties.getProperty(
                SvmProperties.KEY_EMAIL_CLIENT_MULTIPLE_MAILS_SEPARATOR);
        if (emailClientMultipleMailsSeparator.isEmpty()) {
            emailClientMultipleMailsSeparator = ";";
        }

        emailAdresse = switch (emailEmpfaenger) {
            case MUTTER -> schueler.getMutter().getEmail();
            case MUTTER_UND_VATER -> schueler.getMutter().getEmail() + emailClientMultipleMailsSeparator +
                    schueler.getVater().getEmail();
            case VATER -> schueler.getVater().getEmail();
            case RECHNUNGSEMPFAENGER -> schueler.getRechnungsempfaenger().getEmail();
            case SCHUELER -> schueler.getEmail();
        };

        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdresse, false);
        commandInvoker.executeCommand(callDefaultEmailClientCommand);
        return callDefaultEmailClientCommand.getResult();
    }

    @Override
    public void initializeCompleted() {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }
}
