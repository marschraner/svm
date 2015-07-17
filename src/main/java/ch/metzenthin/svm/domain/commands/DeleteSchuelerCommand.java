package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCommand extends GenericDaoCommand {

    private Schueler schueler;

    public enum Result {
        SCHUELER_IN_KURSE_EINGESCHRIEBEN,
        LOESCHEN_ERFOLGREICH
    }

    // output
    private Result result;

    public DeleteSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        //TODO
//        if (schueler.getKurse().size() > 0) {
//            result = Result.SCHUELER_IN_KURSE_EINGESCHRIEBEN;
//            return;
//        }
        schuelerDao.remove(schueler);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
