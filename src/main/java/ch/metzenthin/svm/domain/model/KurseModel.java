package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKursCommand;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

/**
 * @author Martin Schraner
 */
public interface KurseModel {

    KursErfassenModel getKursErfassenModel(SvmContext svmContext, KurseTableModel kurseTableModel, int indexBearbeiten);
    String getTotal(KurseTableModel kurseTableModel);

    DeleteKursCommand.Result kursLoeschenKurseVerwalten(KurseTableModel kurseTableModel, int indexKursToBeRemoved);
    void eintragLoeschenKurseSchueler(KurseTableModel kurseTableModel, int selectedRow, SchuelerDatenblattModel schuelerDatenblattModel);
    void importKurseFromPreviousSemester(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel);
    Semester[] getSelectableSemestersKurseSchueler(SvmModel svmModel);
}
