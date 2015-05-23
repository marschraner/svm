package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.ui.control.CompletedListener;

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
        checkCompleted();
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

    public void checkCompleted() {
        fireCompleted(isCompleted());
    }

}
