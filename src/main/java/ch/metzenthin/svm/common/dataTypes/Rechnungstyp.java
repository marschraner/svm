package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Rechnungstyp {
    VORRECHNUNG("Vorrechnung"),
    NACHRECHNUNG("Nachrechnung");

    private String name;

    Rechnungstyp(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
