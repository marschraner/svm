package ch.metzenthin.svm.domain.model.conversion;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public record CalendarAndConversionResult(Calendar calendar, boolean isValid, String errorMessage) {

  public CalendarAndConversionResult(Calendar calendar) {
    this(calendar, true, null);
  }

  public CalendarAndConversionResult(String errorMessage) {
    this(null, false, errorMessage);
  }
}
