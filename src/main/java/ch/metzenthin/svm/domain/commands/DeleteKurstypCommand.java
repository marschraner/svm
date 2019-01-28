package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKurstypCommand extends GenericDaoCommand {

    public enum Result {
        KURSTYP_VON_KURS_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final KurstypDao kurstypDao = new KurstypDao();

    // input
    private List<Kurstyp> kurstypen;
    private int indexKurstypToBeDeleted;

    // output
    private Result result;

    public DeleteKurstypCommand(List<Kurstyp> kurstypen, int indexKurstypToBeDeleted) {
        this.kurstypen = kurstypen;
        this.indexKurstypToBeDeleted = indexKurstypToBeDeleted;
    }

    @Override
    public void execute() {
        Kurstyp kurstypToBeDeleted = kurstypen.get(indexKurstypToBeDeleted);
        if (kurstypToBeDeleted.getKurse().size() > 0) {
            result = Result.KURSTYP_VON_KURS_REFERENZIERT;
            return;
        }
        kurstypDao.remove(kurstypToBeDeleted);
        kurstypen.remove(indexKurstypToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
