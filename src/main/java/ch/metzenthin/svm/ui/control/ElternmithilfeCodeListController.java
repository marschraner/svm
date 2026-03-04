package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.ElternmithilfeCodeListModel;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class ElternmithilfeCodeListController extends CodeListController<ElternmithilfeCode> {

  public ElternmithilfeCodeListController(
      SvmContext svmContext,
      ElternmithilfeCodeListModel elternmithilfeCodeListModel,
      String panelTitle,
      ActionListener closeListener) {
    super(svmContext, elternmithilfeCodeListModel, panelTitle, closeListener);
  }

  @Override
  protected String getNeuDialogTitle() {
    return "Neuer Eltern-Mithilfe-Code";
  }

  @Override
  protected String getBearbeitenDialogTitle() {
    return "Eltern-Mithilfe-Code bearbeiten";
  }
}
