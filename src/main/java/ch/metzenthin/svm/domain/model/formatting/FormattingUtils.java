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

  @SuppressWarnings("DuplicatedCode")
  public static String formatAsDate(String value) {
    if (value == null) {
      return null;
    }
    int strLen = value.length();
    if (value.trim().matches(".*\\.[4-9]\\d$")) {
      return value.substring(0, strLen - 2) + "19" + value.substring(strLen - 2, strLen);
    } else if (value.matches(".*\\.[0-3]\\d$")) {
      return value.substring(0, strLen - 2) + "20" + value.substring(strLen - 2, strLen);
    }
    return value;
  }

  public static String formatAsPrice(String value) {
    if (value == null) {
      return null;
    }
    String formattedValue = formatString(value);
    if (formattedValue.matches("^\\d+$")) {
      // .00 anhängen, falls Ganzzahl übergeben
      formattedValue = formattedValue + ".00";
    } else if (formattedValue.matches("^\\d+\\.$")) {
      // 00 anhängen, falls x. übergeben
      formattedValue = formattedValue + "00";
    } else if (formattedValue.matches("^\\d+\\.\\d$")) {
      // 0 anhängen, falls x.0 übergeben
      formattedValue = formattedValue + "0";
    }
    return formattedValue;
  }
}
