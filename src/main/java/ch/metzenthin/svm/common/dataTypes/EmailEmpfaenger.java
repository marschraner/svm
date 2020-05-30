package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum EmailEmpfaenger {
    MUTTER("Mutter"),
    MUTTER_UND_VATER("Mutter und Vater"),
    VATER("Vater"),
    RECHNUNGSEMPFAENGER("Rechnungsempfänger"),
    SCHUELER("Schüler");

    private final String name;

    EmailEmpfaenger(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
