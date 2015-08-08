package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteElternmithilfeCodeCommand extends GenericDaoCommand {

    public enum Result {
        CODE_VON_MAERCHEN_EINTEILUNG_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<ElternmithilfeCode> elternmithilfeCodes;
    int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteElternmithilfeCodeCommand(List<ElternmithilfeCode> elternmithilfeCodes, int indexCodeToBeDeleted) {
        this.elternmithilfeCodes = elternmithilfeCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);
        ElternmithilfeCode elternmithilfeCodeToBeDeleted = elternmithilfeCodes.get(indexCodeToBeDeleted);
//        if (elternmithilfeCodeToBeDeleted.getMaerchenEinteilungen().size() > 0) {
//            result = Result.CODE_VON_MAERCHEN_REFERENZIERT;
//            return;
//        }
        elternmithilfeCodeDao.remove(elternmithilfeCodeToBeDeleted);
        elternmithilfeCodes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
