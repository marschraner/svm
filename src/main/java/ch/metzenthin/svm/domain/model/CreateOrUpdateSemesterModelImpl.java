package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.conversion.CalendarAndConversionResult;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedSemesterFields;
import ch.metzenthin.svm.domain.model.entityfields.SemesterFields;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateSemesterModelImpl implements CreateOrUpdateSemesterModel {

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
    return SemesterFields.of(semester);
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

    ConvertedFieldsAndConversionResults<ConvertedSemesterFields>
        convertedSemesterFieldsAndConversionResults = convertAll(semesterFields);
    if (!convertedSemesterFieldsAndConversionResults.isValid()) {
      Set<Field> fieldsWithInvalidConversion =
          convertedSemesterFieldsAndConversionResults.getFieldsWithInvalidConversion();
      ValidationResult validationResult =
          new ValidationResult("Ungültiges Format!", fieldsWithInvalidConversion);
      return new ValidationResultsAndSaveResult(List.of(validationResult));
    }
    ConvertedSemesterFields convertedSemesterFields =
        convertedSemesterFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults = validateAll(convertedSemesterFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    updateModel(convertedSemesterFields);

    SaveSemesterResult saveSemesterResult = saveSemester(updateSemesterrechnungen);
    return new ValidationResultsAndSaveResult(validationResults, saveSemesterResult);
  }

  private static ConvertedFieldsAndConversionResults<ConvertedSemesterFields> convertAll(
      SemesterFields semesterFields) {
    return semesterFields.convert();
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
    semesterFields.mergeIntoEntity(semester);
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
