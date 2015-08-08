package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllElternmithilfeCodesCommand extends GenericDaoCommand {

    // output
    private List<ElternmithilfeCode> elternmithilfeCodesAll;

    @Override
    public void execute() {

        ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao(entityManager);
        elternmithilfeCodesAll = elternmithilfeCodeDao.findAll();
    }

    public List<ElternmithilfeCode> getElternmithilfeCodesAll() {
        return elternmithilfeCodesAll;
    }

}
