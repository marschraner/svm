package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
class TestAttributeAccessor<T> implements AttributeAccessor<T> {
  private T value;

  TestAttributeAccessor(T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public void setValue(T value) {
    this.value = value;
  }
}
