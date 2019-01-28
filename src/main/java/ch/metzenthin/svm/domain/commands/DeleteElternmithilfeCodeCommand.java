package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteElternmithilfeCodeCommand implements Command {

    public enum Result {
        CODE_VON_MAERCHENEINTEILUNGEN_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();

    // input
    private List<ElternmithilfeCode> elternmithilfeCodes;
    private int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteElternmithilfeCodeCommand(List<ElternmithilfeCode> elternmithilfeCodes, int indexCodeToBeDeleted) {
        this.elternmithilfeCodes = elternmithilfeCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        ElternmithilfeCode elternmithilfeCodeToBeDeleted = elternmithilfeCodes.get(indexCodeToBeDeleted);
        if (elternmithilfeCodeToBeDeleted.getMaercheneinteilungen().size() > 0) {
            result = Result.CODE_VON_MAERCHENEINTEILUNGEN_REFERENZIERT;
            return;
        }
        elternmithilfeCodeDao.remove(elternmithilfeCodeToBeDeleted);
        elternmithilfeCodes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
