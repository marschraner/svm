package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckAngehoerigerBereitsInDatenbankCommand extends GenericDaoCommand {

    enum Result {
        VORNAME_NACHNAME_FEHLEN,
        NICHT_IN_DATENBANK,                     // Angehöriger wird neu erfasst (noch nicht in Datenbank)
        EIN_EINTRAG_PASST,                      // In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: ...
                                                // - Diesen Eintrag übernehmen (-> Angehoerigen ersetzen)
                                                // - Abbrechen (-> Eingabe-GUI)
        MEHRERE_EINTRAEGE_PASSEN,               // In der Datenbank wurden mehrere Einträge gefunden, die auf die erfassten Angaben passen: ...
                                                // Angehöriger muss genauer erfasst werden
                                                // - Eingaben korrigieren (-> Eingabe-GUI)
        EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE,  // In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: ...
                                                // - Diesen Eintrag übernehmen (-> Angehoerigen ersetzen)
                                                // - Nicht diesen Eintrag verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen  (-> bisherigen Angehoerigen verwenden)
                                                // - Abbrechen (-> Eingabe-GUI)
        MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE,     // In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ...
                                                // - Keinen dieser Einträge verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen  (-> bisherigen Angehoerigen verwenden)
                                                // - Abbrechen (-> Eingabe-GUI)
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

        // Abbruch, falls Vor- und/oder Nachname nicht gesetzt
        if (angehoeriger.getVorname() == null || angehoeriger.getVorname().trim().isEmpty() || angehoeriger.getNachname() == null || angehoeriger.getNachname().trim().isEmpty()) {
            result = Result.VORNAME_NACHNAME_FEHLEN;
            return;
        }

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
            result = Result.EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE;
            return;
        }

        else if (angehoerigerFoundList != null && angehoerigerFoundList.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE;
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
