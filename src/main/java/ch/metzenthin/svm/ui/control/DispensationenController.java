package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.DispensationErfassenDialog;
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
public class DispensationenController {
    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final int selectedRow;
    private JTable dispensationenTable;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public DispensationenController(SvmContext svmContext, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow) {
        this.svmContext = svmContext;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.selectedRow = selectedRow;
    }

    public void setDispensationenTable(JTable dispensationenTable) {
        this.dispensationenTable = dispensationenTable;
        dispensationenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        dispensationenTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setLblTitel(JLabel lblTitel) {
        lblTitel.setText("Dispensationen " + schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
    }

    public void setBtnNeu(JButton btnNeu) {
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNeu();
            }
        });
    }

    private void onNeu() {
        DispensationErfassenDialog dispensationErfassenDialog = new DispensationErfassenDialog(svmContext, "Neue Dispensation");
        dispensationErfassenDialog.pack();
        dispensationErfassenDialog.setVisible(true);
    }

    public void setBtnBearbeiten(JButton btnBearbeiten) {
        this.btnBearbeiten = btnBearbeiten;
        enableBtnBearbeiten(false);
        btnBearbeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBearbeiten();
            }
        });
    }

    private void enableBtnBearbeiten(boolean enabled) {
        btnBearbeiten.setEnabled(enabled);
    }

    private void onBearbeiten() {
        //TODO
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        this.btnLoeschen = btnLoeschen;
        enableBtnLoeschen(false);
        btnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoeschen();
            }
        });
    }

    private void enableBtnLoeschen(boolean enabled) {
        btnLoeschen.setEnabled(enabled);
    }

    private void onLoeschen() {
        //TODO
    }

    private void onListSelection() {
        int selectedRowIndex = dispensationenTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        btnZurueck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, selectedRow);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Abbrechen"));
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addZurueckZuSchuelerSuchenListener(ActionListener zurueckZuSchuelerSuchenListener) {
        this.zurueckZuSchuelerSuchenListener = zurueckZuSchuelerSuchenListener;
    }
}
