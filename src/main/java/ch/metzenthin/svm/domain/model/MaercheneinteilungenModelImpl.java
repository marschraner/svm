package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungenModelImpl extends AbstractModel implements MaercheneinteilungenModel {

    public MaercheneinteilungenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

//    @Override
//    public MaercheneinteilungenErfassenModel getMaercheneinteilungenErfassenModel(SvmContext svmContext, MaercheneinteilungenTableModel maercheneinteilungenTableModel, int rowSelected) {
//        MaercheneinteilungenErfassenModel maercheneinteilungenErfassenModel = svmContext.getModelFactory().createMaercheneinteilungenErfassenModel();
//        Maercheneinteilung maercheneinteilungSelected = maercheneinteilungenTableModel.getMaercheneinteilungSelected(rowSelected);
//        maercheneinteilungenErfassenModel.setMaercheneinteilungOrigin(maercheneinteilungSelected);
//        return maercheneinteilungenErfassenModel;
//    }
//
//    @Override
//    public void eintragLoeschenMaercheneinteilungen(MaercheneinteilungenTableModel maercheneinteilungenTableModel, Maercheneinteilung maercheneinteilungToBeRemoved, SchuelerDatenblattModel schuelerDatenblattModel) {
//        CommandInvoker commandInvoker = getCommandInvoker();
//        RemoveMaercheneinteilungFromSchuelerCommand removeMaercheneinteilungFromSchuelerCommand = new RemoveMaercheneinteilungFromSchuelerCommand(maercheneinteilungToBeRemoved, schuelerDatenblattModel.getSchueler());
//        commandInvoker.executeCommandAsTransaction(removeMaercheneinteilungFromSchuelerCommand);
//        Schueler schuelerUpdated = removeMaercheneinteilungFromSchuelerCommand.getSchuelerUpdated();
//        // TableData mit von der Datenbank upgedatetem Schüler updaten
//        maercheneinteilungenTableModel.getMaercheneinteilungenTableData().setMaercheneinteilungen(schuelerUpdated.getMaercheneinteilungenAsList());
//    }
//
//    @Override
//    public Semester[] getSelectableSchuljahreMaercheneinteilungenSchueler(SvmModel svmModel) {
//        List<Semester> selectableSemesters = new ArrayList<>();
//        List<Semester> semesterAll = svmModel.getSemestersAll();
//        Calendar today = new GregorianCalendar();
//        today.set(Calendar.HOUR_OF_DAY, 0);
//        today.set(Calendar.MINUTE, 0);
//        today.set(Calendar.SECOND, 0);
//        today.set(Calendar.MILLISECOND, 0);
//        for (Semester semester : semesterAll) {
//            // Keine Semester in der Vergangenheit anzeigen
//            if (semester.getSemesterende().after(today) || semester.getSemesterende().equals(today)) {
//                selectableSemesters.add(semester);
//            }
//        }
//        // Aufsteigende Sortierung für Combobox, d.h. ältestes Semester zuoberst
//        Collections.sort(selectableSemesters, Collections.reverseOrder());
//        return selectableSemesters.toArray(new Semester[selectableSemesters.size()]);
//    }

    @Override
    void doValidate() throws SvmValidationException {}

    @Override
    public boolean isCompleted() {
        return true;
    }

}
