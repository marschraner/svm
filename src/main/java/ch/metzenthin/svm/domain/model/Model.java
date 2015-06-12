package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.ui.control.CompletedListener;
import ch.metzenthin.svm.ui.control.DisableFieldsListener;

import java.beans.PropertyChangeListener;

/**
 * @author Hans Stamm
 */
public interface Model {
    void addPropertyChangeListener(PropertyChangeListener listener);
    void addCompletedListener(CompletedListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
    void addDisableFieldsListener(DisableFieldsListener listener);
    void removeDisableFieldsListener(DisableFieldsListener listener);
    void disableFields();
    void enableFields();
    void initializeCompleted();
    boolean isCompleted();
    void validate() throws SvmValidationException;
}
