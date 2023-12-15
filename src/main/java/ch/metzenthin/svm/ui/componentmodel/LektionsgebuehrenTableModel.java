package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.LektionsgebuehrenTableData;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.List;


public class LektionsgebuehrenTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LektionsgebuehrenTableData lektionsgebuehrenTableData;

    public LektionsgebuehrenTableModel(LektionsgebuehrenTableData lektionsgebuehrenTableData) {
        super();
        this.lektionsgebuehrenTableData = lektionsgebuehrenTableData;
    }

    @Override
    public int getRowCount() {
        return lektionsgebuehrenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return lektionsgebuehrenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return lektionsgebuehrenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return lektionsgebuehrenTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return lektionsgebuehrenTableData.getColumnClass(columnIndex);
    }

    public List<Lektionsgebuehren> getLektionsgebuehrenList() {
        return lektionsgebuehrenTableData.getLektionsgebuehrenList();
    }

    public LektionsgebuehrenTableData getLektionsgebuehrenTableData() {
        return lektionsgebuehrenTableData;
    }
}
