package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMitarbeiterCommand extends GenericDaoCommand {

    public enum Result {
        MITARBEITER_VON_KURS_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Mitarbeiter> mitarbeiters;
    int indexMitarbeiterToBeDeleted;

    // output
    private Result result;

    public DeleteMitarbeiterCommand(List<Mitarbeiter> mitarbeiters, int indexMitarbeiterToBeDeleted) {
        this.mitarbeiters = mitarbeiters;
        this.indexMitarbeiterToBeDeleted = indexMitarbeiterToBeDeleted;
    }

    @Override
    public void execute() {
        MitarbeiterDao MitarbeiterDao = new MitarbeiterDao(entityManager);
        Mitarbeiter mitarbeiterToBeDeleted = mitarbeiters.get(indexMitarbeiterToBeDeleted);
        if (mitarbeiterToBeDeleted.getKurse().size() > 0) {
            result = Result.MITARBEITER_VON_KURS_REFERENZIERT;
            return;
        }
        MitarbeiterDao.remove(mitarbeiterToBeDeleted);
        mitarbeiters.remove(indexMitarbeiterToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}