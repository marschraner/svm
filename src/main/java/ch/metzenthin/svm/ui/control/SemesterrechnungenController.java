package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.model.SemesterrechnungBearbeitenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenModel;
import ch.metzenthin.svm.ui.componentmodel.*;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.RechnungsdatumErfassenDialog;
import ch.metzenthin.svm.ui.components.SemesterrechnungBearbeitenPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
    private JButton btnDatenblatt;
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

        // Spaltenausrichtung und Farben
        Color lila = new Color(200, 0, 200);
        semesterrechnungenTable.getColumnModel().getColumn(0).setCellRenderer(new StringTableCellRenderer());
        semesterrechnungenTable.getColumnModel().getColumn(1).setCellRenderer(new StringTableCellRenderer());
        semesterrechnungenTable.getColumnModel().getColumn(2).setCellRenderer(new CalendarColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(3).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(4).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(5).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(6).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(7).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(8).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(9).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(10).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(11).setCellRenderer(new CalendarColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(12).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(13).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(14).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(15).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(16).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(17).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(18).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(19).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(20).setCellRenderer(new NumberColorTableCellRenderer(lila));

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
        semesterrechnungenTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (semesterrechnungenTableModel.getAnzExport() > 0) {
                    btnExportieren.setEnabled(true);
                } else {
                    btnExportieren.setEnabled(false);
                }
            }
        });
    }

    public void setLblTotal(JLabel lblTotal) {
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

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        if (semesterrechnungenTableModel.getSemesterrechnungen().isEmpty() || semesterrechnungenTableModel.getAnzExport() == 0) {
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
        if (semesterrechnungenTableModel.getAnzExport() < semesterrechnungenTableModel.getRowCount()) {
            String str1;
            String str2;
            if (semesterrechnungenTableModel.getAnzExport() > 1) {
                str1 = "sind nur " + semesterrechnungenTableModel.getAnzExport();
                str2 = "diese Einträge\nwerden";
            } else {
                str1 = "ist nur einer";
                str2 = "dieser Eintrag\nwird";
            }
            JOptionPane.showMessageDialog(null, "Es " + str1 + " der "
                    + semesterrechnungenTableModel.getRowCount() + " Einträge selektiert. Nur " + str2
                    + " beim Exportieren berücksichtigt.", "Nicht alle Einträge selektiert", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
        }
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
                svmContext.getDialogIcons().getWarningIcon(),
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
                svmContext.getDialogIcons().getQuestionIcon(),
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
