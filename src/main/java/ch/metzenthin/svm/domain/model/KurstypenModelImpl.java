package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteKurstypCommand;
import ch.metzenthin.svm.persistence.entities.Kurstyp;

import java.util.List;

/**
 * @author Martin Schraner
 */
public class KurstypenModelImpl extends AbstractModel implements KurstypenModel {

    KurstypenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public DeleteKurstypCommand.Result eintragLoeschen(SvmContext svmContext, int indexKurstypToBeRemoved) {
        List<Kurstyp> kurstypen = svmContext.getSvmModel().getKurstypenAll();
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteKurstypCommand deleteKurstypCommand = new DeleteKurstypCommand(kurstypen, indexKurstypToBeRemoved);
        commandInvoker.executeCommandAsTransaction(deleteKurstypCommand);
        return deleteKurstypCommand.getResult();
    }

    @Override
    public KurstypErfassenModel getKurstypErfassenModel(SvmContext svmContext, int indexKurstypToBeModified) {
        KurstypErfassenModel kurstypErfassenModel = svmContext.getModelFactory().createKurstypErfassenModel();
        List<Kurstyp> kurstyps = svmContext.getSvmModel().getKurstypenAll();
        kurstypErfassenModel.setKurstypOrigin(kurstyps.get(indexKurstypToBeModified));
        return kurstypErfassenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    
}
