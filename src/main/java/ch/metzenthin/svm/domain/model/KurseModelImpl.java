package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteKursCommand;
import ch.metzenthin.svm.domain.commands.ImportKurseFromPreviousSemesterCommand;
import ch.metzenthin.svm.domain.commands.RemoveKursFromSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.util.*;

/**
 * @author Martin Schraner
 */
public class KurseModelImpl extends AbstractModel implements KurseModel {

    public KurseModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public KursErfassenModel getKursErfassenModel(SvmContext svmContext, KurseTableModel kurseTableModel, int rowSelected) {
        KursErfassenModel kursErfassenModel = svmContext.getModelFactory().createKursErfassenModel();
        Kurs kursSelected = kurseTableModel.getKursSelected(rowSelected);
        kursErfassenModel.setKursOrigin(kursSelected);
        return kursErfassenModel;
    }

    @Override
    public String getTotal(KurseTableModel kurseTableModel) {
        return "Total: " + kurseTableModel.getRowCount() + " " + (kurseTableModel.getRowCount() == 1 ? "Kurs" : "Kurse") + ", " + kurseTableModel.getAnzahlSchueler() + " Schüler";
    }

    @Override
    public void importKurseFromPreviousSemester(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        Semester semester = kurseSemesterwahlModel.getSemester(svmModel);
        List<Kurs> kurse = kurseTableModel.getKurse();
        ImportKurseFromPreviousSemesterCommand importKurseFromPreviousSemesterCommand = new ImportKurseFromPreviousSemesterCommand(kurse, semester);
        commandInvoker.executeCommandAsTransaction(importKurseFromPreviousSemesterCommand);
    }

    @Override
    public DeleteKursCommand.Result kursLoeschenKurseVerwalten(KurseTableModel kurseTableModel, int indexKursToBeRemoved) {
        List<Kurs> kurse = kurseTableModel.getKurse();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteKursCommand deleteKursCommand = new DeleteKursCommand(kurse, indexKursToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteKursCommand);
        return deleteKursCommand.getResult();
    }

    @Override
    public void eintragLoeschenKurseSchueler(KurseTableModel kurseTableModel, int indexKursToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveKursFromSchuelerCommand removeKursFromSchuelerCommand = new RemoveKursFromSchuelerCommand(indexKursToBeRemoved, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeKursFromSchuelerCommand);
        Schueler schuelerUpdated = removeKursFromSchuelerCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        kurseTableModel.getKurseTableData().setKurse(schuelerUpdated.getKurse());
    }

    @Override
    public Semester[] getSelectableSemestersKurseSchueler(SvmModel svmModel) {
        List<Semester> selectableSemesters = new ArrayList<>();
        List<Semester> semesterAll = svmModel.getSemestersAll();
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        for (Semester semester : semesterAll) {
            // Keine Semester in der Vergangenheit anzeigen
            if (semester.getSemesterende().after(today) || semester.getSemesterende().equals(today)) {
                selectableSemesters.add(semester);
            }
        }
        // Aufsteigende Sortierung für Combobox, d.h. ältestes Semester zuoberst
        Collections.sort(selectableSemesters, Collections.reverseOrder());
        return selectableSemesters.toArray(new Semester[selectableSemesters.size()]);
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }

}
