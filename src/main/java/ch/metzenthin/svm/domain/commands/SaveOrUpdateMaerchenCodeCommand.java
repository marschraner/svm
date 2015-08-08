package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenCodeDao;
import ch.metzenthin.svm.persistence.entities.MaerchenCode;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaerchenCodeCommand extends GenericDaoCommand {

    // input
    private MaerchenCode maerchenCode;
    private MaerchenCode maerchenCodeOrigin;
    private List<MaerchenCode> bereitsErfassteMaerchenCodes;


    public SaveOrUpdateMaerchenCodeCommand(MaerchenCode maerchenCode, MaerchenCode maerchenCodeOrigin, List<MaerchenCode> bereitsErfassteMaerchenCodes) {
        this.maerchenCode = maerchenCode;
        this.maerchenCodeOrigin = maerchenCodeOrigin;
        this.bereitsErfassteMaerchenCodes = bereitsErfassteMaerchenCodes;
    }

    @Override
    public void execute() {
        MaerchenCodeDao maerchenCodeDao = new MaerchenCodeDao(entityManager);
        if (maerchenCodeOrigin != null) {
            // Update von maerchenCodeOrigin mit Werten von maerchenCode
            maerchenCodeOrigin.copyAttributesFrom(maerchenCode);
            maerchenCodeDao.save(maerchenCodeOrigin);
        } else {
            MaerchenCode maerchenCodeSaved = maerchenCodeDao.save(maerchenCode);
            bereitsErfassteMaerchenCodes.add(maerchenCodeSaved);
        }
        Collections.sort(bereitsErfassteMaerchenCodes);
    }

}
