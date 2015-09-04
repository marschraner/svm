package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.KursanmeldungId;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKursanmeldungCommand extends GenericDaoCommand {

    // input
    private Kursanmeldung kursanmeldung;
    private Kursanmeldung kursanmeldungOrigin;
    private List<Kursanmeldung> bereitsErfassteKursanmeldungen;


    public SaveOrUpdateKursanmeldungCommand(Kursanmeldung kursanmeldung, Kursanmeldung kursanmeldungOrigin, List<Kursanmeldung> bereitsErfassteKursanmeldungen) {
        this.kursanmeldung = kursanmeldung;
        this.kursanmeldungOrigin = kursanmeldungOrigin;
        this.bereitsErfassteKursanmeldungen = bereitsErfassteKursanmeldungen;
    }

    @Override
    public void execute() {
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        if (kursanmeldungOrigin != null) {
            // Update von kurseinteilungOrigin mit Werten von kurseinteilung
            kursanmeldungOrigin.copyAttributesFrom(kursanmeldung);
            // Reload zur Verhinderung von Lazy Loading-Problem
            Kursanmeldung kursanmeldungOriginReloaded = kursanmeldungDao.findById(new KursanmeldungId(kursanmeldungOrigin.getSchueler().getPersonId(), kursanmeldungOrigin.getKurs().getKursId()));
            kursanmeldungDao.save(kursanmeldungOriginReloaded);
        } else {
            Kursanmeldung kursanmeldungSaved = kursanmeldungDao.save(kursanmeldung);
            bereitsErfassteKursanmeldungen.add(kursanmeldungSaved);
        }
        Collections.sort(bereitsErfassteKursanmeldungen);
    }

}
