package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.SemesterrechnungCodeListModel;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import java.awt.event.ActionListener;

/**
 * @author Hans Stamm
 */
public class SemesterrechnungCodeListController extends CodeListController<SemesterrechnungCode> {

  public SemesterrechnungCodeListController(
      SvmContext svmContext,
      SemesterrechnungCodeListModel semesterrechnungCodeListModel,
      String panelTitle,
      ActionListener closeListener) {
    super(svmContext, semesterrechnungCodeListModel, panelTitle, closeListener);
  }

  @Override
  protected String getNeuDialogTitle() {
    return "Neuer Semesterrechnung-Code";
  }

  @Override
  protected String getBearbeitenDialogTitle() {
    return "Semesterrechnung-Code bearbeiten";
  }
}
