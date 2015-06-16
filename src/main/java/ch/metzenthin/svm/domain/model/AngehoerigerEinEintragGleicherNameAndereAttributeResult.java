package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragGleicherNameAndereAttributeResult extends SchuelerErfassenSaveResult {

    private Angehoeriger angehoerigerErfasst;
    private Angehoeriger angehoerigerFoundInDatabase;
    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;

    public AngehoerigerEinEintragGleicherNameAndereAttributeResult(Angehoeriger angehoerigerErfasst, Angehoeriger angehoerigerFoundInDatabase, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoerigerErfasst = angehoerigerErfasst;
        this.angehoerigerFoundInDatabase = angehoerigerFoundInDatabase;
        this.angehoerigenArt = angehoerigenArt;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public Angehoeriger getAngehoerigerErfasst() {
        return angehoerigerErfasst;
    }

    public Angehoeriger getAngehoerigerFoundInDatabase() {
        return angehoerigerFoundInDatabase;
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }
}
