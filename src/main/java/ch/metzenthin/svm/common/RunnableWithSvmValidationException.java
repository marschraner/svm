package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Hans Stamm
 */
@FunctionalInterface
public interface RunnableWithSvmValidationException {

  void run() throws SvmValidationException;
}
