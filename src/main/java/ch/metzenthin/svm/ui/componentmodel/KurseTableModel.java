package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.KurseTableData;
import ch.metzenthin.svm.persistence.entities.Kurs;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.util.List;


public class KurseTableModel extends AbstractTableModel {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient KurseTableData kurseTableData;

    public KurseTableModel(KurseTableData kurseTableData) {
        super();
        this.kurseTableData = kurseTableData;
    }

    @Override
    public int getRowCount() {
        return kurseTableData.size();
    }

    @Override
    public int getColumnCount() {
        return kurseTableData.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return kurseTableData.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return kurseTableData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return kurseTableData.getColumnClass(columnIndex);
    }

    public List<Kurs> getKurse() {
        return kurseTableData.getKurse();
    }

    public Kurs getKursSelected(int rowIndex) {
        return kurseTableData.getKursSelected(rowIndex);
    }

    public int getAnzahlSchueler() {
        return kurseTableData.getAnzahlSchueler();
    }
}
