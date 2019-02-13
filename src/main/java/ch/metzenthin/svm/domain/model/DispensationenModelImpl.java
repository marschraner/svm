package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.RemoveDispensationFromSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;

/**
 * @author Martin Schraner
 */
public class DispensationenModelImpl extends AbstractModel implements DispensationenModel {

    @Override
    public void eintragLoeschen(DispensationenTableModel dispensationenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int indexDispensationToBeDeleted) {
        CommandInvoker commandInvoker = getCommandInvoker();
        RemoveDispensationFromSchuelerCommand removeDispensationFromSchuelerCommand = new RemoveDispensationFromSchuelerCommand(indexDispensationToBeDeleted, schuelerDatenblattModel.getSchueler());
        commandInvoker.executeCommandAsTransaction(removeDispensationFromSchuelerCommand);
        Schueler schuelerUpdated = removeDispensationFromSchuelerCommand.getSchuelerUpdated();
        // TableData mit von der Datenbank upgedatetem Schüler updaten
        if (schuelerUpdated != null) {
            dispensationenTableModel.getDispensationenTableData().setDispensationen(schuelerUpdated.getDispensationen());
        }
    }

    @Override
    public DispensationErfassenModel getDispensationErfassenModel(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel, int indexDispensationToBeModified) {
        DispensationErfassenModel dispensationErfassenModel = svmContext.getModelFactory().createDispensationErfassenModel();
        dispensationErfassenModel.setDispensationOrigin(schuelerDatenblattModel.getSchueler().getDispensationen().get(indexDispensationToBeModified));
        return dispensationErfassenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
