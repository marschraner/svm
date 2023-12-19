package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;

/**
 * @author Martin Schraner
 */
public class FindKursCommand implements Command {

    public enum Result {
        KURS_GEFUNDEN,
        KURS_EXISTIERT_NICHT
    }

    private final KursDao kursDao = new KursDao();

    // input
    private final Semester semester;
    private final Wochentag wochentag;
    private final Time zeitBeginn;
    private final Mitarbeiter mitarbeiter;

    // output
    private Result result;
    private Kurs kursFound;

    public FindKursCommand(Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        kursFound = kursDao.findKurs(semester, wochentag, zeitBeginn, mitarbeiter);
        if (kursFound == null) {
            result = Result.KURS_EXISTIERT_NICHT;
        } else {
            result = Result.KURS_GEFUNDEN;
        }
    }

    public Result getResult() {
        return result;
    }

    public Kurs getKursFound() {
        return kursFound;
    }
}
