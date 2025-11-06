package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAsPercentages;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.domain.model.KurstypenModel;
import ch.metzenthin.svm.domain.model.KurstypenTableData;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;
import ch.metzenthin.svm.ui.components.KurstypErfassenDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
public class KurstypenController implements DialogClosedListener {
  private final SvmContext svmContext;
  private final KurstypenModel kurstypenModel;
  private KurstypenTableModel kurstypenTableModel;
  private JTable kurstypenTable;
  private JButton btnNeu;
  private JButton btnBearbeiten;
  private JButton btnLoeschen;
  private JButton btnAbbrechen;
  private ActionListener closeListener;

  public KurstypenController(KurstypenModel kurstypenModel, SvmContext svmContext) {
    this.kurstypenModel = kurstypenModel;
    this.svmContext = svmContext;
  }

  @SuppressWarnings("DuplicatedCode")
  public void setKurstypenTable(JTable kurstypenTable) {
    this.kurstypenTable = kurstypenTable;
    KurstypenTableData kurstypenTableData =
        new KurstypenTableData(svmContext.getSvmModel().getKurstypenAll());
    kurstypenTableModel = new KurstypenTableModel(kurstypenTableData);
    kurstypenTable.setModel(kurstypenTableModel);
    setColumnCellRenderers(kurstypenTable, kurstypenTableModel);
    setJTableColumnWidthAsPercentages(kurstypenTable, 0.75, 0.25);
    kurstypenTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              if (e.getValueIsAdjusting()) {
                return;
              }
              onListSelection();
            });
    kurstypenTable.addMouseListener(
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
    KurstypErfassenDialog kurstypErfassenDialog =
        new KurstypErfassenDialog(
            svmContext, kurstypenTableModel, kurstypenModel, 0, false, "Neuer Kurstyp", this);
    kurstypErfassenDialog.pack();
    kurstypErfassenDialog.setVisible(true);
    kurstypenTableModel.fireTableDataChanged();
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
    KurstypErfassenDialog kurstypErfassenDialog =
        new KurstypErfassenDialog(
            svmContext,
            kurstypenTableModel,
            kurstypenModel,
            kurstypenTable.getSelectedRow(),
            true,
            "Kurstyp bearbeiten",
            this);
    kurstypErfassenDialog.pack();
    kurstypErfassenDialog.setVisible(true);
    kurstypenTableModel.fireTableDataChanged();
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
            "Kurstyp löschen",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options, // the titles of buttons
            options[1]); // default button title
    if (n == 0) {
      DeleteKurstypResult result =
          kurstypenModel.eintragLoeschen(kurstypenTableModel, kurstypenTable.getSelectedRow());
      if (result == DeleteKurstypResult.KURSTYP_VON_KURS_REFERENZIERT) {
        JOptionPane.showMessageDialog(
            null,
            "Der Kurstyp wird durch mindestens einen Kurs referenziert und "
                + "kann nicht gelöscht werden.",
            "Fehler",
            JOptionPane.ERROR_MESSAGE);
        btnLoeschen.setFocusPainted(false);
      } else if (result == DeleteKurstypResult.LOESCHEN_ERFOLGREICH) {
        reloadTableModel();
      }
    }
    btnLoeschen.setFocusPainted(false);
    enableBtnLoeschen(false);
    kurstypenTable.clearSelection();
  }

  private void onListSelection() {
    int selectedRowIndex = kurstypenTable.getSelectedRow();
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

  @Override
  public void onDialogClosed() {
    reloadTableModel();
  }

  private void reloadTableModel() {
    // TableData mit von der Datenbank upgedateten Kurstypen updaten
    kurstypenTableModel
        .getKurstypenTableData()
        .setKurstypen(svmContext.getSvmModel().getKurstypenAll());
    kurstypenTableModel.fireTableDataChanged();
    kurstypenTable.addNotify();
  }
}
