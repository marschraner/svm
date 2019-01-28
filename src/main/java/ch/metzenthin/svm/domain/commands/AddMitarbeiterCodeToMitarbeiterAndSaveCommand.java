package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public class AddMitarbeiterCodeToMitarbeiterAndSaveCommand extends GenericDaoCommand {

    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // input
    private MitarbeiterCode mitarbeiterCode;
    private Mitarbeiter mitarbeiter;

    // output
    private Mitarbeiter mitarbeiterUpdated;

    AddMitarbeiterCodeToMitarbeiterAndSaveCommand(MitarbeiterCode mitarbeiterCode, Mitarbeiter mitarbeiter) {
        this.mitarbeiterCode = mitarbeiterCode;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        // MitarbeiterCode nachladen wegen Lazy-Loading
        MitarbeiterCode mitarbeiterCodeToBeAdded = mitarbeiterCodeDao.findById(mitarbeiterCode.getCodeId());
        mitarbeiterUpdated = mitarbeiterCodeDao.addToMitarbeiterAndSave(mitarbeiterCodeToBeAdded, mitarbeiter);
    }

    Mitarbeiter getMitarbeiterUpdated() {
        return mitarbeiterUpdated;
    }
}
