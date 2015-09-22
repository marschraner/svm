package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SemestersTableData;

import javax.swing.table.AbstractTableModel;


public class SemestersTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private SemestersTableData semestersTableData;

    public SemestersTableModel(SemestersTableData semestersTableData) {
        super();
        this.semestersTableData = semestersTableData;
    }

    @Override
    public int getRowCount() {
        return semestersTableData.size();
    }

    @Override
    public int getColumnCount() {
        return semestersTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return semestersTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return semestersTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return semestersTableData.getColumnClass(columnIndex);
    }

    public SemestersTableData getSemestersTableData() {
        return semestersTableData;
    }
}
