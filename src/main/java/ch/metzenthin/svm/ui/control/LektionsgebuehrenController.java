package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenModel;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenTableData;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;
import ch.metzenthin.svm.ui.components.CreateOrUpdateLektionsgebuehrenDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenController {
  private final SvmContext svmContext;
  private final LektionsgebuehrenModel lektionsgebuehrenModel;
  private LektionsgebuehrenTableModel lektionsgebuehrenTableModel;
  private JTable lektionsgebuehrenTable;
  private JButton btnNeu;
  private JButton btnBearbeiten;
  private JButton btnLoeschen;
  private JButton btnAbbrechen;
  private ActionListener closeListener;

  public LektionsgebuehrenController(
      LektionsgebuehrenModel lektionsgebuehrenModel, SvmContext svmContext) {
    this.lektionsgebuehrenModel = lektionsgebuehrenModel;
    this.svmContext = svmContext;
  }

  @SuppressWarnings("DuplicatedCode")
  public void setLektionsgebuehrenTable(JTable lektionsgebuehrenTable) {
    this.lektionsgebuehrenTable = lektionsgebuehrenTable;
    LektionsgebuehrenTableData lektionsgebuehrenTableData =
        new LektionsgebuehrenTableData(svmContext.getSvmModel().getLektionsgebuehrenAllList());
    lektionsgebuehrenTableModel = new LektionsgebuehrenTableModel(lektionsgebuehrenTableData);
    lektionsgebuehrenTable.setModel(lektionsgebuehrenTableModel);
    setColumnCellRenderers(lektionsgebuehrenTable, lektionsgebuehrenTableModel);
    lektionsgebuehrenTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              if (e.getValueIsAdjusting()) {
                return;
              }
              onListSelection();
            });
    lektionsgebuehrenTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent me) {
            if (me.getClickCount() == 2) {
              onBearbeiten();
            }
          }
        });
  }

  public void setBtnNeu(JButton btnNeu) {
    this.btnNeu = btnNeu;
    btnNeu.addActionListener(e -> onNeu());
  }

  private void onNeu() {
    btnNeu.setFocusPainted(true);
    CreateOrUpdateLektionsgebuehrenDialog createOrUpdateLektionsgebuehrenDialog =
        new CreateOrUpdateLektionsgebuehrenDialog(
            svmContext,
            lektionsgebuehrenTableModel,
            lektionsgebuehrenModel,
            0,
            false,
            "Neuer Lektionsgebuehren");
    createOrUpdateLektionsgebuehrenDialog.pack();
    createOrUpdateLektionsgebuehrenDialog.setVisible(true);
    // Dialog wurde geschlossen
    reloadTableModel();
    btnNeu.setFocusPainted(false);
  }

  public void setBtnBearbeiten(JButton btnBearbeiten) {
    this.btnBearbeiten = btnBearbeiten;
    enableBtnBearbeiten(false);
    btnBearbeiten.addActionListener(e -> onBearbeiten());
  }

  private void enableBtnBearbeiten(boolean enabled) {
    btnBearbeiten.setEnabled(enabled);
  }

  private void onBearbeiten() {
    btnBearbeiten.setFocusPainted(true);
    CreateOrUpdateLektionsgebuehrenDialog createOrUpdateLektionsgebuehrenDialog =
        new CreateOrUpdateLektionsgebuehrenDialog(
            svmContext,
            lektionsgebuehrenTableModel,
            lektionsgebuehrenModel,
            lektionsgebuehrenTable.getSelectedRow(),
            true,
            "Lektionsgebuehren bearbeiten");
    createOrUpdateLektionsgebuehrenDialog.pack();
    createOrUpdateLektionsgebuehrenDialog.setVisible(true);
    // Dialog wurde geschlossen
    reloadTableModel();
    btnBearbeiten.setFocusPainted(false);
  }

  public void setBtnLoeschen(JButton btnLoeschen) {
    this.btnLoeschen = btnLoeschen;
    enableBtnLoeschen(false);
    btnLoeschen.addActionListener(e -> onLoeschen());
  }

  private void enableBtnLoeschen(boolean enabled) {
    btnLoeschen.setEnabled(enabled);
  }

  private void onLoeschen() {
    btnLoeschen.setFocusPainted(true);
    DeleteLektionsgebuehrenResult result =
        lektionsgebuehrenModel.eintragLoeschen(
            lektionsgebuehrenTableModel, lektionsgebuehrenTable.getSelectedRow());
    switch (result) {
      case LEKTIONSGEBUEHREN_VON_KURS_REFERENZIERT -> {
        JOptionPane.showMessageDialog(
            null,
            "Die Lektionsgebühren können nicht gelöscht werden,\n"
                + "weil Kurse mit dieser Lektionslänge existieren.",
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
        btnLoeschen.setFocusPainted(false);
      }
      case LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
        reloadTableModel();
        JOptionPane.showMessageDialog(
            null,
            "Die Lektionsgebühren können nicht gelöscht werden, da der Eintrag "
                + "unterdessen durch \n"
                + "einen anderen Benutzer verändert oder gelöscht wurde.",
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
      }
      case LOESCHEN_ERFOLGREICH -> reloadTableModel();
    }
    btnLoeschen.setFocusPainted(false);
    enableBtnLoeschen(false);
    lektionsgebuehrenTable.clearSelection();
  }

  private void onListSelection() {
    int selectedRowIndex = lektionsgebuehrenTable.getSelectedRow();
    enableBtnBearbeiten(selectedRowIndex >= 0);
    enableBtnLoeschen(selectedRowIndex >= 0);
  }

  public void setBtnAbbrechen(JButton btnAbbrechen) {
    this.btnAbbrechen = btnAbbrechen;
    btnAbbrechen.addActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeListener.actionPerformed(
        new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Abbrechen"));
  }

  public void addCloseListener(ActionListener closeListener) {
    this.closeListener = closeListener;
  }

  private void reloadTableModel() {
    // TableData mit von der Datenbank upgedateten Lektionsgebühren updaten
    lektionsgebuehrenTableModel
        .getLektionsgebuehrenTableData()
        .setLektionsgebuehrenList(svmContext.getSvmModel().getLektionsgebuehrenAllList());
    lektionsgebuehrenTableModel.fireTableDataChanged();
    lektionsgebuehrenTable.addNotify();
  }
}
