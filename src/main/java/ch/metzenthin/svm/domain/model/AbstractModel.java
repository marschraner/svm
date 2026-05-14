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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model Basisklasse.
 *
 * @author Hans Stamm
 */
public abstract class AbstractModel implements Model, ModelAttributeListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModel.class);

  private final CommandInvoker commandInvoker = new CommandInvokerImpl();

  CommandInvoker getCommandInvoker() {
    return commandInvoker;
  }

  /**
   * Flag zur Steuerung des bulkUpdate-Modus (Ein- bzw. Ausschalten der Validierung).
   *
   * <p>Steuert, ob sich der Controller, bzw. das Model im bulkUpdate-Modus befindet (true), oder
   * nicht. Das Feld wird durch PropertyChange-Aufrufe des Models ein- und ausgeschaltet. Ist
   * bulkUpdate eingeschaltet (true), wird die Validierung der Felder im Model ausgeschaltet. Wird
   * bulkUpdate ausgeschaltet (auf false gesetzt), wird die Validierung angestossen.
   *
   * <p>Siehe auch {@link ch.metzenthin.svm.ui.control.AbstractController}
   */
  private boolean bulkUpdate = false;

  /**
   * Wird bei der Instanziierung des Controllers gesetzt und wird dann nicht mehr verändert.
   *
   * <p>Beschreibung siehe {@link
   * ch.metzenthin.svm.ui.control.AbstractController#isModelValidationMode()}.
   */
  @Getter private boolean modelValidationMode = true;

  /**
   * Setzen des bulkUpdate-Modus.
   *
   * <p>Der neue Wert des bulkUpdate-Flags wird Listenern (Controller des Models) via
   * PropertyChangeEvent mitgeteilt. Der PropertyChange bewirkt, dass die View und das Model
   * validiert werden, falls der bulkUpdate-Modus ausgeschaltet (auf false gesetzt) wird.
   *
   * <p>BulkUpdate true: es folgen mehrere Attribut-Updates nacheinander, für die keine Validierung
   * durchgeführt werden muss (z.B. bei initializeComplete()).<br>
   * BulkUpdate false: Die Attribut-Updates sind durchgeführt. View und Model werden validiert.
   * Falls das Model nicht invalidiert wird bei Fehler (modelValidationMode = false), wird durch
   * fireCompleted(true) die Initialisierung des Models abgeschlossen.
   */
  void setBulkUpdate(boolean bulkUpdate) {
    this.bulkUpdate = bulkUpdate;
    firePropertyChange(Field.BULK_UPDATE, !bulkUpdate, bulkUpdate);
    if (!bulkUpdate && !isModelValidationMode()) {
      fireCompleted(true);
    }
  }

  /**
   * Abfrage des bulkUpdate-Modus.
   *
   * <p>true: bulkUpdate-Modus ist eingeschaltet, d.h. die Validierung ist ausgeschaltet.<br>
   * false: bulkUpdate-Modus ist ausgeschaltet, d.h. die Validierung ist eingeschaltet.
   *
   * @return Status des bulkUpdate-Modus
   */
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

  /**
   * Information der PropertyChangeListener (Controller), dass der Wert des übergebenen Feldes
   * geändert hat.
   *
   * @param field Feld, dessen Wert verändert wurde
   * @param oldValue alter Wert des Feldes
   * @param newValue neuer Wert des Feldes
   */
  @Override
  public final void firePropertyChange(Field field, Object oldValue, Object newValue) {
    if ((oldValue == null) && (newValue == null)) {
      return;
    }
    this.propertyChangeSupport.firePropertyChange(field.toString(), oldValue, newValue);
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

  /**
   * Information der DisableFieldsListener (Controller), dass die übergebenen Felder gemäss dem
   * übergebenen Flag disabled bzw. enabled werden sollen.
   *
   * @param disable Flag, ob die übergebenen Felder disabled (true) bzw. enabled (false) werden
   *     sollen
   * @param fields Felder, die disabled bzw. enabled werden sollen
   */
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

  /**
   * Information der MakeErrorLabelsInvisibleListener (Controller), für welche Felder die
   * Error-Labels unsichtbar gemacht werden sollen.
   *
   * @param fields Felder, die unsichtbar gemacht werden sollen
   */
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

  /**
   * Information der CompletedListener, ob das Model vollständig oder unvollständig ist.
   *
   * @param completed true: das Model ist vollständig, sonst unvollständig
   */
  void fireCompleted(boolean completed) {
    for (CompletedListener completedListener : completedListeners) {
      completedListener.completed(completed);
    }
  }

  /**
   * Diese Methode wird von den Subklassen der noch nicht umgestellten Models mittels
   * super.initializeCompleted() aufgerufen. Die Methode der Subklassen wird durch den Controller
   * aufgerufen.
   */
  @Override
  public void initializeCompleted() {
    fireModelValueInitialisationCompleted();
  }

  private void fireModelValueInitialisationCompleted() {
    if (isModelValidationMode()) {
      fireCompleted(isCompleted());
    } else {
      fireCompleted(true);
    }
  }

  /**
   * Generische Methode für die Initialisierung für neue Model-Objekte, nachdem die Initialisierung
   * des Controllers erfolgt ist.
   *
   * @param allModelValuesSetter Callback zum Setzen der Model-Werte
   */
  protected void initialiseModelValuesOnNeu(AllModelValuesSetter allModelValuesSetter) {
    try {
      allModelValuesSetter.setAllModelValues();
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
    fireModelValueInitialisationCompleted();
  }

  /**
   * Generische Methode für die Initialisierung für bestehende Model-Objekte, nachdem die
   * Initialisierung des Controllers erfolgt ist.
   *
   * <p>Vor dem Aufruf des Callbacks zur Initialisierung der einzelnen Model-Werte wird der
   * bulkUpdate-Modus eingeschaltet (d.h. die Validierung wird ausgeschaltet) und nachher wieder
   * ausgeschaltet, was eine Validierung der View und des Models auslöst.
   *
   * @param allModelValuesSetter Callback zum Setzen der Model-Werte
   */
  protected void initialiseModelValuesOnBearbeiten(AllModelValuesSetter allModelValuesSetter) {
    // Bestehende Entity
    // Validierung ausschalten
    setBulkUpdate(true);
    // Meldung der Attributwerte an die Listener
    try {
      allModelValuesSetter.setAllModelValues();
    } catch (SvmValidationException e) {
      LOGGER.error(e.getMessage());
    }
    // Validierung anstossen
    setBulkUpdate(false);
  }

  /**
   * Model auf ungültig setzen.
   *
   * <p>Es wird ein fireCompleted(false) durchgeführt. Dies bewirkt, dass der Speichern-Button
   * deaktiviert wird im ModelValidationMode=true.
   */
  @Override
  public final void invalidate() {
    if (isModelValidationMode()) {
      fireCompleted(false);
    }
  }

  /**
   * Template Methode für die Validierung des Models.
   *
   * <p>Ist der bulkUpdate-Modus eingeschaltet (d.h. die Validierung ist ausgeschaltet), wird die
   * Validierung nicht ausgeführt.<br>
   * Es werden nur Felder- und Objekt-übergreifende Regeln geprüft.<br>
   * Bei einem Validierungsfehler wird das Model auf ungültig (invalidated) gesetzt (die
   * Completed-Listener werden informiert, dass das Model unvollständig ist (fireCompleted(false)))
   * und die Exception wird weiter geworfen.<br>
   * Wird kein Validierungsfehler gefunden, werden die Completed-Listener informiert, dass das Model
   * vollständig ist (fireCompleted(true)).
   *
   * @throws SvmValidationException bei Validierungsfehler
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
    fireCompleted(true);
  }

  /**
   * Validierung von Felder- und Objekt-übergreifenden Regeln.
   *
   * <p>Subklassen prüfen ihren Teil des Models und schmeissen eine von SvmValidationException
   * abgeleitete Exception bei Fehler. Die Invalidierung des Models erfolgt in der Template Method.
   */
  abstract void doValidate() throws SvmValidationException;

  @Override
  public void setModelValidationMode(boolean modelValidationMode) {
    this.modelValidationMode = modelValidationMode;
  }

  /**
   * Prüfung, ob der Property-Name des übergebenen Events dem übergebenen Feld entspricht.
   *
   * @param field Feld
   * @param evt Event mit Property-Name
   * @return true, wenn der Property-Name des Events dem übergebenen Feld entspricht.
   */
  @Override
  public boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
    return field.toString().equals(evt.getPropertyName());
  }
}
