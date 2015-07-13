package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Adresse;
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

        // Suche nach Vorname, Nachname, Geburtsdatum, Strasse, Hausnummer, PLZ, Ort
        Schueler schuelerToBeFound = new Schueler(schueler.getVorname(), schueler.getNachname(), schueler.getGeburtsdatum(), null, null, null, null, null);
        Adresse adresseToBeFound = new Adresse(schueler.getAdresse().getStrasse(), schueler.getAdresse().getHausnummer(), schueler.getAdresse().getPlz(), schueler.getAdresse().getOrt());
        schuelerToBeFound.setAdresse(adresseToBeFound);
        List<Schueler> schuelerListFound = schuelerDao.findSchueler(schuelerToBeFound);

        if (!schuelerListFound.isEmpty()) {
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
