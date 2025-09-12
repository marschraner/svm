package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setColumnCellRenderers;
import static ch.metzenthin.svm.ui.components.UiComponentsUtils.setJTableColumnWidthAccordingToCellContentAndHeader;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.ListenExportTyp;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.components.KursErfassenDialog;
import ch.metzenthin.svm.ui.components.ListenExportDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * @author Martin Schraner
 */
public class KurseController {
  private final SvmContext svmContext;
  private final KurseSemesterwahlModel kurseSemesterwahlModel;
  private final KurseModel kurseModel;
  private final KurseTableModel kurseTableModel;
  private JTable kurseTable;
  private JLabel lblTotal;
  private JButton btnNeu;
  private JButton btnBearbeiten;
  private JButton btnLoeschen;
  private JButton btnAbbrechen;
  private JButton btnImportieren;
  private JButton btnExportieren;
  private ActionListener closeListener;
  private Exception swingWorkerException;

  public KurseController(
      KurseModel kurseModel,
      SvmContext svmContext,
      KurseSemesterwahlModel kurseSemesterwahlModel,
      KurseTableModel kurseTableModel) {
    this.kurseModel = kurseModel;
    this.kurseSemesterwahlModel = kurseSemesterwahlModel;
    this.kurseTableModel = kurseTableModel;
    this.svmContext = svmContext;
  }

  @SuppressWarnings("DuplicatedCode")
  public void setKurseTable(JTable kurseTable) {
    this.kurseTable = kurseTable;
    kurseTable.setModel(kurseTableModel);
    setColumnCellRenderers(kurseTable, kurseTableModel);
    setJTableColumnWidthAccordingToCellContentAndHeader(kurseTable);
    kurseTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              if (e.getValueIsAdjusting()) {
                return;
              }
              onListSelection();
            });
    kurseTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent me) {
            if (me.getClickCount() == 2) {
              onBearbeiten();
            }
          }
        });
  }

  public void setLblTotal(JLabel lblTotal) {
    this.lblTotal = lblTotal;
    lblTotal.setText(kurseModel.getTotal(kurseTableModel));
  }

  public void setBtnNeu(JButton btnNeu) {
    this.btnNeu = btnNeu;
    if (svmContext.getSvmModel().getSemestersAll().isEmpty()) {
      btnNeu.setEnabled(false);
      return;
    }
    btnNeu.addActionListener(e -> onNeuKurse());
  }

  private void onNeuKurse() {
    if (kurseModel.checkIfSemesterIsInPast(svmContext.getSvmModel(), kurseSemesterwahlModel)) {
      Object[] options = {"Ja", "Nein"};
      int n =
          JOptionPane.showOptionDialog(
              null,
              "Das Schuljahr / Semester liegt in der Vergangenheit. Trotzdem neuen Kurs erfassen?",
              "Schuljahr / Semester in Vergangenheit",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              options, // the titles of buttons
              options[1]); // default button title
      if (n == 1) {
        return;
      }
    }
    btnNeu.setFocusPainted(true);
    KursErfassenDialog kursErfassenDialog =
        new KursErfassenDialog(
            svmContext,
            kurseModel,
            kurseSemesterwahlModel,
            kurseTableModel,
            0,
            false,
            "Neuer Kurs");
    kursErfassenDialog.pack();
    kursErfassenDialog.setVisible(true);
    kurseTableModel.fireTableDataChanged();
    lblTotal.setText(kurseModel.getTotal(kurseTableModel));
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
    KursErfassenDialog kursErfassenDialog =
        new KursErfassenDialog(
            svmContext,
            kurseModel,
            kurseSemesterwahlModel,
            kurseTableModel,
            kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow()),
            true,
            "Kurs bearbeiten");
    kursErfassenDialog.pack();
    kursErfassenDialog.setVisible(true);
    kurseTableModel.fireTableDataChanged();
    btnBearbeiten.setFocusPainted(false);
    btnExportieren.setEnabled(kurseTableModel.getRowCount() > 0);
  }

  public void setBtnLoeschen(JButton btnLoeschen) {
    this.btnLoeschen = btnLoeschen;
    enableBtnLoeschen(false);
    btnLoeschen.addActionListener(e -> onLoeschenKurse());
  }

  private void enableBtnLoeschen(boolean enabled) {
    btnLoeschen.setEnabled(enabled);
  }

  private void onLoeschenKurse() {
    btnLoeschen.setFocusPainted(true);
    int n;
    Object[] options = {"Ja", "Nein"};
    if (kurseModel.checkIfKursHasKursanmeldungen(
        kurseTableModel, kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow()))) {
      n =
          JOptionPane.showOptionDialog(
              null,
              "Der Kurs wird durch mindestens eine Kursanmeldung referenziert. Beim Löschen des Kurses \n"
                  + "werden die Kursanmeldungen ebenfalls unwiderruflich gelöscht. Fortfahren?",
              "Warnung",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              options, // the titles of buttons
              options[1]); // default button title
    } else {
      n =
          JOptionPane.showOptionDialog(
              null,
              "Soll der Eintrag aus der Datenbank gelöscht werden?",
              "Kurs löschen?",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options, // the titles of buttons
              options[1]); // default button title
    }
    if (n == 0) {
      kurseModel.kursLoeschen(
          kurseTableModel, kurseTable.convertRowIndexToModel(kurseTable.getSelectedRow()));
      kurseTableModel.fireTableDataChanged();
      kurseTable.addNotify();
    }
    lblTotal.setText(kurseModel.getTotal(kurseTableModel));
    btnLoeschen.setFocusPainted(false);
    enableBtnLoeschen(false);
    kurseTable.clearSelection();
    btnExportieren.setEnabled(kurseTableModel.getRowCount() > 0);
  }

  public void setBtnImportieren(JButton btnImportieren) {
    this.btnImportieren = btnImportieren;
    btnImportieren.addActionListener(e -> onImportieren());
  }

  private void onImportieren() {
    Object[] options = {"Ja", "Nein"};
    String msg;
    if (kurseSemesterwahlModel.getSemester().getSemesterbezeichnung()
        == Semesterbezeichnung.ERSTES_SEMESTER) {
      msg =
          "Sollen die Kurse vom 1. Semester des vorherigen Schuljahrs (ohne Schüler) importiert werden?";
    } else {
      msg = "Sollen die Kurse vom 1. Semester (inklusive Schüler) importiert werden?";
    }
    msg = msg + "\n(Bereits vorhandene Kurse werden nicht überschrieben.)";
    int n =
        JOptionPane.showOptionDialog(
            null,
            msg,
            "Kurse von früherem Semester importieren?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options, // the titles of buttons
            options[1]); // default button title
    if (n == 0) {
      // Wait-Cursor funktioniert hier nicht (wegen vorhergehendem Dialog)
      final JDialog dialog = new JDialog();
      dialog.setModal(true);
      // Kein Maximierung-Button
      dialog.setResizable(false);
      // Schliessen soll keinen Effekt haben
      dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      dialog.setTitle("Kurse werden importiert");
      final JOptionPane optionPane =
          new JOptionPane(
              "Die Kurse werden importiert. Bitte warten ...",
              JOptionPane.INFORMATION_MESSAGE,
              JOptionPane.DEFAULT_OPTION,
              null,
              new Object[] {},
              null);
      dialog.setContentPane(optionPane);
      // Public method to center the dialog after calling pack()
      dialog.pack();
      dialog.setLocationRelativeTo(null);
      swingWorkerException = null;
      SwingWorker<Void, Void> worker =
          new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
              try {
                kurseModel.importKurseFromPreviousSemester(
                    svmContext.getSvmModel(), kurseSemesterwahlModel, kurseTableModel);
              } catch (Exception e) {
                swingWorkerException = e;
              }
              return null;
            }

            @Override
            protected void done() {
              // Dialog in jedem Fall schliessen
              dialog.dispose();
              // Exception eines Swing-Workers muss in done()-Methode geworfen werden, da sonst der
              // SwingExceptionHandler nicht aufgerufen und kein Fehlerdialog angezeigt wird!
              // (vgl.
              // https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker)
              if (swingWorkerException != null) {
                throw new SvmRuntimeException(
                    "Fehler beim Importieren der Kurse des vorhergehenden Semesters!",
                    swingWorkerException);
              }
              kurseTableModel.fireTableDataChanged();
              lblTotal.setText(kurseModel.getTotal(kurseTableModel));
              if (kurseTableModel.getRowCount() > 0) {
                btnExportieren.setEnabled(true);
              }
            }
          };
      worker.execute();
      dialog.setVisible(true);
    }
    btnImportieren.setFocusPainted(false);
  }

  public void setBtnExportieren(JButton btnExportieren) {
    this.btnExportieren = btnExportieren;
    if (kurseTableModel.getRowCount() == 0) {
      btnExportieren.setEnabled(false);
    }
    btnExportieren.addActionListener(e -> onExportieren());
  }

  private void onExportieren() {
    btnExportieren.setFocusPainted(true);
    ListenExportDialog listenExportDialog =
        new ListenExportDialog(
            svmContext, null, null, kurseTableModel, null, ListenExportTyp.KURSE);
    listenExportDialog.pack();
    listenExportDialog.setVisible(true);
    btnExportieren.setFocusPainted(false);
  }

  private void onListSelection() {
    int selectedRowIndex = kurseTable.getSelectedRow();
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
}
