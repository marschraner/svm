package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMitarbeiterCodeCommand implements Command {

    public enum Result {
        CODE_VON_MITARBEITER_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // input
    private final List<MitarbeiterCode> mitarbeiterCodes;
    private final int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteMitarbeiterCodeCommand(List<MitarbeiterCode> mitarbeiterCodes, int indexCodeToBeDeleted) {
        this.mitarbeiterCodes = mitarbeiterCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
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
