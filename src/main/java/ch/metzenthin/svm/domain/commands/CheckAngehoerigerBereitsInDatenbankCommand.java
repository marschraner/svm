package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckAngehoerigerBereitsInDatenbankCommand implements Command {

    enum Result {
        VORNAME_NACHNAME_FEHLEN,
        EINTRAG_WIRD_MUTIERT,                   // Angehöriger (Origin) wird mutiert (nur bei Bearbeiten möglich)
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

    private final AngehoerigerDao angehoerigerDao = new AngehoerigerDao();

    // input
    private Angehoeriger angehoeriger;
    private final Angehoeriger angehoerigerToBeExcluded;

    // output
    private Result result;
    private List<Angehoeriger> angehoerigerFoundList;
    private Angehoeriger angehoerigerFound;

    CheckAngehoerigerBereitsInDatenbankCommand(Angehoeriger angehoeriger) {
        this(angehoeriger, null);
    }

    CheckAngehoerigerBereitsInDatenbankCommand(Angehoeriger angehoeriger, Angehoeriger angehoerigerToBeExcluded) {
        this.angehoeriger = angehoeriger;
        this.angehoerigerToBeExcluded = angehoerigerToBeExcluded;
    }

    @Override
    public void execute() {

        // Abbruch, falls Vor- und/oder Nachname nicht gesetzt
        if (angehoeriger.getVorname() == null || angehoeriger.getVorname().trim().isEmpty() || angehoeriger.getNachname() == null || angehoeriger.getNachname().trim().isEmpty()) {
            result = Result.VORNAME_NACHNAME_FEHLEN;
            return;
        }

        // Suche mit allen gesetzten Attributen
        angehoerigerFoundList = new ArrayList<>(angehoerigerDao.findAngehoerige(angehoeriger));
        removeAngehoerigerToBeExcluded(angehoerigerFoundList);
        if (angehoerigerFoundList.size() == 1) {
            angehoerigerFound = angehoerigerFoundList.get(0);
            result = Result.EIN_EINTRAG_PASST;
            return;
        }

        else if (angehoerigerFoundList.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN;
            return;
        }

        // Suche nur mit Vorname und Nachname
        Angehoeriger angehoerigerNurVornameNachname = new Angehoeriger(null, angehoeriger.getVorname(), angehoeriger.getNachname(), null, null, null, null);

        angehoerigerFoundList = new ArrayList<>(angehoerigerDao.findAngehoerige(angehoerigerNurVornameNachname));
        removeAngehoerigerToBeExcluded(angehoerigerFoundList);
        if (angehoerigerFoundList.size() == 1) {
            angehoerigerFound = angehoerigerFoundList.get(0);
            result = Result.EIN_EINTRAG_GLEICHER_NAME_ANDERE_ATTRIBUTE;
            return;
        }

        else if (angehoerigerFoundList.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_GLEICHER_NAME_ANDERE_ATTRIBUTE;
            return;
        }

        // Nicht gefunden
        result = (angehoerigerToBeExcluded != null) ? Result.EINTRAG_WIRD_MUTIERT : Result.NICHT_IN_DATENBANK;
    }

    private void removeAngehoerigerToBeExcluded(List<Angehoeriger> angehoerigeFound) {
        if (angehoerigerToBeExcluded == null) {
            return;
        }
        Iterator<Angehoeriger> iterator = angehoerigeFound.iterator();
        while (iterator.hasNext()) {
            Angehoeriger angehoeriger = iterator.next();
            if (angehoerigerToBeExcluded.getPersonId().equals(angehoeriger.getPersonId())) {
                iterator.remove();
            }
        }
    }

    Angehoeriger getAngehoerigerFound() {
        return angehoerigerFound;
    }

    public Result getResult() {
        return result;
    }

    List<Angehoeriger> getAngehoerigerFoundList() {
        return angehoerigerFoundList;
    }
}
