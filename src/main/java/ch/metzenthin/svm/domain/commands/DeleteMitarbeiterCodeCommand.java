package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMitarbeiterCodeCommand extends GenericDaoCommand {

    public enum Result {
        CODE_VON_MITARBEITER_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<MitarbeiterCode> mitarbeiterCodes;
    int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteMitarbeiterCodeCommand(List<MitarbeiterCode> mitarbeiterCodes, int indexCodeToBeDeleted) {
        this.mitarbeiterCodes = mitarbeiterCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        MitarbeiterCode mitarbeiterCodeToBeDeleted = mitarbeiterCodes.get(indexCodeToBeDeleted);
        if (mitarbeiterCodeToBeDeleted.getMitarbeiters().size() > 0) {
            result = Result.CODE_VON_MITARBEITER_REFERENZIERT;
            return;
        }
        mitarbeiterCodeDao.remove(mitarbeiterCodeToBeDeleted);
        mitarbeiterCodes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
