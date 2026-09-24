package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.CreateOrUpdateMitarbeiterModel;
import ch.metzenthin.svm.domain.model.ListenExportModel;
import ch.metzenthin.svm.domain.model.ListenExportModelImpl;
import ch.metzenthin.svm.domain.model.MitarbeiterListModel;
import ch.metzenthin.svm.domain.model.MitarbeiterTableData;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.SwingWorkerWithBusyDialog;
import ch.metzenthin.svm.ui.view.MitarbeiterListView;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("DuplicatedCode")
public class MitarbeiterListController
    extends AbstractListController<
        MitarbeiterListModel, DeleteMitarbeiterResult, MitarbeiterListView> {

  public MitarbeiterListController(
      SvmContext svmContext,
      MitarbeiterListModel mitarbeiterListModel,
      ActionListener closeListener,
      ActionListener zurueckListener) {
    super(
        svmContext,
        mitarbeiterListModel,
        createView(mitarbeiterListModel.getTableModel(), closeListener, zurueckListener));
    configBtnExport();
    setOrUpdateTotalAndListButtons();
  }

  private static MitarbeiterListView createView(
      TableModel<MitarbeiterTableData, Mitarbeiter> tableModel,
      ActionListener closeListener,
      ActionListener zurueckListener) {
    return new MitarbeiterListView(tableModel, closeListener, zurueckListener);
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
    ListenExportModel listenExportModel = new ListenExportModelImpl();
    ListenExportController listenExportController =
        new ListenExportController(
            listenExportModel,
            "Mitarbeiter exportieren",
            new Listentyp[] {
              Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM,
              Listentyp.MITARBEITER_ADRESSLISTE_MIT_GEBURTSDATUM_AHV_IBAN_VERTRETUNGSMOEGLICHKEITEN,
              Listentyp.MITARBEITER_ADRESSLISTE_OHNE_GEBURTSDATUM,
              Listentyp.MITARBEITER_ADRESSETIKETTEN,
              Listentyp.MITARBEITER_LISTE_NAME_EINSPALTIG_CSV,
              Listentyp.MITARBEITER_LISTE_NAME_ZWEISPALTIG_CSV
            });
    listenExportController.showDialog();

    if (listenExportModel.getExportFile() != null) {
      SwingWorkerWithBusyDialog<ExportListResult> swingWorker =
          view.createSwingWorkerWithBusyDialog("Die Datei wird erstellt. Bitte warten ...");
      try {
        ExportListResult exportResult =
            swingWorker.executeAndGetResult(
                () ->
                    model.exportList(
                        listenExportModel.getListentyp(),
                        listenExportModel.getTitel(),
                        listenExportModel.getExportFile()));
        if (exportResult == ExportListResult.LISTE_ERFOLGREICH_ERSTELLT) {
          view.showInfoMessageDialog(
              "Die Liste wurde erfolgreich erstellt.", "Liste erfolgreich erstellt");
        }
      } catch (Exception e) {
        view.showErrorMessageDialog(
            "Die Liste konnte nicht erstellt werden:\n" + e.getCause().getMessage(),
            "Liste nicht erfolgreich erstellt");
      }
    }
  }

  private void setLblTotal() {
    //    String lblTotalText =
    //        "Total: "
    //        + model.getRowCount()
    //        + " Mitarbeiter ("
    //        + model.getAnzSelektiert()
    //        + " selektiert)";
    //    view.lblTotal.setText(lblTotalText);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateMitarbeiterModel createOrUpdateMitarbeiterModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateMitarbeiterController createOrUpdateMitarbeiterController =
        new CreateOrUpdateMitarbeiterController(
            svmContext, createOrUpdateMitarbeiterModel, "Neuer Mitarbeiter");
    createOrUpdateMitarbeiterController.showDialog();
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateMitarbeiterModel createOrUpdateMitarbeiterModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateMitarbeiterController createOrUpdateMitarbeiterController =
        new CreateOrUpdateMitarbeiterController(
            svmContext, createOrUpdateMitarbeiterModel, "Mitarbeiter bearbeiten");
    createOrUpdateMitarbeiterController.showDialog();
  }

  @Override
  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll der Mitarbeiter aus der Datenbank gelöscht werden?", "Mitarbeiter löschen?");
    if (n == 0) {
      // Löschen durchführen
      //      int numberOfReferencedMitarbeiterrechnungen =
      //          model.getNumberOfReferencedMitarbeiterrechnungen(view.convertRowIndexToModel());
      //      boolean existsKurs = model.existsKurs(view.convertRowIndexToModel());
      //      int n1 = 0;
      //      if (!existsKurs && numberOfReferencedMitarbeiterrechnungen > 0) {
      //        n1 =
      //            view.showYesNoDialog(
      //                "ACHTUNG!\n"
      //                    + "Das zu löschende Mitarbeiter wird von "
      //                    + numberOfReferencedMitarbeiterrechnungen
      //                    + " Mitarbeiterrechnungen referenziert. "
      //                    + "Diese werden beim Löschen des Mitarbeiters mitgelöscht!\n"
      //                    + "Soll das Mitarbeiter trotzdem gelöscht werden?",
      //                "Mitarbeiter von Mitarbeiterrechnungen referenziert");
      //      }
      //      if (n1 == 0) {
      //        DeleteMitarbeiterResult deleteMitarbeiterResult =
      //            model.eintragLoeschen(view.convertRowIndexToModel());
      //        switch (deleteMitarbeiterResult) {
      ////          case MITARBEITER_VON_KURS_REFERENZIERT ->
      // showErrorMessageDialog(deleteMitarbeiterResult);
      //          case MITARBEITER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
      //            showErrorMessageDialog(deleteMitarbeiterResult);
      //            reloadTableData();
      //          }
      //          case LOESCHEN_ERFOLGREICH -> reloadTableData();
      //        }
      //      }
    }
  }
}
