package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.domain.model.entityfields.KurstypFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKurstypView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKurstypController
    extends AbstractCreateOrUpdateController<CreateOrUpdateKurstypView> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateOrUpdateKurstypController.class);

  private final CreateOrUpdateKurstypModel model;

  public CreateOrUpdateKurstypController(
      CreateOrUpdateKurstypModel createOrUpdateKurstypModel, String title) {
    super(createView(title));
    model = createOrUpdateKurstypModel;
    configTxtBezeichnung();
    initialiseViewFields();
  }

  private static CreateOrUpdateKurstypView createView(String title) {
    return new CreateOrUpdateKurstypView(title);
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
    LOGGER.trace("CreateOrUpdateKurstypController Event Bezeichnung");
    formatAndValidateString(
        view.getTxtBezeichnungText(),
        model::validateBezeichnung,
        view::setTxtBezeichnungText,
        view::setErrorLabelBezeichnungVisible,
        view::setErrorLabelBezeichnungInvisible);
  }

  private void initialiseViewFields() {
    if (!model.isNeu()) {
      KurstypFields kurstypFields = model.getKurstypFields();
      view.setTxtBezeichnungText(kurstypFields.bezeichnung());
      view.setCheckBoxSelektierbarSelected(kurstypFields.selektierbar());
    } else {
      view.setCheckBoxSelektierbarSelected(true);
    }
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    KurstypFields kurstypFields =
        new KurstypFields(view.getTxtBezeichnungText(), view.isCheckBoxSelektierbarSelected());
    return model.speichern(kurstypFields);
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
