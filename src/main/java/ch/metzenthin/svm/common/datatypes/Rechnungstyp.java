package ch.metzenthin.svm.common.datatypes;

/**
 * @author Martin Schraner
 */
public enum Rechnungstyp {
    VORRECHNUNG("Vorrechnung"),
    NACHRECHNUNG("Nachrechnung");

    private final String name;

    Rechnungstyp(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
