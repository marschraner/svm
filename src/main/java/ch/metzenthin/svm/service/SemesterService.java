package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Calendar;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface SemesterService {

  boolean checkIfUpdateAffectsSemesterrechnungen(
      Integer semesterId,
      Calendar semesterbeginn,
      Calendar semesterende,
      Calendar ferienbeginn1,
      Calendar ferienende1,
      Calendar ferienbeginn2,
      Calendar ferienende2);

  Semester determineNaechstesNochNichtErfasstesSemester();

  List<Semester> findAllSemester();

  List<SemesterAndNumberOfKurse> findAllSemesterAndNumberOfKurse();

  void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
      Semester semester, boolean updateSemesterrechnungen)
      throws EntityAlreadyExistsException, EntityWithOverlappingPeriodsException;

  void deleteSemesterrechnungenAndSemester(Semester semester) throws EntityStillReferencedException;
}
