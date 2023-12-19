package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AnmeldungDao;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class RemoveFruehereAnmeldungenFromSchuelerCommand implements Command {

    private final AnmeldungDao anmeldungDao = new AnmeldungDao();

    // input
    private final Schueler schueler;

    public RemoveFruehereAnmeldungenFromSchuelerCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {
        List<Anmeldung> fruehereAnmeldungen = new ArrayList<>();
        for (int i = 1; i < schueler.getAnmeldungen().size(); i++) {
            fruehereAnmeldungen.add(schueler.getAnmeldungen().get(i));
        }
        // Loeschen
        Iterator<Anmeldung> it = fruehereAnmeldungen.iterator();
        while (it.hasNext()) {
            Anmeldung anmeldungToBeDeleted = it.next();
            it.remove();
            anmeldungDao.removeFromSchuelerAndUpdate(anmeldungToBeDeleted, schueler);
        }
    }
}
