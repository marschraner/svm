package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult extends SchuelerErfassenSaveResult {

    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;
    private Angehoeriger angehoerigerErfasst;
    private List<Angehoeriger> angehoerigeFoundInDatabase = new ArrayList<>();

    public AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult(Angehoeriger angehoerigerErfasst, List<Angehoeriger> angehoerigeFoundInDatabase, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoerigerErfasst = angehoerigerErfasst;
        this.angehoerigeFoundInDatabase = angehoerigeFoundInDatabase;
        this.angehoerigenArt = angehoerigenArt;

    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }

    public Angehoeriger getAngehoerigerErfasst() {
        return angehoerigerErfasst;
    }

    public List<Angehoeriger> getAngehoerigeFoundInDatabase() {
        return angehoerigeFoundInDatabase;
    }
}
