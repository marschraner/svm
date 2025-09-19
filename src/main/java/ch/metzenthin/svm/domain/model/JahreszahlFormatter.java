package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public class JahreszahlFormatter implements Formatter<String> {

  @Override
  public String format(String dateAsString) {
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
