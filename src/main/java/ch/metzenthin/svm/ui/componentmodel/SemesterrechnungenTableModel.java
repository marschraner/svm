package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SemesterrechnungenTableData;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.List;
import java.util.Set;


public class SemesterrechnungenTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient SemesterrechnungenTableData semesterrechnungenTableData;

    public SemesterrechnungenTableModel(SemesterrechnungenTableData semesterrechnungenTableData) {
        super();
        this.semesterrechnungenTableData = semesterrechnungenTableData;
    }

    @Override
    public int getRowCount() {
        return semesterrechnungenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return semesterrechnungenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return semesterrechnungenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        semesterrechnungenTableData.setValueAt(value, rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return semesterrechnungenTableData.isCellEditable(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return semesterrechnungenTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return semesterrechnungenTableData.getColumnClass(columnIndex);
    }

    public Semester getSemester() {
        return semesterrechnungenTableData.getSemester();
    }

    public List<Semesterrechnung> getSemesterrechnungen() {
        return semesterrechnungenTableData.getSemesterrechnungen();
    }

    public Semesterrechnung getSemesterrechnungSelected(int rowIndex) {
        return semesterrechnungenTableData.getSemesterrechnungSelected(rowIndex);
    }

    public int getAnzSelektiert() {
        return semesterrechnungenTableData.getAnzSelektiert();
    }

    public List<Semesterrechnung> getSelektierteSemesterrechnungen() {
        return semesterrechnungenTableData.getSelektierteSemesterrechnungen();
    }

    public boolean isAlleSelektiert() {
        return semesterrechnungenTableData.isAlleSelektiert();
    }

    public void alleSemesterrechnungenSelektieren() {
        semesterrechnungenTableData.alleSemesterrechnungenSelektieren();
    }

    public void alleSemesterrechnungenDeselektieren() {
        semesterrechnungenTableData.alleSemesterrechnungenDeselektieren();
    }

    public void updateSemesterrechnungen(Set<Semesterrechnung> subsetOfUpdatedSemesterrechnungen) {
        semesterrechnungenTableData.updateSemesterrechnungen(subsetOfUpdatedSemesterrechnungen);
    }

    public void loadSelektierteSemesterrechnungenNotContainedInPersistenceContext() {
        semesterrechnungenTableData.loadSelektierteSemesterrechnungenNotContainedInPersistenceContext();
    }

    public void reloadSemesterrechnungenNotContainedInPersistenceContext() {
        semesterrechnungenTableData.reloadSemesterrechnungenNotContainedInPersistenceContext();
    }

}
