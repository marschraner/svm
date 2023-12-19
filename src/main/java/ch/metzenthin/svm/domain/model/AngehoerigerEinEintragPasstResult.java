package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragPasstResult extends SchuelerErfassenSaveResult {

    private final Angehoeriger angehoerigerFound;
    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;

    public AngehoerigerEinEintragPasstResult(Angehoeriger angehoerigerFound, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoerigerFound = angehoerigerFound;
        this.angehoerigenArt = angehoerigenArt;
    }

    public Angehoeriger getAngehoerigerFound() {
        return angehoerigerFound;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }
}
