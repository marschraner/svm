package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.entityfields.KursortFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
    formatAndValidateString(
        view.getTxtBezeichnungText(),
        model::validateBezeichnung,
        view::setTxtBezeichnungText,
        view::setErrorLabelBezeichnungVisible,
        view::setErrorLabelBezeichnungInvisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      KursortFields kursortFields = model.getKursortFields();
      view.setTxtBezeichnungText(kursortFields.bezeichnung());
      view.setCheckBoxSelektierbarSelected(kursortFields.selektierbar());
    } else {
      view.setCheckBoxSelektierbarSelected(true);
    }
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    KursortFields kursortFields =
        new KursortFields(view.getTxtBezeichnungText(), view.isCheckBoxSelektierbarSelected());
    return model.speichern(kursortFields);
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    if (field == Field.BEZEICHNUNG) {
      setErrorLabelVisibleIfRequired(
          validationResult, Field.BEZEICHNUNG, view::setErrorLabelBezeichnungVisible);
    } else {
      throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  public void setAllErrorLabelsInvisible() {
    view.setErrorLabelBezeichnungInvisible();
  }
}
