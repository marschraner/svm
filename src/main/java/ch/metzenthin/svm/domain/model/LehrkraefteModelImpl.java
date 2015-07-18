package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

/**
 * @author Martin Schraner
 */
public class LehrkraefteModelImpl extends AbstractModel implements LehrkraefteModel {

    public LehrkraefteModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    void doValidate() throws SvmValidationException {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
