package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Geschlecht {
    M("männlich"),
    W("weiblich");

    private String name;

    Geschlecht(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
