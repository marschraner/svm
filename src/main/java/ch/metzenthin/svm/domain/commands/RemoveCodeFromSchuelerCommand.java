package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveCodeFromSchuelerCommand extends GenericDaoCommand {

    // input
    private final int indexCodeToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public RemoveCodeFromSchuelerCommand(int indexCodeToBeDeleted, Schueler schueler) {
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        CodeDao codeDao = new CodeDao(entityManager);
        schuelerUpdated = codeDao.removeFromSchuelerAndUpdate(schueler.getCodes().get(indexCodeToBeDeleted), schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
