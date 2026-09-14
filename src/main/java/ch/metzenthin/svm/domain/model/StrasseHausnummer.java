package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hans Stamm
 */
@Getter
@Setter
public class StrasseHausnummer {

  private String strasse;
  private String hausnummer;

  public StrasseHausnummer(String strasse, String hausnummer) {
    this.strasse = strasse;
    this.hausnummer = hausnummer;
  }

  public StrasseHausnummer(String strasseHausnummer) {
    this.strasse = strasseHausnummerGetStrasse(strasseHausnummer);
    this.hausnummer = strasseHausnummerGetHausnummer(strasseHausnummer);
  }

  private static String strasseHausnummerGetStrasse(String strasseHausnummer) {
    String[] splittedStrasseHausnummer = splitStrasseHausnummer(strasseHausnummer);
    return (splittedStrasseHausnummer.length == 0) ? null : splittedStrasseHausnummer[0];
  }

  private static String strasseHausnummerGetHausnummer(String strasseHausnummer) {
    String[] splittedStrasseHausnummer = splitStrasseHausnummer(strasseHausnummer);
    if (splittedStrasseHausnummer.length == 0) {
      return null;
    } else if (splittedStrasseHausnummer.length == 1) {
      return "";
    } else {
      return splittedStrasseHausnummer[1];
    }
  }

  private static String[] splitStrasseHausnummer(String strasseHausnummer) {
    if (strasseHausnummer == null) {
      return new String[] {};
    }
    String[] splitted = strasseHausnummer.trim().split("\\s+");
    // Prüfen, ob mindestens 2 Felder und ob letztes mit Zahlen beginnt
    if (splitted.length > 1 && splitted[splitted.length - 1].matches("\\d+.*")) {
      StringBuilder strasse = new StringBuilder(splitted[0]);
      for (int i = 1; i < splitted.length - 1; i++) {
        strasse.append(" ").append(splitted[i]);
      }
      return new String[] {strasse.toString(), splitted[splitted.length - 1]};
    } else {
      return new String[] {strasseHausnummer};
    }
  }

  @Override
  public String toString() {
    if (!checkNotEmpty(strasse) && !checkNotEmpty(hausnummer)) {
      return "";
    } else if (!checkNotEmpty(hausnummer)) {
      return strasse;
    } else if (!checkNotEmpty(strasse)) {
      return hausnummer;
    } else {
      return strasse + " " + hausnummer;
    }
  }
}
