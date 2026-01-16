package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.RunnableWithSvmValidationException;
import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Hans Stamm
 */
public interface AllModelValuesSetter extends RunnableWithSvmValidationException {

  default void setAllModelValues() throws SvmValidationException {
    this.run();
  }
}
