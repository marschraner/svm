package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Anrede {
    HERR("Herr"),
    FRAU("Frau"),
    KEINE("Keine");

    private String name;

    Anrede(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
