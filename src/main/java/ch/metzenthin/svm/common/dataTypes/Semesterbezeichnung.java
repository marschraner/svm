package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Semesterbezeichnung {
    ERSTES_SEMESTER("1. Semester", "1"),
    ZWEITES_SEMESTER("2. Semester", "2");

    private String name;
    private String kuerzelInTemplateFile;

    Semesterbezeichnung(String name, String kuerzelInTemplateFile) {
        this.name = name;
        this.kuerzelInTemplateFile = kuerzelInTemplateFile;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getKuerzelInTemplateFile() {
        return kuerzelInTemplateFile;
    }
}
