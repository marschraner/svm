package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckSemesterBereitsErfasstCommand implements Command {

  // input
  private final Semester semester;
  private final Semester semesterOrigin;
  private final List<Semester> bereitsErfassteSemesters;

  // output
  private boolean bereitsErfasst;

  public CheckSemesterBereitsErfasstCommand(
      Semester semester, Semester semesterOrigin, List<Semester> bereitsErfassteSemesters) {
    this.semester = semester;
    this.semesterOrigin = semesterOrigin;
    this.bereitsErfassteSemesters = bereitsErfassteSemesters;
  }

  @Override
  public void execute() {
    for (Semester bereitsErfasstesSemester : bereitsErfassteSemesters) {
      if (!bereitsErfasstesSemester.isIdenticalWith(semesterOrigin)
          && bereitsErfasstesSemester.getSchuljahr().equals(semester.getSchuljahr())
          && bereitsErfasstesSemester
              .getSemesterbezeichnung()
              .equals(semester.getSemesterbezeichnung())) {
        bereitsErfasst = true;
        return;
      }
    }
    bereitsErfasst = false;
  }

  public boolean isBereitsErfasst() {
    return bereitsErfasst;
  }
}
