package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.CodesTableData;
import ch.metzenthin.svm.persistence.entities.Code;

import javax.swing.table.AbstractTableModel;


public class CodesTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private CodesTableData codesTableData;

    public CodesTableModel(CodesTableData codesTableData) {
        super();
        this.codesTableData = codesTableData;
    }

    @Override
    public int getRowCount() {
        return codesTableData.size();
    }

    @Override
    public int getColumnCount() {
        return codesTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return codesTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return codesTableData.getColumnName(column);
    }

    public CodesTableData getCodesTableData() {
        return codesTableData;
    }

    public Code getCodeAt(int rowIndex) {
        return codesTableData.getCodeAt(rowIndex);
    }
}
