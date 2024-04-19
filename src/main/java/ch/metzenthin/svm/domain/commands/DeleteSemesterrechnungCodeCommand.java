package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSemesterrechnungCodeCommand implements Command {

    public enum Result {
        CODE_VON_SEMESTERRECHNUNGEN_REFERENZIERT,
        LOESCHEN_ERFOLGREICH
    }

    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();

    // input
    private final List<SemesterrechnungCode> semesterrechnungCodes;
    private final int indexCodeToBeDeleted;

    // output
    private Result result;

    public DeleteSemesterrechnungCodeCommand(List<SemesterrechnungCode> semesterrechnungCodes, int indexCodeToBeDeleted) {
        this.semesterrechnungCodes = semesterrechnungCodes;
        this.indexCodeToBeDeleted = indexCodeToBeDeleted;
    }

    @Override
    public void execute() {
        SemesterrechnungCode semesterrechnungCodeToBeDeleted = semesterrechnungCodes.get(indexCodeToBeDeleted);
        if (!semesterrechnungCodeToBeDeleted.getSemesterrechnungen().isEmpty()) {
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
