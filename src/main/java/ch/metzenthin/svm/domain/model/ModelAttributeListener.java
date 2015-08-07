package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;

/**
 * @author Hans Stamm
 */
public interface ModelAttributeListener {
    void invalidate();
    void firePropertyChange(Field field, Object oldValue, Object newValue);
}
