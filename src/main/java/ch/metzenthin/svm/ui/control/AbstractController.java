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
 * @author Hans Stamm
 */
public abstract class AbstractController
    implements PropertyChangeListener, DisableFieldsListener, MakeErrorLabelsInvisibleListener {

  private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

  private Model untypedModel;
  private boolean bulkUpdate = false;

  /**
   * modelValidationMode true: SvmRequiredExceptions werden nicht markiert (nur als Tooltip). Model
   * wird invalidiert bei Fehler<br>
   * modelValidationMode false: SvmRequiredExceptions werden sofort markiert (in Error labels).
   * Model wird nicht invalidiert bei Fehler
   */
  @Getter private boolean modelValidationMode = false;

  protected AbstractController(Model untypedModel) {
    this.untypedModel = untypedModel;
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
   * Template Methode. Nachdem die Subklassen den PropertyChangeEvent verarbeitet haben, wird die
   * Validierung aufgerufen.
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

  public boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
    return field.toString().equals(evt.getPropertyName());
  }

  /**
   * Subklassen verarbeiten den PropertyChangeEvent und danach wird die Validierung aufgerufen.
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

  public boolean validateOnSpeichern() {
    try {
      validateWithThrowException();
    } catch (SvmValidationException e) {
      LOGGER.trace("AbstractController validateOnSpeichern Exception: {}", e.getMessageLong());
      return false;
    }
    return true;
  }

  public void validateWithThrowException() throws SvmValidationException {
    validateFields();
    validateModelWithThrowException();
  }

  protected void validate() {
    try {
      validateFields();
    } catch (SvmValidationException e) {
      return;
    }
    validateModel();
  }

  private void validateModel() {
    try {
      untypedModel.validate();
    } catch (SvmValidationException e) {
      LOGGER.trace("AbstractController model.validate {}", e.getMessageLong());
      showErrMsgAsToolTip(e);
    }
  }

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

  abstract void validateFields() throws SvmValidationException;

  abstract void showErrMsg(SvmValidationException e);

  abstract void showErrMsgAsToolTip(SvmValidationException e);

  public void makeErrorLabelInvisible(Field field) {
    Set<Field> fields = new HashSet<>();
    fields.add(field);
    makeErrorLabelsInvisible(fields);
  }

  public void setModelValidationMode(boolean modelValidationMode) {
    this.modelValidationMode = modelValidationMode;
    untypedModel.setModelValidationMode(modelValidationMode);
  }

  protected void setUntypedModel(Model untypedModel) {
    this.untypedModel = untypedModel;
  }

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
      // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb
      // muss hier die Validierung angestossen werden.
      // Szenario (mit modelValidationMode = true!):
      // - Kursort bearbeiten (z.B. Saal B)
      // - Fehler provozieren (z.B. Bezeichnung statt "Saal B" nur "S". Es gibt einen Fehler wegen
      // min. Länge 2)
      // → der Speichern-Button ist disabled
      // - Fehler entfernen (Wert zurücksetzen: wieder "Saal B" eingeben)
      // → Ohne dieses if-Statement bleibt der Speichern-Button disabled!
      LOGGER.trace("Validierung wegen equalFieldAndModelValue");
      validate();
    }
  }

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
