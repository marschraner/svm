package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindAllCodesCommand;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodesModelImpl extends AbstractModel implements CodesModel {

    private List<Code> codes;

    CodesModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        initializeCodesModel();
    }

    private void initializeCodesModel() {
        CommandInvoker commandInvoker = getCommandInvoker();
        commandInvoker.openSession();
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommandWithinSession(findAllCodesCommand);
        codes = findAllCodesCommand.getCodesAll();
    }

    @Override
    public List<Code> getCodes() {
        return codes;
    }

    @Override
    public DeleteCodeCommand.Result eintragLoeschen(int selectedRow) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteCodeCommand deleteCodeCommand = new DeleteCodeCommand(codes, selectedRow);
        try {
            commandInvoker.beginTransaction();
            commandInvoker.executeCommandWithinTransaction(deleteCodeCommand);
            commandInvoker.commitTransaction();
            return deleteCodeCommand.getResult();
        } catch (Throwable e) {
            commandInvoker.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }

    @Override
    public CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified) {
        CodeErfassenModel codeErfassenModel = svmContext.getModelFactory().createCodeErfassenModel();
        codeErfassenModel.setCodeOrigin(codes.get(indexCodeToBeModified));
        return codeErfassenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
