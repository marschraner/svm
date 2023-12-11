package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Geschlecht {
    W("weiblich"),
    M("männlich");

    private final String name;

    Geschlecht(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
