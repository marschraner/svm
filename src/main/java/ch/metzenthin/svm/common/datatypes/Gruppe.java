package ch.metzenthin.svm.common.datatypes;

/**
 * @author Martin Schraner
 */
public enum Gruppe {
    ALLE(""),
    A("A"),
    B("B");

    private final String name;

    Gruppe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
