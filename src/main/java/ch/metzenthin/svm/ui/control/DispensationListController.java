package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateDispensationModel;
import ch.metzenthin.svm.domain.model.DispensationListModel;
import ch.metzenthin.svm.domain.model.DispensationTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.listener.DispensationenZurueckListener;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.service.result.DeleteDispensationResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.DispensationListView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("DuplicatedCode")
public class DispensationListController
    extends AbstractListController<
        DispensationListModel, DispensationListView, DeleteDispensationResult> {

  public DispensationListController(
      SvmContext svmContext,
      DispensationListModel dispensationListModel,
      String title,
      DispensationenZurueckListener zurueckListener) {
    super(
        svmContext,
        dispensationListModel,
        createView(dispensationListModel.getTableModel(), title, zurueckListener));
  }

  private static DispensationListView createView(
      TableModel<DispensationTableData, Dispensation> tableModel,
      String title,
      ActionListener zurueckListener) {
    return new DispensationListView(tableModel, title, zurueckListener);
  }

  @Override
  protected void showOnNeuDialog() {
    CreateOrUpdateDispensationModel createOrUpdateDispensationModel =
        model.createCreateOrUpdateModel(svmContext);
    CreateOrUpdateDispensationController createOrUpdateDispensationController =
        new CreateOrUpdateDispensationController(
            createOrUpdateDispensationModel, "Neue Dispensation");
    createOrUpdateDispensationController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndDispensationIdOptional =
        createOrUpdateDispensationModel.getSaveSuccessfulAndDispensationIdOptional();
    Optional<Integer> dispensationIdOptional =
        saveOrUpdateSuccessfulAndDispensationIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndDispensationIdOptional.saveSuccessful()
        && dispensationIdOptional.isPresent()) {
      insertCreatedObjectToList(dispensationIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateDispensationModel createOrUpdateDispensationModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateDispensationController createOrUpdateDispensationController =
        new CreateOrUpdateDispensationController(
            createOrUpdateDispensationModel, "Dispensation bearbeiten");
    createOrUpdateDispensationController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndDispensationIdOptional =
        createOrUpdateDispensationModel.getSaveSuccessfulAndDispensationIdOptional();
    if (saveOrUpdateSuccessfulAndDispensationIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndDispensationIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }
}
