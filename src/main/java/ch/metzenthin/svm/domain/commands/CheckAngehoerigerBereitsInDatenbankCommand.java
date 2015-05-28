package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.AngehoerigerDao;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class CheckAngehoerigerBereitsInDatenbankCommand extends GenericDaoCommand {

    enum Result {
        NICHT_IN_DATENBANK,                     // Mutter wird neu erfasst (noch nicht in Datenbank)
                                                // - Ok
                                                // - Abbrechen
        EIN_EINTRAG_PASST,                      // In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben passt: ...
                                                // - Diesen Eintrag verwenden
                                                // - Eingaben korrigieren
        MEHRERE_EINTRAEGE_PASSEN,               // In der Datenbank wurden mehrere Einträge gefunden, die auf die erfassten Angaben der Mutter passen: ...
                                                // Mutter muss genauer erfasst werden
                                                // - Eingaben korrigieren
        EIN_EINTRAG_PASST_TEILWEISE,            // In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben teilweise übereinstimmt: ...
                                                // - Diesen Eintrag verwenden und mit Eingaben aktualisieren (angehoeriger = angehoerigerMerged)
                                                // - Nicht diesen Eintrag verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen
                                                // - Eingaben korrigieren
        MEHRERE_EINTRAEGE_PASSEN_TEILWEISE,     // In der Datenbank wurden mehrere Einträge gefunden, die mit den erfassten Angaben teilweise übereinstimmen: ...
                                                // - Keinen dieser Einträge verwenden und einen neuen Datenbank-Eintrag gemäss der erfassten Angaben erzeugen (-> mutter = ...command.getAngehoerigerAnswer1(); skipCheckIfMutterAlreadyinDatabase = True, Fenster schliessen und wieder onSaveClicked des GUI aufrufen)
                                                // - Eingaben korrigieren korrigieren (-> mutter = ...command.getAngehoerigerAnswer2(), skipCheckIfMutterAlreadyinDatabase = False und Fenster schliessen (-> Gui))
    }

    // input + output
    private Angehoeriger angehoeriger;

    // output
    private Result result;
    private List<Angehoeriger> angehoerigeFound;
    private Angehoeriger angehoerigerMerged;

    public CheckAngehoerigerBereitsInDatenbankCommand(Angehoeriger angehoeriger) {
        this.angehoeriger = angehoeriger;
    }

    @Override
    public void execute() {

        AngehoerigerDao angehoerigerDao = new AngehoerigerDao(entityManager);

        // Suche mit allen gesetzten Attributen
        angehoerigeFound = angehoerigerDao.findAngehoerige(angehoeriger);
        if (angehoerigeFound != null && angehoerigeFound.size() == 1) {
            angehoeriger = angehoerigeFound.get(0);
            result = Result.EIN_EINTRAG_PASST;
            return;
        }

        else if (angehoerigeFound != null && angehoerigeFound.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN;
            return;
        }

        // Suche nur mit Vorname und Nachname
        Angehoeriger angehoerigerNurVornameNachname = new Angehoeriger(null, angehoeriger.getVorname(), angehoeriger.getNachname(), null, null, null);

        angehoerigeFound = angehoerigerDao.findAngehoerige(angehoerigerNurVornameNachname);
        if (angehoerigeFound != null && angehoerigeFound.size() == 1) {
            angehoerigerMerged = angehoerigeFound.get(0);
            // Verwende aktuelle Attribute von Angehöriger falls vorhanden
            if (angehoeriger.getGeburtsdatum() != null) {
                angehoerigerMerged.setGeburtsdatum(angehoeriger.getGeburtsdatum());
            }
            if (angehoeriger.getNatel() != null) {
                angehoerigerMerged.setNatel(angehoeriger.getNatel());
            }
            if (angehoeriger.getEmail() != null) {
                angehoerigerMerged.setEmail(angehoeriger.getEmail());
            }
            if (angehoeriger.getAdresse() != null) {
                angehoerigerMerged.setAdresse(angehoeriger.getAdresse());
            }
            result = Result.EIN_EINTRAG_PASST_TEILWEISE;
            return;
        }

        else if (angehoerigeFound != null && angehoerigeFound.size() > 1) {
            result = Result.MEHRERE_EINTRAEGE_PASSEN_TEILWEISE;
            return;
        }

        // Nicht gefunden
        result = Result.NICHT_IN_DATENBANK;
    }

    public Angehoeriger getAngehoeriger() {
        return angehoeriger;
    }

    public Result getResult() {
        return result;
    }

    public List<Angehoeriger> getAngehoerigeFound() {
        return angehoerigeFound;
    }

    public Angehoeriger getAngehoerigerMerged() {
        return angehoerigerMerged;
    }
}
