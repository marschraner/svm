package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface SemesterService {

  boolean checkIfUpdateAffectsSemesterrechnungen(Semester semester);

  Semester determineNaechstesNochNichtErfasstesSemester();

  List<Semester> findAllSemesters();

  List<SemesterAndNumberOfKurse> findAllSemestersAndNumberOfKurse();

  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
      Semester semester, boolean updateSemesterrechnungen)
      throws EntityAlreadyExistsException, EntityWithOverlappingPeriodsException;

  void deleteSemesterrechnungenAndSemester(Semester semester) throws EntityStillReferencedException;
}
