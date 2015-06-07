package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.SCHUELER_BEREITS_IN_DATENBANK;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragPasstResult extends SchuelerErfassenSaveResult {

    private Angehoeriger angehoeriger;
    private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;
    private final static String BESCHREIBUNG = "Angehöriger ist bereits in Datenbank"; // todo

    public AngehoerigerEinEintragPasstResult(Angehoeriger angehoeriger, ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt, ValidateSchuelerCommand.Result result) {
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
