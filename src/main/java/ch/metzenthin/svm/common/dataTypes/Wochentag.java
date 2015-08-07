package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Wochentag {
    ALLE(""),
    MONTAG("Montag"),
    DIENSTAG("Dienstag"),
    MITTWOCH("Mittwoch"),
    DONNERSTAG("Donnerstag"),
    FREITAG("Freitag"),
    SAMSTAG("Samstag");

    private String name;

    Wochentag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
