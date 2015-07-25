package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKursCommand;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.components.KursErfassenDialog;

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
public class KurseController {
    private final SvmContext svmContext;
    private KurseSemesterwahlModel kurseSemesterwahlModel;
    private final KurseModel kurseModel;
    private KurseTableModel kurseTableModel;
    private JTable kurseTable;
    private JLabel lblTotal;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private JButton btnImport;
    private ActionListener closeListener;

    public KurseController(KurseModel kurseModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel, SvmContext svmContext) {
        this.kurseModel = kurseModel;
        this.kurseSemesterwahlModel = kurseSemesterwahlModel;
        this.kurseTableModel = kurseTableModel;
        this.svmContext = svmContext;
    }

    public void setKurseTable(JTable kurseTable) {
        this.kurseTable = kurseTable;
        kurseTable.setModel(kurseTableModel);
        setJTableColumnWidthAccordingToCellContentAndHeader(kurseTable);
        kurseTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        kurseTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
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
        KursErfassenDialog kursErfassenDialog = new KursErfassenDialog(svmContext, kurseModel, kurseSemesterwahlModel, kurseTableModel, 0, false, "Neuer Kurs");
        kursErfassenDialog.pack();
        kursErfassenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
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
        KursErfassenDialog kursErfassenDialog = new KursErfassenDialog(svmContext, kurseModel, kurseSemesterwahlModel, kurseTableModel, kurseTable.getSelectedRow(), true, "Kurs bearbeiten");
        kursErfassenDialog.pack();
        kursErfassenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
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
                "Durch Drücken des Ja-Buttons wird der Kurs unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteKursCommand.Result result  = kurseModel.kursLoeschen(kurseTableModel, kurseTable.getSelectedRow());
            switch (result) {
                case KURS_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Die Kurs wird durch mindestens einen Kurs referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    kurseTable.addNotify();
                    break;
            }
        }
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
        btnLoeschen.setFocusPainted(false);
        if (kurseTableModel.getRowCount() == 0) {
            btnImport.setEnabled(true);
        }
    }

    public void setBtnImport(JButton btnImport) {
        this.btnImport = btnImport;
        if (kurseTableModel.getRowCount() > 0) {
            btnImport.setEnabled(false);
            return;
        }
        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onImport();
            }
        });
    }

    private void onImport() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Sollen die Kurse vom vorherigen Semester importiert werden?",
                "Import Kurse vom vorherigen Semester?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            kurseModel.importKurseFromPreviousSemester(svmContext.getSvmModel(), kurseSemesterwahlModel, kurseTableModel);
            kurseTableModel.fireTableDataChanged();
            btnImport.setEnabled(false);
        }
    }

    private void onListSelection() {
        int selectedRowIndex = kurseTable.getSelectedRow();
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
