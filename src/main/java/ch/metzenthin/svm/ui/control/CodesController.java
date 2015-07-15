package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.DeleteCodeCommand;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.CodesTableData;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.CodeErfassenDialog;
import ch.metzenthin.svm.ui.components.CodeSchuelerHinzufuegenDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;
import ch.metzenthin.svm.ui.components.UiComponentsUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Martin Schraner
 */
public class CodesController {
    private final SvmContext svmContext;
    private final CodesModel codesModel;
    private final boolean isCodesSchueler;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final int selectedRow;
    private CodesTableModel codesTableModel;
    private JTable codesTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public CodesController(CodesModel codesModel, SvmContext svmContext, CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, int selectedRow, boolean isCodesSchueler) {
        this.codesModel = codesModel;
        this.svmContext = svmContext;
        this.isCodesSchueler = isCodesSchueler;
        this.codesTableModel = codesTableModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.selectedRow = selectedRow;
    }

    public void setCodesTable(JTable codesTable) {
        this.codesTable = codesTable;
        initializeCodesTable();
        codesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        codesTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setLblTitel(JLabel lblTitel) {
        if (isCodesSchueler) {
            lblTitel.setText("Codes " + schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
        } else {
            lblTitel.setText("Codes verwalten");
        }
    }

    private void initializeCodesTable() {
        if (!isCodesSchueler) {
            CodesTableData codesTableData = new CodesTableData(svmContext.getSvmModel().getCodesAll());
            codesTableModel = new CodesTableModel(codesTableData);
        }
        codesTable.setModel(codesTableModel);
        UiComponentsUtils.setJTableWidthAsPercentages(codesTable, 0.15, 0.85);
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        // Im Falle von Codes-Schüler Neu-Button deaktivieren, falls keine weitere Codes selektiert werden können
        if (isCodesSchueler && codesModel.getSelectableCodes(svmContext.getSvmModel(), schuelerDatenblattModel).length == 0) {
            btnNeu.setEnabled(false);
        }
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCodesSchueler) {
                    onNeuCodesSchueler();
                } else {
                    onNeuCodesVerwalten();
                }
            }
        });
    }

    private void onNeuCodesVerwalten() {
        btnNeu.setFocusPainted(true);
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesModel, 0, false, "Neuer Code");
        codeErfassenDialog.pack();
        codeErfassenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
    }

    private void onNeuCodesSchueler() {
        btnNeu.setFocusPainted(true);
        CodeSchuelerHinzufuegenDialog codeSchuelerHinzufuegenDialog = new CodeSchuelerHinzufuegenDialog(svmContext, codesModel, schuelerDatenblattModel);
        codeSchuelerHinzufuegenDialog.pack();
        codeSchuelerHinzufuegenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (codesModel.getSelectableCodes(svmContext.getSvmModel(), schuelerDatenblattModel).length == 0) {
            btnNeu.setEnabled(false);
        }
    }

    public void setBtnBearbeiten(JButton btnBearbeiten) {
        this.btnBearbeiten = btnBearbeiten;
        // Bearbeiten nicht möglich für Codes Schüler
        if (isCodesSchueler) {
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
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesModel, codesTable.getSelectedRow(), true, "Code bearbeiten");
        codeErfassenDialog.pack();
        codeErfassenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
    }

    public void setBtnLoeschen(JButton btnLoeschen) {
        this.btnLoeschen = btnLoeschen;
        enableBtnLoeschen(false);
        btnLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCodesSchueler) {
                    onLoeschenCodesSchueler();
                } else {
                    onLoeschenCodesVerwalten();
                }
            }
        });
    }

    private void enableBtnLoeschen(boolean enabled) {
        btnLoeschen.setEnabled(enabled);
    }

    private void onLoeschenCodesVerwalten() {
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
            DeleteCodeCommand.Result result  = codesModel.eintragLoeschenCodesVerwalten(svmContext, codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens einen Schüler referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
    }

    private void onLoeschenCodesSchueler() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag gelöscht werden?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            codesModel.eintragLoeschenCodesSchueler(codesTable.getSelectedRow(), schuelerDatenblattModel);
            codesTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        btnNeu.setEnabled(true);
    }

    private void onListSelection() {
        int selectedRowIndex = codesTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        // Zurück nicht möglich für Codes bearbeiten
        if (!isCodesSchueler) {
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
        SchuelerDatenblattPanel schuelerDatenblattPanel = new SchuelerDatenblattPanel(svmContext, schuelerSuchenTableModel, selectedRow);
        schuelerDatenblattPanel.addCloseListener(closeListener);
        schuelerDatenblattPanel.addNextPanelListener(nextPanelListener);
        schuelerDatenblattPanel.addZurueckZuSchuelerSuchenListener(zurueckZuSchuelerSuchenListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerDatenblattPanel.$$$getRootComponent$$$(), "Datenblatt"}, ActionEvent.ACTION_PERFORMED, "Schüler ausgewählt"));
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
