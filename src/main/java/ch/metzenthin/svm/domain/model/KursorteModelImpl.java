package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteKursortCommand;
import ch.metzenthin.svm.persistence.entities.Kursort;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KursorteModelImpl extends AbstractModel implements KursorteModel {

    KursorteModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public DeleteKursortCommand.Result eintragLoeschen(SvmContext svmContext, int indexKursortToBeRemoved) {
        List<Kursort> kursorte = svmContext.getSvmModel().getKursorteAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteKursortCommand deleteKursortCommand = new DeleteKursortCommand(kursorte, indexKursortToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteKursortCommand);
        return deleteKursortCommand.getResult();
    }

    @Override
    public KursortErfassenModel getKursortErfassenModel(SvmContext svmContext, int indexKursortToBeModified) {
        KursortErfassenModel kursortErfassenModel = svmContext.getModelFactory().createKursortErfassenModel();
        List<Kursort> kursorts = svmContext.getSvmModel().getKursorteAll();
        kursortErfassenModel.setKursortOrigin(kursorts.get(indexKursortToBeModified));
        return kursortErfassenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
