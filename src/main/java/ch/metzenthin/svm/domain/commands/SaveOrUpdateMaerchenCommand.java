package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaerchenCommand extends GenericDaoCommand {

    private final MaerchenDao maerchenDao = new MaerchenDao();

    // input
    private Maerchen maerchen;
    private Maerchen maerchenOrigin;
    private List<Maerchen> bereitsErfassteMaerchen;


    public SaveOrUpdateMaerchenCommand(Maerchen maerchen, Maerchen maerchenOrigin, List<Maerchen> bereitsErfassteMaerchen) {
        this.maerchen = maerchen;
        this.maerchenOrigin = maerchenOrigin;
        this.bereitsErfassteMaerchen = bereitsErfassteMaerchen;
    }

    @Override
    public void execute() {
        if (maerchenOrigin != null) {
            // Update von maerchenOrigin mit Werten von maerchen
            maerchenOrigin.copyAttributesFrom(maerchen);
            maerchenDao.save(maerchenOrigin);
        } else {
            Maerchen maerchenSaved = maerchenDao.save(maerchen);
            bereitsErfassteMaerchen.add(maerchenSaved);
        }
        Collections.sort(bereitsErfassteMaerchen);
    }

}
