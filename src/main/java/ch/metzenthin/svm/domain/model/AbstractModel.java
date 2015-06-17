package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.FieldName;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.ui.control.CompletedListener;
import ch.metzenthin.svm.ui.control.DisableFieldsListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;

/**
 * @author Hans Stamm
 */
abstract class AbstractModel implements Model, ModelAttributeListener {

    private final CommandInvoker commandInvoker;

    AbstractModel(CommandInvoker commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Property change support
    //------------------------------------------------------------------------------------------------------------------

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public final void firePropertyChange(FieldName fieldName, Object oldValue, Object newValue) {
        if ((oldValue == null) && (newValue == null)) {
            return;
        }
        this.propertyChangeSupport.firePropertyChange(fieldName.toString(), oldValue, newValue);
    }

    @Override
    public boolean checkIsFieldNameChange(FieldName fieldName, PropertyChangeEvent evt) {
        return fieldName.toString().equals(evt.getPropertyName());
    }


    //------------------------------------------------------------------------------------------------------------------
    // DisableFields support
    //------------------------------------------------------------------------------------------------------------------

    private final List<DisableFieldsListener> disableFieldsListeners = new ArrayList<>();

    public void addDisableFieldsListener(DisableFieldsListener disableFieldsListener) {
        disableFieldsListeners.add(disableFieldsListener);
    }

    public void removeDisableFieldsListener(DisableFieldsListener disableFieldsListener) {
        disableFieldsListeners.remove(disableFieldsListener);
    }

    void fireDisableFields(boolean disable, Set<FieldName> fieldNames) {
        for (DisableFieldsListener disableFieldsListener : disableFieldsListeners) {
            disableFieldsListener.disableFields(disable, fieldNames);
        }
    }

    public void disableFields() {
        Set<FieldName> fieldNames = new HashSet<>();
        fieldNames.add(FieldName.ALLE);
        disableFields(fieldNames);
    }

    public void enableFields() {
        Set<FieldName> fieldNames = new HashSet<>();
        fieldNames.add(FieldName.ALLE);
        enableFields(fieldNames);
    }

    public void disableFields(Set<FieldName> fieldNames) {
        fireDisableFields(true, fieldNames);
    }

    public void enableFields(Set<FieldName> fieldNames) {
        fireDisableFields(false, fieldNames);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Completed support
    //------------------------------------------------------------------------------------------------------------------

    private final List<CompletedListener> completedListeners = new ArrayList<>();

    public void addCompletedListener(CompletedListener completedListener) {
        completedListeners.add(completedListener);
    }

    public void removeCompletedListener(CompletedListener completedListener) {
        completedListeners.remove(completedListener);
    }

    void fireCompleted(boolean completed) {
        for (CompletedListener completedListener : completedListeners) {
            completedListener.completed(completed);
        }
    }

    public void initializeCompleted() {
        fireCompleted(isCompleted());
    }

    @Override
    public final void invalidate() {
        fireCompleted(false);
    }

    private void setValid() {
        fireCompleted(true);
    }

    /**
     * Template Method für die Validierung des Models.
     *
     * @throws SvmValidationException
     */
    @Override
    public void validate() throws SvmValidationException {
        try {
            doValidate();
        } catch (SvmValidationException e) {
            invalidate();
            throw e;
        }
        setValid();
    }

    /**
     * Subklassen prüfen ihren Teil des Models und schmeissen eine von SvmValidationException abgeleitete Exception
     * bei Fehler. Die Invalidierung des Models erfolgt in der Template Method.
     *
     * @throws SvmValidationException
     */
    abstract void doValidate() throws SvmValidationException;

}
