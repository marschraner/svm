package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.LehrkraefteTableData;

import javax.swing.table.AbstractTableModel;


public class LehrkraefteTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private LehrkraefteTableData lehrkraefteTableData;

    public LehrkraefteTableModel(LehrkraefteTableData lehrkraefteTableData) {
        super();
        this.lehrkraefteTableData = lehrkraefteTableData;
    }

    @Override
    public int getRowCount() {
        return lehrkraefteTableData.size();
    }

    @Override
    public int getColumnCount() {
        return lehrkraefteTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return lehrkraefteTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return lehrkraefteTableData.getColumnName(column);
    }

}
