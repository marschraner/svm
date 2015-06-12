package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.ui.control.CompletedListener;
import ch.metzenthin.svm.ui.control.DisableFieldsListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
abstract class AbstractModel implements Model {

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

    void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if ((oldValue == null) && (newValue == null)) {
            return;
        }
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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

    void fireDisableFields(boolean disable) {
        for (DisableFieldsListener disableFieldsListener : disableFieldsListeners) {
            disableFieldsListener.disableFields(disable);
        }
    }

    public void disableFields() {
        fireDisableFields(true);
    }

    public void enableFields() {
        fireDisableFields(false);
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

    void invalidate() {
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
