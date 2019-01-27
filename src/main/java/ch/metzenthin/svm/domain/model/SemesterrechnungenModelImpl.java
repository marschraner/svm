package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.*;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenModelImpl extends AbstractModel implements SemesterrechnungenModel {

    @Override
    public SemesterrechnungBearbeitenModel getSemesterrechnungBearbeitenModel(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel = svmContext.getModelFactory().createSemesterrechnungBearbeitenModel();
        Semesterrechnung semesterrechnungSelected = semesterrechnungenTableModel.getSemesterrechnungSelected(rowSelected);
        semesterrechnungBearbeitenModel.setSemesterrechnungOrigin(semesterrechnungSelected);
        return semesterrechnungBearbeitenModel;
    }

    @Override
    public void semesterrechnungPhysischLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSemesterrechnungCommand deleteSemesterrechnungCommand = new DeleteSemesterrechnungCommand(semesterrechnungenTableModel.getSemesterrechnungen(), rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCommand);
    }

    @Override
    public void semesterrechnungLogischLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteSemesterrechnungLogicallyCommand deleteSemesterrechnungCommand = new DeleteSemesterrechnungLogicallyCommand(semesterrechnungenTableModel.getSemesterrechnungen(), rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteSemesterrechnungCommand);
    }

    @Override
    public void semesterrechnungWiederherstellen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RestoreSemesterrechnungCommand restoreSemesterrechnungCommand = new RestoreSemesterrechnungCommand(semesterrechnungenTableModel.getSemesterrechnungen(), rowSelected);
        commandInvoker.executeCommandAsTransaction(restoreSemesterrechnungCommand);
    }

    @Override
    public boolean hasSemesterrechnungKurse(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected) {
        Semesterrechnung semesterrechnung = semesterrechnungenTableModel.getSemesterrechnungen().get(rowSelected);
        List<Schueler> schuelersRechnungsempfaenger = new ArrayList<>(semesterrechnung.getRechnungsempfaenger().getSchuelerRechnungsempfaenger());
        if (schuelersRechnungsempfaenger.isEmpty()) {
            return false;
        }
        // aktuelles Semester
        Semester currentSemester = semesterrechnung.getSemester();
        for (Schueler schueler : schuelersRechnungsempfaenger) {
            for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungenAsList()) {
                Kurs kurs = kursanmeldung.getKurs();
                if (!kurs.getSemester().getSemesterId().equals(currentSemester.getSemesterId())) {
                    // Nicht passendes Semester
                    continue;
                }
                return true;
            }
        }
        // vorhergehendes Semester (nur nicht abgemeldete Kurse beachten)
        FindPreviousSemesterCommand findPreviousSemesterCommand = new FindPreviousSemesterCommand(currentSemester);
        getCommandInvoker().executeCommand(findPreviousSemesterCommand);
        Semester previousSemester = findPreviousSemesterCommand.getPreviousSemester();
        if (previousSemester != null) {
            for (Schueler schueler : schuelersRechnungsempfaenger) {
                for (Kursanmeldung kursanmeldung : schueler.getKursanmeldungenAsList()) {
                    Kurs kurs = kursanmeldung.getKurs();
                    if (!kurs.getSemester().getSemesterId().equals(previousSemester.getSemesterId()) || kursanmeldung.getAbmeldedatum() != null) {
                        // Nicht passendes Semester oder abgemeldet
                        continue;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    
}
