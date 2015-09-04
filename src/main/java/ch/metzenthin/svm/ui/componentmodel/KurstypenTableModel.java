package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KurstypenTableData;

import javax.swing.table.AbstractTableModel;


public class KurstypenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private KurstypenTableData kurstypenTableData;

    public KurstypenTableModel(KurstypenTableData kurstypenTableData) {
        super();
        this.kurstypenTableData = kurstypenTableData;
    }

    @Override
    public int getRowCount() {
        return kurstypenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return kurstypenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return kurstypenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return kurstypenTableData.getColumnName(column);
    }

    public KurstypenTableData getKurstypenTableData() {
        return kurstypenTableData;
    }
}
