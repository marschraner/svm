package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCommand extends GenericDaoCommand {

    public enum Result {
        SCHUELER_IN_KURSE_EINGESCHRIEBEN,
        SCHUELER_IN_MAERCHEN_EINGETEILT,
        RECHNUNGSEMPFAENGER_HAT_SEMESTERRECHNUNGEN,
        LOESCHEN_ERFOLGREICH
    }

    private final SchuelerDao schuelerDao = new SchuelerDao();

    // input
    private Schueler schueler;

    // output
    private Result result;

    public DeleteSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        if (schueler.getKursanmeldungen().size() > 0) {
            result = Result.SCHUELER_IN_KURSE_EINGESCHRIEBEN;
            return;
        }
        if (schueler.getMaercheneinteilungen().size() > 0) {
            result = Result.SCHUELER_IN_MAERCHEN_EINGETEILT;
            return;
        }
        if (schueler.getRechnungsempfaenger().getSemesterrechnungen().size() > 0 && schueler.getRechnungsempfaenger().getSchuelerRechnungsempfaenger().size() == 1) {
            result = Result.RECHNUNGSEMPFAENGER_HAT_SEMESTERRECHNUNGEN;
            return;
        }
        schuelerDao.remove(schueler);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
