package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public class RemoveMitarbeiterCodeFromMitarbeiterCommand implements Command {

    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // input
    private final MitarbeiterCode mitarbeiterCodeToBeDeleted;
    private final Mitarbeiter mitarbeiter;

    // output
    private Mitarbeiter mitarbeiterUpdated;

    RemoveMitarbeiterCodeFromMitarbeiterCommand(MitarbeiterCode mitarbeiterCodeToBeDeleted, Mitarbeiter mitarbeiter) {
        this.mitarbeiterCodeToBeDeleted = mitarbeiterCodeToBeDeleted;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        mitarbeiterUpdated = mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCodeToBeDeleted, mitarbeiter);
    }

    Mitarbeiter getMitarbeiterUpdated() {
        return mitarbeiterUpdated;
    }
}
