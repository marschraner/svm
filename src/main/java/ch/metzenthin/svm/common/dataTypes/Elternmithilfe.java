package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum Elternmithilfe {
    KEINER(""),
    MUTTER("Mutter"),
    VATER("Vater"),
    DRITTPERSON("Drittperson");

    private String name;

    Elternmithilfe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
