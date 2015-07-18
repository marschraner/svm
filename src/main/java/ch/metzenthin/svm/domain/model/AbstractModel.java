package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private boolean bulkUpdate = false;

    /**
     * validationMode true: Model wird invalidiert bei Fehler
     * validationMode false: Model wird nicht invalidiert bei Fehler
     */
    private boolean validationMode = true;

    void setBulkUpdate(boolean bulkUpdate) {
        this.bulkUpdate = bulkUpdate;
        firePropertyChange(Field.BULK_UPDATE, !bulkUpdate, bulkUpdate);
        if (!bulkUpdate && !isValidationMode()) {
            fireCompleted(true);
        }
    }

    boolean isBulkUpdate() {
        return bulkUpdate;
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
    public final void firePropertyChange(Field field, Object oldValue, Object newValue) {
        if ((oldValue == null) && (newValue == null)) {
            return;
        }
        this.propertyChangeSupport.firePropertyChange(field.toString(), oldValue, newValue);
    }

    @Override
    public boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
        return field.toString().equals(evt.getPropertyName());
    }


    //------------------------------------------------------------------------------------------------------------------
    // Disable fields support
    //------------------------------------------------------------------------------------------------------------------

    private final List<DisableFieldsListener> disableFieldsListeners = new ArrayList<>();

    @Override
    public void addDisableFieldsListener(DisableFieldsListener disableFieldsListener) {
        disableFieldsListeners.add(disableFieldsListener);
    }

    @Override
    public void removeDisableFieldsListener(DisableFieldsListener disableFieldsListener) {
        disableFieldsListeners.remove(disableFieldsListener);
    }

    void fireDisableFields(boolean disable, Set<Field> fields) {
        for (DisableFieldsListener disableFieldsListener : disableFieldsListeners) {
            disableFieldsListener.disableFields(disable, fields);
        }
    }

    @Override
    public void disableFields() {
        Set<Field> fields = new HashSet<>();
        fields.add(Field.ALLE);
        disableFields(fields);
    }

    @Override
    public void enableFields() {
        Set<Field> fields = new HashSet<>();
        fields.add(Field.ALLE);
        enableFields(fields);
    }

    @Override
    public void disableFields(Set<Field> fields) {
        fireDisableFields(true, fields);
    }

    @Override
    public void enableFields(Set<Field> fields) {
        fireDisableFields(false, fields);
    }


    //------------------------------------------------------------------------------------------------------------------
    // Make error labels invisible support
    //------------------------------------------------------------------------------------------------------------------

    private final List<MakeErrorLabelsInvisibleListener> makeErrorLabelsInvisibleListeners = new ArrayList<>();

    @Override
    public void addMakeErrorLabelsInvisibleListener(MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {
        makeErrorLabelsInvisibleListeners.add(makeErrorLabelsInvisibleListener);
    }

    @Override
    public void removeMakeErrorLabelsInvisibleListener(MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {
        makeErrorLabelsInvisibleListeners.remove(makeErrorLabelsInvisibleListener);
    }

    void fireMakeErrorLabelsInvisible(Set<Field> fields) {
        for (MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener : makeErrorLabelsInvisibleListeners) {
            makeErrorLabelsInvisibleListener.makeErrorLabelsInvisible(fields);
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        fireMakeErrorLabelsInvisible(fields);
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
        if (isValidationMode()) {
            fireCompleted(isCompleted());
        } else {
            fireCompleted(true);
        }
    }

    @Override
    public final void invalidate() {
        if (isValidationMode()) {
            fireCompleted(false);
        }
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
        if (isBulkUpdate()) {
            return;
        }
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

    public boolean isValidationMode() {
        return validationMode;
    }

    @Override
    public void setValidationMode(boolean validationMode) {
        this.validationMode = validationMode;
    }

}
