package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class DeleteDispensationCommand extends GenericDaoCommand {

    // input
    private final int indexDispensationToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler updatedSchueler;

    public DeleteDispensationCommand(int indexDispensationToBeDeleted, Schueler schueler) {
        this.indexDispensationToBeDeleted = indexDispensationToBeDeleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        schueler.deleteDispensation(schueler.getDispensationen().get(indexDispensationToBeDeleted));
        SchuelerDao schuelerDao = new SchuelerDao(entityManager);
        updatedSchueler = schuelerDao.save(schueler);
    }

    public Schueler getUpdatedSchueler() {
        return updatedSchueler;
    }
}
