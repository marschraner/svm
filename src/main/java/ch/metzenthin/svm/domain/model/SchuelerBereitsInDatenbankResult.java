package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Schueler;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.SCHUELER_BEREITS_IN_DATENBANK;

/**
 * @author Hans Stamm
 */
public class SchuelerBereitsInDatenbankResult extends SchuelerErfassenSaveResult {

    private Schueler schueler;
    private final static String BESCHREIBUNG = "Schüler ist bereits in Datenbank"; // todo

    public SchuelerBereitsInDatenbankResult(Schueler schueler) {
        super(SCHUELER_BEREITS_IN_DATENBANK);
        this.schueler = schueler;
    }

    public String getSchuelerToString() {
        return schueler.toString();
    }

    public static String getBESCHREIBUNG() {
        return BESCHREIBUNG;
    }

}
