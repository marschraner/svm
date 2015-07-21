package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursortCommand extends GenericDaoCommand {

    public enum Result {
        KURSORT_VON_KURS_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Kursort> kursorte;
    int indexKursortToBeDeleted;

    // output
    private Result result;

    public DeleteKursortCommand(List<Kursort> kursorte, int indexKursortToBeDeleted) {
        this.kursorte = kursorte;
        this.indexKursortToBeDeleted = indexKursortToBeDeleted;
    }

    @Override
    public void execute() {
        KursortDao kursortDao = new KursortDao(entityManager);
        Kursort kursortToBeDeleted = kursorte.get(indexKursortToBeDeleted);
//        if (kursortToBeDeleted.getKurse().size() > 0) {
//            result = Result.KURSORT_VON_KURS_REFERENZIERT;
//            return;
//        }
        kursortDao.remove(kursortToBeDeleted);
        kursorte.remove(indexKursortToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}