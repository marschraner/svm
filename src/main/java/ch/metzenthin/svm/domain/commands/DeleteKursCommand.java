package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteKursCommand implements Command {

  private final KursDao kursDao = new KursDao();
  private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();

  // input
  private final List<Kurs> kurse;
  private final int indexKursToBeDeleted;

  public DeleteKursCommand(List<Kurs> kurse, int indexKursToBeDeleted) {
    this.kurse = kurse;
    this.indexKursToBeDeleted = indexKursToBeDeleted;
  }

  @Override
  public void execute() {
    Kurs kursToBeDeleted = kurse.get(indexKursToBeDeleted);
    Semester semester = kursToBeDeleted.getSemester();

    // Kursanmeldungen löschen
    Iterator<Kursanmeldung> it = kursToBeDeleted.getKursanmeldungen().iterator();
    while (it.hasNext()) {
      Kursanmeldung kursanmeldungToBeDeleted = it.next();
      Angehoeriger rechnungsempfaenger =
          kursanmeldungToBeDeleted.getSchueler().getRechnungsempfaenger();
      it.remove();
      kursanmeldungDao.remove(kursanmeldungToBeDeleted);

      // Semesterrechnung aktualisieren
      UpdateWochenbetragUndAnzWochenCommand updateWochenbetragUndAnzWochenCommand =
          new UpdateWochenbetragUndAnzWochenCommand(rechnungsempfaenger, semester);
      updateWochenbetragUndAnzWochenCommand.execute();
    }

    kursDao.remove(kursToBeDeleted);
    kurse.remove(indexKursToBeDeleted);
  }
}
