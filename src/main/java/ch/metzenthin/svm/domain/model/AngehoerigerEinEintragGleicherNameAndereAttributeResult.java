package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragGleicherNameAndereAttributeResult extends SchuelerErfassenSaveResult {

    private final static String BESCHREIBUNG = "Ein Eintrag passt teilweise"; // todo
    private Angehoeriger angehoeriger;
    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;

    public AngehoerigerEinEintragGleicherNameAndereAttributeResult(Angehoeriger angehoeriger, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoeriger = angehoeriger;
        this.angehoerigenArt = angehoerigenArt;
    }

    public String getAngehoerigerToString() {
        return angehoeriger.toString();
    }

    public String getBeschreibung() {
        return BESCHREIBUNG;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
        return angehoerigenArt;
    }
}
