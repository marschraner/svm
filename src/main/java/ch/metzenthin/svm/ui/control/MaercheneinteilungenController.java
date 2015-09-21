package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.MaercheneinteilungenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.MaercheneinteilungErfassenDialog;
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
public class MaercheneinteilungenController {
    private final SvmContext svmContext;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final SchuelerSuchenTableModel schuelerSuchenTableModel;
    private final JTable schuelerSuchenResultTable;
    private final int selectedRow;
    private final boolean isFromSchuelerSuchenResult;
    private final MaercheneinteilungenModel maercheneinteilungenModel;
    private MaercheneinteilungenTableModel maercheneinteilungenTableModel;
    private JTable maercheneinteilungenTable;
    private JButton btnNeu;
    private JButton btnBearbeiten;
    private JButton btnLoeschen;
    private ActionListener nextPanelListener;
    private ActionListener closeListener;
    private ActionListener zurueckZuSchuelerSuchenListener;

    public MaercheneinteilungenController(MaercheneinteilungenModel maercheneinteilungenModel, SvmContext svmContext, MaercheneinteilungenTableModel maercheneinteilungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, SchuelerSuchenTableModel schuelerSuchenTableModel, JTable schuelerSuchenResultTable, int selectedRow, boolean isFromSchuelerSuchenResult) {
        this.maercheneinteilungenModel = maercheneinteilungenModel;
        this.maercheneinteilungenTableModel = maercheneinteilungenTableModel;
        this.svmContext = svmContext;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.schuelerSuchenTableModel = schuelerSuchenTableModel;
        this.schuelerSuchenResultTable = schuelerSuchenResultTable;
        this.selectedRow = selectedRow;
        this.isFromSchuelerSuchenResult = isFromSchuelerSuchenResult;
    }

    public void setMaercheneinteilungenTable(JTable maercheneinteilungenTable) {
        this.maercheneinteilungenTable = maercheneinteilungenTable;
        maercheneinteilungenTable.setModel(maercheneinteilungenTableModel);
        setJTableColumnWidthAccordingToCellContentAndHeader(maercheneinteilungenTable);
        maercheneinteilungenTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                onListSelection();
            }
        });
        maercheneinteilungenTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    onBearbeiten();
                }
            }
        });
    }

    public void setBtnNeu(JButton btnNeu) {
        this.btnNeu = btnNeu;
        // Neu-Button deaktivieren, falls alle Märchen schon erfasst sind
        if (maercheneinteilungenModel.getSelectableMaerchens(svmContext.getSvmModel(), schuelerDatenblattModel).size() == 0) {
            btnNeu.setEnabled(false);
        }
        btnNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNeu();
            }
        });
    }

    private void onNeu() {
        btnNeu.setFocusPainted(true);
        MaercheneinteilungErfassenDialog maercheneinteilungErfassenDialog = new MaercheneinteilungErfassenDialog(svmContext, maercheneinteilungenModel, maercheneinteilungenTableModel, schuelerDatenblattModel, maercheneinteilungenTable.getSelectedRow(), false, "Märcheneinteilung erfassen");
        maercheneinteilungErfassenDialog.pack();
        maercheneinteilungErfassenDialog.setVisible(true);
        maercheneinteilungenTableModel.fireTableDataChanged();
        btnNeu.setFocusPainted(false);
        if (maercheneinteilungenModel.getSelectableMaerchens(svmContext.getSvmModel(), schuelerDatenblattModel).size() == 0) {
            btnNeu.setEnabled(false);
        }
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
        MaercheneinteilungErfassenDialog maercheneinteilungErfassenDialog = new MaercheneinteilungErfassenDialog(svmContext, maercheneinteilungenModel, maercheneinteilungenTableModel, schuelerDatenblattModel, maercheneinteilungenTable.getSelectedRow(), true, "Märcheneinteilung bearbeiten");
        maercheneinteilungErfassenDialog.pack();
        maercheneinteilungErfassenDialog.setVisible(true);
        maercheneinteilungenTableModel.fireTableDataChanged();
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
                options[1]); //default button title
        if (n == 0) {
            maercheneinteilungenModel.maercheneinteilungLoeschen(maercheneinteilungenTableModel, schuelerDatenblattModel, maercheneinteilungenTable.getSelectedRow());
            maercheneinteilungenTableModel.fireTableDataChanged();
            maercheneinteilungenTable.addNotify();
        }
        btnLoeschen.setFocusPainted(false);
        enableBtnLoeschen(false);
        maercheneinteilungenTable.clearSelection();
        // Neu-Button aktivieren, falls es selektierbare Märchen gibt
        if (maercheneinteilungenModel.getSelectableMaerchens(svmContext.getSvmModel(), schuelerDatenblattModel).size() > 0) {
            btnNeu.setEnabled(true);
        }
    }

    private void onListSelection() {
        int selectedRowIndex = maercheneinteilungenTable.getSelectedRow();
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
