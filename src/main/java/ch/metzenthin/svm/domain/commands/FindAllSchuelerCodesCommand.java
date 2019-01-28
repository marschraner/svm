package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllSchuelerCodesCommand implements Command {

    private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

    // output
    private List<SchuelerCode> schuelerCodesAll;

    @Override
    public void execute() {
        schuelerCodesAll = schuelerCodeDao.findAll();
    }

    public List<SchuelerCode> getSchuelerCodesAll() {
        return schuelerCodesAll;
    }

}
