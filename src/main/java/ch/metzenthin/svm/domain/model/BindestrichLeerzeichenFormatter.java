package ch.metzenthin.svm.domain.model;

/**
 * @author Martin Schraner
 */
public class BindestrichLeerzeichenFormatter implements Formatter<String> {

  @Override
  public String format(String name) {

    if (name == null) {
      return null;
    }

    // Leerzeichen um Bindestriche entfernen
    name = name.replaceAll("\\s*-\\s*", "-");

    // Mehr als ein Leerzeichen durch eines ersetzen
    return name.replaceAll("\\s+", " ");
  }
}
