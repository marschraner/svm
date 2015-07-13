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
   private List<Schueler> schuelerListFound;

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
        schuelerListFound = schuelerDao.findSchueler(schuelerToBeFound);
    }

    public Schueler getSchuelerFound(Schueler schuelerToBeExcluded) {
        if (schuelerListFound.isEmpty()) {
            return null;
        }
        if (schuelerToBeExcluded == null) {
            return schuelerListFound.get(0);
        }
        for (Schueler schuelerFound : schuelerListFound) {
            if (!schuelerFound.getPersonId().equals(schuelerToBeExcluded.getPersonId())) {
                return schuelerFound;
            }
        }
        return null;
    }

}
