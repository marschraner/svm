package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CreateCodeAssignmentModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.view.CodeSelectionView;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CreateSchuelerCodeAssignmentController
    extends AbstractSubmitDialogController<CodeSelectionView<SchuelerCode>> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CreateSchuelerCodeAssignmentController.class);

  private final CreateCodeAssignmentModel<SchuelerCode> model;

  public CreateSchuelerCodeAssignmentController(
      CreateCodeAssignmentModel<SchuelerCode> createSchuelerCodeAssignmentModel, String title) {
    super(createView(title));
    model = createSchuelerCodeAssignmentModel;
    configComboBoxCode();
  }

  private static CodeSelectionView<SchuelerCode> createView(String title) {
    return new CodeSelectionView<>(title);
  }

  private void configComboBoxCode() {
    view.setComboBoxCodeValues(model.getSelectableCodes());
    view.setComboBoxCodeSelectedItem(null);
    view.addComboBoxCodeActionListener(e -> onCodeSelected());
  }

  private void onCodeSelected() {
    LOGGER.trace(
        "CreateSchuelerCodeAssignmentController Event Code selected={}",
        view.getComboBoxCodeSelectedItem());
    ValidationResult validationResult = model.validateCode(view.getComboBoxCodeSelectedItem());
    if (!validationResult.isValid()) {
      setErrorLabelVisible(validationResult, Field.CODE);
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    return model.speichern(view.getComboBoxCodeSelectedItem());
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    if (Objects.requireNonNull(field) == Field.CODE) {
      setErrorLabelVisibleIfRequired(validationResult, field, view::setErrorLabelCodeVisible);
    } else {
      throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelCodeInvisible();
  }
}
