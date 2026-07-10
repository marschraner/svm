package ch.metzenthin.svm.common.datatypes;

import java.util.Calendar;
import lombok.Getter;

/**
 * @author Martin Schraner
 */
public enum Wochentag {
  ALLE("", -1),
  MONTAG("Montag", Calendar.MONDAY),
  DIENSTAG("Dienstag", Calendar.TUESDAY),
  MITTWOCH("Mittwoch", Calendar.WEDNESDAY),
  DONNERSTAG("Donnerstag", Calendar.THURSDAY),
  FREITAG("Freitag", Calendar.FRIDAY),
  SAMSTAG("Samstag", Calendar.SATURDAY),
  SONNTAG("Sonntag", Calendar.SUNDAY);

  private final String name;
  @Getter private final int dayOfWeekCalendar;

  Wochentag(String name, int dayOfWeekCalendar) {
    this.name = name;
    this.dayOfWeekCalendar = dayOfWeekCalendar;
  }

  public static Wochentag[] getAllowedWochentageForKurs() {
    return new Wochentag[] {MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG, SAMSTAG};
  }

  @Override
  public String toString() {
    return name;
  }
}
