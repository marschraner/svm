package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.KursanmeldungenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.KursanmeldungErfassenDialog;
import ch.metzenthin.svm.ui.components.SchuelerDatenblattPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

/**
 * @author Martin Schraner
 */
public class KursanmeldungenController {
    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private final int selectedRow;
    private final boolean isFromSchuelerSuchenResult;
    private final KursanmeldungenModel kursanmeldungenModel;
    private KursanmeldungenTableModel kursanmeldungenTableModel;
    private JTable kursanmeldungenTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public KursanmeldungenController(KursanmeldungenModel kursanmeldungenModel, SvmContext svmContext, KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isFromSchuelerSuchenResult) {
        this.kursanmeldungenModel = kursanmeldungenModel;
        this.kursanmeldungenTableModel = kursanmeldungenTableModel;
        this.svmContext = svmContext;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
    }

    public void setKursanmeldungenTable(JTable kursanmeldungenTable) {
        this.kursanmeldungenTable = kursanmeldungenTable;
        kursanmeldungenTable.setModel(kursanmeldungenTableModel);
        setColumnCellRenderers(kursanmeldungenTable, kursanmeldungenTableModel);
        setJTableColumnWidthAccordingToCellContentAndHeader(kursanmeldungenTable);
        kursanmeldungenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        kursanmeldungenTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
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
        KursanmeldungErfassenDialog kursanmeldungErfassenDialog = new KursanmeldungErfassenDialog(svmContext, kursanmeldungenModel, kursanmeldungenTableModel, schuelerDatenblattModel, kursanmeldungenTable.getSelectedRow(), false, "Kursanmeldung erfassen");
        kursanmeldungErfassenDialog.pack();
        kursanmeldungErfassenDialog.setVisible(true);
        kursanmeldungenTableModel.fireTableDataChanged();
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
        KursanmeldungErfassenDialog kursanmeldungErfassenDialog = new KursanmeldungErfassenDialog(svmContext, kursanmeldungenModel, kursanmeldungenTableModel, schuelerDatenblattModel, kursanmeldungenTable.getSelectedRow(), true, "Kursanmeldung bearbeiten");
        kursanmeldungErfassenDialog.pack();
        kursanmeldungErfassenDialog.setVisible(true);
        kursanmeldungenTableModel.fireTableDataChanged();
        btnBearbeiten.setFocusPainted(false);
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
                "Soll der Eintrag aus der Datenbank gelöscht werden?",
                "Eintrag löschen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                svmContext.getDialogIcons().getQuestionIcon(),
                options,  //the titles of buttons
                options[0]); //default button title
        if (n == 0) {
            kursanmeldungenModel.kursanmeldungLoeschen(kursanmeldungenTableModel, schuelerDatenblattModel, kursanmeldungenTable.getSelectedRow());
            kursanmeldungenTableModel.fireTableDataChanged();
            kursanmeldungenTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        kursanmeldungenTable.clearSelection();
    }

    private void onListSelection() {
        int selectedRowIndex = kursanmeldungenTable.getSelectedRow();
        enableBtnBearbeiten(selectedRowIndex >= 0);
        enableBtnLoeschen(selectedRowIndex >= 0);
    }

    public void setBtnZurueck(JButton btnZurueck) {
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
