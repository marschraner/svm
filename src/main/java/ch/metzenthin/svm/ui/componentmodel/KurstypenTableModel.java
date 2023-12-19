package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KurstypenTableData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;


public class KurstypenTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final KurstypenTableData kurstypenTableData;

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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return kurstypenTableData.getColumnClass();
    }

    public KurstypenTableData getKurstypenTableData() {
        return kurstypenTableData;
    }
}
