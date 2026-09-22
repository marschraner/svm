package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractAddOrRemoveCodeAssignmentListModel;
import ch.metzenthin.svm.domain.model.CodeSelectionModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.view.CodeSelectionView;

/**
 * @author Hans Stamm
 */
public class AddOrRemoveMitarbeiterCodeAssignmentListController
    extends AbstractAddOrRemoveCodeAssignmentListController<MitarbeiterCode> {

  protected AddOrRemoveMitarbeiterCodeAssignmentListController(
      SvmContext svmContext,
      AbstractAddOrRemoveCodeAssignmentListModel<MitarbeiterCode> model,
      String title) {
    super(svmContext, model, title);
  }

  @Override
  protected void showOnNeuDialog() {
    CodeSelectionModel<MitarbeiterCode> codeSelectionModel = model.createCodeSelectionModel();
    CodeSelectionController<MitarbeiterCode> codeSelectionController =
        new CodeSelectionController<>(
            codeSelectionModel,
            new CodeSelectionView<>("Mitarbeiter-Code hinzufügen"),
            model.getAssignedCodesAsSortedList());
    codeSelectionController.showDialog();
    if (codeSelectionModel.getSelectedCode() != null) {
      model.addCode(codeSelectionModel.getSelectedCode());
      view.fireTableDataChanged();
    }
  }
}
