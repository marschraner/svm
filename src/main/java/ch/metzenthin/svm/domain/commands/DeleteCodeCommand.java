package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteCodeCommand extends GenericDaoCommand {

    public enum Result {
        CODE_VON_SCHUELER_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Code> codes;
    int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteCodeCommand(List<Code> codes, int indexCodeToBeDeleted) {
        this.codes = codes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        CodeDao codeDao = new CodeDao(entityManager);
        Code codeToBeDeleted = codes.get(indexCodeToBeDeleted);
        if (codeToBeDeleted.getSchueler().size() > 0) {
            result = Result.CODE_VON_SCHUELER_REFERENZIERT;
            return;
        }
        codeDao.remove(codeToBeDeleted);
        codes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
