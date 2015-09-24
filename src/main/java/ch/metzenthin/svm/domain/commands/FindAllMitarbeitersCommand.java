package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterDao;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllMitarbeitersCommand extends GenericDaoCommand {

    // output
    private List<Mitarbeiter> mitarbeitersAll;

    @Override
    public void execute() {

        MitarbeiterDao mitarbeiterDao = new MitarbeiterDao(entityManager);
        mitarbeitersAll = mitarbeiterDao.findAll();
    }

    public List<Mitarbeiter> getMitarbeitersAll() {
        return mitarbeitersAll;
    }

}
