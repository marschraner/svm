package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class AddSchuelerCodeToSchuelerAndSaveCommand extends GenericDaoCommand {

    // input
    private SchuelerCode schuelerCode;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public AddSchuelerCodeToSchuelerAndSaveCommand(SchuelerCode schuelerCode, Schueler schueler) {
        this.schuelerCode = schuelerCode;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);
        // SchuelerCode nachladen wegen Lazy-Loading
        SchuelerCode schuelerCodeToBeAdded = schuelerCodeDao.findById(schuelerCode.getCodeId());
        schuelerUpdated = schuelerCodeDao.addToSchuelerAndSave(schuelerCodeToBeAdded, schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
