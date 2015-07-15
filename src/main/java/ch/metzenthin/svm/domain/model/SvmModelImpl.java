package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindAllCodesCommand;
import ch.metzenthin.svm.persistence.entities.Code;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<Code> codesAll;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        initSvmModelImpl();
    }

    private void initSvmModelImpl() {
        commandInvoker.openSession();
        initCodesAll();
        commandInvoker.closeSession();
    }

    private void initCodesAll() {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommandWithinSession(findAllCodesCommand);
        codesAll = findAllCodesCommand.getCodesAll();
    }

    @Override
    public List<Code> getCodesAll() {
        return codesAll;
    }

}
