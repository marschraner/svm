package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindSemesterForSchuljahrSemesterbezeichnungCommand implements Command {

  // input
  private final String schuljahr;
  private final Semesterbezeichnung semesterbezeichnung;
  private final List<Semester> erfassteSemesters;

  // output
  private Semester semesterFound;

  FindSemesterForSchuljahrSemesterbezeichnungCommand(
      String schuljahr, Semesterbezeichnung semesterbezeichnung, List<Semester> erfassteSemesters) {
    this.schuljahr = schuljahr;
    this.semesterbezeichnung = semesterbezeichnung;
    this.erfassteSemesters = erfassteSemesters;
  }

  @Override
  public void execute() {
    for (Semester erfasstesSemester : erfassteSemesters) {
      if (erfasstesSemester.getSchuljahr().equals(schuljahr)
          && erfasstesSemester.getSemesterbezeichnung().equals(semesterbezeichnung)) {
        semesterFound = erfasstesSemester;
        return;
      }
    }
    semesterFound = null;
  }

  Semester getSemesterFound() {
    return semesterFound;
  }
}
