package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteSemesterCommand;
import ch.metzenthin.svm.domain.model.SemestersModel;
import ch.metzenthin.svm.domain.model.SemestersTableData;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;
import ch.metzenthin.svm.ui.components.SemesterErfassenDialog;

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
public class SemestersController {
    private final SvmContext svmContext;
    private final SemestersModel semestersModel;
    private SemestersTableModel semestersTableModel;
    private JTable semestersTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;

    public SemestersController(SemestersModel semestersModel, SvmContext svmContext) {
        this.semestersModel = semestersModel;
        this.svmContext = svmContext;
    }

    public void setSemestersTable(JTable semestersTable) {
        this.semestersTable = semestersTable;
        initializeSemestersTable();
        semestersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        semestersTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    private void initializeSemestersTable() {
        SemestersTableData semestersTableData = new SemestersTableData(svmContext.getSvmModel().getSemestersAll());
        semestersTableModel = new SemestersTableModel(semestersTableData);
        semestersTable.setModel(semestersTableModel);
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
        SemesterErfassenDialog semesterErfassenDialog = new SemesterErfassenDialog(svmContext, semestersModel, 0, false, "Neues Semester");
        semesterErfassenDialog.pack();
        semesterErfassenDialog.setVisible(true);
        semestersTableModel.fireTableDataChanged();
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
        SemesterErfassenDialog semesterErfassenDialog = new SemesterErfassenDialog(svmContext, semestersModel, semestersTable.getSelectedRow(), true, "Semester bearbeiten");
        semesterErfassenDialog.pack();
        semesterErfassenDialog.setVisible(true);
        semestersTableModel.fireTableDataChanged();
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
            DeleteSemesterCommand.Result result  = semestersModel.semesterLoeschen(svmContext, semestersTable.getSelectedRow());
            switch (result) {
                case SEMESTER_VON_KURS_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Das Semester wird durch mindestens einen Kurs referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnLoeschen.setFocusPainted(false);
                    break;
                case SEMESTER_VON_SEMESTERRECHNUNG_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Das Semester wird durch mindestens eine Semesterrechnung referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    semestersTableModel.fireTableDataChanged();
                    semestersTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        semestersTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = semestersTable.getSelectedRow();
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
