package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.daos.KursLehrkraftDao;
import ch.metzenthin.svm.persistence.entities.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKursCommand implements Command {

  private final KursDao kursDao = new KursDao();
  private final KursLehrkraftDao kursLehrkraftDao = new KursLehrkraftDao();

  // input
  private final Kurs kurs;
  private final Semester semester;
  private final Kurstyp kurstyp;
  private final Kursort kursort;
  private final Mitarbeiter mitarbeiter1;
  private final Mitarbeiter mitarbeiter2;
  private final Kurs kursOrigin;
  private final List<Kurs> bereitsErfassteKurse;

  @SuppressWarnings("java:S107")
  public SaveOrUpdateKursCommand(
      Kurs kurs,
      Semester semester,
      Kurstyp kurstyp,
      Kursort kursort,
      Mitarbeiter mitarbeiter1,
      Mitarbeiter mitarbeiter2,
      Kurs kursOrigin,
      List<Kurs> bereitsErfassteKurse) {
    this.kurs = kurs;
    this.semester = semester;
    this.kurstyp = kurstyp;
    this.kursort = kursort;
    this.mitarbeiter1 = mitarbeiter1;
    this.mitarbeiter2 = mitarbeiter2;
    this.kursOrigin = kursOrigin;
    this.bereitsErfassteKurse = bereitsErfassteKurse;
  }

  @Override
  public void execute() {
    if (kursOrigin != null) {
      // Update von kursOrigin mit Werten von kurs
      int kurslaengeOrigin = kursOrigin.getKurslaenge();
      kursOrigin.copyAttributesFrom(kurs);
      kursOrigin.setSemester(semester);
      kursOrigin.setKurstyp(kurstyp);
      kursOrigin.setKursort(kursort);
      List<KursLehrkraft> kursLehrkraefteOrigin =
          kursLehrkraftDao.findKursLehrkraefteByKursId(kursOrigin.getKursId());
      for (KursLehrkraft kursLehrkraft : kursLehrkraefteOrigin) {
        kursLehrkraftDao.remove(kursLehrkraft);
      }
      // bereits nach Löschen (ein erstes Mal) speichern, weil sonst Exception, falls Reihenfolge
      // der Lehrkräfte vertauscht werden
      kursDao.save(kursOrigin);
      KursLehrkraft kursLehrkraft1Origin = new KursLehrkraft(kursOrigin, mitarbeiter1, 0);
      kursLehrkraftDao.save(kursLehrkraft1Origin);
      if (mitarbeiter2 != null) {
        KursLehrkraft kursLehrkraft2Origin = new KursLehrkraft(kursOrigin, mitarbeiter2, 1);
        kursLehrkraftDao.save(kursLehrkraft2Origin);
      }
      kursDao.save(kursOrigin);
      // Semesterrechnungen aktualisieren, falls Kurslänge geändert hat
      if (kurslaengeOrigin != kurs.getKurslaenge()) {
        for (Kursanmeldung kursanmeldung : kursOrigin.getKursanmeldungen()) {
          Angehoeriger rechnungsempfaenger = kursanmeldung.getSchueler().getRechnungsempfaenger();
          UpdateWochenbetragUndAnzWochenCommand updateWochenbetragUndAnzWochenCommand =
              new UpdateWochenbetragUndAnzWochenCommand(rechnungsempfaenger, semester);
          updateWochenbetragUndAnzWochenCommand.execute();
        }
      }
    } else {
      kurs.setSemester(semester);
      kurs.setKurstyp(kurstyp);
      kurs.setKursort(kursort);
      KursLehrkraft kursLehrkraft1 = new KursLehrkraft(kurs, mitarbeiter1, 0);
      kursLehrkraftDao.save(kursLehrkraft1);
      if (mitarbeiter2 != null) {
        KursLehrkraft kursLehrkraft2 = new KursLehrkraft(kurs, mitarbeiter2, 1);
        kursLehrkraftDao.save(kursLehrkraft2);
      }
      Kurs kursSaved = kursDao.save(kurs);
      bereitsErfassteKurse.add(kursSaved);
    }
    Collections.sort(bereitsErfassteKurse);
  }
}
