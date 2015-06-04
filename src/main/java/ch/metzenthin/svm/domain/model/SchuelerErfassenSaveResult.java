package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;

/**
 * @author Hans Stamm
 */
public abstract class SchuelerErfassenSaveResult {

    private ValidateSchuelerCommand.Result result;

    public SchuelerErfassenSaveResult(ValidateSchuelerCommand.Result result) {
        this.result = result;
    }

    public ValidateSchuelerCommand.Result getResult() {
        return result;
    }

}
