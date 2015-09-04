package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKursanmeldungenSchuelerCommand extends GenericDaoCommand {

    // input
    private Schueler schueler;

    // output
    private List<Kursanmeldung> kursanmeldungenFound;

    public FindKursanmeldungenSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        KursanmeldungDao kursanmeldungDao = new KursanmeldungDao(entityManager);
        kursanmeldungenFound = kursanmeldungDao.findKursanmeldungenSchueler(schueler);
    }

    public List<Kursanmeldung> getKursanmeldungenFound() {
        return kursanmeldungenFound;
    }
}
