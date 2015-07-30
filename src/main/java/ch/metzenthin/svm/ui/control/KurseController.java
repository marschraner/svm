package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteKursCommand;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.KursErfassenDialog;
import ch.metzenthin.svm.ui.components.KursSchuelerHinzufuegenDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    private JButton btnImport;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;
    private boolean isNeuerKursErfassbar;

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
        this.isNeuerKursErfassbar = isNeuerKursErfassbar();
    }

    private boolean isNeuerKursErfassbar() {
        if (isKurseSchueler) {
            return true;
        }
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return kurseSemesterwahlModel.getSemester(svmContext.getSvmModel()).getSemesterende().after(today) || kurseSemesterwahlModel.getSemester(svmContext.getSvmModel()).getSemesterende().equals(today);
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
        // Total nicht sichtbar für Kurse Schüler
        if (isKurseSchueler) {
            lblTotal.setVisible(false);
            return;
        }
        lblTotal.setText(kurseModel.getTotal(kurseTableModel));
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        if (!isNeuerKursErfassbar) {
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
        KursSchuelerHinzufuegenDialog kursSchuelerHinzufuegenDialog = new KursSchuelerHinzufuegenDialog(svmContext, kurseTableModel, kurseModel, schuelerDatenblattModel);
        kursSchuelerHinzufuegenDialog.pack();
        kursSchuelerHinzufuegenDialog.setVisible(true);
        kurseTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (kurseModel.getSelectableSemestersKurseSchueler(svmContext.getSvmModel()).length == 0) {
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
            DeleteKursCommand.Result result  = kurseModel.kursLoeschenKurseVerwalten(kurseTableModel, kurseTable.getSelectedRow());
            switch (result) {
                case KURS_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Kurs wird durch mindestens einen Schüler referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
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
        enableBtnLoeschen(false);
        kurseTable.clearSelection();
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
            kurseModel.eintragLoeschenKurseSchueler(kurseTableModel, kurseTableModel.getKursAt(kurseTable.getSelectedRow()), schuelerDatenblattModel);
            kurseTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        kurseTable.clearSelection();
    }

    public void setBtnImport(JButton btnImport) {
        this.btnImport = btnImport;
        if (!isNeuerKursErfassbar) {
            btnImport.setEnabled(false);
            return;
        }
        // Import nur möglich für Kurse bearbeiten
        if (isKurseSchueler) {
            btnImport.setVisible(false);
            return;
        }
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
