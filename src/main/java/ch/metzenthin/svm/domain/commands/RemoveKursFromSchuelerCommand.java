package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveKursFromSchuelerCommand extends GenericDaoCommand {

    // input
    private final int indexKursToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public RemoveKursFromSchuelerCommand(int indexKursToBeDeleted, Schueler schueler) {
        this.indexKursToBeDeleted = indexKursToBeDeleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        schuelerUpdated = kursDao.removeFromSchuelerAndUpdate(schueler.getKurse().get(indexKursToBeDeleted), schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
