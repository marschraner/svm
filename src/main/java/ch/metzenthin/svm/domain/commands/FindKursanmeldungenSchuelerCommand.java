package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursanmeldungDao;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKursanmeldungenSchuelerCommand implements Command {

    private final KursanmeldungDao kursanmeldungDao = new KursanmeldungDao();

    // input
    private final Schueler schueler;

    // output
    private List<Kursanmeldung> kursanmeldungenFound;

    FindKursanmeldungenSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        kursanmeldungenFound = kursanmeldungDao.findKursanmeldungenSchueler(schueler);
    }

    List<Kursanmeldung> getKursanmeldungenFound() {
        return kursanmeldungenFound;
    }
}
