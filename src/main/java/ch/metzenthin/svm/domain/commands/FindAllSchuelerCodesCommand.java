package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommand extends GenericDaoCommand {

    // output
    private List<SchuelerCode> codesAll;

    @Override
    public void execute() {

        SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);
        codesAll = schuelerCodeDao.findAll();
    }

    public List<SchuelerCode> getCodesAll() {
        return codesAll;
    }

}
