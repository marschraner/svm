package ch.metzenthin.svm.domain.commands;

/**
 * @author Martin Schraner
 */
public abstract class CreateListeCommand implements Command {

    public enum Result {
        TEMPLATE_FILE_EXISTIERT_NICHT_ODER_NICHT_LESBAR,
        FEHLER_BEIM_LESEN_DES_PROPERTY_FILE,
        LISTE_ERFOLGREICH_ERSTELLT
    }

    //output
    Result result;

    public Result getResult() {
        return result;
    }
}
