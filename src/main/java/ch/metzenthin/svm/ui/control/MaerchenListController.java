package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateMaerchenModel;
import ch.metzenthin.svm.domain.model.MaerchenAndNumberOfMaercheneinteilungen;
import ch.metzenthin.svm.domain.model.MaerchenListModel;
import ch.metzenthin.svm.domain.model.MaerchenTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.service.result.DeleteMaerchenResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.MaerchenListView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class MaerchenListController
    extends AbstractListController<
        MaerchenListModel,
        MaerchenListView,
        MaerchenTableData,
        MaerchenAndNumberOfMaercheneinteilungen,
        DeleteMaerchenResult> {

  public MaerchenListController(
      SvmContext svmContext, MaerchenListModel maerchenListModel, ActionListener closeListener) {
    super(
        svmContext,
        maerchenListModel,
        createView(maerchenListModel.getTableModel(), closeListener));
  }

  private static MaerchenListView createView(
      TableModel<MaerchenTableData, MaerchenAndNumberOfMaercheneinteilungen> tableModel,
      ActionListener closeListener) {
    return new MaerchenListView(tableModel, closeListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateMaerchenModel createOrUpdateMaerchenModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateMaerchenController createOrUpdateMaerchenController =
        new CreateOrUpdateMaerchenController(createOrUpdateMaerchenModel, "Neues Märchen");
    createOrUpdateMaerchenController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndMaerchenIdOptional =
        createOrUpdateMaerchenModel.getSaveSuccessfulAndMaerchenIdOptional();
    Optional<Integer> maerchenIdOptional =
        saveOrUpdateSuccessfulAndMaerchenIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndMaerchenIdOptional.saveSuccessful()
        && maerchenIdOptional.isPresent()) {
      insertCreatedObjectToList(maerchenIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateMaerchenModel createOrUpdateMaerchenModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateMaerchenController createOrUpdateMaerchenController =
        new CreateOrUpdateMaerchenController(createOrUpdateMaerchenModel, "Märchen bearbeiten");
    createOrUpdateMaerchenController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndMaerchenIdOptional =
        createOrUpdateMaerchenModel.getSaveSuccessfulAndMaerchenIdOptional();
    if (saveOrUpdateSuccessfulAndMaerchenIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndMaerchenIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }
}
