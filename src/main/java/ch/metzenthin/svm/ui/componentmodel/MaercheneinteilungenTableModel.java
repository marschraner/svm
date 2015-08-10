package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MaercheneinteilungenTableData;

import javax.swing.table.AbstractTableModel;


public class MaercheneinteilungenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private MaercheneinteilungenTableData maercheneinteilungenTableData;

    public MaercheneinteilungenTableModel(MaercheneinteilungenTableData maercheneinteilungenTableData) {
        super();
        this.maercheneinteilungenTableData = maercheneinteilungenTableData;
    }

    @Override
    public int getRowCount() {
        return maercheneinteilungenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return maercheneinteilungenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return maercheneinteilungenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return maercheneinteilungenTableData.getColumnName(column);
    }

}
