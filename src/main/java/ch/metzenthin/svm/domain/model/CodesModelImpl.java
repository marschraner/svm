package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCodeCommand;
import ch.metzenthin.svm.domain.commands.RemoveSchuelerCodeFromSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

import java.util.*;

/**
 * @author Martin Schraner
 */
public class CodesModelImpl extends AbstractModel implements CodesModel {

    CodesModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public DeleteSchuelerCodeCommand.Result eintragLoeschenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved) {
        List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSchuelerCodeCommand deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(schuelerCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        return deleteSchuelerCodeCommand.getResult();
    }

    @Override
    public void eintragLoeschenCodesSchueler(CodesTableModel codesTableModel, SchuelerCode schuelerCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(schuelerCodeToBeRemoved, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);
        Schueler schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        codesTableModel.getCodesTableData().setSchuelerCodes(schuelerUpdated.getCodesAsList());
    }

    @Override
    public CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified) {
        CodeErfassenModel codeErfassenModel = svmContext.getModelFactory().createCodeErfassenModel();
        List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getCodesAll();
        codeErfassenModel.setSchuelerCodeOrigin(schuelerCodes.get(indexCodeToBeModified));
        return codeErfassenModel;
    }

    @Override
    public SchuelerCode[] getSelectableCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        List<SchuelerCode> selectableSchuelerCodes = new ArrayList<>(svmModel.getCodesAll());
        List<SchuelerCode> codesToBeRemoved = new ArrayList<>();
        for (SchuelerCode schuelerCode : selectableSchuelerCodes) {
            for (SchuelerCode schuelerCodeSchueler : schuelerDatenblattModel.getSchueler().getSchuelerCodes()) {
                // Nur noch nicht zugewiesene Codes sollen selektierbar sein
                if (schuelerCodeSchueler.isIdenticalWith(schuelerCode)) {
                    codesToBeRemoved.add(schuelerCode);
                }
            }
        }
        selectableSchuelerCodes.removeAll(codesToBeRemoved);
        return selectableSchuelerCodes.toArray(new SchuelerCode[selectableSchuelerCodes.size()]);
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
