package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateCodeCommand extends GenericDaoCommand {

    // input
    private Code code;
    private Code codeOrigin;
    private List<Code> bereitsErfassteCodes;


    public SaveOrUpdateCodeCommand(Code code, Code codeOrigin, List<Code> bereitsErfassteCodes) {
        this.code = code;
        this.codeOrigin = codeOrigin;
        this.bereitsErfassteCodes = bereitsErfassteCodes;
    }

    @Override
    public void execute() {
        CodeDao codeDao = new CodeDao(entityManager);
        if (codeOrigin != null) {
            // Update von codeOrigin mit Werten von code
            codeOrigin.copyAttributesFrom(code);
            codeDao.save(codeOrigin);
        } else {
            Code codeSaved = codeDao.save(code);
            bereitsErfassteCodes.add(codeSaved);
        }
        Collections.sort(bereitsErfassteCodes);
    }

}
