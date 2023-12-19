package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.LektionsgebuehrenDao;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateLektionsgebuehrenCommand implements Command {

    private final LektionsgebuehrenDao lektionsgebuehrenDao = new LektionsgebuehrenDao();

    // input
    private final Lektionsgebuehren lektionsgebuehren;
    private final Lektionsgebuehren lektionsgebuehrenOrigin;
    private final List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren;

    public SaveOrUpdateLektionsgebuehrenCommand(Lektionsgebuehren lektionsgebuehren, Lektionsgebuehren lektionsgebuehrenOrigin, List<Lektionsgebuehren> bereitsErfassteLektionsgebuehren) {
        this.lektionsgebuehren = lektionsgebuehren;
        this.lektionsgebuehrenOrigin = lektionsgebuehrenOrigin;
        this.bereitsErfassteLektionsgebuehren = bereitsErfassteLektionsgebuehren;
    }

    @Override
    public void execute() {
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
