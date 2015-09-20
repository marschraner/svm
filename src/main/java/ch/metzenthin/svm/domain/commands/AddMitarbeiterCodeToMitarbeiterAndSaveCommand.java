package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public class AddMitarbeiterCodeToMitarbeiterAndSaveCommand extends GenericDaoCommand {

    // input
    private MitarbeiterCode mitarbeiterCode;
    private Mitarbeiter mitarbeiter;

    // output
    private Mitarbeiter mitarbeiterUpdated;

    public AddMitarbeiterCodeToMitarbeiterAndSaveCommand(MitarbeiterCode mitarbeiterCode, Mitarbeiter mitarbeiter) {
        this.mitarbeiterCode = mitarbeiterCode;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        // MitarbeiterCode nachladen wegen Lazy-Loading
        MitarbeiterCode mitarbeiterCodeToBeAdded = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
        mitarbeiterUpdated = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCodeToBeAdded, mitarbeiter);
    }

    public Mitarbeiter getMitarbeiterUpdated() {
        return mitarbeiterUpdated;
    }
}
