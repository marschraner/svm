package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.persistence.entities.Schueler;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.SCHUELER_BEREITS_IN_DATENBANK;

/**
 * @author Hans Stamm
 */
public class SchuelerBereitsInDatenbankResult extends SchuelerErfassenSaveResult {

    private final Schueler schueler;

    public SchuelerBereitsInDatenbankResult(Schueler schueler) {
        super(SCHUELER_BEREITS_IN_DATENBANK);
        this.schueler = schueler;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public String getErrorMessage() {
        return (schueler.getGeschlecht() == Geschlecht.W ? "Die Schülerin" : "Der Schüler") + " ist bereits in der Datenbank gespeichert und kann nicht ein weiteres Mal erfasst werden.";
    }
}
