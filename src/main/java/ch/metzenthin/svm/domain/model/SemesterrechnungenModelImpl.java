package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteSemesterrechnungCommand;
import ch.metzenthin.svm.domain.commands.ImportSemesterrechnungenFromPreviousSemesterCommand;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenModelImpl extends AbstractModel implements SemesterrechnungenModel {

    SemesterrechnungenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public SemesterrechnungBearbeitenModel getSemesterrechnungBearbeitenModel(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel = svmContext.getModelFactory().createSemesterrechnungBearbeitenModel();
        Semesterrechnung semesterrechnungSelected = semesterrechnungenTableModel.getSemesterrechnungSelected(rowSelected);
        semesterrechnungBearbeitenModel.setSemesterrechnungOrigin(semesterrechnungSelected);
        return semesterrechnungBearbeitenModel;
    }

    @Override
    public String getTotal(SemesterrechnungenTableModel semesterrechnungenTableModel) {
        String semesterStr = " (" + semesterrechnungenTableModel.getSemester().getSchuljahr() + ", " + semesterrechnungenTableModel.getSemester().getSemesterbezeichnung() + ")";
        String semesterrechnungenStr = (semesterrechnungenTableModel.getRowCount() == 1 ? " Semesterrechnung" : " Semesterrechnungen");
        String total = "Total: " + semesterrechnungenTableModel.getRowCount() + semesterrechnungenStr;
        return total + semesterStr;
    }

    @Override
    public void importSemesterrechnungenFromPreviousSemester(SemesterrechnungenTableModel semesterrechnungenTableModel, boolean bisherigeUeberschreiben, boolean importRestbetraege) {
        CommandInvoker commandInvoker = getCommandInvoker();
        ImportSemesterrechnungenFromPreviousSemesterCommand importSemesterrechnungenFromPreviousSemesterCommand = new ImportSemesterrechnungenFromPreviousSemesterCommand(semesterrechnungenTableModel.getSemesterrechnungen(), semesterrechnungenTableModel.getSemester(), bisherigeUeberschreiben, importRestbetraege);
        commandInvoker.executeCommandAsTransaction(importSemesterrechnungenFromPreviousSemesterCommand);
    }

    @Override
    public void semesterrechnungLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSemesterrechnungCommand deleteSemesterrechnungCommand = new DeleteSemesterrechnungCommand(semesterrechnungenTableModel.getSemesterrechnungen(), rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCommand);
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    
}
