package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKurstypModel;
import ch.metzenthin.svm.domain.model.KurstypListModel;
import ch.metzenthin.svm.domain.model.KurstypTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.KurstypListView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class KurstypListController
    extends AbstractListController<
        KurstypListModel, KurstypListView, KurstypTableData, Kurstyp, DeleteKurstypResult> {

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
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndKurstypIdOptional =
        createOrUpdateKurstypModel.getSaveSuccessfulAndKurstypIdOptional();
    Optional<Integer> kurstypIdOptional =
        saveOrUpdateSuccessfulAndKurstypIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndKurstypIdOptional.saveSuccessful()
        && kurstypIdOptional.isPresent()) {
      insertCreatedObjectToList(kurstypIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateKurstypModel createOrUpdateKurstypModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateKurstypController createOrUpdateKurstypController =
        new CreateOrUpdateKurstypController(createOrUpdateKurstypModel, "Kurstyp bearbeiten");
    createOrUpdateKurstypController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndKurstypIdOptional =
        createOrUpdateKurstypModel.getSaveSuccessfulAndKurstypIdOptional();
    if (saveOrUpdateSuccessfulAndKurstypIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndKurstypIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }
}
