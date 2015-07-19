package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindAllCodesCommand;
import ch.metzenthin.svm.domain.commands.FindAllLehrkraefteCommand;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private List<Code> codesAll;
    private List<Lehrkraft> lehrkraefteAll;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        reloadCodesAll();
        reloadLehrkraefteAll();
    }

    @Override
    public void reloadCodesAll() {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommand(findAllCodesCommand);
        codesAll = findAllCodesCommand.getCodesAll();
    }

    @Override
    public void reloadLehrkraefteAll() {
        FindAllLehrkraefteCommand findAllLehrkraefteCommand = new FindAllLehrkraefteCommand();
        commandInvoker.executeCommand(findAllLehrkraefteCommand);
        lehrkraefteAll = findAllLehrkraefteCommand.getLehrkraefteAll();
    }

    @Override
    public List<Code> getCodesAll() {
        return codesAll;
    }

    @Override
    public List<Lehrkraft> getLehrkraefteAll() {
        return lehrkraefteAll;
    }

}
