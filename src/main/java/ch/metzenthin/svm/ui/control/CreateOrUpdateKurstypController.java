package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.domain.model.KurstypFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKurstypView;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
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
    KurstypFields kurstypFields = model.getKurstypFields();
    view.setTxtBezeichnungText(kurstypFields.bezeichnung());
    if (model.isNeu()) {
      view.setCheckBoxSelektierbarSelected(true);
    } else {
      view.setCheckBoxSelektierbarSelected(kurstypFields.selektierbar());
    }
  }

  @Override
  protected ValidationResultsAndSaveResult speichern() {
    KurstypFields kurstypFields =
        new KurstypFields(view.getTxtBezeichnungText(), view.isCheckBoxSelektierbarSelected());
    return model.speichern(kurstypFields);
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
