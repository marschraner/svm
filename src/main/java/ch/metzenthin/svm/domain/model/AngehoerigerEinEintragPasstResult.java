package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragPasstResult extends SchuelerErfassenSaveResult {

    private Angehoeriger angehoeriger;
    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;

    public AngehoerigerEinEintragPasstResult(Angehoeriger angehoeriger, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoeriger = angehoeriger;
        this.angehoerigenArt = angehoerigenArt;
    }

    public Angehoeriger getAngehoeriger() {
        return angehoeriger;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }
}
