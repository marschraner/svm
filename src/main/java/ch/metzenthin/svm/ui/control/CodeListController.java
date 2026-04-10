package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CodeListModel;
import ch.metzenthin.svm.domain.model.CodeTableData;
import ch.metzenthin.svm.domain.model.CreateOrUpdateCodeModel;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.CodeListView;
import java.awt.event.*;

/**
 * @param <T> Code-Typ, z.B. SchuelerCode
 * @author Hans Stamm
 */
public abstract class CodeListController<T extends Code>
    extends AbstractListController<CodeListModel<T>, DeleteCodeResult> {

  protected CodeListController(
      SvmContext svmContext,
      CodeListModel<T> codeListModel,
      String panelTitle,
      ActionListener closeListener) {
    super(
        svmContext,
        codeListModel,
        createView(codeListModel.getTableModel(), panelTitle, closeListener));
  }

  private static <T extends Code> CodeListView createView(
      TableModel<CodeTableData<T>, T> tableModel, String panelTitle, ActionListener closeListener) {
    return new CodeListView(tableModel, panelTitle, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateCodeModel createOrUpdateCodeModel = model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateCodeController createOrUpdateCodeController =
        new CreateOrUpdateCodeController(createOrUpdateCodeModel, false, getNeuDialogTitle());
    createOrUpdateCodeController.initialiseModelValuesAndViewFieldsAndShowDialog();
  }

  protected abstract String getNeuDialogTitle();

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateCodeModel createOrUpdateCodeModel =
        model.createCreateOrUpdateModel(svmContext, view.getSelectedRow());
    CreateOrUpdateCodeController createOrUpdateCodeController =
        new CreateOrUpdateCodeController(createOrUpdateCodeModel, true, getBearbeitenDialogTitle());
    createOrUpdateCodeController.initialiseModelValuesAndViewFieldsAndShowDialog();
  }

  protected abstract String getBearbeitenDialogTitle();
}
