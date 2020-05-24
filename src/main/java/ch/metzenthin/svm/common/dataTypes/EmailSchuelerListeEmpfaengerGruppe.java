package ch.metzenthin.svm.common.dataTypes;

/**
 * @author Martin Schraner
 */
public enum EmailSchuelerListeEmpfaengerGruppe {
    MUTTER_UND_ODER_VATER("Mutter und/oder Vater"),
    SCHUELER("Schüler (oder Mutter und/oder Vater)"),
    ELTERNMITHILFE("Eltern-Mithilfe"),
    ROLLENLISTE("Rollenliste");

    private final String name;

    EmailSchuelerListeEmpfaengerGruppe(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
