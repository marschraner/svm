package ch.metzenthin.svm.common.datatypes;

/**
 * @author Martin Schraner
 */
public enum Elternmithilfe {
    KEINER(""),
    MUTTER("Mutter"),
    VATER("Vater"),
    DRITTPERSON("Drittperson");

    private final String name;

    Elternmithilfe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
