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
    private final static String BESCHREIBUNG = "Angehöriger ist bereits in Datenbank"; // todo

    public AngehoerigerEinEintragPasstResult(Angehoeriger angehoeriger, ValidateSchuelerCommand.Result result) {
        super(result);
        this.angehoeriger = angehoeriger;
    }

    public String getAngehoerigerToString() {
        return angehoeriger.toString();
    }

    public static String getBESCHREIBUNG() {
        return BESCHREIBUNG;
    }

}
