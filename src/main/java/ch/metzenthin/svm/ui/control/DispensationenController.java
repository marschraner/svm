package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.DispensationenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.DispensationenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.DispensationErfassenDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAsPercentages;

/**
 * @author Martin Schraner
 */
public class DispensationenController {
    private final SvmContext svmContext;
    private final DispensationenModel dispensationenModel;
    private final DispensationenTableModel dispensationenTableModel;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private final int selectedRow;
    private final boolean isFromSchuelerSuchenResult;
    private JTable dispensationenTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    @SuppressWarnings("java:S107")
    public DispensationenController(
            DispensationenModel dispensationenModel,
            SvmContext svmContext,
            DispensationenTableModel dispensationenTableModel,
            SchuelerDatenblattModel schuelerDatenblattModel,
            SchuelerSuchenTableModel schuelerSuchenTableModel,
            JTable schuelerSuchenResultTable,
            int selectedRow,
            boolean isFromSchuelerSuchenResult) {
        this.dispensationenModel = dispensationenModel;
        this.svmContext = svmContext;
        this.dispensationenTableModel = dispensationenTableModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
    }

    @SuppressWarnings("DuplicatedCode")
    public void setDispensationenTable(JTable dispensationenTable) {
        this.dispensationenTable = dispensationenTable;
        setColumnCellRenderers(dispensationenTable, dispensationenTableModel);
        setJTableColumnWidthAsPercentages(dispensationenTable, 0.2, 0.2, 0.2, 0.4);
        dispensationenTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            onListSelection();
        });
        dispensationenTable.addMouseListener(new MouseAdapter() {
            @Override
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
        this.btnNeu = btnNeu;
        btnNeu.addActionListener(e -> onNeu());
    }

    private void onNeu() {
        btnNeu.setFocusPainted(true);
        DispensationErfassenDialog dispensationErfassenDialog = new DispensationErfassenDialog(svmContext, dispensationenTableModel, dispensationenModel, schuelerDatenblattModel, 0, false, "Neue Dispensation");
        dispensationErfassenDialog.pack();
        dispensationErfassenDialog.setVisible(true);
        dispensationenTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
    }

    public void setBtnBearbeiten(JButton btnBearbeiten) {
        this.btnBearbeiten = btnBearbeiten;
        enableBtnBearbeiten(false);
        btnBearbeiten.addActionListener(e -> onBearbeiten());
    }

    private void enableBtnBearbeiten(boolean enabled) {
        btnBearbeiten.setEnabled(enabled);
    }

    private void onBearbeiten() {
        btnBearbeiten.setFocusPainted(true);
        DispensationErfassenDialog dispensationErfassenDialog = new DispensationErfassenDialog(svmContext, dispensationenTableModel, dispensationenModel, schuelerDatenblattModel, dispensationenTable.getSelectedRow(), true, "Dispensation bearbeiten");
        dispensationErfassenDialog.pack();
        dispensationErfassenDialog.setVisible(true);
        dispensationenTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        this.btnLoeschen = btnLoeschen;
        enableBtnLoeschen(false);
        btnLoeschen.addActionListener(e -> onLoeschen());
    }

    private void enableBtnLoeschen(boolean enabled) {
        btnLoeschen.setEnabled(enabled);
    }

    private void onLoeschen() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Dispensation löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            dispensationenModel.eintragLoeschen(dispensationenTableModel, schuelerDatenblattModel, dispensationenTable.getSelectedRow());
            dispensationenTableModel.fireTableDataChanged();
            dispensationenTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        dispensationenTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = dispensationenTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        btnZurueck.addActionListener(e -> onZurueck());
    }

    @SuppressWarnings("DuplicatedCode")
    private void onZurueck() {
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isFromSchuelerSuchenResult);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
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
