package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateDayOfWeekMonday;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateDayOfWeekSaturday;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotBefore;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmptyAndLength;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNull;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNullAndWithinPeriod;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validatePeriod;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateWithinPeriod;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.ConvertedValueAndConversionResult;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedSemesterFields;
import ch.metzenthin.svm.domain.model.entityfields.SemesterFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
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
  public SemesterFields getNaechstesNochNichtErfasstesSemester() {
    return SemesterFields.of(semesterService.determineNaechstesNochNichtErfasstesSemester());
  }

  @Override
  public ValidationResult validateSchuljahr(String schuljahr) {
    return validateNotEmptyAndLength(schuljahr, 9, 9, Field.SCHULJAHR);
  }

  @Override
  public ValidationResult validateSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
    return validateNotNull(semesterbezeichnung, Field.SEMESTERBEZEICHNUNG);
  }

  @Override
  public ValidationResult validateSemesterbeginn(Calendar semesterbeginn) {
    ValidationResult validationResult =
        validateNotNullAndWithinPeriod(
            semesterbeginn,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.SEMESTERBEGINN);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekMonday(semesterbeginn, Field.SEMESTERBEGINN);
  }

  @Override
  public ValidationResult validateSemesterende(Calendar semesterende) {
    ValidationResult validationResult =
        validateNotNullAndWithinPeriod(
            semesterende,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.SEMESTERENDE);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekSaturday(semesterende, Field.SEMESTERENDE);
  }

  @Override
  public ValidationResult validateFerienbeginn1(Calendar ferienbeginn1) {
    ValidationResult validationResult =
        validateNotNullAndWithinPeriod(
            ferienbeginn1,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.FERIENBEGINN1);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekMonday(ferienbeginn1, Field.FERIENBEGINN1);
  }

  @Override
  public ValidationResult validateFerienende1(Calendar ferienende1) {
    ValidationResult validationResult =
        validateNotNullAndWithinPeriod(
            ferienende1,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.FERIENENDE1);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekSaturday(ferienende1, Field.FERIENENDE1);
  }

  @Override
  public ValidationResult validateFerienbeginn2(Calendar ferienbeginn2) {
    ValidationResult validationResult =
        validateWithinPeriod(
            ferienbeginn2,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.FERIENBEGINN2);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekMonday(ferienbeginn2, Field.FERIENBEGINN2);
  }

  @Override
  public ValidationResult validateFerienende2(Calendar ferienende2) {
    ValidationResult validationResult =
        validateWithinPeriod(
            ferienende2,
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
            new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
            Field.FERIENENDE2);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateDayOfWeekSaturday(ferienende2, Field.FERIENENDE2);
  }

  // Übergreifende Validierungen
  private ValidationResult validateSemesterPeriod(Calendar beginn, Calendar ende) {
    return validatePeriod(beginn, ende, Field.SEMESTERBEGINN, Field.SEMESTERENDE);
  }

  private ValidationResult validateFerien1Period(Calendar beginn, Calendar ende) {
    return validatePeriod(beginn, ende, Field.FERIENBEGINN1, Field.FERIENBEGINN1);
  }

  private ValidationResult validateFerien2Period(Calendar beginn, Calendar ende) {
    return validatePeriod(beginn, ende, Field.FERIENBEGINN2, Field.FERIENBEGINN2);
  }

  private ValidationResult validateSemesterWithinSchuljahr(
      String schuljahr, Calendar beginn, Calendar ende) {
    ValidationResult validationResult =
        validateWithinSchuljahr(schuljahr, beginn, Field.SEMESTERBEGINN);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateWithinSchuljahr(schuljahr, ende, Field.SEMESTERENDE);
  }

  private ValidationResult validateWithinSchuljahr(String schuljahr, Calendar beginn, Field field) {
    return (schuljahr != null
            && beginn != null
            && !schuljahr.contains(Integer.toString(beginn.get(Calendar.YEAR))))
        ? new ValidationResult(field + " liegt nicht im Schuljahr " + schuljahr, Set.of(field))
        : new ValidationResult();
  }

  private ValidationResult validateFerien1WithinSemester(
      Calendar beginn, Calendar ende, Calendar semesterbeginn, Calendar semesterende) {
    return validationPeriodWithinSemester(
        beginn, ende, semesterbeginn, semesterende, Field.FERIENBEGINN1, Field.FERIENENDE1);
  }

  private ValidationResult validationPeriodWithinSemester(
      Calendar beginn,
      Calendar ende,
      Calendar semesterbeginn,
      Calendar semesterende,
      Field fieldBeginn,
      Field fieldEnde) {
    ValidationResult validationResult =
        validateWithinPeriod(beginn, semesterbeginn, semesterende, fieldBeginn);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateWithinPeriod(ende, semesterbeginn, semesterende, fieldEnde);
  }

  private ValidationResult validateFerien2WithinSemester(
      Calendar beginn, Calendar ende, Calendar semesterbeginn, Calendar semesterende) {
    return validationPeriodWithinSemester(
        beginn, ende, semesterbeginn, semesterende, Field.FERIENBEGINN2, Field.FERIENENDE2);
  }

  private ValidationResult validateFerien2AfterFerien1(
      Calendar ferienende1, Calendar ferienbeginn2) {
    return validateNotBefore(ferienbeginn2, ferienende1, Field.FERIENBEGINN2);
  }

  @Override
  public boolean checkIfUpdateAffectsSemesterrechnungen(
      String semesterbeginnAsString,
      String semesterendeAsString,
      String ferienbeginn1AsString,
      String ferienende1AsString,
      String ferienbeginn2AsString,
      String ferienende2AsString) {
    ConvertedValueAndConversionResult<Calendar> semesterbeginnAndConversionResult =
        CalendarConverter.toCalendar(semesterbeginnAsString);
    ConvertedValueAndConversionResult<Calendar> semesterendeAndConversionResult =
        CalendarConverter.toCalendar(semesterendeAsString);
    ConvertedValueAndConversionResult<Calendar> ferienbeginn1AndConversionResult =
        CalendarConverter.toCalendar(ferienbeginn1AsString);
    ConvertedValueAndConversionResult<Calendar> ferienende1AndConversionResult =
        CalendarConverter.toCalendar(ferienende1AsString);
    ConvertedValueAndConversionResult<Calendar> ferienbeginn2AndConversionResult =
        CalendarConverter.toCalendar(ferienbeginn2AsString);
    ConvertedValueAndConversionResult<Calendar> ferienende2AndConversionResult =
        CalendarConverter.toCalendar(ferienende2AsString);
    if (!semesterbeginnAndConversionResult.isValid()
        || !semesterendeAndConversionResult.isValid()
        || !ferienbeginn1AndConversionResult.isValid()
        || !ferienende1AndConversionResult.isValid()
        || !ferienbeginn2AndConversionResult.isValid()
        || !ferienende2AndConversionResult.isValid()) {
      return false;
    }
    return semesterService.checkIfUpdateAffectsSemesterrechnungen(
        semester.getSemesterId(),
        ferienende1AndConversionResult.convertedValue(),
        semesterendeAndConversionResult.convertedValue(),
        ferienbeginn1AndConversionResult.convertedValue(),
        ferienende1AndConversionResult.convertedValue(),
        ferienbeginn2AndConversionResult.convertedValue(),
        ferienende2AndConversionResult.convertedValue());
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

    // Einzelne Felder validieren
    validationResults.add(validateSchuljahr(convertedSemesterFields.schuljahr()));
    validationResults.add(
        validateSemesterbezeichnung(convertedSemesterFields.semesterbezeichnung()));
    validationResults.add(validateSemesterbeginn(convertedSemesterFields.semesterbeginn()));
    validationResults.add(validateSemesterende(convertedSemesterFields.semesterende()));
    validationResults.add(validateFerienbeginn1(convertedSemesterFields.ferienbeginn1()));
    validationResults.add(validateFerienende1(convertedSemesterFields.ferienende1()));
    validationResults.add(validateFerienbeginn2(convertedSemesterFields.ferienbeginn2()));
    validationResults.add(validateFerienende2(convertedSemesterFields.ferienende2()));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(
        validateSemesterPeriod(
            convertedSemesterFields.semesterbeginn(), convertedSemesterFields.semesterende()));
    validationResults.add(
        validateSemesterWithinSchuljahr(
            convertedSemesterFields.schuljahr(),
            convertedSemesterFields.semesterbeginn(),
            convertedSemesterFields.semesterende()));
    validationResults.add(
        validateFerien1Period(
            convertedSemesterFields.ferienbeginn1(), convertedSemesterFields.ferienende1()));
    validationResults.add(
        validateFerien2Period(
            convertedSemesterFields.ferienbeginn2(), convertedSemesterFields.ferienende2()));
    validationResults.add(
        validateFerien1WithinSemester(
            convertedSemesterFields.ferienbeginn1(),
            convertedSemesterFields.ferienende1(),
            convertedSemesterFields.semesterbeginn(),
            convertedSemesterFields.semesterende()));
    validationResults.add(
        validateFerien2WithinSemester(
            convertedSemesterFields.ferienbeginn2(),
            convertedSemesterFields.ferienende2(),
            convertedSemesterFields.semesterbeginn(),
            convertedSemesterFields.semesterende()));
    validationResults.add(
        validateFerien2AfterFerien1(
            convertedSemesterFields.ferienende1(), convertedSemesterFields.ferienbeginn2()));

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
