package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommand extends GenericDaoCommand {

    // output
    private List<SchuelerCode> schuelerCodesAll;

    @Override
    public void execute() {

        SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao(entityManager);
        schuelerCodesAll = schuelerCodeDao.findAll();
    }

    public List<SchuelerCode> getSchuelerCodesAll() {
        return schuelerCodesAll;
    }

}
