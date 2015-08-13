package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum EmailEmpfaenger {
    SCHUELER("Schüler"),
    MUTTER("Mutter"),
    VATER("Vater"),
    RECHNUNGSEMPFAENGER("Rechnungsempfänger");

    private String name;

    EmailEmpfaenger(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
