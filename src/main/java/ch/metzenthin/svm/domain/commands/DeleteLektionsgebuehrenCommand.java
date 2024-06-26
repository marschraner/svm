package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteLektionsgebuehrenCommand implements Command {

    private final LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao();

    // input
    private final List<Lektionsgebuehren> lektionsgebuehren;
    private final int indexLektionsgebuehrenToBeDeleted;

    public DeleteLektionsgebuehrenCommand(List<Lektionsgebuehren> lektionsgebuehren, int indexLektionsgebuehrenToBeDeleted) {
        this.lektionsgebuehren = lektionsgebuehren;
        this.indexLektionsgebuehrenToBeDeleted = indexLektionsgebuehrenToBeDeleted;
    }

    @Override
    public void execute() {
        Lektionsgebuehren lektionsgebuehrenToBeDeleted = lektionsgebuehren.get(indexLektionsgebuehrenToBeDeleted);
        lektionsgebuehrenDao.remove(lektionsgebuehrenToBeDeleted);
        lektionsgebuehren.remove(indexLektionsgebuehrenToBeDeleted);
    }

}
