package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursanmeldungCommand implements Command {

    private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();

    // input
    private List<Kursanmeldung> kursanmeldungen;
    private int indexKursanmeldungToBeDeleted;

    public DeleteKursanmeldungCommand(List<Kursanmeldung> kursanmeldungen, int indexKursanmeldungToBeDeleted) {
        this.kursanmeldungen = kursanmeldungen;
        this.indexKursanmeldungToBeDeleted = indexKursanmeldungToBeDeleted;
    }

    @Override
    public void execute() {
        Kursanmeldung kursanmeldungToBeDeleted = kursanmeldungen.get(indexKursanmeldungToBeDeleted);
        Angehoeriger rechnungsempfaenger = kursanmeldungToBeDeleted.getSchueler().getRechnungsempfaenger();
        Semester semester = kursanmeldungToBeDeleted.getKurs().getSemester();
        kursanmeldungDao.remove(kursanmeldungToBeDeleted);
        kursanmeldungen.remove(indexKursanmeldungToBeDeleted);

        // Semesterrechnung aktualisieren
        UpdateWochenbetragUndAnzWochenCommand updateWochenbetragUndAnzWochenCommand = new UpdateWochenbetragUndAnzWochenCommand(rechnungsempfaenger, semester);
        updateWochenbetragUndAnzWochenCommand.execute();
    }
}
