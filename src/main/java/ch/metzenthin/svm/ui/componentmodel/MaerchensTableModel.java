package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MaerchensTableData;

import javax.swing.table.AbstractTableModel;


public class MaerchensTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private MaerchensTableData maerchensTableData;

    public MaerchensTableModel(MaerchensTableData maerchensTableData) {
        super();
        this.maerchensTableData = maerchensTableData;
    }

    @Override
    public int getRowCount() {
        return maerchensTableData.size();
    }

    @Override
    public int getColumnCount() {
        return maerchensTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return maerchensTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return maerchensTableData.getColumnName(column);
    }

}
