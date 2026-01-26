package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.DialogClosingListener;
import ch.metzenthin.svm.domain.model.DisableFieldsListener;
import ch.metzenthin.svm.domain.model.MakeErrorLabelsInvisibleListener;
import ch.metzenthin.svm.domain.model.Model;
import ch.metzenthin.svm.ui.components.AbstractDialogView;
import ch.metzenthin.svm.ui.components.SpeichernAbbrechenDialog;
import java.beans.PropertyChangeListener;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("java:S2387")
public abstract class AbstractDialogController<
        T extends Model, U extends AbstractDialogView<? extends SpeichernAbbrechenDialog>>
    extends AbstractController
    implements PropertyChangeListener,
        DisableFieldsListener,
        MakeErrorLabelsInvisibleListener,
        CompletedListener,
        DialogClosingListener {

  @Getter protected T model;

  @Getter protected U view;

  /**
   * modelValidationMode true: SvmRequiredExceptions werden nicht markiert (nur als Tooltip). Model
   * wird invalidiert bei Fehler<br>
   * modelValidationMode false: SvmRequiredExceptions werden sofort markiert (in Error labels).
   * Model wird nicht invalidiert bei Fehler
   */
  @Getter private final boolean modelValidationMode;

  protected AbstractDialogController(T model, U view) {
    this(model, view, false);
  }

  protected AbstractDialogController(T model, U view, boolean modelValidationMode) {
    super(model);
    this.model = model;
    this.view = view;
    this.modelValidationMode = modelValidationMode;
    view.configDialogClosing(this);
    configModel();
  }

  private void configModel() {
    model.addPropertyChangeListener(this);
    model.addDisableFieldsListener(this);
    model.addMakeErrorLabelsInvisibleListener(this);
    model.addCompletedListener(this);
    model.setModelValidationMode(isModelValidationMode());
    model.initializeCompleted();
  }

  public void showDialog() {
    view.showDialog();
  }

  @Override
  public void onCloseDialog() {
    closeDialog();
  }

  protected void closeDialog() {
    view.closeDialog();
  }
}
