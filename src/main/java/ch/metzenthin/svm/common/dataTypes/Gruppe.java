package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Gruppe {
    ALLE(""),
    A("A"),
    B("B");

    private String name;

    Gruppe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
