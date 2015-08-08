package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenCodeDao;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCodeCommand extends GenericDaoCommand {

    public enum Result {
        CODE_VON_MAERCHEN_EINTEILUNG_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<MaerchenCode> maerchenCodes;
    int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteMaerchenCodeCommand(List<MaerchenCode> maerchenCodes, int indexCodeToBeDeleted) {
        this.maerchenCodes = maerchenCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);
        MaerchenCode maerchenCodeToBeDeleted = maerchenCodes.get(indexCodeToBeDeleted);
//        if (maerchenCodeToBeDeleted.getMaerchenEinteilungen().size() > 0) {
//            result = Result.CODE_VON_MAERCHEN_REFERENZIERT;
//            return;
//        }
        maerchenCodeDao.remove(maerchenCodeToBeDeleted);
        maerchenCodes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
