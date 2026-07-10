package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.domain.model.KurstypListModel;
import ch.metzenthin.svm.domain.model.KurstypTableData;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.KurstypListView;
import java.awt.event.ActionListener;

/**
 * @author Martin Schraner
 */
public class KurstypListController
    extends AbstractListController<KurstypListModel, DeleteKurstypResult, KurstypListView> {

  public KurstypListController(
      SvmContext svmContext, KurstypListModel kurstypListModel, ActionListener closeListener) {
    super(
        svmContext, kurstypListModel, createView(kurstypListModel.getTableModel(), closeListener));
  }

  private static KurstypListView createView(
      TableModel<KurstypTableData, Kurstyp> tableModel, ActionListener closeListener) {
    return new KurstypListView(tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateKurstypModel createOrUpdateKurstypModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateKurstypController createOrUpdateKurstypController =
        new CreateOrUpdateKurstypController(createOrUpdateKurstypModel, "Neuer Kurstyp");
    createOrUpdateKurstypController.showDialog();
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateKurstypModel createOrUpdateKurstypModel =
        model.createCreateOrUpdateModel(svmContext, view.getSelectedRow());
    CreateOrUpdateKurstypController createOrUpdateKurstypController =
        new CreateOrUpdateKurstypController(createOrUpdateKurstypModel, "Kurstyp bearbeiten");
    createOrUpdateKurstypController.showDialog();
  }
}
