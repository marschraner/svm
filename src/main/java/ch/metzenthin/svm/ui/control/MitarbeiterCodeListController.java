package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.MitarbeiterCodeListModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class MitarbeiterCodeListController extends CodeListController<MitarbeiterCode> {

  public MitarbeiterCodeListController(
      SvmContext svmContext,
      MitarbeiterCodeListModel mitarbeiterCodeListModel,
      String panelTitle,
      ActionListener closeListener) {
    super(svmContext, mitarbeiterCodeListModel, panelTitle, closeListener);
  }

  @Override
  protected String getNeuDialogTitle() {
    return "Neuer Mitarbeiter-Code";
  }

  @Override
  protected String getBearbeitenDialogTitle() {
    return "Mitarbeiter-Code bearbeiten";
  }
}
