package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DetermineSemesterInitCommand implements Command {

    // input
    private List<Semester> selectableSemesters;

    // output
    private Semester semesterInit;

    public DetermineSemesterInitCommand(SvmModel svmModel) {
        this.selectableSemesters = svmModel.getSemestersAll();
    }

    public DetermineSemesterInitCommand(List<Semester> selectableSemesters) {
        this.selectableSemesters = selectableSemesters;
    }

    @Override
    public void execute() {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(selectableSemesters);
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        Calendar dayToShowNextSemester = new GregorianCalendar();
        dayToShowNextSemester.add(Calendar.DAY_OF_YEAR, 40);
        if (currentSemester == null) {
            // Ferien zwischen 2 Semestern
            semesterInit = nextSemester;
        } else if (dayToShowNextSemester.after(currentSemester.getSemesterende()) && nextSemester != null) {
            // weniger als 40 Tage vor Semesterende
            semesterInit = nextSemester;
        } else {
            semesterInit = currentSemester;
        }
        if (semesterInit == null && !selectableSemesters.isEmpty()) {
            semesterInit = selectableSemesters.get(0);
        }
    }
    
    public Semester getSemesterInit() {
        return semesterInit;
    }
}
