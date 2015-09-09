package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungenModel extends Model {

    String getTotal(SemesterrechnungenTableModel semesterrechnungenTableModel);
    void importSemesterrechnungenFromPreviousSemester(SemesterrechnungenTableModel semesterrechnungenTableModel, boolean bisherigeUeberschreiben, boolean importRestbetraege);
    void semesterrechnungLoeschen(SemesterrechnungenTableModel semesterrechnungenTableModel, int rowSelected);
}
