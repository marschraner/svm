package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.ui.componentmodel.CalendarTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.NumberTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.StringTableCellRenderer;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultController {
    private final SvmContext svmContext;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private JButton btnDatenblatt;
    private JButton btnExportieren;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckListener;

    public SchuelerSuchenResultController(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable) {
        this.svmContext = svmContext;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        setupSchuelerSuchenResultTable();
    }

    private void setupSchuelerSuchenResultTable() {
        schuelerSuchenResultTable.setModel(schuelerSuchenTableModel);
        schuelerSuchenResultTable.getColumnModel().getColumn(0).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(1).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(2).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(3).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(4).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(5).setCellRenderer(new CalendarTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(6).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(7).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(8).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(9).setCellRenderer(new StringTableCellRenderer());
        schuelerSuchenResultTable.getColumnModel().getColumn(10).setCellRenderer(new NumberTableCellRenderer());
        if (schuelerSuchenTableModel.getSemester() != null && schuelerSuchenTableModel.getSemester().getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            // Märchen nur im 1. Semester anzeigen
            schuelerSuchenResultTable.getColumnModel().getColumn(11).setCellRenderer(new StringTableCellRenderer());
        }
        setJTableColumnWidthAccordingToCellContentAndHeader(schuelerSuchenResultTable);
        schuelerSuchenResultTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        schuelerSuchenResultTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onDatenblatt();
                }
            }
        });
    }

    public void setLblTotal(JLabel lblTotal) {
        if (schuelerSuchenTableModel.getSemester() == null) {
            lblTotal.setText("Semester nicht erfasst.");
            return;
        }
        String semesterStr = " (" + schuelerSuchenTableModel.getSemester().getSchuljahr() + ", " + schuelerSuchenTableModel.getSemester().getSemesterbezeichnung() + ")";
        String lektionen = (schuelerSuchenTableModel.getAnzahlLektionen() == 1 ? " Lektion" : " Lektionen");
        String maercheneinteilungen = (schuelerSuchenTableModel.getAnzahlMaercheneinteilungen() == 1 ? " Märcheneinteilung" : " Märcheneinteilungen");
        String lblTotalText = "Total: " + schuelerSuchenTableModel.getRowCount() + " Schüler, " + schuelerSuchenTableModel.getAnzahlLektionen() + lektionen;
        if (schuelerSuchenTableModel.getSemester().getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
            // Nur im 1. Semester Märcheneinteilungen anzeigen
            lblTotalText = lblTotalText + ", " + schuelerSuchenTableModel.getAnzahlMaercheneinteilungen() + maercheneinteilungen;
        }
        lblTotalText = lblTotalText + semesterStr;
        lblTotal.setText(lblTotalText);
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void setBtnDatenblatt(JButton btnDatenblatt) {
        this.btnDatenblatt = btnDatenblatt;
        enableBtnDatenblatt(false);
        btnDatenblatt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDatenblatt();
            }
        });
    }

    private void enableBtnDatenblatt(boolean enabled) {
        btnDatenblatt.setEnabled(enabled);
    }

    private void onDatenblatt() {
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, schuelerSuchenResultTable, schuelerSuchenResultTable.getSelectedRow(), true);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
    }

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        btnExportieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExportieren();
            }
        });
    }

    private void onExportieren() {
        btnExportieren.setFocusPainted(true);
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, schuelerSuchenTableModel, null, null, null, ListenExportTyp.SCHUELER);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = schuelerSuchenResultTable.getSelectedRow();
        enableBtnDatenblatt(selectedRowIndex >= 0);
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
