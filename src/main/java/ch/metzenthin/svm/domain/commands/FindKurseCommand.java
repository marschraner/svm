package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKurseCommand implements Command {

    public enum Result {
        KURSE_GEFUNDEN,
        KEINE_KURSE_GEFUNDEN
    }

    private final KursDao kursDao = new KursDao();

    // input
    private final Semester semester;    // nullable
    private final Wochentag wochentag;  // nullable
    private final Time zeitBeginn;      // nullable
    private final Mitarbeiter mitarbeiter;  // nullable

    // output
    private Result result;
    private List<Kurs> kurseFound;

    public FindKurseCommand(Semester semester, Wochentag wochentag, Time zeitBeginn, Mitarbeiter mitarbeiter) {
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.mitarbeiter = mitarbeiter;
    }

    @Override
    public void execute() {
        kurseFound = kursDao.findKurse(semester, wochentag, zeitBeginn, mitarbeiter);
        if (kurseFound.isEmpty()) {
            result = Result.KEINE_KURSE_GEFUNDEN;
        } else {
            result = Result.KURSE_GEFUNDEN;
        }
    }

    public Result getResult() {
        return result;
    }

    public List<Kurs> getKurseFound() {
        return kurseFound;
    }
}
