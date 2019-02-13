package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllElternmithilfeCodesCommand implements Command {

    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();

    // output
    private List<ElternmithilfeCode> elternmithilfeCodesAll;

    @Override
    public void execute() {
        elternmithilfeCodesAll = elternmithilfeCodeDao.findAll();
    }

    public List<ElternmithilfeCode> getElternmithilfeCodesAll() {
        return elternmithilfeCodesAll;
    }

}
