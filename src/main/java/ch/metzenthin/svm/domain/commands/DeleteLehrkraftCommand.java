package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LehrkraftDao;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteLehrkraftCommand extends GenericDaoCommand {

    public enum Result {
        LEHRKRAFT_VON_KURS_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Lehrkraft> Lehrkraefte;
    int indexLehrkraftToBeDeleted;

    // output
    private Result result;

    public DeleteLehrkraftCommand(List<Lehrkraft> Lehrkraefte, int indexLehrkraftToBeDeleted) {
        this.Lehrkraefte = Lehrkraefte;
        this.indexLehrkraftToBeDeleted = indexLehrkraftToBeDeleted;
    }

    @Override
    public void execute() {
        LehrkraftDao LehrkraftDao = new LehrkraftDao(entityManager);
        Lehrkraft LehrkraftToBeDeleted = Lehrkraefte.get(indexLehrkraftToBeDeleted);
        if (LehrkraftToBeDeleted.getKurse().size() > 0) {
            result = Result.LEHRKRAFT_VON_KURS_REFERENZIERT;
            return;
        }
        LehrkraftDao.remove(LehrkraftToBeDeleted);
        Lehrkraefte.remove(indexLehrkraftToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
