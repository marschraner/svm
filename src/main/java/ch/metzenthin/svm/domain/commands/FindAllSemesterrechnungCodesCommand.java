package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSemesterrechnungCodesCommand extends GenericDaoCommand {

    private final SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao();

    // output
    private List<SemesterrechnungCode> semesterrechnungCodesAll;

    @Override
    public void execute() {
        semesterrechnungCodesAll = semesterrechnungCodeDao.findAll();
    }

    public List<SemesterrechnungCode> getSemesterrechnungCodesAll() {
        return semesterrechnungCodesAll;
    }

}
