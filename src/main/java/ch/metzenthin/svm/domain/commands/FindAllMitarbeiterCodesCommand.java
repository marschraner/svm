package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMitarbeiterCodesCommand extends GenericDaoCommand {

    // output
    private List<MitarbeiterCode> mitarbeiterCodesAll;

    @Override
    public void execute() {

        MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao(entityManager);
        mitarbeiterCodesAll = mitarbeiterCodeDao.findAll();
    }

    public List<MitarbeiterCode> getMitarbeiterCodesAll() {
        return mitarbeiterCodesAll;
    }

}
