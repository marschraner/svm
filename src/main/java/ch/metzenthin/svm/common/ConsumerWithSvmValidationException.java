package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Hans Stamm
 */
@FunctionalInterface
public interface ConsumerWithSvmValidationException<T> {

  void accept(T value) throws SvmValidationException;
}
