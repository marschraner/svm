package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.model.SemesterrechnungBearbeitenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenModel;
import ch.metzenthin.svm.ui.componentmodel.CalendarTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.NumberTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.RechnungsdatumErfassenDialog;
import ch.metzenthin.svm.ui.components.SemesterrechnungBearbeitenPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungenController {
    private final SvmContext svmContext;
    private SemesterrechnungenModel semesterrechnungenModel;
    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
    private JTable semesterrechnungenTable;
    private JLabel lblTotal;
    private JButton btnDatenblatt;
    private JButton btnImportieren;
    private JButton btnExportieren;
    private JButton btnRechnungsdatum;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckListener;

    public SemesterrechnungenController(SvmContext svmContext, SemesterrechnungenModel semesterrechnungenModel, SemesterrechnungenTableModel semesterrechnungenTableModel) {
        this.svmContext = svmContext;
        this.semesterrechnungenModel = semesterrechnungenModel;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;

    }

    public void setSemesterrechnungenTable(JTable semesterrechnungenTable) {
        this.semesterrechnungenTable = semesterrechnungenTable;
        semesterrechnungenTable.setModel(semesterrechnungenTableModel);
        semesterrechnungenTable.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());
        setJTableColumnWidthAccordingToCellContentAndHeader(semesterrechnungenTable);
        semesterrechnungenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        semesterrechnungenTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onDetailsBearbeiten();
                }
            }
        });
        // Farben für Spalten
        semesterrechnungenTable.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());
        TableColumn tm = semesterrechnungenTable.getColumnModel().getColumn(2);
        tm.setCellRenderer(new CalendarTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(3);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(4);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(5);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(6);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(7);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.BLUE));
        tm = semesterrechnungenTable.getColumnModel().getColumn(8);
        tm.setCellRenderer(new CalendarTableCellRenderer(Color.RED));
        tm = semesterrechnungenTable.getColumnModel().getColumn(9);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.RED));
        tm = semesterrechnungenTable.getColumnModel().getColumn(10);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.RED));
        tm = semesterrechnungenTable.getColumnModel().getColumn(11);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.RED));
        tm = semesterrechnungenTable.getColumnModel().getColumn(12);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.RED));
        tm = semesterrechnungenTable.getColumnModel().getColumn(13);
        tm.setCellRenderer(new NumberTableCellRenderer(Color.RED));
        Color darkGreen = new Color(200, 0, 200);
        tm = semesterrechnungenTable.getColumnModel().getColumn(15);
        tm.setCellRenderer(new NumberTableCellRenderer(darkGreen));
        tm = semesterrechnungenTable.getColumnModel().getColumn(16);
        tm.setCellRenderer(new NumberTableCellRenderer(darkGreen));
        tm = semesterrechnungenTable.getColumnModel().getColumn(17);
        tm.setCellRenderer(new NumberTableCellRenderer(darkGreen));
        tm = semesterrechnungenTable.getColumnModel().getColumn(18);
        tm.setCellRenderer(new NumberTableCellRenderer(darkGreen));
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        if (semesterrechnungenTableModel.getSemester() == null) {
            lblTotal.setText("Noch keine Semesterrechnungen erfasst.");
            return;
        }
        lblTotal.setText(semesterrechnungenModel.getTotal(semesterrechnungenTableModel));
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void setBtnDetailsBearbeiten(JButton btnDatenblatt) {
        this.btnDatenblatt = btnDatenblatt;
        enableBtnDetailsBearbeiten(false);
        btnDatenblatt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDetailsBearbeiten();
            }
        });
    }

    private void enableBtnDetailsBearbeiten(boolean enabled) {
        btnDatenblatt.setEnabled(enabled);
    }

    private void onDetailsBearbeiten() {
        SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel = semesterrechnungenModel.getSemesterrechnungBearbeitenModel(svmContext, semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
        SemesterrechnungBearbeitenPanel semesterrechnungBearbeitenPanel = new SemesterrechnungBearbeitenPanel(svmContext, semesterrechnungBearbeitenModel, semesterrechnungenModel, semesterrechnungenTableModel, semesterrechnungenTable, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
        semesterrechnungBearbeitenPanel.addNextPanelListener(nextPanelListener);
        semesterrechnungBearbeitenPanel.addCloseListener(closeListener);
        semesterrechnungBearbeitenPanel.addZurueckZuSemesterrechnungSuchenListener(zurueckListener);
        String title = "Semesterrechnung " + semesterrechnungenTableModel.getSemester().getSemesterbezeichnung() + " " + semesterrechnungenTableModel.getSemester().getSchuljahr();
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungBearbeitenPanel.$$$getRootComponent$$$(), title}, ActionEvent.ACTION_PERFORMED, "Semesterrechnung ausgewählt"));
    }

    public void setBtnImportieren(JButton btnImportieren) {
        this.btnImportieren = btnImportieren;
        btnImportieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onImportieren();
            }
        });
    }

    private void onImportieren() {
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Sollen die Semesterrechnungen vom vorherigen Semester importiert werden?",
                "Semesterrechnungen vom vorherigen Semester importieren?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 1) {
            btnImportieren.setFocusPainted(false);
            return;
        }
        Object[] optionsQuestion = {"Ja", "Nein"};
        n = JOptionPane.showOptionDialog(
                null,
                "Sollen offene Restbeträge von Semesterrechnungen des vorherigen Semesters \n" +
                        "als Zuschlag bzw. Ermässigung verbucht werden?",
                "Offene Restbeträge",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                optionsQuestion,  //the titles of buttons
                optionsQuestion[1]); //default button title
        boolean importRestbetraege = n != 1;
        Object[] optionsUeberschreiben = {"Ja", "Nein"};
        n = JOptionPane.showOptionDialog(
                null,
                "Sollen allfällige im jetzigen Semester bereits bestehende Semesterrechnungen nochmals\n" +
                        "importiert und überschrieben werden?\n" +
                        "(Davon ausgenommen sind Semesterrechnungen mit gesetztem Rechnungsdatum.)",
                "Bereits vorhandene Semesterrechnungen überschreiben?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                optionsUeberschreiben,  //the titles of buttons
                optionsUeberschreiben[1]); //default button title
        boolean bisherigeUeberschreiben = n != 1;
        if (bisherigeUeberschreiben) {
            Object[] optionsWarning = {"OK", "Abbrechen"};
            n = JOptionPane.showOptionDialog(
                    null,
                    "Warnung: Allfällige bereits bestehende Semesterrechnungen werden durch das Importieren überschrieben.\n" +
                            "Davon ausgenommen sind Semesterrechnungen mit gesetztem Rechnungsdatum.",
                    "Warnung",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,     //do not use a custom Icon
                    optionsWarning,  //the titles of buttons
                    optionsWarning[1]); //default button title
            if (n == 1) {
                btnImportieren.setFocusPainted(false);
                return;
            }
        }
        semesterrechnungenModel.importSemesterrechnungenFromPreviousSemester(semesterrechnungenTableModel, bisherigeUeberschreiben, importRestbetraege);
        semesterrechnungenTableModel.fireTableDataChanged();
        lblTotal.setText(semesterrechnungenModel.getTotal(semesterrechnungenTableModel));
        btnImportieren.setFocusPainted(false);
        if (semesterrechnungenTableModel.getRowCount() > 0) {
            btnExportieren.setEnabled(true);
            btnRechnungsdatum.setEnabled(true);
        }
    }

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        if (semesterrechnungenTableModel.getSemesterrechnungen().isEmpty()) {
            btnExportieren.setEnabled(false);
        }
        btnExportieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExportieren();
            }
        });
    }

    private void onExportieren() {
        btnExportieren.setFocusPainted(true);
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, null, null, null, semesterrechnungenTableModel, ListenExportTyp.SEMESTERRECHNUNGEN);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
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
                "Durch Drücken des Ja-Buttons wird die Semesterrechnung unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            semesterrechnungenModel.semesterrechnungLoeschen(semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
            semesterrechnungenTableModel.fireTableDataChanged();
            semesterrechnungenTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        semesterrechnungenTable.clearSelection();
    }


    public void setBtnRechnungsdatum(JButton btnRechnungsdatum) {
        this.btnRechnungsdatum = btnRechnungsdatum;
        if (semesterrechnungenTableModel.getSemesterrechnungen().isEmpty()) {
            btnRechnungsdatum.setEnabled(false);
        }
        btnRechnungsdatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsdatum();
            }
        });
    }

    private void onRechnungsdatum() {
        btnRechnungsdatum.setFocusPainted(true);

        // Wahl des Rechnungstyps
        Object[] optionsRechnungstyp = {"Vorrechnung", "Nachrechnung"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Für welchen Rechnungstyp soll das Rechnungsdatum gesetzt werden?",
                "Wahl des Rechnungstyps",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                optionsRechnungstyp,  //the titles of buttons
                optionsRechnungstyp[0]); //default button title
        Rechnungstyp rechnungstyp = (n == 1 ? Rechnungstyp.NACHRECHNUNG : Rechnungstyp.VORRECHNUNG);

        // Rechnungsdatum erfassen-Dialog
        RechnungsdatumErfassenDialog rechnungsdatumErfassenDialog = new RechnungsdatumErfassenDialog(svmContext, semesterrechnungenTableModel, rechnungstyp);
        rechnungsdatumErfassenDialog.pack();
        rechnungsdatumErfassenDialog.setVisible(true);
        btnRechnungsdatum.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = semesterrechnungenTable.getSelectedRow();
        enableBtnDetailsBearbeiten(selectedRowIndex >= 0);
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

    public void setBtnZurueck(JButton btnZurueck) {
        this.btnZurueck = btnZurueck;
        btnZurueck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        zurueckListener.actionPerformed(new ActionEvent(btnZurueck, ActionEvent.ACTION_PERFORMED, "Zurück"));
    }

    public void addZurueckListener(ActionListener zurueckListener) {
        this.zurueckListener = zurueckListener;
    }

}
