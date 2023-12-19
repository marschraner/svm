package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSemesterrechnungCodeCommand implements Command {

    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();

    // input
    private final SemesterrechnungCode semesterrechnungCode;
    private final SemesterrechnungCode semesterrechnungCodeOrigin;
    private final List<SemesterrechnungCode> bereitsErfassteSemesterrechnungCodes;

    public SaveOrUpdateSemesterrechnungCodeCommand(SemesterrechnungCode semesterrechnungCode, SemesterrechnungCode semesterrechnungCodeOrigin, List<SemesterrechnungCode> bereitsErfassteSemesterrechnungCodes) {
        this.semesterrechnungCode = semesterrechnungCode;
        this.semesterrechnungCodeOrigin = semesterrechnungCodeOrigin;
        this.bereitsErfassteSemesterrechnungCodes = bereitsErfassteSemesterrechnungCodes;
    }

    @Override
    public void execute() {
        if (semesterrechnungCodeOrigin != null) {
            // Update von semesterrechnungCodeOrigin mit Werten von semesterrechnungCode
            semesterrechnungCodeOrigin.copyAttributesFrom(semesterrechnungCode);
            semesterrechnungCodeDao.save(semesterrechnungCodeOrigin);
        } else {
            SemesterrechnungCode semesterrechnungCodeSaved = semesterrechnungCodeDao.save(semesterrechnungCode);
            bereitsErfassteSemesterrechnungCodes.add(semesterrechnungCodeSaved);
        }
        Collections.sort(bereitsErfassteSemesterrechnungCodes);
    }

}
