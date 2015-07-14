package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.CodesTableData;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.components.CodeErfassenDialog;
import ch.metzenthin.svm.ui.components.UiComponentsUtils;

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
public class CodesController {
    private final SvmContext svmContext;
    private final CodesModel codesModel;
    private CodesTableModel codesTableModel;
    private JTable codesTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public CodesController(CodesModel codesModel, SvmContext svmContext) {
        this.codesModel = codesModel;
        this.svmContext = svmContext;
    }

    public void setCodesTable(JTable codesTable) {
        this.codesTable = codesTable;
        initializeCodesTable();
        codesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        codesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    private void initializeCodesTable() {
        CodesTableData codesTableData = new CodesTableData(codesModel.getCodes());
        codesTableModel = new CodesTableModel(codesTableData);
        codesTable.setModel(codesTableModel);
        UiComponentsUtils.setJTableWidthAsPercentages(codesTable, 0.15, 0.85);
        // SvmModel.codesAllTableModel soll dieses CodeTableModel verwenden
        this.svmContext.getSvmModel().setCodesAllTableModel(codesTableModel);
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
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesModel, 0, false, "Neuer Code");
        codeErfassenDialog.pack();
        codeErfassenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
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
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesModel, codesTable.getSelectedRow(), true, "Code bearbeiten");
        codeErfassenDialog.pack();
        codeErfassenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
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
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteCodeCommand.Result result  = codesModel.eintragLoeschen(codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens einen Schüler referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = codesTable.getSelectedRow();
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
