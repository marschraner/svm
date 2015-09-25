package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class MitarbeitersModelImpl extends AbstractModel implements MitarbeitersModel {

    public MitarbeitersModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public MitarbeiterErfassenModel getMitarbeiterErfassenModel(SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel, int indexMitarbeiterToBeModified) {
        MitarbeiterErfassenModel mitarbeiterErfassenModel = svmContext.getModelFactory().createMitarbeiterErfassenModel();
        List<Mitarbeiter> mitarbeiters = mitarbeitersTableModel.getMitarbeitersTableData().getMitarbeiters();
        mitarbeiterErfassenModel.setMitarbeiterOrigin(mitarbeiters.get(indexMitarbeiterToBeModified));
        return mitarbeiterErfassenModel;
    }

    @Override
    public DeleteMitarbeiterCommand.Result mitarbeiterLoeschen(MitarbeitersTableModel mitarbeitersTableModel, int indexMitarbeiterToBeRemoved) {
        List<Mitarbeiter> mitarbeiters = mitarbeitersTableModel.getMitarbeiters();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteMitarbeiterCommand deleteMitarbeiterCommand = new DeleteMitarbeiterCommand(mitarbeiters, indexMitarbeiterToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteMitarbeiterCommand);
        return deleteMitarbeiterCommand.getResult();
    }

    @Override
    public String getTotal(MitarbeitersTableModel mitarbeitersTableModel) {
        return "Total: " + mitarbeitersTableModel.getRowCount() + " Mitarbeiter";
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return false;
    }
}
