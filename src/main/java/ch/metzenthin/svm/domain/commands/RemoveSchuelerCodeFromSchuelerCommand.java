package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveSchuelerCodeFromSchuelerCommand implements Command {

    private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

    // input
    private SchuelerCode schuelerCodeToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public RemoveSchuelerCodeFromSchuelerCommand(SchuelerCode schuelerCodeToBeDeleleted, Schueler schueler) {
        this.schuelerCodeToBeDeleted = schuelerCodeToBeDeleleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        schuelerUpdated = schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCodeToBeDeleted, schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
