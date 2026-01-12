package ch.metzenthin.svm.ui.components;

import java.util.function.Supplier;

/**
 * @author Hans Stamm
 */
public interface ViewValueGetter<T> extends Supplier<T> {

  default T getViewValue() {
    return this.get();
  }
}
