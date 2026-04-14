package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.conversion.CalendarAndConversionResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateSemesterModel {

  boolean isNeu();

  SemesterFields getSemesterFields();

  String formatDateAsString(String dateAsString);

  CalendarAndConversionResult convertDateAsStringToCalendar(String dateAsString);

  ValidationResult validateSemesterbeginn(Calendar semesterbeginn);

  ValidationResult validateSemesterende(Calendar semesterende);

  ValidationResult validateFerienbeginn1(Calendar ferienbeginn1);

  ValidationResult validateFerienende1(Calendar ferienende1);

  ValidationResult validateFerienbeginn2(Calendar ferienbeginn2);

  ValidationResult validateFerienende2(Calendar ferienende2);

  ValidationResultsAndSaveResult speichern(
      SemesterFields semesterFields, boolean updateSemesterrechnungen);
}
