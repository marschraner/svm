package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMaercheneinteilungCommand;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Schraner
 */
class MaercheneinteilungenModelImpl extends AbstractModel implements MaercheneinteilungenModel {

    @Override
    public MaercheneinteilungErfassenModel getMaercheneinteilungErfassenModel(SvmContext svmContext, MaercheneinteilungenTableModel maercheneinteilungenTableModel, int rowSelected) {
        MaercheneinteilungErfassenModel maercheneinteilungErfassenModel = svmContext.getModelFactory().createMaercheneinteilungErfassenModel();
        Maercheneinteilung maercheneinteilungSelected = maercheneinteilungenTableModel.getMaercheneinteilungSelected(rowSelected);
        maercheneinteilungErfassenModel.setMaercheneinteilungOrigin(maercheneinteilungSelected);
        return maercheneinteilungErfassenModel;
    }

    @Override
    public void maercheneinteilungLoeschen(MaercheneinteilungenTableModel maercheneinteilungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int rowSelected) {
        CommandInvoker commandInvoker = getCommandInvoker();
        DeleteMaercheneinteilungCommand deleteMaercheneinteilungCommand = new DeleteMaercheneinteilungCommand(schuelerDatenblattModel.getSchueler().getMaercheneinteilungenAsList(), rowSelected);
        commandInvoker.executeCommandAsTransaction(deleteMaercheneinteilungCommand);
        // TableData mit von der Datenbank upgedateter Maercheneinteilung updaten
        maercheneinteilungenTableModel.getMaercheneinteilungenTableData().setMaercheneinteilungen(schuelerDatenblattModel.getSchueler().getMaercheneinteilungenAsList());
    }

    @Override
    public List<Maerchen> getSelectableMaerchens(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        List<Maerchen> selectableMaerchens = new ArrayList<>(svmModel.getMaerchensAll());
        List<Maerchen> maerchensToBeRemoved = new ArrayList<>();
        for (Maerchen maerchen : selectableMaerchens) {
            for (Maercheneinteilung maercheneinteilung : schuelerDatenblattModel.getSchueler().getMaercheneinteilungen()) {
                // Nur noch nicht erfasste Märchen sollen selektierbar sein
                if (maercheneinteilung.getMaerchen().isIdenticalWith(maerchen)) {
                    maerchensToBeRemoved.add(maerchen);
                }
            }
        }
        selectableMaerchens.removeAll(maerchensToBeRemoved);
        return selectableMaerchens;
    }

    @Override
    public boolean isSchuelerAbgemeldet(SchuelerDatenblattModel schuelerDatenblattModel) {
        return schuelerDatenblattModel.getSchueler().isAbgemeldet();
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

}
