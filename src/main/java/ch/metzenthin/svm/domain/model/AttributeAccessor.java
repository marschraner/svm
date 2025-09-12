package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public interface AttributeAccessor<T> {

  T getValue();

  void setValue(T value);
}
