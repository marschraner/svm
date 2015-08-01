package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenTableData;
import ch.metzenthin.svm.persistence.entities.Semester;

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

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return schuelerSuchenTableData.getColumnClass(columnIndex);
    }

    public int getAnzahlLektionen() {
        return schuelerSuchenTableData.getAnzahlLektionen();
    }

    public Semester getSemester() {
        return schuelerSuchenTableData.getSemester();
    }

    public SchuelerSuchenTableData getSchuelerSuchenTableData() {
        return schuelerSuchenTableData;
    }
}
