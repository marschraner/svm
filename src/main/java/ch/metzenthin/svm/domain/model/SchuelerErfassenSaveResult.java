package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;

/**
 * @author Hans Stamm
 */
public abstract class SchuelerErfassenSaveResult {

    private final ValidateSchuelerCommand.Result result;

    protected SchuelerErfassenSaveResult(ValidateSchuelerCommand.Result result) {
        this.result = result;
    }

    public ValidateSchuelerCommand.Result getResult() {
        return result;
    }

    public abstract void accept(SchuelerErfassenSaveResultVisitor visitor);

}
