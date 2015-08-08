package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenCodeDao;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMaerchenCodesCommand extends GenericDaoCommand {

    // output
    private List<MaerchenCode> maerchenCodesAll;

    @Override
    public void execute() {

        MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);
        maerchenCodesAll = maerchenCodeDao.findAll();
    }

    public List<MaerchenCode> getMaerchenCodesAll() {
        return maerchenCodesAll;
    }

}
