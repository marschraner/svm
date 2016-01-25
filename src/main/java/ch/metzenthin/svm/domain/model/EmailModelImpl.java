package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.EmailEmpfaenger;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
public class EmailModelImpl extends AbstractModel implements EmailModel {

    private EmailEmpfaenger emailEmpfaenger;

    public EmailModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

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
        if (checkNotEmpty(schueler.getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.SCHUELER);
        }
        if (schueler.getMutter() != null && checkNotEmpty(schueler.getMutter().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.MUTTER);
        }
        if (schueler.getVater() != null && checkNotEmpty(schueler.getVater().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.VATER);
        }
        if (!(schueler.getVater() != null && schueler.getVater().isIdenticalWith(schueler.getRechnungsempfaenger())) &&
                !(schueler.getMutter() != null && schueler.getMutter().isIdenticalWith(schueler.getRechnungsempfaenger())) &&
                checkNotEmpty(schueler.getRechnungsempfaenger().getEmail())) {
            selectableEmailEmpfaengerList.add(EmailEmpfaenger.RECHNUNGSEMPFAENGER);
        }
        return selectableEmailEmpfaengerList.toArray(new EmailEmpfaenger[selectableEmailEmpfaengerList.size()]);
    }

    @Override
    public CallDefaultEmailClientCommand.Result callEmailClient(SchuelerDatenblattModel schuelerDatenblattModel) {
        Schueler schueler = schuelerDatenblattModel.getSchueler();
        String emailAdresse = "";
        switch (emailEmpfaenger) {
            case SCHUELER:
                emailAdresse = schueler.getEmail();
                break;
            case MUTTER:
                emailAdresse = schueler.getMutter().getEmail();
                break;
            case VATER:
                emailAdresse = schueler.getVater().getEmail();
                break;
            case RECHNUNGSEMPFAENGER:
                emailAdresse = schueler.getRechnungsempfaenger().getEmail();
                break;
        }
        CommandInvoker commandInvoker = getCommandInvoker();
        CallDefaultEmailClientCommand callDefaultEmailClientCommand = new CallDefaultEmailClientCommand(emailAdresse, false);
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
