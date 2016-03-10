package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungenModel extends Model {

    SemesterrechnungBearbeitenModel getSemesterrechnungBearbeitenModel(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
    String getTotal(SemesterrechnungenTableModel semesterrechnungenTableModel);
    void semesterrechnungPhysischLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
    void semesterrechnungLogischLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
    void semesterrechnungWiederherstellen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
    boolean hasSemesterrechnungKurse(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
}
