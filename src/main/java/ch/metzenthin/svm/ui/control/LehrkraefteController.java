package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteLehrkraftCommand;
import ch.metzenthin.svm.domain.model.LehrkraefteModel;
import ch.metzenthin.svm.domain.model.LehrkraefteTableData;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;
import ch.metzenthin.svm.ui.components.LehrkraftErfassenDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Martin Schraner
 */
public class LehrkraefteController {
    private final SvmContext svmContext;
    private final LehrkraefteModel lehrkraefteModel;
    private LehrkraefteTableModel lehrkraefteTableModel;
    private JTable lehrkraefteTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public LehrkraefteController(LehrkraefteModel lehrkraefteModel, SvmContext svmContext) {
        this.lehrkraefteModel = lehrkraefteModel;
        this.svmContext = svmContext;
        svmContext.getSvmModel().loadLehrkraefteAll();
    }

    public void setLehrkraefteTable(JTable lehrkraefteTable) {
        this.lehrkraefteTable = lehrkraefteTable;
        initializeLehrkraefteTable();
        lehrkraefteTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        lehrkraefteTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    private void initializeLehrkraefteTable() {
        LehrkraefteTableData lehrkraefteTableData = new LehrkraefteTableData(svmContext.getSvmModel().getLehrkraefteAll());
        lehrkraefteTableModel = new LehrkraefteTableModel(lehrkraefteTableData);
        lehrkraefteTable.setModel(lehrkraefteTableModel);
        setJTableColumnWidthAccordingToCellContentAndHeader(lehrkraefteTable);
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
        LehrkraftErfassenDialog lehrkraftErfassenDialog = new LehrkraftErfassenDialog(svmContext, lehrkraefteModel, 0, false, "Neue Lehrkraft");
        lehrkraftErfassenDialog.pack();
        lehrkraftErfassenDialog.setVisible(true);
        lehrkraefteTableModel.fireTableDataChanged();
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
        LehrkraftErfassenDialog lehrkraftErfassenDialog = new LehrkraftErfassenDialog(svmContext, lehrkraefteModel, lehrkraefteTable.getSelectedRow(), true, "Lehrkraft bearbeiten");
        lehrkraftErfassenDialog.pack();
        lehrkraftErfassenDialog.setVisible(true);
        lehrkraefteTableModel.fireTableDataChanged();
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
                "Durch Drücken des Ja-Buttons wird die Lehrkraft unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteLehrkraftCommand.Result result  = lehrkraefteModel.lehrkraftLoeschen(svmContext, lehrkraefteTable.getSelectedRow());
            switch (result) {
                case LEHRKRAFT_VON_KURS_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Die Lehrkraft wird durch mindestens einen Kurs referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    lehrkraefteTable.addNotify();
                    JOptionPane.showMessageDialog(
                            null,
                            "Die Lehrkraft wurde erfolgreich aus der Datenbank gelöscht.",
                            "Löschen erfolgreich",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        lehrkraefteTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = lehrkraefteTable.getSelectedRow();
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
