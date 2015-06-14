package ch.metzenthin.svm.domain.model;

/**
 * @author Hans Stamm
 */
public interface ModelAttributeListener {
    void invalidate();
    void firePropertyChange(String propertyName, Object oldValue, Object newValue);
}
