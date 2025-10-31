package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public class StrasseFormatter implements Formatter<String> {

  @Override
  public String format(String strasse) {
    if (strasse == null) {
      return null;
    }
    strasse = strasse.replace("str.", "strasse");
    return strasse.replace("Str.", "Strasse");
  }
}
