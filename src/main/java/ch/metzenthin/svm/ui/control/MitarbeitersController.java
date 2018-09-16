package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.domain.commands.CallDefaultEmailClientCommand;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCommand;
import ch.metzenthin.svm.domain.model.MitarbeitersModel;
import ch.metzenthin.svm.ui.componentmodel.CalendarTableCellRenderer;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import ch.metzenthin.svm.ui.components.MitarbeiterErfassenDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Martin Schraner
 */
public class MitarbeitersController {
    private static final String EMAIL = "E-Mail";
    private static final String GRUPPEN_EMAIL = "Gruppen-E-Mail";
    private final SvmContext svmContext;
    private final MitarbeitersModel mitarbeitersModel;
    private MitarbeitersTableModel mitarbeitersTableModel;
    private JTable mitarbeitersTable;
    private JLabel lblTotal;
    private JButton btnAlleDeselektieren;
    private JButton btnAlleSelektieren;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnExportieren;
    private JButton btnEmail;
    private JButton btnAbbrechen;
    private JButton btnZurueck;
    private ActionListener closeListener;
    private ActionListener zurueckListener;

    public MitarbeitersController(MitarbeitersModel mitarbeitersModel, SvmContext svmContext, MitarbeitersTableModel mitarbeitersTableModel) {
        this.mitarbeitersModel = mitarbeitersModel;
        this.svmContext = svmContext;
        this.mitarbeitersTableModel = mitarbeitersTableModel;
    }

    public void setMitarbeitersTable(final JTable mitarbeitersTable) {
        this.mitarbeitersTable = mitarbeitersTable;
        initializeMitarbeitersTable();
        mitarbeitersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        mitarbeitersTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
        mitarbeitersTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int anzSelektiert = mitarbeitersTableModel.getAnzSelektiert();
                int rowCount = mitarbeitersTableModel.getRowCount();
                if (anzSelektiert > 0) {
                    btnExportieren.setEnabled(true);
                    btnEmail.setEnabled(true);
                } else {
                    btnExportieren.setEnabled(false);
                    btnEmail.setEnabled(false);
                }
                if (rowCount <= 1 || anzSelektiert == 1) {
                    btnEmail.setText(EMAIL);
                } else {
                    btnEmail.setText(GRUPPEN_EMAIL);
                }
                if (anzSelektiert == rowCount) {
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

    private void initializeMitarbeitersTable() {
        mitarbeitersTable.setModel(mitarbeitersTableModel);
        mitarbeitersTable.setDefaultRenderer(Calendar.class, new CalendarTableCellRenderer());
        setColumnCellRenderers(mitarbeitersTable, mitarbeitersTableModel);
        setJTableColumnWidthAccordingToCellContentAndHeader(mitarbeitersTable);
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        setLblTotal();
    }

    private void setLblTotal() {
        String lblTotalText = "Total: " + mitarbeitersTableModel.getRowCount() + " Mitarbeiter (" +
                mitarbeitersTableModel.getAnzSelektiert() + " selektiert)";
        lblTotal.setText(lblTotalText);
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
        mitarbeitersTableModel.alleMitarbeiterDeselektieren();
        btnAlleDeselektieren.setVisible(false);
        btnAlleSelektieren.setVisible(true);
        mitarbeitersTableModel.fireTableDataChanged();
        setLblTotal();
        if (mitarbeitersTableModel.getRowCount() <= 1) {
            btnEmail.setText(EMAIL);
        } else {
            btnEmail.setText(GRUPPEN_EMAIL);
        }
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
        mitarbeitersTableModel.alleMitarbeiterSelektieren();
        btnAlleSelektieren.setVisible(false);
        btnAlleDeselektieren.setVisible(true);
        mitarbeitersTableModel.fireTableDataChanged();
        setLblTotal();
        if (mitarbeitersTableModel.getRowCount() <= 1) {
            btnEmail.setText(EMAIL);
        } else {
            btnEmail.setText(GRUPPEN_EMAIL);
        }
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
        MitarbeiterErfassenDialog mitarbeiterErfassenDialog = new MitarbeiterErfassenDialog(svmContext, mitarbeitersTableModel, mitarbeitersModel, 0, false, "Neuer Mitarbeiter");
        mitarbeiterErfassenDialog.pack();
        mitarbeiterErfassenDialog.setVisible(true);
        setLblTotal();
        if (mitarbeitersTableModel.getRowCount() <= 1) {
            btnEmail.setText(EMAIL);
        } else {
            btnEmail.setText(GRUPPEN_EMAIL);
        }
        mitarbeitersTableModel.fireTableDataChanged();
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
        MitarbeiterErfassenDialog mitarbeiterErfassenDialog = new MitarbeiterErfassenDialog(svmContext, mitarbeitersTableModel, mitarbeitersModel, mitarbeitersTable.convertRowIndexToModel(mitarbeitersTable.getSelectedRow()), true, "Mitarbeiter bearbeiten");
        mitarbeiterErfassenDialog.pack();
        mitarbeiterErfassenDialog.setVisible(true);
        mitarbeitersTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
        int rowCount = mitarbeitersTableModel.getRowCount();
        if (rowCount > 0) {
            btnExportieren.setEnabled(true);
            btnEmail.setEnabled(true);
            if (rowCount <= 1) {
                btnEmail.setText(EMAIL);
            } else {
                btnEmail.setText(GRUPPEN_EMAIL);
            }
        } else {
            btnExportieren.setEnabled(false);
            btnEmail.setEnabled(false);
        }
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
                "Durch Drücken des Ja-Buttons wird der Mitarbeiter unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                svmContext.getDialogIcons().getWarningIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteMitarbeiterCommand.Result result  = mitarbeitersModel.mitarbeiterLoeschen(mitarbeitersTableModel, mitarbeitersTable.convertRowIndexToModel(mitarbeitersTable.getSelectedRow()));
            switch (result) {
                case MITARBEITER_VON_KURS_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Mitarbeiter wird durch mindestens einen Kurs referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    mitarbeitersTableModel.fireTableDataChanged();
                    mitarbeitersTable.addNotify();
                    JOptionPane.showMessageDialog(
                            null,
                            "Der Mitarbeiter wurde erfolgreich aus der Datenbank gelöscht.",
                            "Löschen erfolgreich",
                            JOptionPane.INFORMATION_MESSAGE,
                            svmContext.getDialogIcons().getInformationIcon());
                    break;
            }
        }
        setLblTotal();
        mitarbeitersTableModel.fireTableDataChanged();
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        mitarbeitersTable.clearSelection();
        int rowCount = mitarbeitersTableModel.getRowCount();
        if (rowCount > 0) {
            btnExportieren.setEnabled(true);
            btnEmail.setEnabled(true);
            if (rowCount <= 1) {
                btnEmail.setText(EMAIL);
            }
        } else {
            btnExportieren.setEnabled(false);
            btnEmail.setEnabled(false);
        }
    }

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        if (mitarbeitersTableModel.getRowCount() == 0) {
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
        int anzSelektiert = mitarbeitersTableModel.getAnzSelektiert();
        int rowCount = mitarbeitersTableModel.getRowCount();
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
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, null, mitarbeitersTableModel, null, null, ListenExportTyp.MITARBEITERS);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
    }

    public void setBtnEmail(JButton btnEmail) {
        this.btnEmail = btnEmail;
        int rowCount = mitarbeitersTableModel.getRowCount();
        if (rowCount == 0) {
            btnEmail.setEnabled(false);
        }
        if (rowCount <= 1) {
            btnEmail.setText(EMAIL);
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
        int anzSelektiert = mitarbeitersTableModel.getAnzSelektiert();
        int rowCount = mitarbeitersTableModel.getRowCount();
        if (anzSelektiert > 1 && anzSelektiert < rowCount) {
            JOptionPane.showMessageDialog(null, "Es sind nur " + anzSelektiert + " der "
                    + rowCount + " Einträge selektiert. Nur diese Einträge\nwerden"
                    + " für die Gruppen-E-Mail berücksichtigt.", "Nicht alle Einträge selektiert", JOptionPane.INFORMATION_MESSAGE, svmContext.getDialogIcons().getInformationIcon());
        }
        CallDefaultEmailClientCommand.Result result = mitarbeitersModel.callEmailClient(mitarbeitersTableModel);
        if (result == CallDefaultEmailClientCommand.Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT) {
            JOptionPane.showMessageDialog(null, "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
        }
        if (!mitarbeitersModel.getFehlendeEmailAdressen().isEmpty()) {
            StringBuilder fehlend = new StringBuilder();
            for (String emailAdresse : mitarbeitersModel.getFehlendeEmailAdressen()) {
                fehlend.append(emailAdresse);
                fehlend.append("\n");
            }
            fehlend.setLength(fehlend.length() - 1);
            JOptionPane.showMessageDialog(null, "Für folgende Mitarbeiter ist keine E-Mail-Adresse erfasst:\n" + fehlend, "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
        }
        if (!mitarbeitersModel.getUngueltigeEmailAdressen().isEmpty()) {
            StringBuilder ungueltig = new StringBuilder();
            for (String emailAdresse : mitarbeitersModel.getUngueltigeEmailAdressen()) {
                ungueltig.append(emailAdresse);
                ungueltig.append("\n");
            }
            ungueltig.setLength(ungueltig.length() - 1);
            JOptionPane.showMessageDialog(null, "Die folgende(n) E-Mail-Adresse(n) ist/sind ungültig und wurde(n) ignoriert:\n" + ungueltig, "Warnung", JOptionPane.WARNING_MESSAGE, svmContext.getDialogIcons().getWarningIcon());
        }
        btnEmail.setFocusPainted(false);
    }


    private void onListSelection() {
        int selectedRowIndex = mitarbeitersTable.getSelectedRow();
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
