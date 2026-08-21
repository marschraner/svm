package ch.metzenthin.svm.service;

import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

  Optional<Semester> findNaechstesSemester(Semester semester);

  Semester determineNaechstesNochNichtErfasstesSemester();

  List<Semester> findAllSemester();

  List<SemesterAndNumberOfKurse> findAllSemesterAndNumberOfKurse();

  SaveSemesterResult saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
      Semester semester, boolean updateSemesterrechnungen);

  DeleteSemesterResult deleteSemesterrechnungenAndSemester(Semester semester);
}
