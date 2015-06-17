package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.FieldName;

/**
 * @author Hans Stamm
 */
public interface ModelAttributeListener {
    void invalidate();
    void firePropertyChange(FieldName fieldName, Object oldValue, Object newValue);
}
