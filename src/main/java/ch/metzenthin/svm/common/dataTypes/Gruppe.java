package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Gruppe {
    ALLE(""),
    A("a"),
    B("b");

    private String name;

    Gruppe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
