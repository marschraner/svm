package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;
import java.text.ParseException;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public class CalendarConverter {

  public static String toString(Calendar calendar) {
    return Converter.asString(calendar);
  }

  public static String toString(Calendar calendar, String dateFormatString) {
    return Converter.asString(calendar, dateFormatString);
  }

  public static CalendarAndConversionResult toCalendar(String string) {
    try {
      return new CalendarAndConversionResult(Converter.toCalendar(string));
    } catch (ParseException e) {
      return new CalendarAndConversionResult(e.getMessage());
    }
  }

  public static CalendarAndConversionResult toCalendar(String string, String dateFormatString) {
    try {
      return new CalendarAndConversionResult(Converter.toCalendar(string, dateFormatString));
    } catch (ParseException e) {
      return new CalendarAndConversionResult(e.getMessage());
    }
  }
}
