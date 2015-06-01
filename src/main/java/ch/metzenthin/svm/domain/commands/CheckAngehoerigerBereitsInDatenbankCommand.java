package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckAngehoerigerBereitsInDatenbankCommand extends GenericDaoCommand {

    enum Result {
        NICHT_IN_DATENBANK,                     // Angehöriger wird neu erfasst (noch nicht in Datenbank)
                                                // - Ok (-> bisherigen Angehoerigen verwenden)
                                                // - Abbrechen (-> Eingabe-GUI)
        EIN_EINTRAG_PASST,                      // In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: ...
                                                // - Diesen Eintrag verwenden (-> Angehoerigen ersetzen)
                                                // - Eingaben korrigieren (-> Eingabe-GUI)
        MEHRERE_EINTRAEGE_PASSEN,               // In der Datenbank wurden mehrere Einträge gefunden, die auf die erfassten Angaben passen: ...
                                                // Angehöriger muss genauer erfasst werden
                                                // - Eingaben korrigieren (-> Eingabe-GUI)
        EIN_EINTRAG_PASST_TEILWEISE,            // In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: ...
                                                // - Diesen Eintrag verwenden (-> Angehoerigen ersetzen)
                                                // - Nicht diesen Eintrag verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen  (-> bisherigen Angehoerigen verwenden)
                                                // - Eingaben korrigieren (-> Eingabe-GUI)
        MEHRERE_EINTRAEGE_PASSEN_TEILWEISE,     // In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ...
                                                // - Keinen dieser Einträge verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen  (-> bisherigen Angehoerigen verwenden)
                                                // - Eingaben korrigieren korrigieren (-> Eingabe-GUI)
    }

    // input
    private Angehoeriger angehoeriger;

    // output
    private Result result;
    private List<Angehoeriger> angehoerigerFoundList;
    private Angehoeriger angehoerigerFound;

    public CheckAngehoerigerBereitsInDatenbankCommand(Angehoeriger angehoeriger) {
        this.angehoeriger = angehoeriger;
    }

    @Override
    public void execute() {

        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

        // Suche mit allen gesetzten Attributen
        angehoerigerFoundList = angehoerigerDao.findAngehoerige(angehoeriger);
        if (angehoerigerFoundList != null && angehoerigerFoundList.size() == 1) {
            angehoerigerFound = angehoerigerFoundList.get(0);
            result = Result.EIN_EINTRAG_PASST;
            return;
        }

        else if (angehoerigerFoundList != null && angehoerigerFoundList.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN;
            return;
        }

        // Suche nur mit Vorname und Nachname
        Angehoeriger angehoerigerNurVornameNachname = new Angehoeriger(null, angehoeriger.getVorname(), angehoeriger.getNachname(), null, null);

        angehoerigerFoundList = angehoerigerDao.findAngehoerige(angehoerigerNurVornameNachname);
        if (angehoerigerFoundList != null && angehoerigerFoundList.size() == 1) {
            angehoerigerFound = angehoerigerFoundList.get(0);
            result = Result.EIN_EINTRAG_PASST_TEILWEISE;
            return;
        }

        else if (angehoerigerFoundList != null && angehoerigerFoundList.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN_TEILWEISE;
            return;
        }

        // Nicht gefunden
        result = Result.NICHT_IN_DATENBANK;
    }

    public Angehoeriger getAngehoerigerFound() {
        return angehoerigerFound;
    }

    public Result getResult() {
        return result;
    }

    public List<Angehoeriger> getAngehoerigerFoundList() {
        return angehoerigerFoundList;
    }
}
