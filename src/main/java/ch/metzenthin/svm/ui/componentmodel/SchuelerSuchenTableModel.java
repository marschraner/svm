package ch.metzenthin.svm.ui.componentmodel;

import ch.metzenthin.svm.domain.model.SchuelerSuchenResult;

import javax.swing.table.AbstractTableModel;


public class SchuelerSuchenTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private SchuelerSuchenResult schuelerSuchenResult;

    public SchuelerSuchenTableModel(SchuelerSuchenResult schuelerSuchenResult) {
        super();
        this.schuelerSuchenResult = schuelerSuchenResult;
    }

    @Override
    public int getRowCount() {
        return schuelerSuchenResult.size();
    }

    @Override
    public int getColumnCount() {
        return schuelerSuchenResult.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return schuelerSuchenResult.getValueAt(rowIndex, columnIndex);
    }

}
