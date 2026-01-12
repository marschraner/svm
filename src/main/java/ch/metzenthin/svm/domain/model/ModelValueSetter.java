package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.ConsumerWithSvmValidationException;
import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Hans Stamm
 */
public interface ModelValueSetter<T> extends ConsumerWithSvmValidationException<T> {

  default void setModelValue(T modelValue) throws SvmValidationException {
    this.accept(modelValue);
  }
}
