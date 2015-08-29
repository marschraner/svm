package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCodeCommand extends GenericDaoCommand {

    public enum Result {
        CODE_VON_SEMESTERRECHNUNGEN_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    // input
    private List<SemesterrechnungCode> semesterrechnungCodes;
    int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteSemesterrechnungCodeCommand(List<SemesterrechnungCode> semesterrechnungCodes, int indexCodeToBeDeleted) {
        this.semesterrechnungCodes = semesterrechnungCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);
        SemesterrechnungCode semesterrechnungCodeToBeDeleted = semesterrechnungCodes.get(indexCodeToBeDeleted);
        if (semesterrechnungCodeToBeDeleted.getSemesterrechnungen().size() > 0) {
            result = Result.CODE_VON_SEMESTERRECHNUNGEN_REFERENZIERT;
            return;
        }
        semesterrechnungCodeDao.remove(semesterrechnungCodeToBeDeleted);
        semesterrechnungCodes.remove(indexCodeToBeDeleted);
        result = Result.LOESCHEN_ERFOLGREICH;
    }

    public Result getResult() {
        return result;
    }
}
