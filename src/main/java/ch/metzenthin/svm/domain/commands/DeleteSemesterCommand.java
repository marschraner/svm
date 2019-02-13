package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterCommand implements Command {

    public enum Result {
        SEMESTER_VON_KURS_REFERENZIERT,
        SEMESTER_VON_SEMESTERRECHNUNG_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final SemesterDao semesterDao = new SemesterDao();

    // input
    private List<Semester> Semesters;
    private int indexSemesterToBeDeleted;

    // output
    private Result result;

    public DeleteSemesterCommand(List<Semester> Semesters, int indexSemesterToBeDeleted) {
        this.Semesters = Semesters;
        this.indexSemesterToBeDeleted = indexSemesterToBeDeleted;
    }

    @Override
    public void execute() {
        Semester SemesterToBeDeleted = Semesters.get(indexSemesterToBeDeleted);
        if (SemesterToBeDeleted.getKurse().size() > 0) {
            result = Result.SEMESTER_VON_KURS_REFERENZIERT;
            return;
        }
        if (SemesterToBeDeleted.getSemesterrechnungen().size() > 0) {
            result = Result.SEMESTER_VON_SEMESTERRECHNUNG_REFERENZIERT;
            return;
        }
        semesterDao.remove(SemesterToBeDeleted);
        Semesters.remove(indexSemesterToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
