package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KursorteTableData;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;


public class KursorteTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final KursorteTableData kursorteTableData;

    public KursorteTableModel(KursorteTableData kursorteTableData) {
        super();
        this.kursorteTableData = kursorteTableData;
    }

    @Override
    public int getRowCount() {
        return kursorteTableData.size();
    }

    @Override
    public int getColumnCount() {
        return kursorteTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return kursorteTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return kursorteTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return kursorteTableData.getColumnClass();
    }

    public KursorteTableData getKursorteTableData() {
        return kursorteTableData;
    }
}
