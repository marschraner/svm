package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.DialogClosedListener;
import ch.metzenthin.svm.domain.model.SemestersModel;
import ch.metzenthin.svm.domain.model.SemestersTableData;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;
import ch.metzenthin.svm.ui.components.CreateOrUpdateSemesterDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("DuplicatedCode")
public class SemestersController implements DialogClosedListener {

  public static final String ERROR_TITLE = "Fehler";
  private final SvmContext svmContext;
  private final SemestersModel semestersModel;
  private SemestersTableModel semestersTableModel;
  private JTable semestersTable;
  private JButton btnNeu;
  private JButton btnBearbeiten;
  private JButton btnLoeschen;
  private JButton btnAbbrechen;
  private ActionListener closeListener;

  public SemestersController(SemestersModel semestersModel, SvmContext svmContext) {
    this.semestersModel = semestersModel;
    this.svmContext = svmContext;
  }

  public void setSemestersTable(JTable semestersTable) {
    this.semestersTable = semestersTable;
    initializeSemestersTable();
    semestersTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              if (e.getValueIsAdjusting()) {
                return;
              }
              onListSelection();
            });
    semestersTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent me) {
            if (me.getClickCount() == 2) {
              onBearbeiten();
            }
          }
        });
  }

  private void initializeSemestersTable() {
    SemestersTableData semestersTableData =
        new SemestersTableData(svmContext.getSvmModel().getSemestersAndNumberOfKurseAll());
    semestersTableModel = new SemestersTableModel(semestersTableData);
    semestersTable.setModel(semestersTableModel);
    setColumnCellRenderers(semestersTable, semestersTableModel);
  }

  public void setBtnNeu(JButton btnNeu) {
    this.btnNeu = btnNeu;
    btnNeu.addActionListener(e -> onNeu());
  }

  private void onNeu() {
    btnNeu.setFocusPainted(true);
    CreateOrUpdateSemesterDialog createOrUpdateSemesterDialog =
        new CreateOrUpdateSemesterDialog(
            svmContext, semestersTableModel, semestersModel, 0, false, "Neues Semester", this);
    createOrUpdateSemesterDialog.pack();
    createOrUpdateSemesterDialog.setVisible(true);
    semestersTableModel.fireTableDataChanged();
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
    CreateOrUpdateSemesterDialog createOrUpdateSemesterDialog =
        new CreateOrUpdateSemesterDialog(
            svmContext,
            semestersTableModel,
            semestersModel,
            semestersTable.getSelectedRow(),
            true,
            "Semester bearbeiten",
            this);
    createOrUpdateSemesterDialog.pack();
    createOrUpdateSemesterDialog.setVisible(true);
    semestersTableModel.fireTableDataChanged();
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
            "Soll das Semester aus der Datenbank gelöscht werden?",
            "Semester löschen?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options, // the titles of buttons
            options[1]); // default button title
    if (n == 0) {
      // Löschen durchführen
      int numberOfReferencedSemesterrechnungen =
          semestersModel.getNumberOfReferencedSemesterrechnungen(
              semestersTableModel, semestersTable.getSelectedRow());
      boolean existsKurs =
          semestersModel.existsKurs(semestersTableModel, semestersTable.getSelectedRow());
      int n1 = 0;
      if (!existsKurs && numberOfReferencedSemesterrechnungen > 0) {
        n1 =
            JOptionPane.showOptionDialog(
                null,
                "ACHTUNG!\n"
                    + "Das zu löschende Semester wird von "
                    + numberOfReferencedSemesterrechnungen
                    + " Semesterrechnungen referenziert. "
                    + "Diese werden beim Löschen des Semesters mitgelöscht!\n"
                    + "Soll das Semester trotzdem gelöscht werden?",
                "Semester von Semesterrechnungen referenziert",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options, // the titles of buttons
                options[1]); // default button title
      }
      if (n1 == 0) {
        DeleteSemesterResult deleteSemesterResult =
            semestersModel.semesterLoeschen(semestersTableModel, semestersTable.getSelectedRow());
        switch (deleteSemesterResult) {
          case SEMESTER_VON_KURS_REFERENZIERT -> {
            JOptionPane.showMessageDialog(
                null,
                "Das Semester wird durch mindestens einen Kurs "
                    + "referenziert und kann nicht gelöscht werden.",
                ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
            btnLoeschen.setFocusPainted(false);
          }
          case SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
            JOptionPane.showMessageDialog(
                null,
                "Das Semester kann nicht gelöscht werden, da der Eintrag unterdessen durch\n"
                    + "einen anderen Benutzer verändert oder gelöscht wurde.",
                ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
            reloadTableModel();
          }
          case LOESCHEN_ERFOLGREICH -> reloadTableModel();
        }
      }
    }
    btnLoeschen.setFocusPainted(false);
    enableBtnLoeschen(false);
    semestersTable.clearSelection();
  }

  private void onListSelection() {
    int selectedRowIndex = semestersTable.getSelectedRow();
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
    // TableData mit von der Datenbank upgedateten Semesters updaten
    semestersTableModel
        .getSemestersTableData()
        .setSemestersAndNumberOfKurses(svmContext.getSvmModel().getSemestersAndNumberOfKurseAll());
    semestersTableModel.fireTableDataChanged();
    semestersTable.addNotify();
  }
}
