package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Schueler;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.SCHUELER_BEREITS_IN_DATENBANK;

/**
 * @author Hans Stamm
 */
public class SchuelerBereitsInDatenbankResult extends SchuelerErfassenSaveResult {

    private Schueler schueler;

    public SchuelerBereitsInDatenbankResult(Schueler schueler) {
        super(SCHUELER_BEREITS_IN_DATENBANK);
        this.schueler = schueler;
    }

    @Override
    public void accept(SchuelerErfassenSaveResultVisitor visitor) {
        visitor.visit(this);
    }

    public Schueler getSchueler() {
        return schueler;
    }

}
