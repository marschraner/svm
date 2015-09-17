package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKursCommand extends GenericDaoCommand {

    // input
    private Kurs kurs;
    private final Semester semester;
    private final Kurstyp kurstyp;
    private final Kursort kursort;
    private final Mitarbeiter mitarbeiter1;
    private final Mitarbeiter mitarbeiter2;
    private Kurs kursOrigin;
    private List<Kurs> bereitsErfassteKurse;


    public SaveOrUpdateKursCommand(Kurs kurs, Semester semester, Kurstyp kurstyp, Kursort kursort, Mitarbeiter mitarbeiter1, Mitarbeiter mitarbeiter2, Kurs kursOrigin, List<Kurs> bereitsErfassteKurse) {
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
        KursDao kursDao = new KursDao(entityManager);
        if (kursOrigin != null) {
            // Update von kursOrigin mit Werten von kurs
            int kurslaengeOrigin = kursOrigin.getKurslaenge();
            kursOrigin.copyAttributesFrom(kurs);
            kursOrigin.setSemester(semester);
            kursOrigin.setKurstyp(kurstyp);
            kursOrigin.setKursort(kursort);
            for (Mitarbeiter mitarbeiter : new ArrayList<>(kursOrigin.getLehrkraefte())) {
                kursOrigin.deleteLehrkraft(mitarbeiter);
            }
            kursOrigin.addLehrkraft(mitarbeiter1);
            if (mitarbeiter2 != null) {
                kursOrigin.addLehrkraft(mitarbeiter2);
            }
            kursDao.save(kursOrigin);
            // Semesterrechnungen aktualisieren, falls Kurslänge geändert hat
            if (kurslaengeOrigin != kurs.getKurslaenge()) {
                for (Kursanmeldung kursanmeldung : kursOrigin.getKursanmeldungen()) {
                    Angehoeriger rechnungsempfaenger = kursanmeldung.getSchueler().getRechnungsempfaenger();
                    UpdateWochenbetragCommand updateWochenbetragCommand = new UpdateWochenbetragCommand(rechnungsempfaenger, semester);
                    updateWochenbetragCommand.setEntityManager(entityManager);
                    updateWochenbetragCommand.execute();
                }
            }
        } else {
            kurs.setSemester(semester);
            kurs.setKurstyp(kurstyp);
            kurs.setKursort(kursort);
            kurs.addLehrkraft(mitarbeiter1);
            if (mitarbeiter2 != null) {
                kurs.addLehrkraft(mitarbeiter2);
            }
            Kurs kursSaved = kursDao.save(kurs);
            bereitsErfassteKurse.add(kursSaved);
        }
        Collections.sort(bereitsErfassteKurse);
    }

}
