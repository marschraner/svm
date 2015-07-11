package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class SaveDispensationCommand extends GenericDaoCommand {

    // input
    private Dispensation dispensation;
    private Dispensation dispensationOrigin;
    private Schueler schueler;

    // output
    private Schueler savedSchueler;

    public SaveDispensationCommand(Dispensation dispensation, Dispensation dispensationOrigin, Schueler schueler) {
        this.dispensation = dispensation;
        this.dispensationOrigin = dispensationOrigin;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        if (dispensationOrigin != null) {
            // Update von dispensationOrigin mit Werten von dispensation
            dispensationOrigin.copyAttributesFrom(dispensation);
        } else {
            // Neue Dispensation hinzufügen
            schueler.addDispensation(dispensation);
        }
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        savedSchueler = schuelerDao.save(schueler);
    }

    public Schueler getSavedSchueler() {
        return savedSchueler;
    }
}
