package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public class RemoveMitarbeiterCodeFromMitarbeiterCommand extends GenericDaoCommand {

    // input
    private MitarbeiterCode mitarbeiterCodeToBeDeleted;
    private Mitarbeiter mitarbeiter;

    // output
    private Mitarbeiter mitarbeiterUpdated;

    public RemoveMitarbeiterCodeFromMitarbeiterCommand(MitarbeiterCode mitarbeiterCodeToBeDeleleted, Mitarbeiter mitarbeiter) {
        this.mitarbeiterCodeToBeDeleted = mitarbeiterCodeToBeDeleleted;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        mitarbeiterUpdated = mitarbeiterCodeDao.removeFromMitarbeiterAndUpdate(mitarbeiterCodeToBeDeleted, mitarbeiter);
    }

    public Mitarbeiter getMitarbeiterUpdated() {
        return mitarbeiterUpdated;
    }
}
