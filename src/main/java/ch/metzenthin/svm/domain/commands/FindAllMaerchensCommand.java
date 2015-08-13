package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMaerchensCommand extends GenericDaoCommand {

    // output
    private List<Maerchen> maerchensAll;

    @Override
    public void execute() {

        MaerchenDao maerchenDao = new MaerchenDao(entityManager);
        maerchensAll = maerchenDao.findAll();
    }

    public List<Maerchen> getMaerchensAll() {
        return maerchensAll;
    }

}
