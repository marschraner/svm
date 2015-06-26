package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultController {
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;

    public SchuelerSuchenResultController(SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        schuelerSuchenResultTable.setModel(schuelerSuchenTableModel);
    }

}
