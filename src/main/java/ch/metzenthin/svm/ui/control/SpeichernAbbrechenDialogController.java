package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.CreateOrUpdateModel;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.DisableFieldsListener;
import ch.metzenthin.svm.domain.model.MakeErrorLabelsInvisibleListener;
import ch.metzenthin.svm.service.result.SaveDialogResult;
import ch.metzenthin.svm.ui.components.SpeichernAbbrechenDialog;
import ch.metzenthin.svm.ui.view.SpeichernAbbrechenDialogView;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @param <T> Model-Typ, z.B. CreateOrUpdateKursortModel
 * @param <U> View-Typ, z.B. CreateOrUpdateKursortView
 * @param <V> Save-Result-Typ, z.B. SaveKursortResult
 * @author Hans Stamm
 */
public abstract class SpeichernAbbrechenDialogController<
        T extends CreateOrUpdateModel<V>,
        U extends SpeichernAbbrechenDialogView<? extends SpeichernAbbrechenDialog>,
        V extends SaveDialogResult>
    extends AbstractController
    implements DisableFieldsListener,
        MakeErrorLabelsInvisibleListener,
        CompletedListener,
        DialogClosingListener {

  private static final Logger LOGGER =
      LogManager.getLogger(SpeichernAbbrechenDialogController.class);

  protected final T model;

  protected final U view;

  protected boolean isBearbeiten;

  /**
   * modelValidationMode true: SvmRequiredExceptions werden nicht markiert (nur als Tooltip). Model
   * wird invalidiert bei Fehler<br>
   * modelValidationMode false: SvmRequiredExceptions werden sofort markiert (in Error labels).
   * Model wird nicht invalidiert bei Fehler
   */
  @Getter private final boolean modelValidationMode;

  protected SpeichernAbbrechenDialogController(T model, U view, boolean isBearbeiten) {
    this(model, view, isBearbeiten, false);
  }

  protected SpeichernAbbrechenDialogController(
      T model, U view, boolean isBearbeiten, boolean modelValidationMode) {
    super(model);
    this.model = model;
    this.view = view;
    this.isBearbeiten = isBearbeiten;
    this.modelValidationMode = modelValidationMode;
    view.configDialogClosing(this);
    configModel();
    configBtnSpeichern();
    configBtnAbbrechen();
  }

  /**
   * Aktionen, nachdem die Konstruktion des Controllers beendet ist.
   *
   * <p>Zuerst wird das Model informiert, dass die Initialisierung des Controllers erfolgt ist.
   * Dadurch wird das Model initialisiert.
   *
   * <p>Bei einem neuen Objekt (!isBearbeiten) und im ModelValidationMode ist danach die
   * Durchführung der Validierung nötig, insbesondere dann, wenn keine Initialwerte auf dem neuen
   * Objekt gesetzt werden. Sind Validierungsfehler vorhanden, wird so der Speichern-Button
   * deaktiviert.
   *
   * <p>Bei einem neuen Objekt können von den Subklassen noch die Initialwerte gesetzt werden.
   */
  protected void initialiseModelAndViewFields() {
    model.initializeCompleted();
    if (!isBearbeiten && isModelValidationMode()) {
      validate();
    }
    if (!isBearbeiten) {
      setDefaultValuesOnNeu();
    }
  }

  /** Möglichkeit für Subklassen, das Model und die View mit Defaultwerten zu initialisieren. */
  protected abstract void setDefaultValuesOnNeu();

  private void configModel() {
    model.addPropertyChangeListener(this);
    model.addDisableFieldsListener(this);
    model.addMakeErrorLabelsInvisibleListener(this);
    model.addCompletedListener(this);
    model.setModelValidationMode(isModelValidationMode());
  }

  public void initialiseModelAndViewFieldsAndShowDialog() {
    initialiseModelAndViewFields();
    view.showDialog();
  }

  private void configBtnSpeichern() {
    if (isModelValidationMode()) {
      view.setButtonSpeichernDisabled();
    }
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  private void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      view.setButtonSpeichernFocusPainted(false);
      return;
    }

    speichern();
  }

  protected void speichern() {
    V saveDialogResult = model.speichern();
    if (saveDialogResult.isErrorMessage()) {
      view.showErrorMessageDialog(saveDialogResult.getMessage(), "Fehler");
      if (saveDialogResult.isCloseDialog()) {
        closeDialog();
      } else {
        view.setButtonSpeichernFocusPainted(false);
      }
    } else {
      closeDialog();
    }
  }

  private void configBtnAbbrechen() {
    view.addButtonAbbrechenActionListener(e -> onAbbrechen());
  }

  private void onAbbrechen() {
    closeDialog();
  }

  @Override
  public void onCloseDialog() {
    closeDialog();
  }

  protected void closeDialog() {
    view.closeDialog();
  }

  @Override
  public void completed(boolean completed) {
    LOGGER.trace("Model completed={}", completed);
    if (completed) {
      view.setButtonSpeichernEnabled();
    } else {
      view.setButtonSpeichernDisabled("Bitte Eingabedaten vervollständigen");
    }
  }
}
