package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Elternteil {
    MUTTER("Mutter"),
    VATER("Vater");

    private String name;

    Elternteil(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
