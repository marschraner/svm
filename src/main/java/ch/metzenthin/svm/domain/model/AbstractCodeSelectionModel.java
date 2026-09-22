package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotNull;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.result.SelectResult;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
public abstract class AbstractCodeSelectionModel<T extends Code> implements CodeSelectionModel<T> {

  @Getter private T selectedCode = null;

  @Override
  public ValidationResult validateCode(T code) {
    return validateNotNull(code, Field.CODE);
  }

  @Override
  public ValidationResultsAndSubmitResult submit(T selectedCode) {
    ValidationResult validationResult = validateCode(selectedCode);
    if (!validationResult.isValid()) {
      return new ValidationResultsAndSubmitResult(List.of(validationResult));
    }
    this.selectedCode = selectedCode;
    return new ValidationResultsAndSubmitResult(
        List.of(validationResult), SelectResult.ERFOLGREICH);
  }

  public T[] getSelectableCodes(List<T> codesToBeExcluded) {
    List<T> codesWithSelektierbarTrue = getCodesWithSelektierbarTrue();
    List<T> selectableCodes = new ArrayList<>(codesWithSelektierbarTrue);
    selectableCodes.removeAll(codesToBeExcluded);
    return toArray(selectableCodes);
  }

  protected abstract List<T> getCodesWithSelektierbarTrue();

  protected abstract T[] toArray(List<T> codeList);
}
