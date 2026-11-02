package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNMonthsAfterNow;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConvertedValueAndConversionResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndServiceReturnValue;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.service.KursService;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikKurseModelImpl implements MonatsstatistikKurseModel {

  private final KursService kursService;

  private final Calendar earliestValidDate =
      new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1);
  private final Calendar latestValidDate = getNMonthsAfterNow(12);

  public MonatsstatistikKurseModelImpl(KursService kursService) {
    this.kursService = kursService;
  }

  @Override
  public ValidationResult validateMonatJahr(Calendar monatJahr) {
    return ValidationUtils.validateNotNullAndWithinPeriod(
        monatJahr, earliestValidDate, latestValidDate, Field.MONAT_JAHR);
  }

  @Override
  public ValidationResultsAndServiceReturnValue<MonatsstatistikKurse> calculateMonatsstatistik(
      String monatJahr) {

    ConvertedValueAndConversionResult<Calendar> calendarConvertedValueAndConversionResult =
        CalendarConverter.toCalendar(monatJahr, MONAT_JAHR_DATE_FORMAT_STRING);
    if (!calendarConvertedValueAndConversionResult.isValid()) {
      return new ValidationResultsAndServiceReturnValue<>(
          List.of(
              new ValidationResult(
                  calendarConvertedValueAndConversionResult.errorMessage(),
                  Set.of(Field.MONAT_JAHR))));
    }

    ValidationResult validationResult =
        validateMonatJahr(calendarConvertedValueAndConversionResult.convertedValue());
    if (!validationResult.isValid()) {
      return new ValidationResultsAndServiceReturnValue<>(List.of(validationResult));
    }

    MonatsstatistikKurse monatsstatistikKurse =
        kursService.calculateMonatsstatistik(
            calendarConvertedValueAndConversionResult.convertedValue());

    return new ValidationResultsAndServiceReturnValue<>(
        List.of(validationResult), monatsstatistikKurse);
  }
}
