package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
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
    public DeleteSchuelerCodeCommand.Result eintragLoeschenSchuelerCodesVerwalten(SvmContext svmContext, CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
        List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getSchuelerCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSchuelerCodeCommand deleteSchuelerCodeCommand = new DeleteSchuelerCodeCommand(schuelerCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteSchuelerCodeCommand);
        // TableData mit von der Datenbank upgedateten SchülerCodes updaten
        codesTableModel.getCodesTableData().setCodes(svmContext.getSvmModel().getSchuelerCodesAll());
        return deleteSchuelerCodeCommand.getResult();
    }

    @Override
    public DeleteMitarbeiterCodeCommand.Result eintragLoeschenMitarbeiterCodesVerwalten(SvmContext svmContext, CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
        List<MitarbeiterCode> mitarbeiterCodes = svmContext.getSvmModel().getMitarbeiterCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteMitarbeiterCodeCommand deleteMitarbeiterCodeCommand = new DeleteMitarbeiterCodeCommand(mitarbeiterCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCodeCommand);
        // TableData mit von der Datenbank upgedateten MitarbeiterCodes updaten
        codesTableModel.getCodesTableData().setCodes(svmContext.getSvmModel().getMitarbeiterCodesAll());
        return deleteMitarbeiterCodeCommand.getResult();
    }

    @Override
    public DeleteElternmithilfeCodeCommand.Result eintragLoeschenElternmithilfeCodesVerwalten(SvmContext svmContext, CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
        List<ElternmithilfeCode> elternmithilfeCodes = svmContext.getSvmModel().getElternmithilfeCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteElternmithilfeCodeCommand deleteElternmithilfeCodeCommand = new DeleteElternmithilfeCodeCommand(elternmithilfeCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteElternmithilfeCodeCommand);
        // TableData mit von der Datenbank upgedateten ElternmithilfeCodes updaten
        codesTableModel.getCodesTableData().setCodes(svmContext.getSvmModel().getElternmithilfeCodesAll());
        return deleteElternmithilfeCodeCommand.getResult();
    }

    @Override
    public DeleteSemesterrechnungCodeCommand.Result eintragLoeschenSemesterrechnungCodesVerwalten(SvmContext svmContext, CodesTableModel codesTableModel, int indexCodeToBeRemoved) {
        List<SemesterrechnungCode> semesterrechnungCodes = svmContext.getSvmModel().getSemesterrechnungCodesAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSemesterrechnungCodeCommand deleteSemesterrechnungCodeCommand = new DeleteSemesterrechnungCodeCommand(semesterrechnungCodes, indexCodeToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCodeCommand);
        // TableData mit von der Datenbank upgedateten SemesterrrechnungCodes updaten
        codesTableModel.getCodesTableData().setCodes(svmContext.getSvmModel().getSemesterrechnungCodesAll());
        return deleteSemesterrechnungCodeCommand.getResult();
    }

    @Override
    public void eintragLoeschenSchuelerCodesSchueler(CodesTableModel codesTableModel, SchuelerCode schuelerCodeToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveSchuelerCodeFromSchuelerCommand removeSchuelerCodeFromSchuelerCommand = new RemoveSchuelerCodeFromSchuelerCommand(schuelerCodeToBeRemoved, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeSchuelerCodeFromSchuelerCommand);
        Schueler schuelerUpdated = removeSchuelerCodeFromSchuelerCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedateten SchülerCodes updaten
        codesTableModel.getCodesTableData().setCodes(schuelerUpdated.getSchuelerCodesAsList());
    }

    @Override
    public void eintragLoeschenMitarbeiterCodesMitarbeiter(CodesTableModel codesTableModel, MitarbeiterCode mitarbeiterCodeToBeRemoved, MitarbeiterErfassenModel mitarbeiterErfassenModel) {
        mitarbeiterErfassenModel.getMitarbeiterCodes().remove(mitarbeiterCodeToBeRemoved);
        // TableData updaten
        codesTableModel.getCodesTableData().setCodes(mitarbeiterErfassenModel.getMitarbeiterCodesAsList());
    }

    @Override
    public CodeErfassenModel getCodeErfassenModel(SvmContext svmContext, int indexCodeToBeModified, Codetyp codetyp) {
        CodeErfassenModel codeErfassenModel = svmContext.getModelFactory().createCodeErfassenModel();
        switch (codetyp) {
            case SCHUELER:
                List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getSchuelerCodesAll();
                codeErfassenModel.setSchuelerCodeOrigin(schuelerCodes.get(indexCodeToBeModified));
                break;
            case MITARBEITER:
                List<MitarbeiterCode> mitarbeiterCodes = svmContext.getSvmModel().getMitarbeiterCodesAll();
                codeErfassenModel.setMitarbeiterCodeOrigin(mitarbeiterCodes.get(indexCodeToBeModified));
                break;
            case ELTERNMITHILFE:
                List<ElternmithilfeCode> elternmithilfeCodes = svmContext.getSvmModel().getElternmithilfeCodesAll();
                codeErfassenModel.setElternmithilfeCodeOrigin(elternmithilfeCodes.get(indexCodeToBeModified));
                break;
            case SEMESTERRECHNUNG:
                List<SemesterrechnungCode> semesterrechnungCodes = svmContext.getSvmModel().getSemesterrechnungCodesAll();
                codeErfassenModel.setSemesterrechnungCodeOrigin(semesterrechnungCodes.get(indexCodeToBeModified));
                break;
        }
        return codeErfassenModel;
    }

    @Override
    public SchuelerCode[] getSelectableSchuelerCodes(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        List<SchuelerCode> selectableSchuelerCodes = new ArrayList<>(svmModel.getSelektierbareSchuelerCodesAll());
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
    public MitarbeiterCode[] getSelectableMitarbeiterCodes(SvmModel svmModel, MitarbeiterErfassenModel mitarbeiterErfassenModel) {
        List<MitarbeiterCode> selectableMitarbeiterCodes = new ArrayList<>(svmModel.getSelektierbareMitarbeiterCodesAll());
        List<MitarbeiterCode> codesToBeRemoved = new ArrayList<>();
        for (MitarbeiterCode mitarbeiterCode : selectableMitarbeiterCodes) {
            for (MitarbeiterCode mitarbeiterCodeMitarbeiter : mitarbeiterErfassenModel.getMitarbeiterCodes()) {
                // Nur noch nicht zugewiesene Codes sollen selektierbar sein
                if (mitarbeiterCodeMitarbeiter.isIdenticalWith(mitarbeiterCode)) {
                    codesToBeRemoved.add(mitarbeiterCode);
                }
            }
        }
        selectableMitarbeiterCodes.removeAll(codesToBeRemoved);
        return selectableMitarbeiterCodes.toArray(new MitarbeiterCode[selectableMitarbeiterCodes.size()]);
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
