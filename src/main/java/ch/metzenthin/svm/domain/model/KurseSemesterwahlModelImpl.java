package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindKurseSemesterCommand;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurseSemesterwahlModelImpl extends AbstractModel implements KurseSemesterwahlModel {

    private Semester semester;

    public KurseSemesterwahlModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Semester semester) {
        Semester oldValue = this.semester;
        this.semester = semester;
        firePropertyChange(Field.SEMESTER, oldValue, this.semester);
    }

    @Override
    public Semester getInitSemester(SvmModel svmModel) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        getCommandInvoker().executeCommand(findSemesterForCalendarCommand);
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        Calendar dayToShowNextSemester = new GregorianCalendar();
        dayToShowNextSemester.add(Calendar.DAY_OF_YEAR, 40);
        Semester initSemester;
        if (currentSemester == null) {
            // Ferien zwischen 2 Semestern
            initSemester = nextSemester;
        } else if (dayToShowNextSemester.after(currentSemester.getSemesterende()) && nextSemester != null) {
            // weniger als 40 Tage vor Semesterende
            initSemester = nextSemester;
        } else {
            // Neues Semester noch nicht erfasst
            initSemester = currentSemester;
        }
        if (initSemester != null) {
            return initSemester;
        } else {
            return svmModel.getSemestersAll().get(0);
        }
    }

    @Override
    public KurseTableData suchen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindKurseSemesterCommand findKurseSemesterCommand = new FindKurseSemesterCommand(semester);
        commandInvoker.executeCommand(findKurseSemesterCommand);
        List<Kurs> kurseFound = findKurseSemesterCommand.getKurseFound();
        return new KurseTableData(kurseFound, false);
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }
}
