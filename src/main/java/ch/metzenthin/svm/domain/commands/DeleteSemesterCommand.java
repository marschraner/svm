package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterDao;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterCommand extends GenericDaoCommand {

    public enum Result {
        SEMESTER_VON_KURS_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<Semester> Semesters;
    int indexSemesterToBeDeleted;

    // output
    private Result result;

    public DeleteSemesterCommand(List<Semester> Semesters, int indexSemesterToBeDeleted) {
        this.Semesters = Semesters;
        this.indexSemesterToBeDeleted = indexSemesterToBeDeleted;
    }

    @Override
    public void execute() {
        SemesterDao SemesterDao = new SemesterDao(entityManager);
        Semester SemesterToBeDeleted = Semesters.get(indexSemesterToBeDeleted);
        //TODO
//        if (SemesterToBeDeleted.getKurse().size() > 0) {
//            result = Result.SEMESTER_VON_KURS_REFERENZIERT;
//            return;
//        }
        SemesterDao.remove(SemesterToBeDeleted);
        Semesters.remove(indexSemesterToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}