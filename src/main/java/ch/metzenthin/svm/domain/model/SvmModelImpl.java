package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindAllCodesCommand;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public class SvmModelImpl implements SvmModel {

    private CodesTableModel codesAllTableModel;
    private CommandInvoker commandInvoker;

    public SvmModelImpl(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
        initSvmModelImpl();
    }

    private void initSvmModelImpl() {
        commandInvoker.openSession();
        initCodesAllTableModel();
        commandInvoker.closeSession();
    }

    private void initCodesAllTableModel() {
        FindAllCodesCommand findAllCodesCommand = new FindAllCodesCommand();
        commandInvoker.executeCommandWithinSession(findAllCodesCommand);
        CodesTableData codesTableData = new CodesTableData(findAllCodesCommand.getCodesAll());
        codesAllTableModel = new CodesTableModel(codesTableData);
    }

    @Override
    public CodesTableModel getCodesAllTableModel() {
        return codesAllTableModel;
    }

    @Override
    public void setCodesAllTableModel(CodesTableModel codesAllTableModel) {
        this.codesAllTableModel = codesAllTableModel;
    }
}
