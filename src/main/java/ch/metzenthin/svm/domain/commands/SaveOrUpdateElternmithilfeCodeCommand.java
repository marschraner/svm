package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.ElternmithilfeCodeDao;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateElternmithilfeCodeCommand extends GenericDaoCommand {

    private final ElternmithilfeCodeDao elternmithilfeCodeDao = new ElternmithilfeCodeDao();

    // input
    private ElternmithilfeCode elternmithilfeCode;
    private ElternmithilfeCode elternmithilfeCodeOrigin;
    private List<ElternmithilfeCode> bereitsErfassteElternmithilfeCodes;


    public SaveOrUpdateElternmithilfeCodeCommand(ElternmithilfeCode elternmithilfeCode, ElternmithilfeCode elternmithilfeCodeOrigin, List<ElternmithilfeCode> bereitsErfassteElternmithilfeCodes) {
        this.elternmithilfeCode = elternmithilfeCode;
        this.elternmithilfeCodeOrigin = elternmithilfeCodeOrigin;
        this.bereitsErfassteElternmithilfeCodes = bereitsErfassteElternmithilfeCodes;
    }

    @Override
    public void execute() {
        if (elternmithilfeCodeOrigin != null) {
            // Update von elternmithilfeCodeOrigin mit Werten von elternmithilfeCode
            elternmithilfeCodeOrigin.copyAttributesFrom(elternmithilfeCode);
            elternmithilfeCodeDao.save(elternmithilfeCodeOrigin);
        } else {
            ElternmithilfeCode elternmithilfeCodeSaved = elternmithilfeCodeDao.save(elternmithilfeCode);
            bereitsErfassteElternmithilfeCodes.add(elternmithilfeCodeSaved);
        }
        Collections.sort(bereitsErfassteElternmithilfeCodes);
    }

}
