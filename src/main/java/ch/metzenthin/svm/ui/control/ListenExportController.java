package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.service.result.ListenExportSubmitResult.FILE_AUSWAHL_ABGEBROCHEN;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.ListenExportModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.ui.view.ListenExportView;
import java.awt.event.*;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hans Stamm
 */
public class ListenExportController extends AbstractSubmitDialogController<ListenExportView> {

  private static final String FEHLER = "Fehler";

  private static final Logger LOGGER = LoggerFactory.getLogger(ListenExportController.class);

  private final ListenExportModel model;
  private final Listentyp[] selectableListentypen;

  public ListenExportController(
      ListenExportModel listenExportModel, String dialogTitle, Listentyp[] selectableListentypen) {
    super(createView(dialogTitle));
    model = listenExportModel;
    this.selectableListentypen = selectableListentypen;
    configComboBoxListentyp();
    configTxtTitel();
    initialiseViewFields();
  }

  private static ListenExportView createView(String title) {
    return new ListenExportView(title);
  }

  private void configComboBoxListentyp() {
    view.setComboBoxListentypValues(selectableListentypen);
    // ComboBox auf erstes Item setzen
    view.setComboBoxListentypSelectedItem(selectableListentypen[0]);
    view.addComboBoxListentypActionListener(e -> onListentypSelected());
  }

  private void onListentypSelected() {
    LOGGER.trace(
        "ListenExportConfigController Event Listentyp selected={}",
        view.getComboBoxListentypSelectedItem());
    ValidationResult validationResult =
        model.validateListentyp(view.getComboBoxListentypSelectedItem());
    if (!validationResult.isValid()) {
      view.showErrorMessageDialog(validationResult.errorMessage(), FEHLER);
    } else {
      initialiseTitel();
    }
  }

  private void configTxtTitel() {
    view.addTxtTitelActionListener(e -> onTitelEvent());
    view.addTxtTitelFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onTitelEvent();
          }
        });
  }

  private void onTitelEvent() {
    LOGGER.trace("ListenExportConfigController Event Titel");
    formatAndValidateString(
        view.getTxtTitelText(),
        model::validateTitel,
        view::setTxtTitelText,
        view::setErrorLabelTitelVisible,
        view::setErrorLabelTitelInvisible);
  }

  private void initialiseViewFields() {
    initialiseTitel();
  }

  private void initialiseTitel() {
    Listentyp selectedListentyp = view.getComboBoxListentypSelectedItem();
    if (selectedListentyp != null) {
      view.setTxtTitelText(selectedListentyp.getListenTitel());
      if (selectedListentyp.isListenTitelRequired()) {
        view.setTxtTitelEnabled();
      } else {
        view.setTxtTitelDisabled();
      }
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    ValidationResultsAndSubmitResult validationResultsAndSaveResult =
        model.submit(view.getComboBoxListentypSelectedItem(), view.getTxtTitelText());
    if (validationResultsAndSaveResult.isValidationSuccessful()) {
      File selectedFile = view.showFileChooserAndGetSelectedFile();
      model.setExportFile(selectedFile);
      if (selectedFile == null) {
        return new ValidationResultsAndSubmitResult(
            validationResultsAndSaveResult.validationResults(), FILE_AUSWAHL_ABGEBROCHEN);
      }
    }
    return validationResultsAndSaveResult;
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case LISTENTYP ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelListentypVisible);
      case TITEL ->
          setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelTitelVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelListentypInvisible();
    view.setErrorLabelTitelInvisible();
  }
}
