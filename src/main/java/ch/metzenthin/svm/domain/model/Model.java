package ch.metzenthin.svm.domain.model;

import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public interface Model {
    void addPropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
    boolean isValid();
}
