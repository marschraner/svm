package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Codetyp;
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
        // TableData mit von der Datenbank upgedateten SemesterrechnungCodes updaten
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
            case SCHUELER -> {
                List<SchuelerCode> schuelerCodes = svmContext.getSvmModel().getSchuelerCodesAll();
                codeErfassenModel.setSchuelerCodeOrigin(schuelerCodes.get(indexCodeToBeModified));
            }
            case MITARBEITER -> {
                List<MitarbeiterCode> mitarbeiterCodes = svmContext.getSvmModel().getMitarbeiterCodesAll();
                codeErfassenModel.setMitarbeiterCodeOrigin(mitarbeiterCodes.get(indexCodeToBeModified));
            }
            case ELTERNMITHILFE -> {
                List<ElternmithilfeCode> elternmithilfeCodes = svmContext.getSvmModel().getElternmithilfeCodesAll();
                codeErfassenModel.setElternmithilfeCodeOrigin(elternmithilfeCodes.get(indexCodeToBeModified));
            }
            case SEMESTERRECHNUNG -> {
                List<SemesterrechnungCode> semesterrechnungCodes = svmContext.getSvmModel().getSemesterrechnungCodesAll();
                codeErfassenModel.setSemesterrechnungCodeOrigin(semesterrechnungCodes.get(indexCodeToBeModified));
            }
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
        return selectableSchuelerCodes.toArray(new SchuelerCode[0]);
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
        return selectableMitarbeiterCodes.toArray(new MitarbeiterCode[0]);
    }

    @Override
    void doValidate() throws SvmValidationException {
        // Keine feldübergreifende Validierung notwendig
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
