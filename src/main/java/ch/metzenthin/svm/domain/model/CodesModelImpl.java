package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMaerchenCodeCommand;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCodeCommand;
import ch.metzenthin.svm.domain.commands.RemoveSchuelerCodeFromSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CodesModelImpl extends AbstractModel implements CodesModel {

    CodesModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public DeleteSchuelerCodeCommand.Result eintragLoeschenSchuelerCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved) {
        List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getSchuelerCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSchuelerCodeCommand deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(schuelerCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        return deleteSchuelerCodeCommand.getResult();
    }

    @Override
    public DeleteMaerchenCodeCommand.Result eintragLoeschenMaerchenCodesVerwalten(SvmContext svmContext, int indexCodeToBeRemoved) {
        List<MaerchenCode> maerchenCodes = svmContext.getSvmModel().getMaerchenCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteMaerchenCodeCommand deleteMaerchenCodeCommand = new DeleteMaerchenCodeCommand(maerchenCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteMaerchenCodeCommand);
        return deleteMaerchenCodeCommand.getResult();
    }

    @Override
    public void eintragLoeschenSchuelerCodesSchueler(CodesTableModel codesTableModel, SchuelerCode schuelerCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(schuelerCodeToBeRemoved, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);
        Schueler schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getCodesAsList());
    }

    @Override
    public CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified, Codetyp codetyp) {
        CodeErfassenModel codeErfassenModel = svmContext.getModelFactory().createCodeErfassenModel();
        switch (codetyp) {
            case SCHUELER:
                List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getSchuelerCodesAll();
                codeErfassenModel.setSchuelerCodeOrigin(schuelerCodes.get(indexCodeToBeModified));
                break;
            case MAERCHEN:
                List<MaerchenCode> maerchenCodes = svmContext.getSvmModel().getMaerchenCodesAll();
                codeErfassenModel.setMaerchenCodeOrigin(maerchenCodes.get(indexCodeToBeModified));
                break;
        }
        return codeErfassenModel;
    }

    @Override
    public SchuelerCode[] getSelectableSchuelerCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        List<SchuelerCode> selectableSchuelerCodes = new ArrayList<>(svmModel.getSchuelerCodesAll());
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
