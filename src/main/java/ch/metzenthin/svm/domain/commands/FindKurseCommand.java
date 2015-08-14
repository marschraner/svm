package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindKurseCommand extends GenericDaoCommand {

    public enum Result {
        KURSE_GEFUNDEN,
        KEINE_KURSE_GEFUNDEN
    }

    // input

    private Schueler schueler;    // nullable
    private Semester semester;
    private Wochentag wochentag;  // nullable
    private Time zeitBeginn;      // nullable
    private Lehrkraft lehrkraft;  // nullable

    // output
    private Result result;
    private List<Kurs> kurseFound;

    public FindKurseCommand(Schueler schueler, Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        this.schueler = schueler;
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.lehrkraft = lehrkraft;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        kurseFound = kursDao.findKurse(schueler, semester, wochentag, zeitBeginn, lehrkraft);
        if (kurseFound.size() == 0) {
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
