package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSemestersCommand implements Command {

  private final SemesterDao semesterDao = new SemesterDao();

  // output
  private List<Semester> semestersAll;

  @Override
  public void execute() {
    semestersAll = semesterDao.findAll();
  }

  public List<Semester> getSemestersAll() {
    return semestersAll;
  }
}
