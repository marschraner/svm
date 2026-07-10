package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursModel;
import ch.metzenthin.svm.domain.model.KursAndLehrkraefteAndNumberOfKursanmeldungen;
import ch.metzenthin.svm.domain.model.KursListModel;
import ch.metzenthin.svm.domain.model.KursTableData;
import ch.metzenthin.svm.service.result.DeleteKursResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.KursListPanel;
import ch.metzenthin.svm.ui.view.KursListView;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class KursListController
    extends AbstractListController<KursListModel, DeleteKursResult, KursListView> {

  public KursListController(
      SvmContext svmContext, KursListModel kursListModel, ActionListener closeListener) {
    super(
        svmContext,
        kursListModel,
        createView(
            "Kurse " + kursListModel.getSemesterDisplayName(),
            kursListModel.getTableModel(),
            closeListener));
    configBtnImport();
    configBtnExport();
    setOrUpdateTotalAndListButtons();
  }

  private static KursListView createView(
      String titel,
      TableModel<KursTableData, KursAndLehrkraefteAndNumberOfKursanmeldungen> tableModel,
      ActionListener closeListener) {
    // KursListPanel bereits hier erstellen, weil im Konstruktor der View Attribute benötigt werden.
    return new KursListView(new KursListPanel(titel), tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    if (model.checkIfSemesterIsInPast()) {
      int n =
          view.showYesNoDialog(
              "Das Schuljahr / Semester liegt in der Vergangenheit. Trotzdem neuen Kurs erfassen?",
              "Schuljahr / Semester in Vergangenheit");
      if (n == 1) {
        return;
      }
    }
    CreateOrUpdateKursModel createOrUpdateKursModel = model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateKursController createOrUpdateKursController =
        new CreateOrUpdateKursController(createOrUpdateKursModel, "Neuer Kurs");
    createOrUpdateKursController.showDialog();
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateKursModel createOrUpdateKursModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateKursController createOrUpdateKursController =
        new CreateOrUpdateKursController(createOrUpdateKursModel, "Kurs bearbeiten");
    createOrUpdateKursController.showDialog();
  }

  @Override
  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog("Soll der Kurs aus der Datenbank gelöscht werden?", "Kurs löschen?");
    if (n == 0) {
      // Löschen durchführen
      int numberOfReferencedKursanmeldungen =
          model.getNumberOfReferencedKursanmeldungen(view.convertRowIndexToModel());
      int n1 = 0;
      if (numberOfReferencedKursanmeldungen > 0) {
        n1 =
            view.showYesNoDialog(
                "Der Kurs wird durch mindestens eine Kursanmeldung referenziert. Beim Löschen des Kurses \n"
                    + "werden die Kursanmeldungen ebenfalls unwiderruflich gelöscht. Fortfahren?",
                "Warnung");
      }
      if (n1 == 0) {
        DeleteKursResult deleteKursResult = model.eintragLoeschen(view.convertRowIndexToModel());
        switch (deleteKursResult) {
          case KURS_VON_KURSANMELDUNGEN_REFERENZIERT -> showErrorMessageDialog(deleteKursResult);
          case KURS_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
            showErrorMessageDialog(deleteKursResult);
            reloadTableData();
          }
          case LOESCHEN_ERFOLGREICH -> reloadTableData();
        }
      }
    }
  }

  private void showErrorMessageDialog(DeleteKursResult deleteKursResult) {
    view.showErrorMessageDialog(deleteKursResult.getMessage(), "Fehler");
  }

  private void configBtnImport() {
    view.addButtonImportierenActionListener(e -> onImport());
  }

  private void onImport() {
    view.setButtonImportierenFocusPainted(true);
    showOnImportDialog();
    // Dialog wurde geschlossen
    reloadTableData();
    view.setButtonImportierenFocusPainted(false);
  }

  private void showOnImportDialog() {
    // TODO Importieren
  }

  private void configBtnExport() {
    view.addButtonExportierenActionListener(e -> onExport());
  }

  private void onExport() {
    view.setButtonExportierenFocusPainted(true);
    showOnExportDialog();
    // Dialog wurde geschlossen
    view.setButtonExportierenFocusPainted(false);
  }

  private void showOnExportDialog() {
    // TODO Exportieren
  }

  @Override
  protected void setOrUpdateTotalAndListButtons() {
    view.setTotalText(model.getTableModel().getTotalText());
    view.setButtonExportierenEnabled(model.getTableModel().getRowCount() > 0);
  }
}
