package ch.metzenthin.svm.domain.model.formatting;

/**
 * @author Martin Schraner
 */
public class FormattingUtils {

  private FormattingUtils() {}

  public static String formatString(String value) {
    return value != null
        ? value.transform(String::trim).transform(str -> str.replaceAll("\\s+", " "))
        : null;
  }

  public static String formatCalendar(String dateAsString) {
    if (dateAsString == null) {
      return null;
    }
    int strLen = dateAsString.length();
    if (dateAsString.trim().matches(".*\\.[4-9]\\d$")) {
      return dateAsString.substring(0, strLen - 2)
          + "19"
          + dateAsString.substring(strLen - 2, strLen);
    } else if (dateAsString.matches(".*\\.[0-3]\\d$")) {
      return dateAsString.substring(0, strLen - 2)
          + "20"
          + dateAsString.substring(strLen - 2, strLen);
    }
    return dateAsString;
  }
}
