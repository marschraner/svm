package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;

/**
 * @author Martin Schraner
 */
public class AddKursToSchuelerAndSaveCommand extends GenericDaoCommand {

    public enum Result {
        KURS_BEREITS_ERFASST,
        KURS_EXISTIERT_NICHT,
        SPEICHERN_ERFOLGREICH
    }

    // input
    private Semester semester;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;
    private Schueler schueler;

    // output
    private Result result;
    private Schueler schuelerUpdated;

    public AddKursToSchuelerAndSaveCommand(Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft, Schueler schueler) {
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.lehrkraft = lehrkraft;
        this.schueler = schueler;
    }

    @Override
    public void execute() {

        // Existiert Kurs?
        KursDao kursDao = new KursDao(entityManager);
        Kurs kursFound = kursDao.findKurs(semester, wochentag, zeitBeginn, lehrkraft);
        if (kursFound == null) {
            schuelerUpdated = null;
            result = Result.KURS_EXISTIERT_NICHT;
            return;
        }

        // Ist der Kurs beim Schüler bereits erfasst?
        for (Kurs kurs : schueler.getKurse()) {
            if (kurs.isIdenticalWith(kursFound)) {
                result = Result.KURS_BEREITS_ERFASST;
                schuelerUpdated = null;
                return;
            }
        }

        // Kurs hinzufügen
        schuelerUpdated = kursDao.addToSchuelerAndSave(kursFound, schueler);
        result = Result.SPEICHERN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }

    public Schueler getSchuelerUpdated() {
        return schuelerUpdated;
    }
}
