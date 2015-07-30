package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveCodeFromSchuelerCommand extends GenericDaoCommand {

    // input
    private Code codeToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public RemoveCodeFromSchuelerCommand(Code codeToBeDeleleted, Schueler schueler) {
        this.codeToBeDeleted = codeToBeDeleleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        CodeDao codeDao = new CodeDao(entityManager);
        schuelerUpdated = codeDao.removeFromSchuelerAndUpdate(codeToBeDeleted, schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
