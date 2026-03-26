package ch.metzenthin.svm.ui.control;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.DisableFieldsListener;
import ch.metzenthin.svm.domain.model.MakeErrorLabelsInvisibleListener;
import ch.metzenthin.svm.domain.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller Basis-Klasse.
 *
 * @author Hans Stamm
 */
public abstract class AbstractController
    implements PropertyChangeListener, DisableFieldsListener, MakeErrorLabelsInvisibleListener {

  private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

  /**
   * Model ohne parametrisierten Typ.
   *
   * <p>Diese Klasse verwendet diese Variable, damit nicht alle Controller, die von dieser Klasse
   * erben, auf einmal angepasst werden müssen.<br>
   * TODO Nachdem alle Subklassen umgestellt sind, soll die Variable mit dem parametrisierten Typ
   * verwendet werden.
   */
  private Model untypedModel;

  /**
   * Flag zur Steuerung des bulkUpdate-Modus (Ein- bzw. Ausschalten der Validierung).
   *
   * <p>Steuert, ob sich der Controller, bzw. das Model im bulkUpdate-Modus befindet (true), oder
   * nicht. Das Feld wird durch PropertyChange-Aufrufe des Models ein- und ausgeschaltet. Ist
   * bulkUpdate eingeschaltet (true), wird die Validierung der Felder im Model ausgeschaltet. Wird
   * bulkUpdate ausgeschaltet (auf false gesetzt), wird die Validierung angestossen.
   */
  private boolean bulkUpdate = false;

  /**
   * Flag zur Steuerung des Validierungszeitpunkts, der Art der Anzeige von Validierungsfehlern und
   * der Aktivierung bzw. Inaktivierung des Speichern-Buttons.
   *
   * <p>Für beide Modi gilt:<br>
   * - Benutzereingaben, die ins Model übertragen werden, werden sofort validiert.<br>
   * - Beim Drücken des Speichern-Buttons wird die Validierung durchgeführt und bei
   * Validierungsfehler abgebrochen.
   *
   * <p>ModelValidationMode eingeschaltet (true):<br>
   * - SvmRequiredExceptions werden nur als Tooltip angezeigt.<br>
   * - Bei Validierungsfehlern wird der Speichern-Button inaktiviert.<br>
   * - Die Validierung wird nach jedem durch das Model ausgelösten PropertyChange durchgeführt,
   * damit der Speichern-Button aktiviert wird, wenn kein Validierungsfehler (mehr) vorhanden ist.
   *
   * <p>ModelValidationMode ausgeschaltet (false):<br>
   * - SvmValidationExceptions (auch SvmRequiredExceptions) werden in den Error-Labels angezeigt.
   * <br>
   * - Der Speichern-Button ist immer aktiviert.
   */
  @Getter private boolean modelValidationMode;

  protected AbstractController(Model untypedModel) {
    this(untypedModel, false);
  }

  protected AbstractController(Model untypedModel, boolean modelValidationMode) {
    this.untypedModel = untypedModel;
    this.modelValidationMode = modelValidationMode;
  }

  /**
   * Abfrage, ob das Model im Initialisierungsmodus ist (bulkUpdate=true).
   *
   * <p>Ist der Model-Validation-Mode eingeschaltet (isModelValidationMode()=true), gilt folgendes:
   * Einige Listener reagieren auch auf das Setzen des Model-Wertes in den View-Komponenten während
   * der Initialisierung. Dadurch würde vom Model ein PropertyChange ausgelöst werden, auch dann,
   * wenn der alte und der neue Wert identisch sind. Das wiederum löst eine Validierung aus in der
   * alle Werte von der View ins Model übertragen werden. Alle View-Werte, die noch nicht
   * initialisiert wurden, würden in die Model-Werte übertragen und somit auf "null" bzw. nichts
   * gesetzt werden. Diese Methode muss von den entsprechenden Event-Handlern als Filter verwendet
   * werden, um zu entscheiden, ob der Event verarbeitet werden darf. Gibt diese Methode true
   * zurück, darf der Event verarbeitet werden.<br>
   * Bisher bekannte UI-Komponenten mit betroffenen Listeners:<br>
   * - Spinner (ChangeListener)<br>
   * - ComboBox (ActionListener)
   *
   * @return true, wenn das Model im Initialisierungsmodus ist.
   */
  protected boolean isModelInInitialisationMode() {
    return bulkUpdate;
  }

  /**
   * Template Methode für die PropertyChange-Events vom Model.
   *
   * <p>Der Controller trägt sich als PropertyChangeListener bei seinem Model ein. Bei einem
   * PropertyChange im Model, wird diese Methode aufgerufen. Nachdem die Subklassen den
   * PropertyChangeEvent verarbeitet haben, wird die Validierung aufgerufen, falls die Validierung
   * nicht ausgeschaltet ist (bulkUpdate=false) und der ModelValidationMode=true ist. Dies ist
   * notwendig, damit der Speichern-Button aktiviert (keine Validierungsfehler vorhanden) bzw.
   * deaktiviert (Validierungsfehler vorhanden) wird.
   *
   * @param evt Event
   */
  @Override
  public final void propertyChange(PropertyChangeEvent evt) {
    doPropertyChange(evt);
    if (!bulkUpdate && isModelValidationMode()) {
      validate();
    }
  }

  /**
   * Prüfung, ob der Property-Name des übergebenen Events dem übergebenen Feld entspricht.
   *
   * @param field Feld
   * @param evt Event mit Property-Name
   * @return true, wenn der Property-Name des Events dem übergebenen Feld entspricht.
   */
  protected boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
    return field.toString().equals(evt.getPropertyName());
  }

  /**
   * Verarbeitung eines vom Model ausgelösten PropertyChanges.
   *
   * <p>Zuerst wird die in der Subklasse überschriebene Methode aufgerufen (durch Polymorphismus).
   * Als Erstes muss die überschriebene Methode der direkten Subklasse super.doPropertyChange(evt)
   * aufrufen und untenstehender Code wird ausgeführt. Wenn der PropertyChange-Event eine Änderung
   * für den bulkUpdate-Modus enthält, wird die Validierung aufgerufen, falls der Event den
   * bulkUpdate-Modus auf false setzt, was heisst, dass die Validierung wieder eingeschaltet wird.
   * <br>
   * Dann wir der PropertyChangeEvent von der Subklasse verarbeitet, das heisst, der Model-Wert wird
   * für das entsprechende Feld gemäss Event in die View übertragen.
   *
   * <p>TODO Verbesserungsvorschlag: Den Methodeninhalt in der Template-Methode als Erstes
   * durchführen, und im else-Fall den Inhalt der Template-Methode durchführen. So müssten die
   * direkten Subklassen super.doPropertyChange(evt) nicht aufrufen.
   *
   * @param evt Event
   */
  void doPropertyChange(PropertyChangeEvent evt) {
    if (checkIsFieldChange(Field.BULK_UPDATE, evt)) {
      bulkUpdate = (boolean) evt.getNewValue();
      if (!bulkUpdate) {
        validate();
      }
    }
  }

  /**
   * Validierung der Felder und der Felder- und Objekt-übergreifenden Regeln ausgelöst durch Drücken
   * des Speichern-Buttons.
   *
   * <p>Validierungsfehler (SvmValidationExceptions) werden gefangen und false als Return-Wert
   * zurückgegeben.
   *
   * @return true, wenn keine Validierungsfehler gefunden wurden, sonst false.
   */
  protected boolean validateOnSpeichern() {
    try {
      validateWithThrowException();
    } catch (SvmValidationException e) {
      LOGGER.trace("AbstractController validateOnSpeichern Exception: {}", e.getMessageLong());
      return false;
    }
    return true;
  }

  /**
   * Validierung der Felder und der Felder- und Objekt-übergreifenden Regeln.
   *
   * <p>Validierungsfehler werden nicht gefangen, sondern weitergeleitet.
   *
   * @throws SvmValidationException bei Validierungsfehler
   */
  protected void validateWithThrowException() throws SvmValidationException {
    validateFields();
    validateModelWithThrowException();
  }

  /**
   * Validierung der Felder und der Felder- und Objekt-übergreifenden Regeln.
   *
   * <p>Diese Methode wird durch den Controller ausgelöst, nachdem das Model-Objekt verändert wurde.
   *
   * <p>Validierungsfehler (SvmValidationExceptions) werden gefangen und nicht weitergeleitet.
   *
   * <p>Validierungsfehler der Felder werden gemäss ModelValidationMode angezeigt (im Error-Label
   * oder als ToolTip). Validierungsfehler der Felder- und Objekt-übergreifenden Regeln werden als
   * ToolTip angezeigt.
   */
  protected void validate() {
    try {
      validateFields();
    } catch (SvmValidationException e) {
      return;
    }
    validateModel();
  }

  /**
   * Validierung der Felder- und Objekt-übergreifenden Regeln.
   *
   * <p>Validierungsfehler (SvmValidationExceptions) werden gefangen und nicht weitergeleitet.
   *
   * <p>Validierungsfehler der Felder- und Objekt-übergreifenden Regeln werden als ToolTip
   * angezeigt.
   */
  private void validateModel() {
    try {
      untypedModel.validate();
    } catch (SvmValidationException e) {
      LOGGER.trace("AbstractController model.validate {}", e.getMessageLong());
      showErrMsgAsToolTip(e);
    }
  }

  /**
   * Validierung der Felder- und Objekt-übergreifenden Regeln.
   *
   * <p>Validierungsfehler (SvmValidationExceptions) werden gefangen und weiter geworfen.
   *
   * <p>Validierungsfehler der Felder- und Objekt-übergreifenden Regeln werden in den Error-Labels
   * angezeigt.
   *
   * @throws SvmValidationException bei Validierungsfehler
   */
  private void validateModelWithThrowException() throws SvmValidationException {
    try {
      untypedModel.validate();
    } catch (SvmValidationException e) {
      LOGGER.trace(
          "AbstractController model.validateModelWithThrowException {}", e.getMessageLong());
      showErrMsg(e);
      throw e;
    }
  }

  /**
   * Validierung der einzelnen Felder in den Subklassen.
   *
   * <p>Die Validierung erfolgt durch Übertragen des View-Wertes in den Model-Wert.
   *
   * <p>Validierungsfehler der Felder werden gemäss ModelValidationMode angezeigt (im Error-Label
   * oder als ToolTip).
   *
   * @throws SvmValidationException bei Validierungsfehler
   */
  abstract void validateFields() throws SvmValidationException;

  /**
   * Anzeigen des Validierungsfehlers in den Error-Labels.
   *
   * @param e der anzuzeigende Validierungsfehler
   */
  abstract void showErrMsg(SvmValidationException e);

  /**
   * Anzeigen des Validierungsfehlers als ToolTip.
   *
   * @param e der anzuzeigende Validierungsfehler
   */
  abstract void showErrMsgAsToolTip(SvmValidationException e);

  /**
   * Utility-Methode zur Entfernung (unsichtbar machen) der Error-Labels.
   *
   * @param field das Feld, dessen Error-Label unsichtbar gemacht werden soll
   */
  protected void makeErrorLabelInvisible(Field field) {
    Set<Field> fields = new HashSet<>();
    fields.add(field);
    makeErrorLabelsInvisible(fields);
  }

  /**
   * TODO Nachdem alle Controller umgestellt sind, kann setModelValidationMode(boolean) entfernt
   * werden. Das Flag wird neu im Konstruktor übergeben.
   */
  public void setModelValidationMode(boolean modelValidationMode) {
    this.modelValidationMode = modelValidationMode;
    untypedModel.setModelValidationMode(modelValidationMode);
  }

  /**
   * TODO Nachdem alle Controller umgestellt sind, kann setUntypedModel(Model) entfernt werden. Das
   * Model wird dann im Konstruktor mit dem parametrisierten Typ übergeben.
   */
  protected void setUntypedModel(Model untypedModel) {
    this.untypedModel = untypedModel;
  }

  /**
   * Generische Setter-Methode zum Übertragen des View-Wertes in den Model-Wert mit Auslösung der
   * Validierung.
   *
   * <p>Diese Methode prüft, ob der View-Wert dem Model-Wert (vor dem Setzen) entspricht. Falls ja,
   * wird im Fall von ModelValidationMode=true eine Validierung ausgelöst. Grund:<br>
   * Wenn View und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss
   * hier die Validierung angestossen werden.<br>
   * Szenario (mit modelValidationMode = true!):<br>
   * - Kursort bearbeiten (z.B. Saal B)<br>
   * - Fehler provozieren (z.B. Bezeichnung statt "Saal B" nur "S". Es gibt einen Fehler wegen min.
   * Länge 2)<br>
   * → der Speichern-Button ist disabled<br>
   * - Fehler entfernen (Wert zurücksetzen: wieder "Saal B" eingeben)<br>
   * → Ohne die Auslösung der Validierung wäre der Speichern-Button disabled!
   *
   * @param modelAndViewAccessor Klasse mit Getter- und Setter-Methoden für die View- bzw.
   *     Model-Werte
   * @param showRequiredErrMsg SvmRequiredExceptions werden als ToolTip angezeigt (true) bzw. in den
   *     Error-Labels, wie alle SvmValidationExceptions (false)
   * @param <T> Typ des Feldes, z.B. String
   * @see #setModelValueFromView(ModelAndViewAccessor, boolean showRequiredErrMsg)
   */
  protected <T> void setModelValueFromViewWithViewValueChangedCheck(
      ModelAndViewAccessor<T> modelAndViewAccessor, boolean showRequiredErrMsg) {
    boolean equalFieldAndModelValue =
        equalsNullSafe(modelAndViewAccessor.getViewValue(), modelAndViewAccessor.getModelValue());
    try {
      setModelValueFromView(modelAndViewAccessor, showRequiredErrMsg);
    } catch (SvmValidationException e) {
      return;
    }
    if (equalFieldAndModelValue && isModelValidationMode()) {
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

  /**
   * Generische Setter-Methode zum Übertragen des View-Wertes in den Model-Wert.
   *
   * @param modelAndViewAccessor Klasse mit Getter- und Setter-Methoden für die View- bzw.
   *     Model-Werte
   * @param showRequiredErrMsg SvmRequiredExceptions werden als ToolTip angezeigt (true) bzw. in den
   *     Error-Labels, wie alle SvmValidationExceptions (false)
   * @param <T> Typ des Feldes, z.B. String
   * @throws SvmValidationException bei Validierungsfehler
   */
  protected <T> void setModelValueFromView(
      ModelAndViewAccessor<T> modelAndViewAccessor, boolean showRequiredErrMsg)
      throws SvmValidationException {
    makeErrorLabelInvisible(modelAndViewAccessor.getField());
    try {
      modelAndViewAccessor.setModelValue(modelAndViewAccessor.getViewValue());
    } catch (SvmRequiredException e) {
      LOGGER.trace("setModelValue RequiredException={}", e.getMessage());
      if (isModelValidationMode() || !showRequiredErrMsg) {
        modelAndViewAccessor.setToolTipText(e.getMessage());
        // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen
        // bestanden sind.
      } else {
        showErrMsg(e);
      }
      throw e;
    } catch (SvmValidationException e) {
      LOGGER.trace("setModelValue Exception={}", e.getMessage());
      showErrMsg(e);
      throw e;
    }
  }
}
