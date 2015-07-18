package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;

import javax.swing.table.AbstractTableModel;


public class SchuelerSuchenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private SchuelerSuchenTableData schuelerSuchenTableData;

    public SchuelerSuchenTableModel(SchuelerSuchenTableData schuelerSuchenTableData) {
        super();
        this.schuelerSuchenTableData = schuelerSuchenTableData;
    }

    @Override
    public int getRowCount() {
        return schuelerSuchenTableData.size();
    }

    @Override
    public int getColumnCount() {
        return schuelerSuchenTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return schuelerSuchenTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return schuelerSuchenTableData.getColumnName(column);
    }

    public SchuelerDatenblattModel getSchuelerDatenblattModel(int rowIndex) {
        return schuelerSuchenTableData.getSchuelerDatenblattModel(rowIndex);
    }

}
