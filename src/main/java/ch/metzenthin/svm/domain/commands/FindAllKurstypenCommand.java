package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class FindAllKurstypenCommand implements Command {

    private final KurstypDao kurstypDao = new KurstypDao();

    // output
    private List<Kurstyp> kurstypenAll;

    @Override
    public void execute() {
        kurstypenAll = kurstypDao.findAll();
    }

    public List<Kurstyp> getKurstypenAll() {
        return kurstypenAll;
    }

}
