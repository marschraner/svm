package ch.metzenthin.svm.domain.model.validation;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S1192")
public class ValidationUtils {

  private static final String EINTRAG_OBLIGATORISCH = "Eintrag ist obligatorisch!";
  private static final String KEIN_GUELTIGER_PREIS_IM_FORMAT_FR_RP =
      "Kein gültiger Preis im Format 'Fr.Rp'";

  private ValidationUtils() {}

  public static ValidationResult validateNotEmpty(String value, Field field) {
    return (value == null || value.isBlank())
        ? new ValidationResult(EINTRAG_OBLIGATORISCH, Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotTooShort(String value, int minLength, Field field) {
    return (value != null && value.length() < minLength)
        ? new ValidationResult("Länge muss mindestens " + minLength + " sein!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotTooLong(String value, int maxLength, Field field) {
    return (value != null && value.length() > maxLength)
        ? new ValidationResult("Länge darf höchstens " + maxLength + " sein!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotEmptyAndLength(
      String value, int minLength, int maxLength, Field field) {
    ValidationResult validationResult = validateNotEmpty(value, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validateNotTooShort(value, minLength, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateNotTooLong(value, maxLength, field);
  }

  public static ValidationResult validateNotNull(Object value, Field field) {
    return (value == null)
        ? new ValidationResult(EINTRAG_OBLIGATORISCH, Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotValueNotSet(int value, Field field) {
    return (value == IntegerConverter.VALUE_NOT_SET)
        ? new ValidationResult(EINTRAG_OBLIGATORISCH, Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateDayOfWeekMonday(Calendar calendar, Field field) {
    return (calendar != null && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
        ? new ValidationResult(field + " muss ein Montag sein", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateDayOfWeekSaturday(Calendar calendar, Field field) {
    return (calendar != null && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
        ? new ValidationResult(field + " muss ein Samstag sein", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotBefore(
      Calendar calendar, Calendar earliestValidDate, Field field) {
    return (calendar != null && earliestValidDate != null && calendar.before(earliestValidDate))
        ? new ValidationResult(
            field + " darf nicht vor " + asString(earliestValidDate) + " liegen!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotAfter(
      Calendar calendar, Calendar latestValidDate, Field field) {
    return (calendar != null && latestValidDate != null && calendar.after(latestValidDate))
        ? new ValidationResult(
            field + " darf nicht nach " + asString(latestValidDate) + " liegen!", Set.of(field))
        : new ValidationResult();
  }

  @SuppressWarnings("DuplicatedCode")
  public static ValidationResult validatePeriod(
      Calendar beginn, Calendar ende, Field beginnField, Field endeField) {
    ValidationResult validationResult = validatePeriodNotWithoutBeginn(beginn, ende, endeField);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validatePeriodNotWithoutEnde(beginn, ende, beginnField);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateNotAfter(beginn, ende, beginnField);
  }

  public static ValidationResult validatePeriodNotWithoutBeginn(
      Calendar beginn, Calendar ende, Field field) {
    return (beginn == null && ende != null)
        ? new ValidationResult(field + " ohne Beginn-Datum", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validatePeriodNotWithoutEnde(
      Calendar beginn, Calendar ende, Field field) {
    return (beginn != null && ende == null)
        ? new ValidationResult(field + " ohne Ende-Datum", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateWithinPeriod(
      Calendar calendar, Calendar earliestValidDate, Calendar latestValidDate, Field field) {
    ValidationResult validationResult = validateNotBefore(calendar, earliestValidDate, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateNotAfter(calendar, latestValidDate, field);
  }

  public static ValidationResult validateNotNullAndWithinPeriod(
      Calendar calendar, Calendar earliestValidDate, Calendar latestValidDate, Field field) {
    ValidationResult validationResult = validateNotNull(calendar, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateWithinPeriod(calendar, earliestValidDate, latestValidDate, field);
  }

  public static ValidationResult validateNotBefore(Time time, Time earliestValidTime, Field field) {
    return (time != null && earliestValidTime != null && time.before(earliestValidTime))
        ? new ValidationResult(
            field + " darf nicht vor " + asString(earliestValidTime) + " liegen!", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateNotAfter(Time time, Time latestValidTime, Field field) {
    return (time != null && latestValidTime != null && time.after(latestValidTime))
        ? new ValidationResult(
            field + " darf nicht nach " + asString(latestValidTime) + " liegen!", Set.of(field))
        : new ValidationResult();
  }

  @SuppressWarnings("DuplicatedCode")
  public static ValidationResult validatePeriod(
      Time beginn, Time ende, Field beginnField, Field endeField) {
    ValidationResult validationResult = validatePeriodNotWithoutBeginn(beginn, ende, endeField);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validatePeriodNotWithoutEnde(beginn, ende, beginnField);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateNotAfter(beginn, ende, beginnField);
  }

  public static ValidationResult validatePeriodNotWithoutBeginn(
      Time beginn, Time ende, Field field) {
    return (beginn == null && ende != null)
        ? new ValidationResult(field + " ohne Beginn-Datum", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validatePeriodNotWithoutEnde(Time beginn, Time ende, Field field) {
    return (beginn != null && ende == null)
        ? new ValidationResult(field + " ohne Ende-Datum", Set.of(field))
        : new ValidationResult();
  }

  public static ValidationResult validateWithinPeriod(
      Time time, Time earliestValidTime, Time latestValidTime, Field field) {

    ValidationResult validationResult = validateNotBefore(time, earliestValidTime, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateNotAfter(time, latestValidTime, field);
  }

  public static ValidationResult validateNotValueNotSetAndWithinRange(
      int intValue, int minValue, int maxValue, Field field) {
    ValidationResult validationResult = validateNotValueNotSet(intValue, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateWithinRange(intValue, minValue, maxValue, field);
  }

  public static ValidationResult validateWithinRange(
      Integer integerValue, int minValue, int maxValue, Field field) {
    if (integerValue < minValue) {
      return new ValidationResult(
          String.format("%s darf nicht kleiner als %s sein", field, minValue), Set.of(field));
    }
    if (integerValue > maxValue) {
      return new ValidationResult(
          String.format("%s darf nicht grösser als %s sein", field, maxValue), Set.of(field));
    }
    return new ValidationResult();
  }

  public static ValidationResult validateNotNullAndPriceFormatAndWithinRange(
      BigDecimal bigDecimalValue, BigDecimal minValue, BigDecimal maxValue, Field field) {
    ValidationResult validationResult = validateNotNull(bigDecimalValue, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    validationResult = validatePriceFormat(bigDecimalValue, field);
    if (!validationResult.isValid()) {
      return validationResult;
    }
    return validateWithinRange(bigDecimalValue, minValue, maxValue, field);
  }

  private static ValidationResult validatePriceFormat(BigDecimal bigDecimalValue, Field field) {
    BigDecimal multipliedBy20 = bigDecimalValue.multiply(new BigDecimal(20));
    int multipliedBy20AsInt = multipliedBy20.intValue();
    if (new BigDecimal(multipliedBy20AsInt).compareTo(multipliedBy20) != 0) {
      return new ValidationResult(KEIN_GUELTIGER_PREIS_IM_FORMAT_FR_RP, Set.of(field));
    }
    return new ValidationResult();
  }

  public static ValidationResult validateWithinRange(
      BigDecimal bigDecimalValue, BigDecimal minValue, BigDecimal maxValue, Field field) {
    if (bigDecimalValue.compareTo(minValue) < 0) {
      return new ValidationResult(
          String.format("%s darf nicht kleiner als %s sein", field, minValue), Set.of(field));
    }
    if (bigDecimalValue.compareTo(maxValue) > 0) {
      return new ValidationResult(
          String.format("%s darf nicht grösser als %s sein", field, maxValue), Set.of(field));
    }
    return new ValidationResult();
  }
}
