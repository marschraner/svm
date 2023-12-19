package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKurstypCommand implements Command {

    private final KurstypDao kurstypDao = new KurstypDao();

    // input
    private final Kurstyp kurstyp;
    private final Kurstyp kurstypOrigin;
    private final List<Kurstyp> bereitsErfassteKurstypen;

    public SaveOrUpdateKurstypCommand(Kurstyp kurstyp, Kurstyp kurstypOrigin, List<Kurstyp> bereitsErfassteKurstypen) {
        this.kurstyp = kurstyp;
        this.kurstypOrigin = kurstypOrigin;
        this.bereitsErfassteKurstypen = bereitsErfassteKurstypen;
    }

    @Override
    public void execute() {
        if (kurstypOrigin != null) {
            // Update von kurstypOrigin mit Werten von kurstyp
            kurstypOrigin.copyAttributesFrom(kurstyp);
            kurstypDao.save(kurstypOrigin);
        } else {
            Kurstyp kurstypenSaved = kurstypDao.save(kurstyp);
            bereitsErfassteKurstypen.add(kurstypenSaved);
        }
        Collections.sort(bereitsErfassteKurstypen);
    }

}
