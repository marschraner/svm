package ch.metzenthin.svm.common.dataTypes;

import java.util.Calendar;

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
    private final int dayOfWeekCalendar;

    Wochentag(String name, int dayOfWeekCalendar) {
        this.name = name;
        this.dayOfWeekCalendar = dayOfWeekCalendar;
    }

    public int getDayOfWeekCalendar() {
        return dayOfWeekCalendar;
    }

    @Override
    public String toString() {
        return name;
    }
}
