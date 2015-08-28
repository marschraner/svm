package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.ListenExportTyp;
import ch.metzenthin.svm.domain.commands.DeleteKursCommand;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.KursErfassenDialog;
import ch.metzenthin.svm.ui.components.KursSchuelerHinzufuegenDialog;
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
 * @author Martin Schraner
 */
public class KurseController {
    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private final int selectedRow;
    private final boolean isKurseSchueler;
    private final boolean isFromSchuelerSuchenResult;
    private KurseSemesterwahlModel kurseSemesterwahlModel;
    private final KurseModel kurseModel;
    private KurseTableModel kurseTableModel;
    private JTable kurseTable;
    private JLabel lblTotal;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private JButton btnImportieren;
    private JButton btnExportieren;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public KurseController(KurseModel kurseModel, SvmContext svmContext, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isKurseSchueler, boolean isFromSchuelerSuchenResult) {
        this.kurseModel = kurseModel;
        this.kurseSemesterwahlModel = kurseSemesterwahlModel;
        this.kurseTableModel = kurseTableModel;
        this.svmContext = svmContext;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.selectedRow = selectedRow;
        this.isKurseSchueler = isKurseSchueler;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
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
        if (!isKurseSchueler) {
            kurseTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        onBearbeiten();
                    }
                }
            });
        }
    }

    public void setLblTotal(JLabel lblTotal) {
        this.lblTotal = lblTotal;
        // Total nicht sichtbar für Kurse Schüler
        if (isKurseSchueler) {
            lblTotal.setVisible(false);
            return;
        }
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
            btnNeu.setEnabled(false);
            return;
        }
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isKurseSchueler) {
                    onNeuKurseSchueler();
                } else {
                    onNeuKurseVerwalten();
                }
            }
        });
    }

    private void onNeuKurseVerwalten() {
        if (kurseModel.checkIfSemesterIsInPast(svmContext.getSvmModel(), kurseSemesterwahlModel)) {
            Object[] options = {"Ja", "Nein"};
            int n = JOptionPane.showOptionDialog(
                    null,
                    "Das Schuljahr / Semester liegt in der Vergangenheit. Trotzdem neuen Kurs erfassen?",
                    "Schuljahr / Semester in Vergangenheit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[1]); //default button title
            if (n == 1) {
                return;
            }
        }
        btnNeu.setFocusPainted(true);
        KursErfassenDialog kursErfassenDialog = new KursErfassenDialog(svmContext, kurseModel, kurseSemesterwahlModel, kurseTableModel, 0, false, "Neuer Kurs");
        kursErfassenDialog.pack();
        kursErfassenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
        btnNeu.setFocusPainted(false);
    }

    private void onNeuKurseSchueler() {
        btnNeu.setFocusPainted(true);
        KursSchuelerHinzufuegenDialog kursSchuelerHinzufuegenDialog = new KursSchuelerHinzufuegenDialog(svmContext, kurseTableModel, schuelerDatenblattModel);
        kursSchuelerHinzufuegenDialog.pack();
        kursSchuelerHinzufuegenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (svmContext.getSvmModel().getSemestersAll().size() == 0) {
            btnNeu.setEnabled(false);
        }
    }

    public void setBtnBearbeiten(JButton btnBearbeiten) {
        this.btnBearbeiten = btnBearbeiten;
        // Bearbeiten nicht möglich für Kurse Schüler
        if (isKurseSchueler) {
            btnBearbeiten.setVisible(false);
            return;
        }
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
        KursErfassenDialog kursErfassenDialog = new KursErfassenDialog(svmContext, kurseModel, kurseSemesterwahlModel, kurseTableModel, kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow()), true, "Kurs bearbeiten");
        kursErfassenDialog.pack();
        kursErfassenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
        if (kurseTableModel.getRowCount() > 0) {
            btnExportieren.setEnabled(true);
        } else {
            btnExportieren.setEnabled(false);
        }
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        this.btnLoeschen = btnLoeschen;
        enableBtnLoeschen(false);
        btnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isKurseSchueler) {
                    onLoeschenKurseSchueler();
                } else {
                    onLoeschenKurseVerwalten();
                }
            }
        });
    }

    private void enableBtnLoeschen(boolean enabled) {
        btnLoeschen.setEnabled(enabled);
    }

    private void onLoeschenKurseVerwalten() {
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
            DeleteKursCommand.Result result  = kurseModel.kursLoeschenKurseVerwalten(kurseTableModel, kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow()));
            switch (result) {
                case KURS_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Kurs wird durch mindestens einen Schüler referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    kurseTableModel.fireTableDataChanged();
                    kurseTable.addNotify();
                    break;
            }
        }
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        kurseTable.clearSelection();
        if (kurseTableModel.getRowCount() == 0) {
            btnImportieren.setEnabled(true);
        }
        if (kurseTableModel.getRowCount() > 0) {
            btnExportieren.setEnabled(true);
        } else {
            btnExportieren.setEnabled(false);
        }
    }

    private void onLoeschenKurseSchueler() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag gelöscht werden?",
                "Kurs löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            kurseModel.eintragLoeschenKurseSchueler(kurseTableModel, kurseTableModel.getKursAt(kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow())), schuelerDatenblattModel);
            kurseTableModel.fireTableDataChanged();
            kurseTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        kurseTable.clearSelection();
    }

    public void setBtnImportieren(JButton btnImportieren) {
        this.btnImportieren = btnImportieren;
        // Importieren nur möglich für Kurse bearbeiten
        if (isKurseSchueler) {
            btnImportieren.setVisible(false);
            return;
        }
        if (kurseTableModel.getRowCount() > 0) {
            btnImportieren.setEnabled(false);
            return;
        }
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
            btnImportieren.setEnabled(false);
            if (kurseTableModel.getRowCount() > 0) {
                btnExportieren.setEnabled(true);
            }
        }
    }

    public void setBtnExportieren(JButton btnExportieren) {
        this.btnExportieren = btnExportieren;
        // Exportieren nur möglich für Kurse bearbeiten
        if (isKurseSchueler) {
            btnExportieren.setVisible(false);
            return;
        }
        if (kurseTableModel.getRowCount() == 0) {
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
        ListenExportDialog listenExportDialog = new ListenExportDialog(svmContext, null, null, kurseTableModel, ListenExportTyp.KURSE);
        listenExportDialog.pack();
        listenExportDialog.setVisible(true);
        btnExportieren.setFocusPainted(false);
    }

    private void onListSelection() {
        int selectedRowIndex = kurseTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        // Zurück nicht möglich für Kurse bearbeiten
        if (!isKurseSchueler) {
            btnZurueck.setVisible(false);
            return;
        }
        btnZurueck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, schuelerSuchenResultTable, selectedRow, isFromSchuelerSuchenResult);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        // Abbrechen nur möglich für Kurse bearbeiten
        if (isKurseSchueler) {
            btnAbbrechen.setVisible(false);
            return;
        }
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

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addZurueckZuSchuelerSuchenListener(ActionListener zurueckZuSchuelerSuchenListener) {
        this.zurueckZuSchuelerSuchenListener = zurueckZuSchuelerSuchenListener;
    }

}
