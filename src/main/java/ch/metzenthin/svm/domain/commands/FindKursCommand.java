package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.persistence.daos.KursDao;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.sql.Time;

/**
 * @author Martin Schraner
 */
public class FindKursCommand extends GenericDaoCommand {

    public enum Result {
        KURS_GEFUNDEN,
        KURS_EXISTIERT_NICHT
    }

    // input
    private Semester semester;
    private Wochentag wochentag;
    private Time zeitBeginn;
    private Lehrkraft lehrkraft;

    // output
    private Result result;
    private Kurs kursFound;

    public FindKursCommand(Semester semester, Wochentag wochentag, Time zeitBeginn, Lehrkraft lehrkraft) {
        this.semester = semester;
        this.wochentag = wochentag;
        this.zeitBeginn = zeitBeginn;
        this.lehrkraft = lehrkraft;
    }

    @Override
    public void execute() {
        KursDao kursDao = new KursDao(entityManager);
        kursFound = kursDao.findKurs(semester, wochentag, zeitBeginn, lehrkraft);
        if (kursFound == null) {
            kursFound = null;
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
