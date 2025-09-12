package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public interface Formatter<T> {
  T format(T value);
}
