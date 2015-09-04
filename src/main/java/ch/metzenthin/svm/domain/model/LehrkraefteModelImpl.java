package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteLehrkraftCommand;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class LehrkraefteModelImpl extends AbstractModel implements LehrkraefteModel {

    public LehrkraefteModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public LehrkraftErfassenModel getLehrkraftErfassenModel(SvmContext svmContext, int indexLehrkraftToBeModified) {
        LehrkraftErfassenModel lehrkraftErfassenModel = svmContext.getModelFactory().createLehrkraftErfassenModel();
        List<Lehrkraft> lehrkraefte = svmContext.getSvmModel().getLehrkraefteAll();
        lehrkraftErfassenModel.setLehrkraftOrigin(lehrkraefte.get(indexLehrkraftToBeModified));
        return lehrkraftErfassenModel;
    }

    @Override
    public DeleteLehrkraftCommand.Result lehrkraftLoeschen(SvmContext svmContext, LehrkraefteTableModel lehrkraefteTableModel, int indexLehrkraftToBeRemoved) {
        List<Lehrkraft> lehrkraefte = svmContext.getSvmModel().getLehrkraefteAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteLehrkraftCommand deleteLehrkraftCommand = new DeleteLehrkraftCommand(lehrkraefte, indexLehrkraftToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteLehrkraftCommand);
        // TableData mit von der Datenbank upgedateten Lehrkräften updaten
        lehrkraefteTableModel.getLehrkraefteTableData().setLehrkraefte(svmContext.getSvmModel().getLehrkraefteAll());
        return deleteLehrkraftCommand.getResult();
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
