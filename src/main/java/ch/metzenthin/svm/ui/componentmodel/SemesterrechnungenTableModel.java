package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SemesterrechnungenTableData;
import ch.metzenthin.svm.persistence.entities.Semester;

import javax.swing.table.AbstractTableModel;


public class SemesterrechnungenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private SemesterrechnungenTableData semesterrechnungenTableData;

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
    public String getColumnName(int column) {
        return semesterrechnungenTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return semesterrechnungenTableData.getColumnClass(columnIndex);
    }

    public Semester getSemester() {
        return semesterrechnungenTableData.getSememester();
    }
}
