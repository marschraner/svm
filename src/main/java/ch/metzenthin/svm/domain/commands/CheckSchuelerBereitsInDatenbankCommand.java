package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckSchuelerBereitsInDatenbankCommand extends GenericDaoCommand {

    // input
    private Schueler schueler;

    // output
    private boolean inDatenbank;
    private Schueler schuelerFound;

    public CheckSchuelerBereitsInDatenbankCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {

        SchuelerDao schuelerDao = new SchuelerDao(entityManager);

        // Suche mit allen gesetzten Attributen
        List<Schueler> schuelerListFound = schuelerDao.findSchueler(schueler);

        if (schuelerListFound != null) {
            schuelerFound = schuelerListFound.get(0);
            inDatenbank = true;
            return;
        }

        // Nicht gefunden
        inDatenbank = false;
    }

    public boolean isInDatenbank() {
        return inDatenbank;
    }

    public Schueler getSchuelerFound() {
        return schuelerFound;
    }
}
