package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.EmailEmpfaenger;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;

/**
 * @author Martin Schraner
 */
public interface EmailModel extends Model {

    EmailEmpfaenger getEmailEmpfaenger();

    void setEmailEmpfaenger(EmailEmpfaenger emailEmpfaenger);

    EmailEmpfaenger[] getSelectableEmailEmpfaengers(SchuelerDatenblattModel schuelerDatenblattModel);

    CallDefaultEmailClientCommand.Result callEmailClient(SchuelerDatenblattModel schuelerDatenblattModel);
}
