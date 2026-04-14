package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.KursortFields;
import ch.metzenthin.svm.domain.model.validation.ValidationAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortController implements DialogClosingListener {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateKursortController.class);

  private final CreateOrUpdateKursortModel model;
  private final CreateOrUpdateKursortView view;

  public CreateOrUpdateKursortController(
      CreateOrUpdateKursortModel createOrUpdateKursortModel, String title) {
    model = createOrUpdateKursortModel;
    view = new CreateOrUpdateKursortView(title);
    view.configDialogClosing(this);
    configTxtBezeichnung();
    configBtnSpeichern();
    configBtnAbbrechen();
    initialiseViewFields();
  }

  private void initialiseViewFields() {
    KursortFields kursortFields = model.getKursortFields();
    view.setTxtBezeichnungText(kursortFields.bezeichnung());
    if (model.isNeu()) {
      view.setCheckBoxSelektierbarSelected(true);
    } else {
      view.setCheckBoxSelektierbarSelected(kursortFields.selektierbar());
    }
  }

  private void configTxtBezeichnung() {
    view.addTxtBezeichnungActionListener(e -> onBezeichnungEvent());
    view.addTxtBezeichnungFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onBezeichnungEvent();
          }
        });
  }

  private void onBezeichnungEvent() {
    LOGGER.trace("CreateOrUpdateKursortController Event Bezeichnung");
    String formattedBezeichnung = model.formatBezeichnung(view.getTxtBezeichnungText());
    view.setTxtBezeichnungText(formattedBezeichnung);
    ValidationResult validationResult = model.validateBezeichnung(formattedBezeichnung);
    if (validationResult.isValid()) {
      view.setErrLblBezeichnungInvisible();
    } else {
      view.setErrLblBezeichnungVisible(validationResult.errorMessage());
    }
  }

  private void configBtnSpeichern() {
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  private void onSpeichern() {
    KursortFields kursortFields =
        new KursortFields(view.getTxtBezeichnungText(), view.isCheckBoxSelektierbarSelected());
    ValidationAndSaveResult validationAndSaveResult = model.speichern(kursortFields);

    if (!validationAndSaveResult.isValidationSuccessful()
        && validationAndSaveResult.affectedFieldsOfValidationContains(Field.BEZEICHNUNG)) {
      view.setTxtBezeichnungToolTipText(validationAndSaveResult.getValidationErrorMessage());
      view.setButtonSpeichernFocusPainted(false);

    } else if (!validationAndSaveResult.isSaveSuccessful()) {
      view.showErrorMessageDialog(validationAndSaveResult.getSaveErrorMessage(), "Fehler");
      if (validationAndSaveResult.isDialogToBeClosedAfterSave()) {
        closeDialog();
      } else {
        view.setButtonSpeichernFocusPainted(false);
      }

    } else {
      closeDialog();
    }
  }

  private void closeDialog() {
    view.closeDialog();
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  public void showDialog() {
    view.showDialog();
  }

  @Override
  public void onCloseDialog() {
    closeDialog();
  }
}
