package ch.metzenthin.svm.domain.model.conversion;

import static ch.metzenthin.svm.common.utils.Converter.getDeutscheBezeichnungOfDateFormatString;

import ch.metzenthin.svm.common.utils.Converter;
import java.text.ParseException;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public class CalendarConverter {

  private CalendarConverter() {
    /* This utility class should not be instantiated */
  }

  public static String toString(Calendar calendar) {
    return Converter.asString(calendar);
  }

  public static String toString(Calendar calendar, String dateFormatString) {
    return Converter.asString(calendar, dateFormatString);
  }

  public static ConvertedValueAndConversionResult<Calendar> toCalendar(String string) {
    try {
      return new ConvertedValueAndConversionResult<>(Converter.toCalendar(string));
    } catch (ParseException e) {
      return new ConvertedValueAndConversionResult<>(getErrorMessage(string));
    }
  }

  public static ConvertedValueAndConversionResult<Calendar> toCalendar(
      String string, String dateFormatString) {
    try {
      return new ConvertedValueAndConversionResult<>(
          Converter.toCalendar(string, dateFormatString));
    } catch (ParseException e) {
      return new ConvertedValueAndConversionResult<>(getErrorMessage(string));
    }
  }

  public static ConversionResult<Calendar> convertToCalendar(String fieldName, String string) {
    try {
      return new ConversionResult<>(fieldName, Converter.toCalendar(string));
    } catch (ParseException e) {
      return new ConversionResult<>(fieldName, getErrorMessage(string));
    }
  }

  private static String getErrorMessage(String value) {
    return "Kein gültiges Datum im Format '"
        + getDeutscheBezeichnungOfDateFormatString(value)
        + "'";
  }
}
