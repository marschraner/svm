package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKursortCommand extends GenericDaoCommand {

    private final KursortDao kursortDao = new KursortDao();

    // input
    private Kursort kursort;
    private Kursort kursortOrigin;
    private List<Kursort> bereitsErfassteKursorte;


    public SaveOrUpdateKursortCommand(Kursort kursort, Kursort kursortOrigin, List<Kursort> bereitsErfassteKursorte) {
        this.kursort = kursort;
        this.kursortOrigin = kursortOrigin;
        this.bereitsErfassteKursorte = bereitsErfassteKursorte;
    }

    @Override
    public void execute() {
        if (kursortOrigin != null) {
            // Update von kursortOrigin mit Werten von kursort
            kursortOrigin.copyAttributesFrom(kursort);
            kursortDao.save(kursortOrigin);
        } else {
            Kursort kursorteSaved = kursortDao.save(kursort);
            bereitsErfassteKursorte.add(kursorteSaved);
        }
        Collections.sort(bereitsErfassteKursorte);
    }

}
