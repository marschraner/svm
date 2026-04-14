package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.conversion.CalendarAndConversionResult;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.formatting.FormattingUtils;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import jakarta.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateSemesterModelImpl implements CreateOrUpdateSemesterModel {

  private record ConvertedSemesterFieldsAndConversionResults(
      String schuljahr,
      Semesterbezeichnung semesterbezeichnung,
      CalendarAndConversionResult semesterbeginnAndConversionResult,
      CalendarAndConversionResult semesterendeAndConversionResult,
      CalendarAndConversionResult ferienbeginn1AndConversionResult,
      CalendarAndConversionResult ferienende1AndConversionResult,
      CalendarAndConversionResult ferienbeginn2AndConversionResult,
      CalendarAndConversionResult ferienende2AndConversionResult) {

    Set<Field> getFieldsWithInvalidConversion() {
      Set<Field> fieldsWithInvalidConversion = new HashSet<>();
      if (!semesterbeginnAndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.SEMESTERBEGINN);
      }
      if (!semesterendeAndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.SEMESTERENDE);
      }
      if (!ferienbeginn1AndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.FERIENBEGINN1);
      }
      if (!ferienende1AndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.FERIENENDE1);
      }
      if (!ferienbeginn2AndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.FERIENBEGINN2);
      }
      if (!ferienende2AndConversionResult.isValid()) {
        fieldsWithInvalidConversion.add(Field.FERIENENDE2);
      }
      return fieldsWithInvalidConversion;
    }

    ConvertedSemesterFields toConvertedFields() {
      return new ConvertedSemesterFields(
          schuljahr,
          semesterbezeichnung,
          semesterbeginnAndConversionResult.calendar(),
          semesterendeAndConversionResult.calendar(),
          ferienbeginn1AndConversionResult.calendar(),
          ferienende1AndConversionResult.calendar(),
          ferienbeginn2AndConversionResult.calendar(),
          ferienende2AndConversionResult.calendar());
    }
  }

  private record ConvertedSemesterFields(
      String schuljahr,
      Semesterbezeichnung semesterbezeichnung,
      Calendar semesterbeginn,
      Calendar semesterende,
      Calendar ferienbeginn1,
      Calendar ferienende1,
      Calendar ferienbeginn2,
      Calendar ferienende2) {}

  private final boolean neu;
  private final Semester semester;
  private final SemesterService semesterService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateSemesterModelImpl(
      Optional<Semester> semesterToBeModifiedOptional, SemesterService semesterService) {
    this.neu = semesterToBeModifiedOptional.isEmpty();
    this.semester = semesterToBeModifiedOptional.orElseGet(Semester::new);
    this.semesterService = semesterService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public SemesterFields getSemesterFields() {
    String semesterbeginn = CalendarConverter.toString(semester.getSemesterbeginn());
    String semesterende = CalendarConverter.toString(semester.getSemesterende());
    String ferienbeginn1 = CalendarConverter.toString(semester.getFerienbeginn1());
    String ferienende1 = CalendarConverter.toString(semester.getFerienende1());
    String ferienbeginn2 = CalendarConverter.toString(semester.getFerienbeginn2());
    String ferienende2 = CalendarConverter.toString(semester.getFerienende2());
    return new SemesterFields(
        semester.getSchuljahr(),
        semester.getSemesterbezeichnung(),
        semesterbeginn,
        semesterende,
        ferienbeginn1,
        ferienende1,
        ferienbeginn2,
        ferienende2);
  }

  @Override
  public String formatDateAsString(String dateAsString) {
    return FormattingUtils.formatCalendar(dateAsString);
  }

  @Override
  public CalendarAndConversionResult convertDateAsStringToCalendar(String dateAsString) {
    return CalendarConverter.toCalendar(dateAsString);
  }

  @Override
  public ValidationResult validateSemesterbeginn(Calendar semesterbeginn) {
    return ValidationUtils.validateNotNullAndWithinPeriod(
        semesterbeginn,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.SEMESTERBEGINN);
  }

  @Override
  public ValidationResult validateSemesterende(Calendar semesterende) {
    return ValidationUtils.validateNotNullAndWithinPeriod(
        semesterende,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.SEMESTERENDE);
  }

  @Override
  public ValidationResult validateFerienbeginn1(Calendar ferienbeginn1) {
    return ValidationUtils.validateNotNullAndWithinPeriod(
        ferienbeginn1,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.FERIENBEGINN1);
  }

  @Override
  public ValidationResult validateFerienende1(Calendar ferienende1) {
    return ValidationUtils.validateNotNullAndWithinPeriod(
        ferienende1,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.FERIENENDE1);
  }

  @Override
  public ValidationResult validateFerienbeginn2(Calendar ferienbeginn2) {
    return ValidationUtils.validateWithinPeriod(
        ferienbeginn2,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.FERIENBEGINN2);
  }

  @Override
  public ValidationResult validateFerienende2(Calendar ferienende2) {
    return ValidationUtils.validateWithinPeriod(
        ferienende2,
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
        new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
        Field.FERIENENDE1);
  }

  @Override
  public ValidationResultsAndSaveResult speichern(
      SemesterFields semesterFields, boolean updateSemesterrechnungen) {

    ConvertedSemesterFieldsAndConversionResults convertedSemesterFieldsAndConversionResults =
        convertAll(semesterFields);
    Set<Field> fieldsWithInvalidConversion =
        convertedSemesterFieldsAndConversionResults.getFieldsWithInvalidConversion();
    if (!fieldsWithInvalidConversion.isEmpty()) {
      ValidationResult validationResult =
          new ValidationResult("Ungültiges Format!", fieldsWithInvalidConversion);
      return new ValidationResultsAndSaveResult(List.of(validationResult));
    }
    ConvertedSemesterFields convertedSemesterFields =
        convertedSemesterFieldsAndConversionResults.toConvertedFields();

    List<ValidationResult> validationResults = validateAll(convertedSemesterFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    updateModel(convertedSemesterFields);

    SaveSemesterResult saveSemesterResult = saveSemester(updateSemesterrechnungen);
    return new ValidationResultsAndSaveResult(validationResults, saveSemesterResult);
  }

  private ConvertedSemesterFieldsAndConversionResults convertAll(SemesterFields semesterFields) {
    CalendarAndConversionResult semesterbeginnAndConversionResult =
        convertDateAsStringToCalendar(semesterFields.semesterbeginn());
    CalendarAndConversionResult semesterendeAndConversionResult =
        convertDateAsStringToCalendar(semesterFields.semesterende());
    CalendarAndConversionResult ferienbeginn1AndConversionResult =
        convertDateAsStringToCalendar(semesterFields.ferienbeginn1());
    CalendarAndConversionResult ferienende1AndConversionResult =
        convertDateAsStringToCalendar(semesterFields.ferienende1());
    CalendarAndConversionResult ferienbeginn2AndConversionResult =
        convertDateAsStringToCalendar(semesterFields.ferienbeginn2());
    CalendarAndConversionResult ferienende2AndConversionResult =
        convertDateAsStringToCalendar(semesterFields.ferienende2());
    return new ConvertedSemesterFieldsAndConversionResults(
        semesterFields.schuljahr(),
        semesterFields.semesterbezeichnung(),
        semesterbeginnAndConversionResult,
        semesterendeAndConversionResult,
        ferienbeginn1AndConversionResult,
        ferienende1AndConversionResult,
        ferienbeginn2AndConversionResult,
        ferienende2AndConversionResult);
  }

  private List<ValidationResult> validateAll(ConvertedSemesterFields convertedSemesterFields) {
    List<ValidationResult> validationResults = new ArrayList<>();
    validationResults.add(validateSemesterbeginn(convertedSemesterFields.semesterbeginn()));
    validationResults.add(validateSemesterende(convertedSemesterFields.semesterende()));
    validationResults.add(validateFerienbeginn1(convertedSemesterFields.ferienbeginn1()));
    validationResults.add(validateFerienende1(convertedSemesterFields.ferienende1()));
    validationResults.add(validateFerienbeginn2(convertedSemesterFields.ferienbeginn2()));
    validationResults.add(validateFerienende2(convertedSemesterFields.ferienende2()));
    return validationResults;
  }

  void updateModel(ConvertedSemesterFields semesterFields) {
    semester.setSchuljahr(semesterFields.schuljahr());
    semester.setSemesterbezeichnung(semesterFields.semesterbezeichnung());
    semester.setSemesterbeginn(semesterFields.semesterbeginn());
    semester.setSemesterende(semesterFields.semesterende());
    semester.setFerienbeginn1(semesterFields.ferienbeginn1());
    semester.setFerienende1(semesterFields.ferienende1());
    semester.setFerienbeginn2(semesterFields.ferienbeginn2());
    semester.setFerienende2(semesterFields.ferienende2());
  }

  private SaveSemesterResult saveSemester(boolean updateSemesterrechnungen) {
    SaveSemesterResult saveSemesterResult;
    try {
      semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
          semester, updateSemesterrechnungen);
      saveSemesterResult = SaveSemesterResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_BEREITS_ERFASST;
    } catch (EntityWithOverlappingPeriodsException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveSemesterResult;
  }
}
