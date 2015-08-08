package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCommand extends GenericDaoCommand {

    public enum Result {
        MAERCHEN_VON_SCHUELER_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Maerchen> Maerchens;
    int indexMaerchenToBeDeleted;

    // output
    private Result result;

    public DeleteMaerchenCommand(List<Maerchen> Maerchens, int indexMaerchenToBeDeleted) {
        this.Maerchens = Maerchens;
        this.indexMaerchenToBeDeleted = indexMaerchenToBeDeleted;
    }

    @Override
    public void execute() {
        MaerchenDao MaerchenDao = new MaerchenDao(entityManager);
        Maerchen MaerchenToBeDeleted = Maerchens.get(indexMaerchenToBeDeleted);
//        if (MaerchenToBeDeleted.getSchueler().size() > 0) {
//            result = Result.MAERCHEN_VON_SCHUELER_REFERENZIERT;
//            return;
//        }
        MaerchenDao.remove(MaerchenToBeDeleted);
        Maerchens.remove(indexMaerchenToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
