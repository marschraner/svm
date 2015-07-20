package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KursortDao;
import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllKursorteCommand extends GenericDaoCommand {

    // output
    private List<Kursort> kursorteAll;

    @Override
    public void execute() {

        KursortDao kursortDao = new KursortDao(entityManager);
        kursorteAll = kursortDao.findAll();
    }

    public List<Kursort> getKursorteAll() {
        return kursorteAll;
    }

}
