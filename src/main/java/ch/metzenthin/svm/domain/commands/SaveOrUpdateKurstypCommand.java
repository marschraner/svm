package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.KurstypDao;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateKurstypCommand extends GenericDaoCommand {

    private final KurstypDao kurstypDao = new KurstypDao();

    // input
    private Kurstyp kurstyp;
    private Kurstyp kurstypOrigin;
    private List<Kurstyp> bereitsErfassteKurstypen;


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
            Kurstyp kurstypenaved = kurstypDao.save(kurstyp);
            bereitsErfassteKurstypen.add(kurstypenaved);
        }
        Collections.sort(bereitsErfassteKurstypen);
    }

}
