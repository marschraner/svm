package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MitarbeiterCodeDao;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMitarbeiterCodeCommand implements Command {

    private final MitarbeiterCodeDao mitarbeiterCodeDao = new MitarbeiterCodeDao();

    // input
    private MitarbeiterCode mitarbeiterCode;
    private MitarbeiterCode mitarbeiterCodeOrigin;
    private List<MitarbeiterCode> bereitsErfassteMitarbeiterCodes;


    public SaveOrUpdateMitarbeiterCodeCommand(MitarbeiterCode mitarbeiterCode, MitarbeiterCode mitarbeiterCodeOrigin, List<MitarbeiterCode> bereitsErfassteMitarbeiterCodes) {
        this.mitarbeiterCode = mitarbeiterCode;
        this.mitarbeiterCodeOrigin = mitarbeiterCodeOrigin;
        this.bereitsErfassteMitarbeiterCodes = bereitsErfassteMitarbeiterCodes;
    }

    @Override
    public void execute() {
        if (mitarbeiterCodeOrigin != null) {
            // Update von mitarbeiterCodeOrigin mit Werten von mitarbeiterCode
            mitarbeiterCodeOrigin.copyAttributesFrom(mitarbeiterCode);
            mitarbeiterCodeDao.save(mitarbeiterCodeOrigin);
        } else {
            MitarbeiterCode mitarbeiterCodeSaved = mitarbeiterCodeDao.save(mitarbeiterCode);
            bereitsErfassteMitarbeiterCodes.add(mitarbeiterCodeSaved);
        }
        Collections.sort(bereitsErfassteMitarbeiterCodes);
    }

}
