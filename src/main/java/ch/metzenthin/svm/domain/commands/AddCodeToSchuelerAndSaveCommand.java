package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class AddCodeToSchuelerAndSaveCommand extends GenericDaoCommand {

    // input
    private Code code;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public AddCodeToSchuelerAndSaveCommand(Code code, Schueler schueler) {
        this.code = code;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        CodeDao codeDao = new CodeDao(entityManager);
        // Code nachladen wegen Lazy-Loading
        Code codeToBeAdded = codeDao.findById(code.getCodeId());
        schuelerUpdated = codeDao.addToSchuelerAndSave(codeToBeAdded, schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
