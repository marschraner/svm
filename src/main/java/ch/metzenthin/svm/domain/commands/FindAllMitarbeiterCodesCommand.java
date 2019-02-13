package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMitarbeiterCodesCommand implements Command {

    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // output
    private List<MitarbeiterCode> mitarbeiterCodesAll;

    @Override
    public void execute() {
        mitarbeiterCodesAll = mitarbeiterCodeDao.findAll();
    }

    public List<MitarbeiterCode> getMitarbeiterCodesAll() {
        return mitarbeiterCodesAll;
    }

}
