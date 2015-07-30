package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveKursFromSchuelerCommand extends GenericDaoCommand {

    // input
    private Kurs kursToBeDeleted;
    private Schueler schueler;

    // output
    private Schueler schuelerUpdated;

    public RemoveKursFromSchuelerCommand(Kurs kursToBeDeleted, Schueler schueler) {
        this.kursToBeDeleted = kursToBeDeleted;
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        schuelerUpdated = kursDao.removeFromSchuelerAndUpdate(kursToBeDeleted, schueler);
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
