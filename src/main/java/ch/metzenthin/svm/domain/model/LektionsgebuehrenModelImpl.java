package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteLektionsgebuehrenCommand;
import ch.metzenthin.svm.domain.commands.FindKurseCommand;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenModelImpl extends AbstractModel implements LektionsgebuehrenModel {

    LektionsgebuehrenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public boolean checkIfLektionslaengeInVerwendung(LektionsgebuehrenTableModel lektionsgebuehrenTableModel, int indexLektionsgebuehrenToBeRemoved) {
        Lektionsgebuehren lektionsgebuehrenToBeRemoved = lektionsgebuehrenTableModel.getLektionsgebuehrenList().get(indexLektionsgebuehrenToBeRemoved);
        FindKurseCommand findKurseCommand = new FindKurseCommand(null, null, null, null);
        getCommandInvoker().executeCommand(findKurseCommand);
        for (Kurs kurs : findKurseCommand.getKurseFound()) {
            if (kurs.getKurslaenge() == lektionsgebuehrenToBeRemoved.getLektionslaenge()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void eintragLoeschen(SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel, int indexLektionsgebuehrenToBeRemoved) {
        List<Lektionsgebuehren> lektionsgebuehren = svmContext.getSvmModel().getLektionsgebuehrenAllList();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteLektionsgebuehrenCommand deleteLektionsgebuehrenCommand = new DeleteLektionsgebuehrenCommand(lektionsgebuehren, indexLektionsgebuehrenToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteLektionsgebuehrenCommand);
        // TableData mit von der Datenbank upgedateten Lektionsgebühren updaten
        lektionsgebuehrenTableModel.getLektionsgebuehrenTableData().setLektionsgebuehrenList(svmContext.getSvmModel().getLektionsgebuehrenAllList());
    }

    @Override
    public LektionsgebuehrenErfassenModel getLektionsgebuehrenErfassenModel(SvmContext svmContext, int indexLektionsgebuehrenToBeModified) {
        LektionsgebuehrenErfassenModel lektionsgebuehrenErfassenModel = svmContext.getModelFactory().createLektionsgebuehrenErfassenModel();
        List<Lektionsgebuehren> lektionsgebuehrens = svmContext.getSvmModel().getLektionsgebuehrenAllList();
        lektionsgebuehrenErfassenModel.setLektionsgebuehrenOrigin(lektionsgebuehrens.get(indexLektionsgebuehrenToBeModified));
        return lektionsgebuehrenErfassenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
