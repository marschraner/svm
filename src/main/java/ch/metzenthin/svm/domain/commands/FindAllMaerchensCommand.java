package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMaerchensCommand extends GenericDaoCommand {

    private final MaerchenDao maerchenDao = new MaerchenDao();

    // output
    private List<Maerchen> maerchensAll;

    @Override
    public void execute() {
        maerchensAll = maerchenDao.findAll();
    }

    public List<Maerchen> getMaerchensAll() {
        return maerchensAll;
    }

}
