package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.CalendarTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
    private final JTable semesterrechnungenTableData;
    private JButton btnDatenblatt;
    private JButton btnExportieren;
    private JButton btnRechnungsdatumAlle;
    private JButton btnSchulgeldAlle;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckListener;

    public SemesterrechnungenController(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, JTable semesterrechnungenTableData) {
        this.svmContext = svmContext;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.semesterrechnungenTableData = semesterrechnungenTableData;
        semesterrechnungenTableData.setModel(semesterrechnungenTableModel);
        semesterrechnungenTableData.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());
        setJTableColumnWidthAccordingToCellContentAndHeader(semesterrechnungenTableData);
        semesterrechnungenTableData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        semesterrechnungenTableData.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onDetailsBearbeiten();
                }
            }
        });
    }

    public void setLblTotal(JLabel lblTotal) {
        if (semesterrechnungenTableModel.getSemester() == null) {
            return;
        }
        String semesterStr = " (" + semesterrechnungenTableModel.getSemester().getSchuljahr() + ", " + semesterrechnungenTableModel.getSemester().getSemesterbezeichnung() + ")";
        String semesterrechnungenStr = (semesterrechnungenTableModel.getRowCount() == 1 ? " Semesterrechnung" : " Semesterrechnungen");
        String lblTotalText = "Total: " + semesterrechnungenTableModel.getRowCount() + semesterrechnungenStr;
        lblTotalText = lblTotalText + semesterStr;
        lblTotal.setText(lblTotalText);
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
//        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, semesterrechnungenTableModel, semesterrechnungenTableData, semesterrechnungenTableData.getSelectedRow(), true);
//        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
//        schuelerDatenblattPanel.addCloseListener(closeListener);
//        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckListener);
//        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
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
//        btnExportieren.setFocusPainted(true);
//        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, semesterrechnungenTableModel, null, null, ListenExportTyp.SCHUELER);
//        listenExportDialog.pack();
//        listenExportDialog.setVisible(true);
//        btnExportieren.setFocusPainted(false);
    }

    public void setBtnRechnungsdatumAlle(JButton btnRechnungsdatumAlle) {
        this.btnRechnungsdatumAlle = btnRechnungsdatumAlle;
        btnRechnungsdatumAlle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRechnungsdatumAlle();
            }
        });
    }

    private void onRechnungsdatumAlle() {
//        btnRechnungsdatumAlle.setFocusPainted(true);
//        btnRechnungsdatumAlle.setFocusPainted(false);
    }

    public void setBtnSchulgeldAlle(JButton btnSchulgeldAlle) {
        this.btnSchulgeldAlle = btnSchulgeldAlle;
        btnSchulgeldAlle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSchulgeldAlle();
            }
        });
    }

    private void onSchulgeldAlle() {
//        btnSchulgeldAlle.setFocusPainted(true);
//        btnSchulgeldAlle.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = semesterrechnungenTableData.getSelectedRow();
        enableBtnDetailsBearbeiten(selectedRowIndex >= 0);
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
