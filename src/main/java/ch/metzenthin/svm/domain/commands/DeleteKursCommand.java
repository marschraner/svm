package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursCommand extends GenericDaoCommand {

    // input
    private List<Kurs> kurse;
    int indexKursToBeDeleted;

    public DeleteKursCommand(List<Kurs> kurse, int indexKursToBeDeleted) {
        this.kurse = kurse;
        this.indexKursToBeDeleted = indexKursToBeDeleted;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        Kurs kursToBeDeleted = kurse.get(indexKursToBeDeleted);
        Semester semester = kursToBeDeleted.getSemester();

        // Kursanmeldungen löschen
        for (Kursanmeldung kursanmeldungenToBeDeleted : kursToBeDeleted.getKursanmeldungen()) {
            Angehoeriger rechnungsempfaenger = kursanmeldungenToBeDeleted.getSchueler().getRechnungsempfaenger();
            kursanmeldungDao.remove(kursanmeldungenToBeDeleted);

            // Semesterrechnung aktualisieren
            UpdateWochenbetragCommand updateWochenbetragCommand = new UpdateWochenbetragCommand(rechnungsempfaenger, semester);
            updateWochenbetragCommand.setEntityManager(entityManager);
            updateWochenbetragCommand.execute();
        }

        kursDao.remove(kursToBeDeleted);
        kurse.remove(indexKursToBeDeleted);
    }
}
