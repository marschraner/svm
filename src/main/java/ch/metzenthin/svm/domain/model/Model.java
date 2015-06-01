package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.ui.control.CompletedListener;

import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public interface Model {
    void addPropertyChangeListener(PropertyChangeListener listener);
    void addCompletedListener(CompletedListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
    void initializeCompleted();
    boolean isCompleted();
    void validate() throws SvmValidationException;
}
