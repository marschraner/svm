package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.KursortFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortController
    extends AbstractCreateOrUpdateController<CreateOrUpdateKursortView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateKursortController.class);

  private final CreateOrUpdateKursortModel model;

  public CreateOrUpdateKursortController(
      CreateOrUpdateKursortModel createOrUpdateKursortModel, String title) {
    super(createView(title));
    model = createOrUpdateKursortModel;
    configTxtBezeichnung();
    initialiseViewFields();
  }

  private static CreateOrUpdateKursortView createView(String title) {
    return new CreateOrUpdateKursortView(title);
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
      view.setErrorLabelBezeichnungInvisible();
    } else {
      view.setErrorLabelBezeichnungVisible(validationResult.errorMessage());
    }
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

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    KursortFields kursortFields =
        new KursortFields(view.getTxtBezeichnungText(), view.isCheckBoxSelektierbarSelected());
    return model.speichern(kursortFields);
  }

  @Override
  public void setErrorLabelsVisible(List<ValidationResult> validationResults) {
    for (ValidationResult validationResult : validationResults) {
      if (!validationResult.isValid()) {
        setErrorLabelVisibleIfRequired(
            validationResult, Field.BEZEICHNUNG, view::setErrorLabelBezeichnungVisible);
      }
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelBezeichnungInvisible();
  }
}
