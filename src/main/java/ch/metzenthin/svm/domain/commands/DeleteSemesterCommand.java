package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterCommand implements Command {

  public enum Result {
    SEMESTER_VON_KURS_REFERENZIERT,
    LOESCHEN_ERFOLGREICH
  }

  private final SemesterDao semesterDao = new SemesterDao();

  // input
  private final List<Semester> semesters;
  private final int indexSemesterToBeDeleted;

  // output
  private Result result;

  public DeleteSemesterCommand(List<Semester> semesters, int indexSemesterToBeDeleted) {
    this.semesters = semesters;
    this.indexSemesterToBeDeleted = indexSemesterToBeDeleted;
  }

  @Override
  public void execute() {
    Semester semesterToBeDeleted = semesters.get(indexSemesterToBeDeleted);
    if (!semesterToBeDeleted.getKurse().isEmpty()) {
      result = Result.SEMESTER_VON_KURS_REFERENZIERT;
      return;
    }
    semesterDao.remove(semesterToBeDeleted);
    semesters.remove(indexSemesterToBeDeleted);
    result = Result.LOESCHEN_ERFOLGREICH;
  }

  public Result getResult() {
    return result;
  }
}
