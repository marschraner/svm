package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllCodesCommand extends GenericDaoCommand {

    // output
    private List<Code> codesAll;

    @Override
    public void execute() {

        CodeDao codeDao = new CodeDao(entityManager);
        codesAll = codeDao.findAll();
    }

    public List<Code> getCodesAll() {
        return codesAll;
    }

}
