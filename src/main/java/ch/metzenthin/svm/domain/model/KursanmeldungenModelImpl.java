package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteKursanmeldungCommand;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

/**
 * @author Martin Schraner
 */
public class KursanmeldungenModelImpl extends AbstractModel implements KursanmeldungenModel {

    public KursanmeldungenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    @Override
    public KursanmeldungErfassenModel getKursanmeldungErfassenModel(SvmContext svmContext, KursanmeldungenTableModel kursanmeldungenTableModel, int rowSelected) {
        KursanmeldungErfassenModel kursanmeldungErfassenModel = svmContext.getModelFactory().createKursanmeldungErfassenModel();
        Kursanmeldung kursanmeldungSelected = kursanmeldungenTableModel.getKursanmeldungSelected(rowSelected);
        kursanmeldungErfassenModel.setKursanmeldungOrigin(kursanmeldungSelected);
        return kursanmeldungErfassenModel;
    }

    @Override
    public void kurseinteilungLoeschen(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteKursanmeldungCommand deleteKursanmeldungCommand = new DeleteKursanmeldungCommand(schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList(), rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteKursanmeldungCommand);
        // TableData mit von der Datenbank upgedateter Kursanmeldung updaten
        kursanmeldungenTableModel.getKursanmeldungenTableData().setKursanmeldungen(schuelerDatenblattModel.getSchueler().getKursanmeldungenAsList());
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }

}
