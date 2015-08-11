package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.MaercheneinteilungenTableData;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;

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

    public Maercheneinteilung getMaercheneinteilungSelected(int rowIndex) {
        return maercheneinteilungenTableData.getMaercheneinteilungSelected(rowIndex);
    }

    public MaercheneinteilungenTableData getMaercheneinteilungenTableData() {
        return maercheneinteilungenTableData;
    }

}
