package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.SchuelerCodeListModel;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class SchuelerCodeListController extends CodeListController<SchuelerCode> {

  public SchuelerCodeListController(
      SvmContext svmContext,
      SchuelerCodeListModel schuelerCodeListModel,
      String panelTitle,
      ActionListener closeListener) {
    super(svmContext, schuelerCodeListModel, panelTitle, closeListener);
  }

  @Override
  protected String getNeuDialogTitle() {
    return "Neuer Schüler-Code";
  }

  @Override
  protected String getBearbeitenDialogTitle() {
    return "Schüler-Code bearbeiten";
  }
}
