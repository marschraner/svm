package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungDao;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCommand implements Command {

  private final SemesterrechnungDao semesterrechnungDao = new SemesterrechnungDao();

  // input
  private final List<Semesterrechnung> semesterrechnungen;
  private final int indexSemesterrechnungToBeDeleted;

  public DeleteSemesterrechnungCommand(
      List<Semesterrechnung> semesterrechnungen, int indexSemesterrechnungToBeDeleted) {
    this.semesterrechnungen = semesterrechnungen;
    this.indexSemesterrechnungToBeDeleted = indexSemesterrechnungToBeDeleted;
  }

  @Override
  public void execute() {
    Semesterrechnung semesterrechnungToBeDeleted =
        semesterrechnungen.get(indexSemesterrechnungToBeDeleted);
    semesterrechnungDao.remove(semesterrechnungToBeDeleted);
    semesterrechnungen.remove(indexSemesterrechnungToBeDeleted);
  }
}
