package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LehrkraftDao;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllLehrkraefteCommand extends GenericDaoCommand {

    // output
    private List<Lehrkraft> lehrkraefteAll;

    @Override
    public void execute() {

        LehrkraftDao lehrkraftDao = new LehrkraftDao(entityManager);
        lehrkraefteAll = lehrkraftDao.findAll();
    }

    public List<Lehrkraft> getLehrkraefteAll() {
        return lehrkraefteAll;
    }

}
