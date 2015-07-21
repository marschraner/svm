package ch.metzenthin.svm.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Semesterbezeichnung {
    ERSTES_SEMESTER("1. Semester"),
    ZWEITES_SEMESTER("2. Semester");

    private String name;

    Semesterbezeichnung(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
