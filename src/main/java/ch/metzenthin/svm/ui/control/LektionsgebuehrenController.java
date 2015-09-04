package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenModel;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenTableData;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;
import ch.metzenthin.svm.ui.components.LektionsgebuehrenErfassenDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenController {
    private final SvmContext svmContext;
    private final LektionsgebuehrenModel lektionsgebuehrenModel;
    private LektionsgebuehrenTableModel lektionsgebuehrenTableModel;
    private JTable lektionsgebuehrenTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public LektionsgebuehrenController(LektionsgebuehrenModel lektionsgebuehrenModel, SvmContext svmContext) {
        this.lektionsgebuehrenModel = lektionsgebuehrenModel;
        this.svmContext = svmContext;
    }

    public void setLektionsgebuehrenTable(JTable lektionsgebuehrenTable) {
        this.lektionsgebuehrenTable = lektionsgebuehrenTable;
        LektionsgebuehrenTableData lektionsgebuehrenTableData = new LektionsgebuehrenTableData(svmContext.getSvmModel().getLektionsgebuehrenAllList());
        lektionsgebuehrenTableModel = new LektionsgebuehrenTableModel(lektionsgebuehrenTableData);
        lektionsgebuehrenTable.setModel(lektionsgebuehrenTableModel);
        lektionsgebuehrenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        lektionsgebuehrenTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNeu();
            }
        });
    }

    private void onNeu() {
        btnNeu.setFocusPainted(true);
        LektionsgebuehrenErfassenDialog lektionsgebuehrenErfassenDialog = new LektionsgebuehrenErfassenDialog(svmContext, lektionsgebuehrenTableModel, lektionsgebuehrenModel, 0, false, "Neuer Lektionsgebuehren");
        lektionsgebuehrenErfassenDialog.pack();
        lektionsgebuehrenErfassenDialog.setVisible(true);
        lektionsgebuehrenTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
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
        btnBearbeiten.setFocusPainted(true);
        LektionsgebuehrenErfassenDialog lektionsgebuehrenErfassenDialog = new LektionsgebuehrenErfassenDialog(svmContext, lektionsgebuehrenTableModel, lektionsgebuehrenModel, lektionsgebuehrenTable.getSelectedRow(), true, "Lektionsgebuehren bearbeiten");
        lektionsgebuehrenErfassenDialog.pack();
        lektionsgebuehrenErfassenDialog.setVisible(true);
        lektionsgebuehrenTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
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
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Wenn der Eintrag gelöscht wird, ist eine automatische Berechnung des Schulgelds für diese Kurslänge nicht mehr möglich.\nSoll der Eintrag aus der Datenbank gelöscht werden?",
                "Lektionsgebühren löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            lektionsgebuehrenModel.eintragLoeschen(svmContext, lektionsgebuehrenTableModel, lektionsgebuehrenTable.getSelectedRow());
            lektionsgebuehrenTableModel.fireTableDataChanged();
            lektionsgebuehrenTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        lektionsgebuehrenTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = lektionsgebuehrenTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
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

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }


}
