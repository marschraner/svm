package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CodeTableData;
import ch.metzenthin.svm.domain.model.CreateCodeAssignmentModel;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.domain.model.SchuelerCodeAssignmentListModel;
import ch.metzenthin.svm.listener.SchuelerCodesZurueckListener;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.result.DeleteAssignmentResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.CodeAssignmentListPanelView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public class SchuelerCodeAssignmentListController
    extends AbstractCreateOrDeleteListController<
        SchuelerCodeAssignmentListModel, CodeAssignmentListPanelView, DeleteAssignmentResult> {

  protected SchuelerCodeAssignmentListController(
      SvmContext svmContext,
      SchuelerCodeAssignmentListModel schuelerCodeAssignmentListModel,
      String title,
      SchuelerCodesZurueckListener schuelerCodesZurueckListener) {
    super(
        svmContext,
        schuelerCodeAssignmentListModel,
        createView(
            schuelerCodeAssignmentListModel.getTableModel(),
            title,
            e -> schuelerCodesZurueckListener.onSchuelerCodesZurueck()));
  }

  private static <T extends Code> CodeAssignmentListPanelView createView(
      TableModel<CodeTableData<T>, T> tableModel, String title, ActionListener zurueckListener) {
    return new CodeAssignmentListPanelView(tableModel, title, zurueckListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateCodeAssignmentModel<SchuelerCode> createSchuelerCodeAssignmentModel =
        model.createCreateModel(svmContext);
    CreateSchuelerCodeAssignmentController createSchuelerCodeAssignmentController =
        new CreateSchuelerCodeAssignmentController(
            createSchuelerCodeAssignmentModel, "Neuer Schüler-Code");
    createSchuelerCodeAssignmentController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveSuccessfulAndCodeIdOptional =
        createSchuelerCodeAssignmentModel.getSaveSuccessfulAndCodeIdOptional();
    Optional<Integer> codeIdOptional = saveSuccessfulAndCodeIdOptional.entityIdOptional();
    if (saveSuccessfulAndCodeIdOptional.saveSuccessful() && codeIdOptional.isPresent()) {
      insertCreatedObjectToList(codeIdOptional.get());
    }
  }
}
