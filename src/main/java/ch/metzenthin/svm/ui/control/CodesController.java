package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.commands.DeleteElternmithilfeCodeCommand;
import ch.metzenthin.svm.domain.commands.DeleteMitarbeiterCodeCommand;
import ch.metzenthin.svm.domain.commands.DeleteSchuelerCodeCommand;
import ch.metzenthin.svm.domain.commands.DeleteSemesterrechnungCodeCommand;
import ch.metzenthin.svm.domain.model.CodesModel;
import ch.metzenthin.svm.domain.model.CodesTableData;
import ch.metzenthin.svm.domain.model.MitarbeiterErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.StringTableCellRenderer;
import ch.metzenthin.svm.ui.components.CodeErfassenDialog;
import ch.metzenthin.svm.ui.components.CodeSpecificHinzufuegenDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;
import ch.metzenthin.svm.ui.components.UiComponentsUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

/**
 * @author Martin Schraner
 */
public class CodesController {
    private final SvmContext svmContext;
    private final CodesModel codesModel;
    private final JTable schuelerSuchenResultTable;
    private MitarbeiterErfassenModel mitarbeiterErfassenModel;
    private final boolean isCodesSpecificSchueler;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final int selectedRow;
    private final boolean isFromSchuelerSuchenResult;
    private boolean isCodesSpecificMitarbeiter;
    private boolean isFromCodesDialog;
    private Codetyp codetyp;
    private CodesTableModel codesTableModel;
    private JTable codesTable;
    private JDialog codesDialog;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private JButton btnAbbrechen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public CodesController(CodesModel codesModel, SvmContext svmContext, CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, MitarbeiterErfassenModel mitarbeiterErfassenModel, int selectedRow, boolean isCodesSpecificSchueler, boolean isFromSchuelerSuchenResult, boolean isCodesSpecificMitarbeiter, boolean isFromCodesDialog, Codetyp codetyp) {
        this.codesModel = codesModel;
        this.svmContext = svmContext;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.mitarbeiterErfassenModel = mitarbeiterErfassenModel;
        this.isCodesSpecificSchueler = isCodesSpecificSchueler;
        this.codesTableModel = codesTableModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
        this.isCodesSpecificMitarbeiter = isCodesSpecificMitarbeiter;
        this.isFromCodesDialog = isFromCodesDialog;
        this.codetyp = codetyp;
    }

    public void setCodesDialog(JDialog codesDialog) {
        // call onCancel() when cross is clicked
        this.codesDialog = codesDialog;
        codesDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        codesDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setCodesTable(JTable codesTable) {
        this.codesTable = codesTable;
        initializeCodesTable();
        codesTable.getColumnModel().getColumn(0).setCellRenderer(new StringTableCellRenderer());
        codesTable.getColumnModel().getColumn(1).setCellRenderer(new StringTableCellRenderer());
        if (!isCodesSpecificSchueler && !isCodesSpecificMitarbeiter) {
            codesTable.getColumnModel().getColumn(2).setCellRenderer(new StringTableCellRenderer());
        }
        codesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        if (!isCodesSpecificSchueler && !isCodesSpecificMitarbeiter) {
            // Doppelklick zum Bearbeiten darf nicht funktionieren für Schüler-Codes
            codesTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        onBearbeiten();
                    }
                }
            });
        }
    }

    public void setLblTitel(JLabel lblTitel) {
        switch (codetyp) {
            case SCHUELER:
                if (isCodesSpecificSchueler) {
                    lblTitel.setText("Schüler-Codes " + schuelerDatenblattModel.getSchuelerVorname() + " " + schuelerDatenblattModel.getSchuelerNachname());
                } else {
                    lblTitel.setText("Schüler-Codes verwalten");
                }
                break;
            case MITARBEITER:
                if (!isCodesSpecificMitarbeiter) {
                    lblTitel.setText("Mitarbeiter-Codes verwalten");
                }
                break;
            case ELTERNMITHILFE:
                lblTitel.setText("Eltern-Mithilfe-Codes verwalten");
                break;
            case SEMESTERRECHNUNG:
                lblTitel.setText("Semesterrechnung-Codes verwalten");
                break;
        }
    }

    private void initializeCodesTable() {
        if (!isCodesSpecificSchueler && !isCodesSpecificMitarbeiter) {
            switch (codetyp) {
                case SCHUELER:
                    CodesTableData schuelerCodesTableData = new CodesTableData(svmContext.getSvmModel().getSchuelerCodesAll(), false);
                    codesTableModel = new CodesTableModel(schuelerCodesTableData);
                    break;
                case MITARBEITER:
                    CodesTableData mitarbeiterCodesTableData = new CodesTableData(svmContext.getSvmModel().getMitarbeiterCodesAll(), false);
                    codesTableModel = new CodesTableModel(mitarbeiterCodesTableData);
                    break;
                case ELTERNMITHILFE:
                    CodesTableData elternmithilfeCodesTableData = new CodesTableData(svmContext.getSvmModel().getElternmithilfeCodesAll(), false);
                    codesTableModel = new CodesTableModel(elternmithilfeCodesTableData);
                    break;
                case SEMESTERRECHNUNG:
                    CodesTableData semesterrechnungCodesTableData = new CodesTableData(svmContext.getSvmModel().getSemesterrechnungCodesAll(), false);
                    codesTableModel = new CodesTableModel(semesterrechnungCodesTableData);
                    break;
            }
        }
        codesTable.setModel(codesTableModel);
        if (isCodesSpecificSchueler || isCodesSpecificMitarbeiter) {
            UiComponentsUtils.setJTableColumnWidthAsPercentages(codesTable, 0.2, 0.8);
        } else {
            UiComponentsUtils.setJTableColumnWidthAsPercentages(codesTable, 0.15, 0.65, 0.2);
        }
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        // Im Falle von Schüler-Codes eines bestimmten Schülers Neu-Button deaktivieren, falls keine weitere Codes selektiert werden können
        if ((codetyp == Codetyp.SCHUELER && isCodesSpecificSchueler && codesModel.getSelectableSchuelerCodes(svmContext.getSvmModel(), schuelerDatenblattModel).length == 0) ||
                (codetyp == Codetyp.MITARBEITER && isCodesSpecificMitarbeiter && codesModel.getSelectableMitarbeiterCodes(svmContext.getSvmModel(), mitarbeiterErfassenModel).length == 0)) {
            btnNeu.setEnabled(false);
        }
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (codetyp) {
                    case SCHUELER:
                        if (isCodesSpecificSchueler) {
                            onNeuSchuelerCodesSpecificSchueler();
                        } else {
                            onNeuCodesVerwalten();
                        }
                        break;
                    case MITARBEITER:
                        if (isCodesSpecificMitarbeiter) {
                            onNeuMitarbeiterCodesSpecificMitarbeiter();
                        } else {
                            onNeuCodesVerwalten();
                        }
                        break;
                    case ELTERNMITHILFE:
                        onNeuCodesVerwalten();
                        break;
                    case SEMESTERRECHNUNG:
                        onNeuCodesVerwalten();
                        break;
                }
            }
        });
    }

    private void onNeuCodesVerwalten() {
        btnNeu.setFocusPainted(true);
        String titel = "";
        switch (codetyp) {
            case SCHUELER:
                titel = "Neuer Schüler-Code";
                break;
            case MITARBEITER:
                titel = "Neuer Mitarbeiter-Code";
                break;
            case ELTERNMITHILFE:
                titel = "Neuer Eltern-Mithilfe-Code";
                break;
            case SEMESTERRECHNUNG:
                titel = "Neuer Semesterrechnung-Code";
                break;
        }
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesTableModel, codesModel, 0, false, titel, codetyp);
        codeErfassenDialog.pack();
        codeErfassenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
    }

    private void onNeuSchuelerCodesSpecificSchueler() {
        btnNeu.setFocusPainted(true);
        CodeSpecificHinzufuegenDialog codeSpecificHinzufuegenDialog = new CodeSpecificHinzufuegenDialog(svmContext, codesTableModel, codesModel, schuelerDatenblattModel, null, Codetyp.SCHUELER);
        codeSpecificHinzufuegenDialog.pack();
        codeSpecificHinzufuegenDialog.setVisible(true);
        codesTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (codesModel.getSelectableSchuelerCodes(svmContext.getSvmModel(), schuelerDatenblattModel).length == 0) {
            btnNeu.setEnabled(false);
        }
    }

    private void onNeuMitarbeiterCodesSpecificMitarbeiter() {
        btnNeu.setFocusPainted(true);
        CodeSpecificHinzufuegenDialog codeSpecificHinzufuegenDialog = new CodeSpecificHinzufuegenDialog(svmContext, codesTableModel, codesModel, null, mitarbeiterErfassenModel, Codetyp.MITARBEITER);
        codeSpecificHinzufuegenDialog.pack();
        codeSpecificHinzufuegenDialog.setVisible(true);
        codesTable.addNotify();
        codesTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (codesModel.getSelectableMitarbeiterCodes(svmContext.getSvmModel(), mitarbeiterErfassenModel).length == 0) {
            btnNeu.setEnabled(false);
        }
    }

    public void setBtnBearbeiten(JButton btnBearbeiten) {
        this.btnBearbeiten = btnBearbeiten;
        // Bearbeiten nicht möglich für Codes Schüler und Codes Mitarbeiter
        if (isCodesSpecificSchueler || isCodesSpecificMitarbeiter) {
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
        if (btnBearbeiten != null) {
            btnBearbeiten.setEnabled(enabled);
        }
    }

    private void onBearbeiten() {
        btnBearbeiten.setFocusPainted(true);
        String titel = "";
        switch (codetyp) {
            case SCHUELER:
                titel = "Schüler-Code bearbeiten";
                break;
            case MITARBEITER:
                titel = "Mitarbeiter-Code bearbeiten";
                break;
            case ELTERNMITHILFE:
                titel = "Eltern-Mithilfe-Code bearbeiten";
                break;
            case SEMESTERRECHNUNG:
                titel = "Semesterrechnung-Code bearbeiten";
                break;
        }
        CodeErfassenDialog codeErfassenDialog = new CodeErfassenDialog(svmContext, codesTableModel, codesModel, codesTable.getSelectedRow(), true, titel, codetyp);
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
                switch (codetyp) {
                    case SCHUELER:
                        if (isCodesSpecificSchueler) {
                            onLoeschenSchuelerCodesSpecificSchueler();
                        } else {
                            onLoeschenSchuelerCodesVerwalten();
                        }
                        break;
                    case MITARBEITER:
                        if (isCodesSpecificMitarbeiter) {
                            onLoeschenMitarbeiterCodesSpecificMitarbeiter();
                        } else {
                            onLoeschenMitarbeiterCodesVerwalten();
                        }
                        break;
                    case ELTERNMITHILFE:
                        onLoeschenElternmithilfeCodesVerwalten();
                        break;
                    case SEMESTERRECHNUNG:
                        onLoeschenSemesterrechnungCodesVerwalten();
                        break;
                }
            }
        });
    }

    private void enableBtnLoeschen(boolean enabled) {
        btnLoeschen.setEnabled(enabled);
    }

    private void onLoeschenSchuelerCodesVerwalten() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Schueler-Code löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteSchuelerCodeCommand.Result result  = codesModel.eintragLoeschenSchuelerCodesVerwalten(svmContext, codesTableModel, codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_SCHUELER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens einen Schüler referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTableModel.fireTableDataChanged();
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
    }

    private void onLoeschenSchuelerCodesSpecificSchueler() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag gelöscht werden?",
                "Schueler-Code löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            codesModel.eintragLoeschenSchuelerCodesSchueler(codesTableModel, (SchuelerCode) codesTableModel.getCodeAt(codesTable.getSelectedRow()), schuelerDatenblattModel);
            codesTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
        btnNeu.setEnabled(true);
    }

    private void onLoeschenMitarbeiterCodesVerwalten() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Mitarbeiter-Code löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteMitarbeiterCodeCommand.Result result  = codesModel.eintragLoeschenMitarbeiterCodesVerwalten(svmContext, codesTableModel, codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_MITARBEITER_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens einen Mitarbeiter referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTableModel.fireTableDataChanged();
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
    }

    private void onLoeschenMitarbeiterCodesSpecificMitarbeiter() {
        btnLoeschen.setFocusPainted(true);
        codesModel.eintragLoeschenMitarbeiterCodesMitarbeiter(codesTableModel, (MitarbeiterCode) codesTableModel.getCodeAt(codesTable.getSelectedRow()), mitarbeiterErfassenModel);
        codesTable.addNotify();
        codesTableModel.fireTableDataChanged();
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
        btnNeu.setEnabled(true);
    }

    private void onLoeschenElternmithilfeCodesVerwalten() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Märchen-Code löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteElternmithilfeCodeCommand.Result result  = codesModel.eintragLoeschenElternmithilfeCodesVerwalten(svmContext, codesTableModel, codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_MAERCHENEINTEILUNGEN_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens eine Märcheneinteilung referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTableModel.fireTableDataChanged();
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
    }

    private void onLoeschenSemesterrechnungCodesVerwalten() {
        btnLoeschen.setFocusPainted(true);
        Object[] options = {"Ja", "Nein"};
        int n = JOptionPane.showOptionDialog(
                null,
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Semesterrechnung-Code löschen",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[1]); //default button title
        if (n == 0) {
            DeleteSemesterrechnungCodeCommand.Result result  = codesModel.eintragLoeschenSemesterrechnungCodesVerwalten(svmContext, codesTableModel, codesTable.getSelectedRow());
            switch (result) {
                case CODE_VON_SEMESTERRECHNUNGEN_REFERENZIERT:
                    JOptionPane.showMessageDialog(null, "Der Code wird durch mindestens eine Semesterrechnung referenziert und kann nicht gelöscht werden.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
                    btnLoeschen.setFocusPainted(false);
                    break;
                case LOESCHEN_ERFOLGREICH:
                    codesTableModel.fireTableDataChanged();
                    codesTable.addNotify();
                    break;
            }
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        codesTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = codesTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        // Zurück nur möglich für Schüler-Codes eines bestimmten Schülers
        if (codetyp != Codetyp.SCHUELER || !isCodesSpecificSchueler) {
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
        // Abbrechen nicht möglich für Schüler-Codes eines bestimmten Schülers
        if (isCodesSpecificSchueler) {
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
        if (isFromCodesDialog) {
            codesDialog.dispose();
        } else {
            closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Abbrechen"));
        }
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
