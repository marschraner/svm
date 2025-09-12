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
  private final List<Semester> selectableSemesters;
  private final int daysBeforeSemesterEndToShowNextSemester;

  // output
  private Semester semesterInit;

  public DetermineSemesterInitCommand(
      SvmModel svmModel, int daysBeforeSemesterEndToShowNextSemester) {
    this(svmModel.getSemestersAll(), daysBeforeSemesterEndToShowNextSemester);
  }

  public DetermineSemesterInitCommand(
      List<Semester> selectableSemesters, int daysBeforeSemesterEndToShowNextSemester) {
    this.selectableSemesters = selectableSemesters;
    this.daysBeforeSemesterEndToShowNextSemester = daysBeforeSemesterEndToShowNextSemester;
  }

  @Override
  public void execute() {
    FindSemesterForCalendarCommand findSemesterForCalendarCommand =
        new FindSemesterForCalendarCommand(selectableSemesters);
    findSemesterForCalendarCommand.execute();
    Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
    Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
    Calendar dayToShowNextSemester = new GregorianCalendar();
    dayToShowNextSemester.add(Calendar.DAY_OF_YEAR, daysBeforeSemesterEndToShowNextSemester);
    if (currentSemester == null) {
      // Ferien zwischen 2 Semestern
      semesterInit = nextSemester;
    } else if (dayToShowNextSemester.after(currentSemester.getSemesterende())
        && nextSemester != null) {
      // weniger als daysBeforeSemesterEndToShowNextSemester Tage vor Semesterende
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
