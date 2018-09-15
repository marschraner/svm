package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.EmailSchuelerListeDialog;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Hans Stamm
 */
public class SchuelerSuchenResultController {
    private final SvmContext svmContext;
    private SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private JLabel lblTotal;
    private JButton btnAlleDeselektieren;
    private JButton btnAlleSelektieren;
    private JButton btnDatenblatt;
    private JButton btnExportieren;
    private JButton btnEmail;
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
        setColumnCellRenderers(schuelerSuchenResultTable, schuelerSuchenTableModel);
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
        schuelerSuchenResultTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (schuelerSuchenTableModel.getAnzSelektiert() > 0) {
                    btnExportieren.setEnabled(true);
                    btnEmail.setEnabled(true);
                } else {
                    btnExportieren.setEnabled(false);
                    btnEmail.setEnabled(false);
                }
                if (schuelerSuchenTableModel.isAlleSelektiert()) {
                    btnAlleSelektieren.setVisible(false);
                    btnAlleDeselektieren.setVisible(true);
                } else {
                    btnAlleDeselektieren.setVisible(false);
                    btnAlleSelektieren.setVisible(true);
                }
                setLblTotal();
            }
        });
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        setLblTotal();
    }

    private void setLblTotal() {
        if (schuelerSuchenTableModel.getSemester() == null) {
            lblTotal.setText("Semester nicht erfasst.");
            return;
        }
        String semesterStr = " (" + schuelerSuchenTableModel.getSemester().getSchuljahr() + ", " + schuelerSuchenTableModel.getSemester().getSemesterbezeichnung() + ")";
        String lektionen = (schuelerSuchenTableModel.getAnzahlLektionen() == 1 ? " Lektion" : " Lektionen");
        String maercheneinteilungen = (schuelerSuchenTableModel.getAnzahlMaercheneinteilungen() == 1 ? " Märcheneinteilung" : " Märcheneinteilungen");
        String lblTotalText = "Total: " + schuelerSuchenTableModel.getRowCount() + " Schüler (" +
                schuelerSuchenTableModel.getAnzSelektiert() + " selektiert), " + schuelerSuchenTableModel.getAnzahlLektionen() + lektionen;
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

    public void setBtnAlleDeselektieren(JButton btnAlleDeselektieren) {
        this.btnAlleDeselektieren = btnAlleDeselektieren;
        btnAlleDeselektieren.setVisible(true);
        btnAlleDeselektieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAlleDeselektieren();
            }
        });
    }

    private void onAlleDeselektieren() {
        schuelerSuchenTableModel.alleSchuelerDeselektieren();
        btnAlleDeselektieren.setVisible(false);
        btnAlleSelektieren.setVisible(true);
        schuelerSuchenTableModel.fireTableDataChanged();
        setLblTotal();
    }

    public void setBtnAlleSelektieren(JButton btnAlleSelektieren) {
        this.btnAlleSelektieren = btnAlleSelektieren;
        btnAlleSelektieren.setVisible(false);
        btnAlleSelektieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAlleSelektieren();
            }
        });
    }

    private void onAlleSelektieren() {
        schuelerSuchenTableModel.alleSchuelerSelektieren();
        btnAlleSelektieren.setVisible(false);
        btnAlleDeselektieren.setVisible(true);
        schuelerSuchenTableModel.fireTableDataChanged();
        setLblTotal();
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
        if (schuelerSuchenTableModel.getAnzSelektiert() == 0) {
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
        int anzSelektiert = schuelerSuchenTableModel.getAnzSelektiert();
        int rowCount = schuelerSuchenTableModel.getRowCount();
        if (anzSelektiert < rowCount) {
            String str1;
            String str2;
            if (anzSelektiert > 1) {
                str1 = "sind nur " + anzSelektiert;
                str2 = "diese Einträge\nwerden";
            } else {
                str1 = "ist nur einer";
                str2 = "dieser Eintrag\nwird";
            }
            JOptionPane.showMessageDialog(null, "Es " + str1 + " der "
                    + rowCount + " Einträge selektiert. Nur " + str2
                    + " beim Exportieren berücksichtigt.", "Nicht alle Einträge selektiert", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
        }
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, schuelerSuchenTableModel, null, null, null, ListenExportTyp.SCHUELER);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
    }

    public void setBtnEmail(JButton btnEmail) {
        this.btnEmail = btnEmail;
        if (schuelerSuchenTableModel.getAnzSelektiert() == 0) {
            btnEmail.setEnabled(false);
        }
        btnEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmail();
            }
        });
    }

    private void onEmail() {
        btnEmail.setFocusPainted(true);
        int anzSelektiert = schuelerSuchenTableModel.getAnzSelektiert();
        int rowCount = schuelerSuchenTableModel.getRowCount();
        if (anzSelektiert < rowCount) {
            String str1;
            String str2;
            if (anzSelektiert > 1) {
                str1 = "sind nur " + anzSelektiert;
                str2 = "diese Einträge\nwerden";
            } else {
                str1 = "ist nur einer";
                str2 = "dieser Eintrag\nwird";
            }
            JOptionPane.showMessageDialog(null, "Es " + str1 + " der "
                    + rowCount + " Einträge selektiert. Nur " + str2
                    + " für die Gruppen-E-Mail berücksichtigt.", "Nicht alle Einträge selektiert", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
        }
        EmailSchuelerListeDialog emailSchuelerListeDialog = new EmailSchuelerListeDialog(svmContext, schuelerSuchenTableModel);
        emailSchuelerListeDialog.pack();
        emailSchuelerListeDialog.setVisible(true);
        btnEmail.setFocusPainted(false);
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
