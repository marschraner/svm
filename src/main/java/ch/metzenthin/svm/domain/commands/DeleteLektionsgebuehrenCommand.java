package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteLektionsgebuehrenCommand extends GenericDaoCommand {

    // input
    private List<Lektionsgebuehren> lektionsgebuehren;
    int indexLektionsgebuehrenToBeDeleted;

    public DeleteLektionsgebuehrenCommand(List<Lektionsgebuehren> lektionsgebuehren, int indexLektionsgebuehrenToBeDeleted) {
        this.lektionsgebuehren = lektionsgebuehren;
        this.indexLektionsgebuehrenToBeDeleted = indexLektionsgebuehrenToBeDeleted;
    }

    @Override
    public void execute() {
        LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao(entityManager);
        Lektionsgebuehren lektionsgebuehrenToBeDeleted = lektionsgebuehren.get(indexLektionsgebuehrenToBeDeleted);
        lektionsgebuehrenDao.remove(lektionsgebuehrenToBeDeleted);
        lektionsgebuehren.remove(indexLektionsgebuehrenToBeDeleted);
    }

}
