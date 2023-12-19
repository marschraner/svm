package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.List;

/**
 * @author Hans Stamm
 */
public class AngehoerigerMehrereEintraegePassenResult extends SchuelerErfassenSaveResult {

    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;
    private final List<Angehoeriger> angehoerigeFound;

    public AngehoerigerMehrereEintraegePassenResult(List<Angehoeriger> angehoerigeFound, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoerigenArt = angehoerigenArt;
        this.angehoerigeFound = angehoerigeFound;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }

    public List<Angehoeriger> getAngehoerigeFound() {
        return angehoerigeFound;
    }

}
