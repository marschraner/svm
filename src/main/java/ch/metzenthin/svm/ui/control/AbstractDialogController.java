package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.CreateOrUpdateModel;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.DisableFieldsListener;
import ch.metzenthin.svm.domain.model.MakeErrorLabelsInvisibleListener;
import ch.metzenthin.svm.service.result.SaveDialogResult;
import ch.metzenthin.svm.ui.components.AbstractDialogView;
import ch.metzenthin.svm.ui.components.SpeichernAbbrechenDialog;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @param <T> Model-Typ, z.B. CreateOrUpdateKursortModel
 * @param <U> View-Typ, z.B. CreateOrUpdateKursortView
 * @param <V> Save-Result-Typ, z.B. SaveKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractDialogController<
        T extends CreateOrUpdateModel<V>,
        U extends AbstractDialogView<? extends SpeichernAbbrechenDialog>,
        V extends SaveDialogResult>
    extends AbstractController
    implements DisableFieldsListener,
        MakeErrorLabelsInvisibleListener,
        CompletedListener,
        DialogClosingListener {

  private static final Logger LOGGER = LogManager.getLogger(AbstractDialogController.class);

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

  protected AbstractDialogController(T model, U view, boolean isBearbeiten) {
    this(model, view, isBearbeiten, false);
  }

  protected AbstractDialogController(
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

  protected void onConstructionFinished() {
    model.initializeCompleted();
  }

  private void configModel() {
    model.addPropertyChangeListener(this);
    model.addDisableFieldsListener(this);
    model.addMakeErrorLabelsInvisibleListener(this);
    model.addCompletedListener(this);
    model.setModelValidationMode(isModelValidationMode());
  }

  public void showDialog() {
    view.showDialog();
  }

  private void configBtnSpeichern() {
    if (isModelValidationMode()) {
      view.setButtonSpeichernDisabled();
    }
    view.addButtonSpeichernActionListener(e -> onSpeichern());
  }

  protected void onSpeichern() {
    if (!isModelValidationMode() && !validateOnSpeichern()) {
      view.setButtonSpeichernFocusPainted(false);
      return;
    }

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
