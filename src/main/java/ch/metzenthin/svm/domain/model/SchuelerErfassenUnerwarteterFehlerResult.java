package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;

/**
 * @author Hans Stamm
 */
public class SchuelerErfassenUnerwarteterFehlerResult extends SchuelerErfassenSaveResult {

    private final Throwable e;

    public SchuelerErfassenUnerwarteterFehlerResult(ValidateSchuelerCommand.Result result, Throwable e) {
        super(result);
        this.e = e;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public Throwable getFehler() {
        return e;
    }
}
