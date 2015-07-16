package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.domain.commands.RemoveCodeFromSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.*;

/**
 * @author Martin Schraner
 */
public class CodesModelImpl extends AbstractModel implements CodesModel {

    CodesModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public DeleteCodeCommand.Result eintragLoeschenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved) {
        List<Code> codes = svmContext.getSvmModel().getCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteCodeCommand deleteCodeCommand = new DeleteCodeCommand(codes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteCodeCommand);
        return deleteCodeCommand.getResult();
    }

    @Override
    public void eintragLoeschenCodesSchueler(int indexCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveCodeFromSchuelerCommand removeCodeFromSchuelerCommand = new RemoveCodeFromSchuelerCommand(indexCodeToBeRemoved, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeCodeFromSchuelerCommand);
    }

    @Override
    public CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified) {
        CodeErfassenModel codeErfassenModel = svmContext.getModelFactory().createCodeErfassenModel();
        List<Code> codes = svmContext.getSvmModel().getCodesAll();
        codeErfassenModel.setCodeOrigin(codes.get(indexCodeToBeModified));
        return codeErfassenModel;
    }

    @Override
    public Code[] getSelectableCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        List<Code> selectableCodes = new ArrayList<>(svmModel.getCodesAll());
        List<Code> codesToBeRemoved = new ArrayList<>();
        for (Code code : selectableCodes) {
            for (Code codeSchueler: schuelerDatenblattModel.getSchueler().getCodes()) {
                // Nur noch nicht zugewiesene Codes sollen selektierbar sein
                if (codeSchueler.isIdenticalWith(code)) {
                    codesToBeRemoved.add(code);
                }
            }
        }
        selectableCodes.removeAll(codesToBeRemoved);
        return selectableCodes.toArray(new Code[selectableCodes.size()]);
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
