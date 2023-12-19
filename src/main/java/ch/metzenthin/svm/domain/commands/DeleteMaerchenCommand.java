package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCommand implements Command {

    public enum Result {
        MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final MaerchenDao maerchenDao = new MaerchenDao();

    // input
    private final List<Maerchen> maerchens;
    private final int indexMaerchenToBeDeleted;

    // output
    private Result result;

    public DeleteMaerchenCommand(List<Maerchen> Maerchens, int indexMaerchenToBeDeleted) {
        this.maerchens = Maerchens;
        this.indexMaerchenToBeDeleted = indexMaerchenToBeDeleted;
    }

    @Override
    public void execute() {
        Maerchen MaerchenToBeDeleted = maerchens.get(indexMaerchenToBeDeleted);
        if (MaerchenToBeDeleted.getMaercheneinteilungen().size() > 0) {
            result = Result.MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT;
            return;
        }
        maerchenDao.remove(MaerchenToBeDeleted);
        maerchens.remove(indexMaerchenToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
