package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Geschlecht {
    W("weiblich"),
    M("männlich");

    private String name;

    Geschlecht(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
