package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.entityfields.SemesterFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateSemesterModel {

  boolean isNeu();

  SemesterFields getSemesterFields();

  SemesterFields getNaechstesNochNichtErfasstesSemester();

  ValidationResult validateSemesterbeginn(Calendar semesterbeginn);

  ValidationResult validateSchuljahr(String schuljahr);

  ValidationResult validateSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung);

  ValidationResult validateSemesterende(Calendar semesterende);

  ValidationResult validateFerienbeginn1(Calendar ferienbeginn1);

  ValidationResult validateFerienende1(Calendar ferienende1);

  ValidationResult validateFerienbeginn2(Calendar ferienbeginn2);

  ValidationResult validateFerienende2(Calendar ferienende2);

  boolean checkIfUpdateAffectsSemesterrechnungen(
      String semesterbeginnAsString,
      String semesterendeAsString,
      String ferienbeginn1AsString,
      String ferienende1AsString,
      String ferienbeginn2AsString,
      String ferienende2AsString);

  ValidationResultsAndSaveResult speichern(
      SemesterFields semesterFields, boolean updateSemesterrechnungen);
}
