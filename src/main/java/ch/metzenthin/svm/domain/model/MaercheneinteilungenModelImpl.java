package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.DeleteMaercheneinteilungCommand;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenModelImpl extends AbstractModel implements MaercheneinteilungenModel {

    public MaercheneinteilungenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

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
    public Maerchen[] getSelectableMaerchens(SvmModel svmModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.JANUARY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        List<Maerchen> selectableMaerchens = new ArrayList<>(svmModel.getMaerchensAll());
        List<Maerchen> maerchensToBeRemoved = new ArrayList<>();
        for (Maerchen maerchen : selectableMaerchens) {
            // Falls das Märchen in der Vergangenheit ist, soll es nicht mehr selektierbar sein
            int schuljahr1Maerchen = Integer.parseInt(maerchen.getSchuljahr().substring(0, 4));
            if (schuljahr1Maerchen < schuljahr1) {
                maerchensToBeRemoved.add(maerchen);
            }
            for (Maercheneinteilung maercheneinteilung : schuelerDatenblattModel.getSchueler().getMaercheneinteilungen()) {
                // Nur noch nicht erfasste Märchen sollen selektierbar sein
                if (maercheneinteilung.getMaerchen().isIdenticalWith(maerchen)) {
                    maerchensToBeRemoved.add(maerchen);
                }
            }
        }
        selectableMaerchens.removeAll(maerchensToBeRemoved);
        return selectableMaerchens.toArray(new Maerchen[selectableMaerchens.size()]);
    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }

}
