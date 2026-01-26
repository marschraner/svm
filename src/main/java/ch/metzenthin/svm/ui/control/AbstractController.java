package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
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
}
