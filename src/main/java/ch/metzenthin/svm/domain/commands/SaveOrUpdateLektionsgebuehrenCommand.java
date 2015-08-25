package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLektionsgebuehrenCommand extends GenericDaoCommand {

    // input
    private Lektionsgebuehren lektionsgebuehren;
    private Lektionsgebuehren lektionsgebuehrenOrigin;
    private List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren;


    public SaveOrUpdateLektionsgebuehrenCommand(Lektionsgebuehren lektionsgebuehren, Lektionsgebuehren lektionsgebuehrenOrigin, List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren) {
        this.lektionsgebuehren = lektionsgebuehren;
        this.lektionsgebuehrenOrigin = lektionsgebuehrenOrigin;
        this.bereitsErfassteLektionsgebuehren = bereitsErfassteLektionsgebuehren;
    }

    @Override
    public void execute() {
        LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao(entityManager);
        if (lektionsgebuehrenOrigin != null) {
            // Update von lektionsgebuehrenOrigin mit Werten von lektionsgebuehren
            lektionsgebuehrenOrigin.copyAttributesFrom(lektionsgebuehren);
            lektionsgebuehrenDao.save(lektionsgebuehrenOrigin);
        } else {
            Lektionsgebuehren lektionsgebuehrenSaved = lektionsgebuehrenDao.save(lektionsgebuehren);
            bereitsErfassteLektionsgebuehren.add(lektionsgebuehrenSaved);
        }
        Collections.sort(bereitsErfassteLektionsgebuehren);
    }

}
