package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.CodeSelectionModel;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.view.CodeSelectionView;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
public class CodeSelectionController<T extends Code>
    extends AbstractSubmitDialogController<CodeSelectionView<T>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CodeSelectionController.class);

  private final CodeSelectionModel<T> model;

  public CodeSelectionController(
      CodeSelectionModel<T> codeSelectionModel,
      CodeSelectionView<T> codeSelectionView,
      List<T> codesToBeExcluded) {
    super(codeSelectionView);
    model = codeSelectionModel;
    configComboBoxCode(codesToBeExcluded);
  }

  private void configComboBoxCode(List<T> codesToBeExcluded) {
    view.setComboBoxCodeValues(model.getSelectableCodes(codesToBeExcluded));
    view.setComboBoxCodeSelectedItem(null);
    view.addComboBoxCodeActionListener(e -> onCodeSelected());
  }

  private void onCodeSelected() {
    LOGGER.trace(
        "AddCodeAssignmentController Event Code selected={}", view.getComboBoxCodeSelectedItem());
    ValidationResult validationResult = model.validateCode(view.getComboBoxCodeSelectedItem());
    if (!validationResult.isValid()) {
      setErrorLabelVisible(validationResult, Field.CODE);
    }
  }

  @Override
  protected ValidationResultsAndSubmitResult submit() {
    return model.submit(view.getComboBoxCodeSelectedItem());
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
