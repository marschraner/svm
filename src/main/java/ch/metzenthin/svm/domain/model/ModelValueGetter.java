package ch.metzenthin.svm.domain.model;

import java.util.function.Supplier;

/**
 * @author Hans Stamm
 */
public interface ModelValueGetter<T> extends Supplier<T> {

  default T getModelValue() {
    return this.get();
  }
}
