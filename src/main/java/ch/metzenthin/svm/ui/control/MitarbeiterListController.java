package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.CreateOrUpdateMitarbeiterModel;
import ch.metzenthin.svm.domain.model.ListenExportModel;
import ch.metzenthin.svm.domain.model.ListenExportModelImpl;
import ch.metzenthin.svm.domain.model.MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert;
import ch.metzenthin.svm.domain.model.MitarbeiterListModel;
import ch.metzenthin.svm.domain.model.MitarbeiterTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.service.result.CallEmailClientResult;
import ch.metzenthin.svm.service.result.CheckEmailResult;
import ch.metzenthin.svm.service.result.DeleteMitarbeiterResult;
import ch.metzenthin.svm.service.result.ExportListResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.components.SwingWorkerWithBusyDialog;
import ch.metzenthin.svm.ui.view.MitarbeiterListView;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.Set;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("DuplicatedCode")
public class MitarbeiterListController
    extends AbstractListController<
        MitarbeiterListModel,
        MitarbeiterListView,
        MitarbeiterTableData,
        MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert,
        DeleteMitarbeiterResult> {

  private static final String EMAIL = "E-Mail";
  private static final String GRUPPEN_EMAIL = "Gruppen-E-Mail";

  public MitarbeiterListController(
      SvmContext svmContext,
      MitarbeiterListModel mitarbeiterListModel,
      ActionListener closeListener,
      ActionListener zurueckListener) {
    super(
        svmContext,
        mitarbeiterListModel,
        createView(mitarbeiterListModel.getTableModel(), closeListener, zurueckListener));
    configTable();
    configBtnAlleSelektieren();
    configBtnAlleDeselektieren();
    configBtnExportieren();
    configBtnEmail();
    updateButtonsAndTotalText();
  }

  private static MitarbeiterListView createView(
      TableModel<MitarbeiterTableData, MitarbeiterAndMitarbeiterCodesAsStringAndSelektiert>
          tableModel,
      ActionListener closeListener,
      ActionListener zurueckListener) {
    return new MitarbeiterListView(tableModel, closeListener, zurueckListener);
  }

  private void configTable() {
    view.addTableModelListener(e -> updateButtonsAndTotalText());
  }

  private void configBtnAlleDeselektieren() {
    view.setButtonAlleDeselektierenVisible(true);
    view.addButtonAlleDeselektierenActionListener(e -> onAlleDeselektieren());
  }

  private void onAlleDeselektieren() {
    model.getTableModel().alleMitarbeiterDeselektieren();
    view.setButtonAlleDeselektierenVisible(false);
    view.setButtonAlleSelektierenVisible(true);
  }

  private void configBtnAlleSelektieren() {
    view.setButtonAlleSelektierenVisible(false);
    view.addButtonAlleSelektierenActionListener(e -> onAlleSelektieren());
  }

  private void onAlleSelektieren() {
    model.getTableModel().alleMitarbeiterSelektieren();
    view.setButtonAlleSelektierenVisible(false);
    view.setButtonAlleDeselektierenVisible(true);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateMitarbeiterModel createOrUpdateMitarbeiterModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateMitarbeiterController createOrUpdateMitarbeiterController =
        new CreateOrUpdateMitarbeiterController(
            svmContext, createOrUpdateMitarbeiterModel, "Neuer Mitarbeiter");
    createOrUpdateMitarbeiterController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndMitarbeiterIdOptional =
        createOrUpdateMitarbeiterModel.getSaveSuccessfulAndMitarbeiterIdOptional();
    Optional<Integer> mitarbeiterIdOptional =
        saveOrUpdateSuccessfulAndMitarbeiterIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndMitarbeiterIdOptional.saveSuccessful()
        && mitarbeiterIdOptional.isPresent()) {
      insertCreatedObjectToList(mitarbeiterIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateMitarbeiterModel createOrUpdateMitarbeiterModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateMitarbeiterController createOrUpdateMitarbeiterController =
        new CreateOrUpdateMitarbeiterController(
            svmContext, createOrUpdateMitarbeiterModel, "Mitarbeiter bearbeiten");
    createOrUpdateMitarbeiterController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndMitarbeiterIdOptional =
        createOrUpdateMitarbeiterModel.getSaveSuccessfulAndMitarbeiterIdOptional();
    if (saveOrUpdateSuccessfulAndMitarbeiterIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndMitarbeiterIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }

  @Override
  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Durch Drücken des Ja-Buttons wird der Mitarbeiter unwiderruflich aus der Datenbank gelöscht. Fortfahren?",
            "Mitarbeiter löschen?");
    if (n == 0) {
      DeleteMitarbeiterResult deleteMitarbeiterResult =
          model.eintragLoeschen(view.convertRowIndexToModel());
      switch (deleteMitarbeiterResult) {
        case MITARBEITER_VON_KURS_REFERENZIERT -> showErrorMessageDialog(deleteMitarbeiterResult);
        case MITARBEITER_DURCH_ANDEREN_BENUTZER_VERAENDERT -> {
          showErrorMessageDialog(deleteMitarbeiterResult);
          reloadSelectedRow();
        }
        case LOESCHEN_ERFOLGREICH -> {
          view.showInfoMessageDialog(
              "Der Mitarbeiter wurde erfolgreich aus der Datenbank gelöscht.",
              "Löschen erfolgreich");
          removeSelectedRow();
        }
      }
    }
  }

  private void showErrorMessageDialog(DeleteMitarbeiterResult deleteMitarbeiterResult) {
    view.showErrorMessageDialog(deleteMitarbeiterResult.getMessage(), "Fehler");
  }

  private void configBtnExportieren() {
    view.addButtonExportierenActionListener(e -> onExportieren());
  }

  private void onExportieren() {
    view.setButtonExportierenFocusPainted(true);
    showOnExportDialog();
    // Dialog wurde geschlossen
    view.setButtonExportierenFocusPainted(false);
  }

  private void showOnExportDialog() {

    int anzSelektiert = model.getTableModel().getAnzahlSelektiert();
    int rowCount = model.getTableModel().getRowCount();
    if (anzSelektiert < rowCount) {
      view.showInfoMessageDialog(
          "Es "
              + ((anzSelektiert > 1) ? "sind nur " + anzSelektiert : "ist nur einer")
              + " der "
              + rowCount
              + " Einträge selektiert. Nur "
              + ((anzSelektiert > 1) ? "diese Einträge\nwerden" : "dieser Eintrag\nwird")
              + " beim Exportieren berücksichtigt.",
          "Nicht alle Einträge selektiert");
    }

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

  private void configBtnEmail() {
    view.addButtonEmailActionListener(e -> onEmail());
  }

  private void onEmail() {
    view.setButtonEmailFocusPainted(true);

    int anzSelektiert = model.getTableModel().getAnzahlSelektiert();
    int rowCount = model.getTableModel().getRowCount();
    if (anzSelektiert > 1 && anzSelektiert < rowCount) {
      view.showInfoMessageDialog(
          "Es sind nur "
              + anzSelektiert
              + " der "
              + rowCount
              + " Einträge selektiert. "
              + "Nur diese Einträge\nwerden für die Gruppen-E-Mail berücksichtigt.",
          "Nicht alle Einträge selektiert");
    }

    CheckEmailResult checkEmailResult = model.validateEmailAdressen();
    if (!checkEmailResult.isSuccessful()) {
      StringBuilder message = new StringBuilder();

      appendCheckMessage(checkEmailResult, message);

      if (checkEmailResult.gueltigeEmailAdressen().isEmpty()) {
        message.append(
            "Die Verarbeitung wird abgebrochen, da keine gültigen E-Mail-Adressen "
                + "selektiert worden sind.");
        view.showErrorMessageDialog(message.toString(), "Verarbeitung wird abgebrochen");
        return;
      }

      int numberOfValidEmailAdressen = checkEmailResult.gueltigeEmailAdressen().size();
      if (numberOfValidEmailAdressen > 1) {
        message
            .append("Soll ein Gruppen-E-Mail für die ")
            .append(numberOfValidEmailAdressen)
            .append(" gültigen E-Mail-Adressen erstellt werden?");
      } else {
        message.append("Soll ein E-Mail für die gültige E-Mail-Adresse erstellt werden?");
      }
      int result = view.showYesNoDialog(message.toString(), "Fortfahren?");
      if (result != 0) {
        return;
      }
    }

    CallEmailClientResult callEmailClientResult = model.callEmailClient();

    if (!callEmailClientResult.isSuccessful()) {
      view.showErrorMessageDialog(callEmailClientResult.getMessage(), "Fehler");
    }

    view.setButtonEmailFocusPainted(false);
  }

  private static void appendCheckMessage(CheckEmailResult checkEmailResult, StringBuilder message) {
    if (checkEmailResult.gueltigeEmailAdressen().isEmpty()) {
      message.append("Es sind keine gültigen E-Mail-Adressen selektiert worden!\n\n");
    }

    appendInvalidEmailAdressenToMessage(
        checkEmailResult.fehlendeEmailAdressen(),
        checkEmailResult.ungueltigeEmailAdressen(),
        message);
  }

  private static void appendInvalidEmailAdressenToMessage(
      Set<String> fehlendeEmailAdressen,
      Set<String> ungueltigeEmailAdressen,
      StringBuilder message) {

    if (!fehlendeEmailAdressen.isEmpty()) {
      message
          .append("Für ")
          .append(fehlendeEmailAdressen.size())
          .append(" Mitarbeiter ist keine E-Mail-Adresse erfasst:\n")
          .append(String.join("\n", fehlendeEmailAdressen))
          .append("\n\n");
    }

    if (!ungueltigeEmailAdressen.isEmpty()) {
      message
          .append("Für ")
          .append(ungueltigeEmailAdressen.size())
          .append(" Mitarbeiter ist eine ungültige E-Mail-Adresse erfasst: \n")
          .append(String.join("\n", ungueltigeEmailAdressen))
          .append("\n\n");
    }
  }

  @Override
  protected void updateButtonsAndTotalText() {
    int anzahlSelektiert = model.getTableModel().getAnzahlSelektiert();
    int rowCount = model.getTableModel().getRowCount();
    if (anzahlSelektiert > 0) {
      view.setButtonExportierenEnabled(true);
      view.setButtonEmailEnabled(true);
    } else {
      view.setButtonExportierenEnabled(false);
      view.setButtonEmailEnabled(false);
    }
    if (rowCount <= 1 || anzahlSelektiert == 1) {
      view.setButtonEmailText(EMAIL);
    } else {
      view.setButtonEmailText(GRUPPEN_EMAIL);
    }
    if (anzahlSelektiert == rowCount) {
      view.setButtonAlleSelektierenVisible(false);
      view.setButtonAlleDeselektierenVisible(true);
    } else {
      view.setButtonAlleDeselektierenVisible(false);
      view.setButtonAlleSelektierenVisible(true);
    }
    String lblTotalText =
        "Total: " + rowCount + " Mitarbeiter (" + anzahlSelektiert + " selektiert)";
    view.setLabelTotalText(lblTotalText);
  }
}
