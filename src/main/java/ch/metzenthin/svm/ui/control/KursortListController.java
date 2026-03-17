package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.KursortListModel;
import ch.metzenthin.svm.domain.model.KursortTableData;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.KursortListView;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class KursortListController
    extends AbstractListController<KursortListModel, DeleteKursortResult> {

  public KursortListController(
      SvmContext svmContext, KursortListModel kursortListModel, ActionListener closeListener) {
    super(
        svmContext, kursortListModel, createView(kursortListModel.getTableModel(), closeListener));
  }

  private static KursortListView createView(
      TableModel<KursortTableData, Kursort> tableModel, ActionListener closeListener) {
    return new KursortListView(tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateKursortModel createOrUpdateKursortModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateKursortController createOrUpdateKursortController =
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, false, "Neuer Kursort");
    createOrUpdateKursortController.initialiseModelAndViewFieldsAndShowDialog();
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateKursortModel createOrUpdateKursortModel =
        model.createCreateOrUpdateModel(svmContext, view.getSelectedRow());
    CreateOrUpdateKursortController createOrUpdateKursortController =
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, true, "Kursort bearbeiten");
    createOrUpdateKursortController.initialiseModelAndViewFieldsAndShowDialog();
  }
}
