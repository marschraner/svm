package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.DispensationenTableData;

import javax.swing.table.AbstractTableModel;


public class DispensationenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private DispensationenTableData dispensationenTableData;

    public DispensationenTableModel(DispensationenTableData dispensationenTableData) {
        super();
        this.dispensationenTableData = dispensationenTableData;
    }

    @Override
    public int getRowCount() {
        return dispensationenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return dispensationenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dispensationenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return dispensationenTableData.getColumnName(column);
    }

    public DispensationenTableData getDispensationenTableData() {
        return dispensationenTableData;
    }
}
