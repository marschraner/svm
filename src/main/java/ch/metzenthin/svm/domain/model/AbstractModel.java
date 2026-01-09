package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.CommandInvokerImpl;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
abstract class AbstractModel implements Model, ModelAttributeListener {

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();

  CommandInvoker getCommandInvoker() {
    return commandInvoker;
  }

  private boolean bulkUpdate = false;

  /**
   * Wird bei der Instanziierung des Controllers gesetzt und wird dann nicht mehr verändert.<br>
   * modelValidationMode true: Model wird invalidiert bei Fehler<br>
   * modelValidationMode false: Model wird nicht invalidiert bei Fehler
   */
  @Getter private boolean modelValidationMode = true;

  /**
   * BulkUpdate true: es folgen mehrere Attribut-Updates nacheinander, für die keine Validierung
   * durchgeführt werden muss (z.B. bei initializeComplete()).<br>
   * BulkUpdate false: Die Attribut-Updates sind durchgeführt. View und Model werden validiert.
   * Falls das Model nicht invalidiert wird bei Fehler (modelValidationMode = false), wird durch
   * fireCompleted(true) die Initialisierung des Models abgeschlossen.
   */
  void setBulkUpdate(boolean bulkUpdate) {
    this.bulkUpdate = bulkUpdate;
    // Der folgende firePropertyChange bewirkt, dass die View und das Model validiert werden, falls
    // bulkUpdate false ist.
    firePropertyChange(Field.BULK_UPDATE, !bulkUpdate, bulkUpdate);
    if (!bulkUpdate && !isModelValidationMode()) {
      fireCompleted(true);
    }
  }

  boolean isBulkUpdate() {
    return bulkUpdate;
  }

  // ------------------------------------------------------------------------------------------------------------------
  // Property change support
  // ------------------------------------------------------------------------------------------------------------------

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

  // ------------------------------------------------------------------------------------------------------------------
  // Disable fields support
  // ------------------------------------------------------------------------------------------------------------------

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

  // ------------------------------------------------------------------------------------------------------------------
  // Make error labels invisible support
  // ------------------------------------------------------------------------------------------------------------------

  private final List<MakeErrorLabelsInvisibleListener> makeErrorLabelsInvisibleListeners =
      new ArrayList<>();

  @Override
  public void addMakeErrorLabelsInvisibleListener(
      MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {
    makeErrorLabelsInvisibleListeners.add(makeErrorLabelsInvisibleListener);
  }

  @Override
  public void removeMakeErrorLabelsInvisibleListener(
      MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {
    makeErrorLabelsInvisibleListeners.remove(makeErrorLabelsInvisibleListener);
  }

  void fireMakeErrorLabelsInvisible(Set<Field> fields) {
    for (MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener :
        makeErrorLabelsInvisibleListeners) {
      makeErrorLabelsInvisibleListener.makeErrorLabelsInvisible(fields);
    }
  }

  @Override
  public void makeErrorLabelsInvisible(Set<Field> fields) {
    fireMakeErrorLabelsInvisible(fields);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // Completed support
  // ------------------------------------------------------------------------------------------------------------------

  private final List<CompletedListener> completedListeners = new ArrayList<>();

  public void addCompletedListener(CompletedListener completedListener) {
    completedListeners.add(completedListener);
  }

  void fireCompleted(boolean completed) {
    for (CompletedListener completedListener : completedListeners) {
      completedListener.completed(completed);
    }
  }

  @Override
  public void initializeCompleted() {
    if (isModelValidationMode()) {
      fireCompleted(isCompleted());
    } else {
      fireCompleted(true);
    }
  }

  @Override
  public final void invalidate() {
    if (isModelValidationMode()) {
      fireCompleted(false);
    }
  }

  /** Template Method für die Validierung des Models. */
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
    fireCompleted(true);
  }

  /**
   * Subklassen prüfen ihren Teil des Models und schmeissen eine von SvmValidationException
   * abgeleitete Exception bei Fehler. Die Invalidierung des Models erfolgt in der Template Method.
   */
  abstract void doValidate() throws SvmValidationException;

  @Override
  public void setModelValidationMode(boolean modelValidationMode) {
    this.modelValidationMode = modelValidationMode;
  }
}
