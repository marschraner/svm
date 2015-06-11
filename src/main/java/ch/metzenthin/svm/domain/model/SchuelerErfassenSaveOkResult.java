package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenSaveOkResult extends SchuelerErfassenSaveResult {

    private Geschlecht geschlecht;

    public SchuelerErfassenSaveOkResult(ValidateSchuelerCommand.Result result, Geschlecht geschlecht) {
        super(result);
        this.geschlecht = geschlecht;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public String getBeschreibung() {
        String schuelerStr = (geschlecht == Geschlecht.W ? "Die Schülerin" : "Der Schüler");
        return schuelerStr + " wurde erfolgreich in der Datenbank gespeichert.";
    }

}