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
    private boolean nachGeloeschtenGesucht;
    private JTable semesterrechnungenTable;
    private JLabel lblTotal;
    private JButton btnAlleDeselektieren;
    private JButton btnAlleSelektieren;
    private JButton btnDatenblatt;
    private JButton btnExportieren;
    private JButton btnRechnungsdatum;
    private JButton btnLoeschen;
    private JButton btnWiederherstellen;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckListener;

    public SemesterrechnungenController(SvmContext svmContext, SemesterrechnungenModel semesterrechnungenModel, SemesterrechnungenTableModel semesterrechnungenTableModel, boolean nachGeloeschtenGesucht) {
        this.svmContext = svmContext;
        this.semesterrechnungenModel = semesterrechnungenModel;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.nachGeloeschtenGesucht = nachGeloeschtenGesucht;
    }

    public void setSemesterrechnungenTable(JTable semesterrechnungenTable) {
        this.semesterrechnungenTable = semesterrechnungenTable;
        semesterrechnungenTable.setModel(semesterrechnungenTableModel);
        semesterrechnungenTable.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());

        // Spaltenausrichtung und Farben
        Color lila = new Color(200, 0, 200);
        int columnIndex = (nachGeloeschtenGesucht ? 0 : 1);
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new StringTableCellRenderer());
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new StringTableCellRenderer());
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new CalendarColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.BLUE));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new CalendarColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex++).setCellRenderer(new NumberColorTableCellRenderer(Color.RED));
        semesterrechnungenTable.getColumnModel().getColumn(columnIndex).setCellRenderer(new NumberColorTableCellRenderer(lila));

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
                if (!nachGeloeschtenGesucht && me.getClickCount() == 2) {
                    onDetailsBearbeiten();
                }
            }
        });
        semesterrechnungenTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (semesterrechnungenTableModel.getAnzSelektiert() > 0) {
                    btnExportieren.setEnabled(true);
                } else {
                    btnExportieren.setEnabled(false);
                }
                if (!nachGeloeschtenGesucht) {
                    if (semesterrechnungenTableModel.isAlleSelektiert()) {
                        btnAlleSelektieren.setVisible(false);
                        btnAlleDeselektieren.setVisible(true);
                    } else {
                        btnAlleDeselektieren.setVisible(false);
                        btnAlleSelektieren.setVisible(true);
                    }
                    setLblTotal();
                }
            }
        });
    }

    public void setLblTitel(JLabel lblTitel) {
        lblTitel.setText(nachGeloeschtenGesucht ? "Gelöschte Semesterrechnungen" : "Suchresultat");
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        setLblTotal();
    }

    private void setLblTotal() {
        if (semesterrechnungenTableModel.getSemester() == null) {
            lblTotal.setText("Noch keine Semesterrechnungen erfasst.");
            return;
        }
        String semesterStr = " (" + semesterrechnungenTableModel.getSemester().getSchuljahr() + ", " + semesterrechnungenTableModel.getSemester().getSemesterbezeichnung() + ")";
        String semesterrechnungenStr = (semesterrechnungenTableModel.getRowCount() == 1 ? " Semesterrechnung" : " Semesterrechnungen");
        String selektiertStr = (nachGeloeschtenGesucht ? "" : " (" + semesterrechnungenTableModel.getAnzSelektiert() + " selektiert)");
        String lblTotalText = "Total: " + semesterrechnungenTableModel.getRowCount() + semesterrechnungenStr + selektiertStr + semesterStr;
        lblTotal.setText(lblTotalText);
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void setBtnDetailsBearbeiten(JButton btnDatenblatt) {
        this.btnDatenblatt = btnDatenblatt;
        if (nachGeloeschtenGesucht) {
            btnDatenblatt.setVisible(false);
        }
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

    public void setBtnAlleDeselektieren(JButton btnAlleDeselektieren) {
        this.btnAlleDeselektieren = btnAlleDeselektieren;
        if (nachGeloeschtenGesucht) {
            btnAlleDeselektieren.setVisible(false);
        } else {
            btnAlleDeselektieren.setVisible(true);
        }
        btnAlleDeselektieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAlleDeselektieren();
            }
        });
    }

    private void onAlleDeselektieren() {
        semesterrechnungenTableModel.alleSemesterrechnungenDeselektieren();
        btnAlleDeselektieren.setVisible(false);
        btnAlleSelektieren.setVisible(true);
        semesterrechnungenTableModel.fireTableDataChanged();
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
        semesterrechnungenTableModel.alleSemesterrechnungenSelektieren();
        btnAlleSelektieren.setVisible(false);
        btnAlleDeselektieren.setVisible(true);
        semesterrechnungenTableModel.fireTableDataChanged();
        setLblTotal();
    }

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        if (nachGeloeschtenGesucht) {
            btnExportieren.setVisible(false);
        }
        if (semesterrechnungenTableModel.getSemesterrechnungen().isEmpty() || semesterrechnungenTableModel.getAnzSelektiert() == 0) {
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
        int anzSelektiert = semesterrechnungenTableModel.getAnzSelektiert();
        int rowCount = semesterrechnungenTableModel.getRowCount();
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
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, null, null, null, semesterrechnungenTableModel, ListenExportTyp.SEMESTERRECHNUNGEN);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        this.btnLoeschen = btnLoeschen;
        if (nachGeloeschtenGesucht) {
            btnLoeschen.setVisible(false);
        }
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
        if (semesterrechnungenModel.hasSemesterrechnungKurse(semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()))) {
            // Logisches Löschen für Semesterrechnungen mit Kursen im aktuellen Semester bzw. nicht abgemeldeten Kursen
            // des vorhergenden Semesters, da bei physischem Löschen bei der nächsten Suche automatisch wieder eine neue
            // Semesterrechnung erzeugt würde
            int n = JOptionPane.showOptionDialog(
                    null,
                    "Soll die Semesterrechnung gelöscht werden?\n" +
                    "(Die Rechnung kann später wenn nötig wiederhergestellt werden.)",
                    "Eintrag löschen?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    svmContext.getDialogIcons().getQuestionIcon(),
                    options,  //the titles of buttons
                    options[1]); //default button title
            if (n == 0) {
                semesterrechnungenModel.semesterrechnungLogischLoeschen(semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
                semesterrechnungenTableModel.fireTableDataChanged();
                semesterrechnungenTable.addNotify();
                setLblTotal();
            }
        } else {
            // Physisches Löschen
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
                semesterrechnungenModel.semesterrechnungPhysischLoeschen(semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
                semesterrechnungenTableModel.fireTableDataChanged();
                semesterrechnungenTable.addNotify();
                setLblTotal();
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        semesterrechnungenTable.clearSelection();
    }

    public void setBtnWiederherstellen(JButton btnWiederherstellen) {
        this.btnWiederherstellen = btnWiederherstellen;
        if (!nachGeloeschtenGesucht) {
            btnWiederherstellen.setVisible(false);
        }
        enableBtnWiederherstellen(false);
        btnWiederherstellen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWiederherstellen();
            }
        });
    }

    private void enableBtnWiederherstellen(boolean enabled) {
        btnWiederherstellen.setEnabled(enabled);
    }

    private void onWiederherstellen() {
        btnWiederherstellen.setFocusPainted(true);
        semesterrechnungenModel.semesterrechnungWiederherstellen(semesterrechnungenTableModel, semesterrechnungenTable.convertRowIndexToModel(semesterrechnungenTable.getSelectedRow()));
        semesterrechnungenTableModel.fireTableDataChanged();
        semesterrechnungenTable.addNotify();
        setLblTotal();
        btnWiederherstellen.setFocusPainted(false);
        enableBtnWiederherstellen(false);
        semesterrechnungenTable.clearSelection();
    }

    public void setBtnRechnungsdatum(JButton btnRechnungsdatum) {
        this.btnRechnungsdatum = btnRechnungsdatum;
        if (nachGeloeschtenGesucht) {
            btnRechnungsdatum.setVisible(false);
        }
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

        int anzSelektiert = semesterrechnungenTableModel.getAnzSelektiert();
        int rowCount = semesterrechnungenTableModel.getRowCount();
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
                    + " beim Setzen des Rechnungsdatums berücksichtigt.", "Nicht alle Einträge selektiert", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
        }

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
        RechnungsdatumErfassenDialog rechnungsdatumErfassenDialog = new RechnungsdatumErfassenDialog(svmContext, semesterrechnungenTableModel.getSelektierteSemesterrechnungen(), rechnungstyp);
        rechnungsdatumErfassenDialog.pack();
        rechnungsdatumErfassenDialog.setVisible(true);
        semesterrechnungenTableModel.fireTableDataChanged();
        btnRechnungsdatum.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = semesterrechnungenTable.getSelectedRow();
        enableBtnDetailsBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
        enableBtnWiederherstellen(selectedRowIndex >= 0);
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
