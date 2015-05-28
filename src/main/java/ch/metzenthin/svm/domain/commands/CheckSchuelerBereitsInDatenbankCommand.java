package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckSchuelerBereitsInDatenbankCommand extends GenericDaoCommand {

    enum Result {
        NICHT_IN_DATENBANK,                     // Alles i.O.
        EIN_EINTRAG_PASST,                      // In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: ...
                                                // Schüler kann nicht neu erfasst werden!
                                                // - Ok
        EIN_EINTRAG_PASST_TEILWEISE,            // In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: ...
                                                // - Nicht diesen Eintrag verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen
                                                // - Schüler nicht neu erfassen und abbrechen
        MEHRERE_EINTRAEGE_PASSEN_TEILWEISE,     // In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ...
                                                // - Keinen dieser Einträge verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen
                                                // - Schüler nicht neu erfassen und abbrechen
    }

    // input
    private Schueler schueler;

    // output
    private Result result;
    private Schueler schuelerFound;
    private List<Schueler> schuelerListFound;

    public CheckSchuelerBereitsInDatenbankCommand(Schueler schueler) {
        this.schueler = schueler;
    }

    @Override
    public void execute() {

        SchuelerDao schuelerDao = new SchuelerDao(entityManager);

        // Suche mit allen gesetzten Attributen
        schuelerListFound = schuelerDao.findSchueler(schueler);

        if (schuelerListFound != null && schuelerListFound.size() == 1) {
            schueler = schuelerListFound.get(0);
            result = Result.EIN_EINTRAG_PASST;
            return;
        }

        // Suche nur mit Vorname und Nachname
        Schueler angehoerigerNurVornameNachname = new Schueler(schueler.getVorname(), schueler.getNachname(), null, null, null, null, null, null, null);

        schuelerListFound = schuelerDao.findSchueler(angehoerigerNurVornameNachname);

        if (schuelerListFound != null && schuelerListFound.size() == 1) {
            schuelerFound = schuelerListFound.get(0);
            result = Result.EIN_EINTRAG_PASST_TEILWEISE;
            return;
        }

        else if (schuelerListFound != null && schuelerListFound.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN_TEILWEISE;
            return;
        }

        // Nicht gefunden
        result = Result.NICHT_IN_DATENBANK;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public Result getResult() {
        return result;
    }

    public Schueler getSchuelerFound() {
        return schuelerFound;
    }

    public List<Schueler> getSchuelerListFound() {
        return schuelerListFound;
    }
}
