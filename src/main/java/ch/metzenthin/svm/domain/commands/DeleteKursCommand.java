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
    private List<Kurs> kurse;
    int indexKursToBeDeleted;

    // output
    private Result result;

    public DeleteKursCommand(List<Kurs> kurse, int indexKursToBeDeleted) {
        this.kurse = kurse;
        this.indexKursToBeDeleted = indexKursToBeDeleted;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        Kurs kursToBeDeleted = kurse.get(indexKursToBeDeleted);
        if (kursToBeDeleted.getKursanmeldungen().size() > 0) {
            result = Result.KURS_VON_SCHUELER_REFERENZIERT;
            return;
        }
        kursDao.remove(kursToBeDeleted);
        kurse.remove(indexKursToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
