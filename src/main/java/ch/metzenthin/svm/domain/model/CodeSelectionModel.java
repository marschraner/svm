package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.Code;
import java.util.List;

/**
 * @author Hans Stamm
 */
public interface CodeSelectionModel<T extends Code> {

  T[] getSelectableCodes(List<T> codesToBeExcluded);

  ValidationResult validateCode(T code);

  ValidationResultsAndSubmitResult submit(T code);

  T getSelectedCode();
}
