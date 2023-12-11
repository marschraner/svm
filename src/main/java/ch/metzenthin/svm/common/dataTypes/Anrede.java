package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Anrede {
    FRAU("Frau"),
    HERR("Herr"),
    KEINE("Keine");

    private final String name;

    Anrede(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
