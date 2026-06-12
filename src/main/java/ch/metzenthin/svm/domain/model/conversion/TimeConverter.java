package ch.metzenthin.svm.domain.model.conversion;

import ch.metzenthin.svm.common.utils.Converter;
import java.sql.Time;
import java.text.ParseException;

/**
 * @author Hans Stamm
 */
public class TimeConverter {

  private static final String ERROR_MESSAGE = "Keine gültige Zeit im Format 'HH.MM'";

  private TimeConverter() {
    /* This utility class should not be instantiated */
  }

  public static String toString(Time time) {
    if (time == null) {
      return null;
    }
    return Converter.asString(time);
  }

  public static ConvertedValueAndConversionResult<Time> toTime(String string) {
    try {
      return new ConvertedValueAndConversionResult<>(Converter.toTime(string));
    } catch (ParseException e) {
      return new ConvertedValueAndConversionResult<>(ERROR_MESSAGE);
    }
  }

  public static ConversionResult<Time> convertToTime(String fieldName, String string) {
    try {
      return new ConversionResult<>(fieldName, Converter.toTime(string));
    } catch (ParseException e) {
      return new ConversionResult<>(fieldName, ERROR_MESSAGE);
    }
  }
}
