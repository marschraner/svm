package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateLektionsgebuehrenModel;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenListModel;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.LektionsgebuehrenListView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenListController
    extends AbstractListController<
        LektionsgebuehrenListModel, LektionsgebuehrenListView, DeleteLektionsgebuehrenResult> {

  public LektionsgebuehrenListController(
      SvmContext svmContext,
      LektionsgebuehrenListModel lektionsgebuehrenListModel,
      ActionListener closeListener) {
    super(
        svmContext,
        lektionsgebuehrenListModel,
        createView(lektionsgebuehrenListModel.getTableModel(), closeListener));
  }

  private static LektionsgebuehrenListView createView(
      TableModel<LektionsgebuehrenTableData, Lektionsgebuehren> tableModel,
      ActionListener closeListener) {
    return new LektionsgebuehrenListView(tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateLektionsgebuehrenModel createOrUpdateLektionsgebuehrenModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateLektionsgebuehrenController createOrUpdateLektionsgebuehrenController =
        new CreateOrUpdateLektionsgebuehrenController(
            createOrUpdateLektionsgebuehrenModel, "Neue Lektionsgebühren");
    createOrUpdateLektionsgebuehrenController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional =
        createOrUpdateLektionsgebuehrenModel.getSaveSuccessfulAndLektionsgebuehrenIdOptional();
    Optional<Integer> lektionsgebuehrenIdOptional =
        saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional.saveSuccessful()
        && lektionsgebuehrenIdOptional.isPresent()) {
      insertCreatedObjectToList(lektionsgebuehrenIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateLektionsgebuehrenModel createOrUpdateLektionsgebuehrenModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateLektionsgebuehrenController createOrUpdateLektionsgebuehrenController =
        new CreateOrUpdateLektionsgebuehrenController(
            createOrUpdateLektionsgebuehrenModel, "Lektionsgebühren bearbeiten");
    createOrUpdateLektionsgebuehrenController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional =
        createOrUpdateLektionsgebuehrenModel.getSaveSuccessfulAndLektionsgebuehrenIdOptional();
    if (saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndLektionsgebuehrenIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }
}
