package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.DispensationDao;
import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class AddDispensationToSchuelerAndSaveCommand extends GenericDaoCommand {

    private final SchuelerDao schuelerDao = new SchuelerDao();
    private final DispensationDao dispensationDao = new DispensationDao();

    // input
    private Dispensation dispensation;
    private Dispensation dispensationOrigin;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public AddDispensationToSchuelerAndSaveCommand(Dispensation dispensation, Dispensation dispensationOrigin, Schueler schueler) {
        this.dispensation = dispensation;
        this.dispensationOrigin = dispensationOrigin;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        if (dispensationOrigin != null) {
            // Update von dispensationOrigin mit Werten von dispensation
            dispensationOrigin.copyAttributesFrom(dispensation);
            schuelerUpdated = schuelerDao.save(schueler);
        } else {
            schuelerUpdated = dispensationDao.addToSchuelerAndSave(dispensation, schueler);
        }

    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
