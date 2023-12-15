package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKursortCommand;
import ch.metzenthin.svm.domain.model.KursorteModel;
import ch.metzenthin.svm.domain.model.KursorteTableData;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;
import ch.metzenthin.svm.ui.components.KursortErfassenDialog;

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
public class KursorteController {
    private final SvmContext svmContext;
    private final KursorteModel kursorteModel;
    private KursorteTableModel kursorteTableModel;
    private JTable kursorteTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public KursorteController(KursorteModel kursorteModel, SvmContext svmContext) {
        this.kursorteModel = kursorteModel;
        this.svmContext = svmContext;
    }

    @SuppressWarnings("DuplicatedCode")
    public void setKursorteTable(JTable kursorteTable) {
        this.kursorteTable = kursorteTable;
        KursorteTableData kursorteTableData = new KursorteTableData(svmContext.getSvmModel().getKursorteAll());
        kursorteTableModel = new KursorteTableModel(kursorteTableData);
        kursorteTable.setModel(kursorteTableModel);
        setColumnCellRenderers(kursorteTable, kursorteTableModel);
        setJTableColumnWidthAsPercentages(kursorteTable, 0.75, 0.25);
        kursorteTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            onListSelection();
        });
        kursorteTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        btnNeu.addActionListener(e -> onNeu());
    }

    private void onNeu() {
        btnNeu.setFocusPainted(true);
        KursortErfassenDialog kursortErfassenDialog = new KursortErfassenDialog(svmContext, kursorteTableModel, kursorteModel, 0, false, "Neuer Kursort");
        kursortErfassenDialog.pack();
        kursortErfassenDialog.setVisible(true);
        kursorteTableModel.fireTableDataChanged();
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
        KursortErfassenDialog kursortErfassenDialog = new KursortErfassenDialog(svmContext, kursorteTableModel, kursorteModel, kursorteTable.getSelectedRow(), true, "Kursort bearbeiten");
        kursortErfassenDialog.pack();
        kursortErfassenDialog.setVisible(true);
        kursorteTableModel.fireTableDataChanged();
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
                "Kursort löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteKursortCommand.Result result = kursorteModel.eintragLoeschen(svmContext, kursorteTableModel, kursorteTable.getSelectedRow());
            switch (result) {
                case KURSORT_VON_KURS_REFERENZIERT -> {
                    JOptionPane.showMessageDialog(null, "Der Kursort wird durch mindestens einen Kurs referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnLoeschen.setFocusPainted(false);
                }
                case LOESCHEN_ERFOLGREICH -> {
                    kursorteTableModel.fireTableDataChanged();
                    kursorteTable.addNotify();
                }
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        kursorteTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = kursorteTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Abbrechen"));
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }


}
