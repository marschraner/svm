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
}
