package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenSaveOkResult extends SchuelerErfassenSaveResult {

    public SchuelerErfassenSaveOkResult(ValidateSchuelerCommand.Result result) {
        super(result);
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public String getBeschreibung() {
        return "Der Schüler wurde erfolgreich in der Datenbank gespeichert.";
    }

}
