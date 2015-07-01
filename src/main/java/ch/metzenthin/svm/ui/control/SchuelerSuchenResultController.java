package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultController {
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private JButton btnDatenblatt;
    private ActionListener nextPanelListener;

    public SchuelerSuchenResultController(SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable) {
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        schuelerSuchenResultTable.setModel(schuelerSuchenTableModel);
        schuelerSuchenResultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        schuelerSuchenResultTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onDatenblatt();
                }
            }
        });
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void setBtnDatenblatt(JButton btnDatenblatt) {
        this.btnDatenblatt = btnDatenblatt;
        enableBtnDatenblatt(false);
        btnDatenblatt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDatenblatt();
            }
        });
    }

    private void enableBtnDatenblatt(boolean enabled) {
        btnDatenblatt.setEnabled(enabled);
    }

    private void onDatenblatt() {
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(schuelerSuchenTableModel, schuelerSuchenResultTable.getSelectedRow());
        nextPanelListener.actionPerformed(new ActionEvent(new Object[] {schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
    }

    private void onListSelection() {
        int selectedRowIndex = schuelerSuchenResultTable.getSelectedRow();
        enableBtnDatenblatt(selectedRowIndex >= 0);
    }

}
