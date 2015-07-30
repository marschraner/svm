package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.CodeDao;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class ReloadCodesCommand extends GenericDaoCommand {

    public ReloadCodesCommand(List<Code> codes) {
        this.codes = codes;
    }

    // input
    private List<Code> codes;

    // output
    private List<Code> codesReloaded = new ArrayList<>();

    @Override
    public void execute() {

        CodeDao codeDao = new CodeDao(entityManager);
        for (Code code : codes) {
            codesReloaded.add(codeDao.findById(code.getCodeId()));
        }
    }

    public List<Code> getCodesReloaded() {
        return codesReloaded;
    }
}
