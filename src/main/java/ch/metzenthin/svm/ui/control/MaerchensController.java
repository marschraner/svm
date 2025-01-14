package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteMaerchenCommand;
import ch.metzenthin.svm.domain.model.MaerchensModel;
import ch.metzenthin.svm.domain.model.MaerchensTableData;
import ch.metzenthin.svm.ui.componentmodel.MaerchensTableModel;
import ch.metzenthin.svm.ui.components.MaerchenErfassenDialog;

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
public class MaerchensController {
    private final SvmContext svmContext;
    private final MaerchensModel maerchensModel;
    private MaerchensTableModel maerchensTableModel;
    private JTable maerchensTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public MaerchensController(MaerchensModel maerchensModel, SvmContext svmContext) {
        this.maerchensModel = maerchensModel;
        this.svmContext = svmContext;
    }

    @SuppressWarnings("DuplicatedCode")
    public void setMaerchensTable(JTable maerchensTable) {
        this.maerchensTable = maerchensTable;
        initializeMaerchensTable();
        maerchensTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            onListSelection();
        });
        maerchensTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    private void initializeMaerchensTable() {
        MaerchensTableData maerchensTableData = new MaerchensTableData(svmContext.getSvmModel().getMaerchensAll());
        maerchensTableModel = new MaerchensTableModel(maerchensTableData);
        maerchensTable.setModel(maerchensTableModel);
        setColumnCellRenderers(maerchensTable, maerchensTableModel);
        setJTableColumnWidthAsPercentages(maerchensTable, 0.15, 0.55, 0.15, 0.15);
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        btnNeu.addActionListener(e -> onNeu());
    }

    private void onNeu() {
        btnNeu.setFocusPainted(true);
        MaerchenErfassenDialog maerchenErfassenDialog = new MaerchenErfassenDialog(svmContext, maerchensTableModel, maerchensModel, 0, false, "Neues Märchen");
        maerchenErfassenDialog.pack();
        maerchenErfassenDialog.setVisible(true);
        maerchensTableModel.fireTableDataChanged();
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
        MaerchenErfassenDialog maerchenErfassenDialog = new MaerchenErfassenDialog(svmContext, maerchensTableModel, maerchensModel, maerchensTable.getSelectedRow(), true, "Märchen bearbeiten");
        maerchenErfassenDialog.pack();
        maerchenErfassenDialog.setVisible(true);
        maerchensTableModel.fireTableDataChanged();
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
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteMaerchenCommand.Result result =
                    maerchensModel.maerchenLoeschen(
                            svmContext, maerchensTableModel, maerchensTable.getSelectedRow());
            if (result == DeleteMaerchenCommand.Result.MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT) {
                JOptionPane.showMessageDialog(
                        null,
                        "Das Maerchen wird durch mindestens eine Märcheneinteilung " +
                                "referenziert und kann nicht gelöscht werden.",
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                btnLoeschen.setFocusPainted(false);
            } else if (result == DeleteMaerchenCommand.Result.LOESCHEN_ERFOLGREICH) {
                maerchensTableModel.fireTableDataChanged();
                maerchensTable.addNotify();
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        maerchensTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = maerchensTable.getSelectedRow();
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
