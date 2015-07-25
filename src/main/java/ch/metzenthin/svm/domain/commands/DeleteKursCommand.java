package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursCommand extends GenericDaoCommand {

    public enum Result {
        KURS_VON_SCHUELER_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Kurs> Kurs;
    int indexKursToBeDeleted;

    // output
    private Result result;

    public DeleteKursCommand(List<Kurs> Kurs, int indexKursToBeDeleted) {
        this.Kurs = Kurs;
        this.indexKursToBeDeleted = indexKursToBeDeleted;
    }

    @Override
    public void execute() {
        KursDao KursDao = new KursDao(entityManager);
        Kurs KursToBeDeleted = Kurs.get(indexKursToBeDeleted);
//        if (KursToBeDeleted.getSchueler().size() > 0) {
//            result = Result.KURS_VON_SCHUELER_REFERENZIERT;
//            return;
//        }
        KursDao.remove(KursToBeDeleted);
        Kurs.remove(indexKursToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
