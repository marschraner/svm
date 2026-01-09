package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAsPercentages;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.KursorteModel;
import ch.metzenthin.svm.domain.model.KursorteTableData;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;
import ch.metzenthin.svm.ui.components.CreateOrUpdateKursortDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
public class KursorteController {
  private final SvmContext svmContext;
  private final KursorteModel kursorteModel;
  private KursorteTableModel kursorteTableModel;
  private JTable kursorteTable;
  private JButton btnNeu;
  private JButton btnBearbeiten;
  private JButton btnLoeschen;
  private JButton btnAbbrechen;
  private ActionListener closeListener;

  public KursorteController(KursorteModel kursorteModel, SvmContext svmContext) {
    this.kursorteModel = kursorteModel;
    this.svmContext = svmContext;
  }

  @SuppressWarnings("DuplicatedCode")
  public void setKursorteTable(JTable kursorteTable) {
    this.kursorteTable = kursorteTable;
    KursorteTableData kursorteTableData =
        new KursorteTableData(svmContext.getSvmModel().getKursorteAll());
    kursorteTableModel = new KursorteTableModel(kursorteTableData);
    kursorteTable.setModel(kursorteTableModel);
    setColumnCellRenderers(kursorteTable, kursorteTableModel);
    setJTableColumnWidthAsPercentages(kursorteTable, 0.75, 0.25);
    kursorteTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              if (e.getValueIsAdjusting()) {
                return;
              }
              onListSelection();
            });
    kursorteTable.addMouseListener(
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
    CreateOrUpdateKursortDialog createOrUpdateKursortDialog =
        new CreateOrUpdateKursortDialog(
            svmContext, kursorteTableModel, kursorteModel, 0, false, "Neuer Kursort");
    createOrUpdateKursortDialog.pack();
    createOrUpdateKursortDialog.setVisible(true);
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
    CreateOrUpdateKursortDialog createOrUpdateKursortDialog =
        new CreateOrUpdateKursortDialog(
            svmContext,
            kursorteTableModel,
            kursorteModel,
            kursorteTable.getSelectedRow(),
            true,
            "Kursort bearbeiten");
    createOrUpdateKursortDialog.pack();
    createOrUpdateKursortDialog.setVisible(true);
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
    Object[] options = {"Ja", "Nein"};
    int n =
        JOptionPane.showOptionDialog(
            null,
            "Soll der Eintrag aus der Datenbank gelöscht werden?",
            "Kursort löschen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options, // the titles of buttons
            options[1]); // default button title
    if (n == 0) {
      DeleteKursortResult result =
          kursorteModel.eintragLoeschen(kursorteTableModel, kursorteTable.getSelectedRow());
      switch (result) {
        case KURSORT_VON_KURS_REFERENZIERT -> {
          JOptionPane.showMessageDialog(
              null,
              "Der Kursort wird durch mindestens einen Kurs referenziert und "
                  + "kann nicht gelöscht werden.",
              "Fehler",
              JOptionPane.ERROR_MESSAGE);
          btnLoeschen.setFocusPainted(false);
        }
        case KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
          JOptionPane.showMessageDialog(
              null,
              "Der Kursort konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
                  + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
              "Fehler",
              JOptionPane.ERROR_MESSAGE);
          reloadTableModel();
        }
        case LOESCHEN_ERFOLGREICH -> reloadTableModel();
      }
    }
    btnLoeschen.setFocusPainted(false);
    enableBtnLoeschen(false);
    kursorteTable.clearSelection();
  }

  private void onListSelection() {
    int selectedRowIndex = kursorteTable.getSelectedRow();
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
    // TableData mit von der Datenbank upgedateten Kursorte updaten
    kursorteTableModel
        .getKursorteTableData()
        .setKursorte(svmContext.getSvmModel().getKursorteAll());
    kursorteTableModel.fireTableDataChanged();
    kursorteTable.addNotify();
  }
}
