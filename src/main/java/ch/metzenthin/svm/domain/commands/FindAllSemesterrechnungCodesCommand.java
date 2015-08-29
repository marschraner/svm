package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SemesterrechnungCodeDao;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSemesterrechnungCodesCommand extends GenericDaoCommand {

    // output
    private List<SemesterrechnungCode> semesterrechnungCodesAll;

    @Override
    public void execute() {

        SemesterrechnungCodeDao semesterrechnungCodeDao = new SemesterrechnungCodeDao(entityManager);
        semesterrechnungCodesAll = semesterrechnungCodeDao.findAll();
    }

    public List<SemesterrechnungCode> getSemesterrechnungCodesAll() {
        return semesterrechnungCodesAll;
    }

}
