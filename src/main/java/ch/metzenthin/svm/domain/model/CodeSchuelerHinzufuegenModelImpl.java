package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddCodeToSchuelerAndSaveCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Martin Schraner
 */
public class CodeSchuelerHinzufuegenModelImpl extends AbstractModel implements CodeSchuelerHinzufuegenModel {

    private Code code;

    CodeSchuelerHinzufuegenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public void hinzufuegen(SchuelerDatenblattModel schuelerDatenblattModel) {
        AddCodeToSchuelerAndSaveCommand addCodeToSchuelerAndSaveCommand = new AddCodeToSchuelerAndSaveCommand(code, schuelerDatenblattModel.getSchueler());
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.beginTransaction();
            commandInvoker.executeCommandWithinTransaction(addCodeToSchuelerAndSaveCommand);
            commandInvoker.commitTransaction();
        } catch (Throwable e) {
            commandInvoker.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
