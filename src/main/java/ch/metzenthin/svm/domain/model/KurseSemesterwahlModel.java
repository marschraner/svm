package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public interface KurseSemesterwahlModel {

  List<Semester> getAllSemesters();

  Optional<Semester> getInitSemester();

  KursListModel suchen(Semester semester);
}
