package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.KursorteModel;
import ch.metzenthin.svm.domain.model.KursorteTableData;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;
import ch.metzenthin.svm.ui.components.TablePanelView;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

/**
 * @author Martin Schraner
 */
public class KursorteController {
  private final SvmContext svmContext;
  private final KursorteModel kursorteModel;
  private final TablePanelView kursorteView;

  public KursorteController(SvmContext svmContext, ActionListener closeListener) {
    this.svmContext = svmContext;
    this.kursorteModel = svmContext.getModelFactory().createKursorteModel();
    KursorteTableData kursorteTableData = this.kursorteModel.getKursorteTableData();
    KursorteTableModel kursorteTableModel = new KursorteTableModel(kursorteTableData);
    ListSelectionListener listSelectionListener = createListSelectionListener();
    MouseListener mouseListener = createMouseListener();
    this.kursorteView =
        new TablePanelView(kursorteTableModel, listSelectionListener, mouseListener, closeListener);
    configBtnNeu();
    configBtnBearbeiten();
    configBtnLoeschen();
  }

  private MouseListener createMouseListener() {
    return new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent me) {
        if (me.getClickCount() == 2) {
          onBearbeiten();
        }
      }
    };
  }

  private ListSelectionListener createListSelectionListener() {
    return e -> {
      if (e.getValueIsAdjusting()) {
        return;
      }
      onListSelection();
    };
  }

  private void configBtnNeu() {
    kursorteView.addButtonNeuActionListener(e -> onNeu());
  }

  private void onNeu() {
    kursorteView.setButtonNeuFocusPainted(true);
    CreateOrUpdateKursortModel createOrUpdateKursortModel =
        kursorteModel.createOrUpdateKursortModel(svmContext);
    CreateOrUpdateKursortController createOrUpdateKursortController =
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, false, "Neuer Kursort");
    createOrUpdateKursortController.showDialog();
    // Dialog wurde geschlossen
    reloadTableData();
    kursorteView.setButtonNeuFocusPainted(false);
  }

  private void configBtnBearbeiten() {
    kursorteView.setButtonBearbeitenEnabled(false);
    kursorteView.addButtonBearbeitenActionListener(e -> onBearbeiten());
  }

  private void onBearbeiten() {
    kursorteView.setButtonBearbeitenFocusPainted(true);
    CreateOrUpdateKursortModel createOrUpdateKursortModel =
        kursorteModel.createOrUpdateKursortModel(svmContext, kursorteView.getSelectedRow());
    CreateOrUpdateKursortController createOrUpdateKursortController =
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, true, "Kursort bearbeiten");
    createOrUpdateKursortController.showDialog();
    // Dialog wurde geschlossen
    reloadTableData();
    kursorteView.setButtonBearbeitenFocusPainted(false);
  }

  private void configBtnLoeschen() {
    kursorteView.setButtonLoeschenEnabled(false);
    kursorteView.addButtonLoeschenActionListener(e -> onLoeschen());
  }

  private void onLoeschen() {
    kursorteView.setButtonLoeschenFocusPainted(true);
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
      DeleteKursortResult result = kursorteModel.eintragLoeschen(kursorteView.getSelectedRow());
      switch (result) {
        case KURSORT_VON_KURS_REFERENZIERT -> {
          JOptionPane.showMessageDialog(
              null,
              "Der Kursort wird durch mindestens einen Kurs referenziert und "
                  + "kann nicht gelöscht werden.",
              "Fehler",
              JOptionPane.ERROR_MESSAGE);
          kursorteView.setButtonLoeschenFocusPainted(false);
        }
        case KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
          JOptionPane.showMessageDialog(
              null,
              "Der Kursort konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
                  + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
              "Fehler",
              JOptionPane.ERROR_MESSAGE);
          reloadTableData();
        }
        case LOESCHEN_ERFOLGREICH -> reloadTableData();
      }
    }
    kursorteView.setButtonLoeschenFocusPainted(false);
    kursorteView.setButtonLoeschenEnabled(false);
    kursorteView.clearSelection();
  }

  private void onListSelection() {
    int selectedRowIndex = kursorteView.getSelectedRow();
    kursorteView.setButtonBearbeitenEnabled(selectedRowIndex >= 0);
    kursorteView.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  private void reloadTableData() {
    // TableData mit von der Datenbank upgedateten Kursorte updaten
    kursorteModel.reloadData();
    kursorteView.fireTableDataChanged();
  }

  public JComponent getPanelRootComponent() {
    return kursorteView.getRootComponent();
  }
}
