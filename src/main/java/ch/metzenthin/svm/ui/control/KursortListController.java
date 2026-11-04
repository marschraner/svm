package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.CreateOrUpdateKursortModel;
import ch.metzenthin.svm.domain.model.KursortListModel;
import ch.metzenthin.svm.domain.model.KursortTableData;
import ch.metzenthin.svm.domain.model.SaveSuccessfulAndEntityIdOptional;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import ch.metzenthin.svm.ui.view.KursortListView;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class KursortListController
    extends AbstractListController<KursortListModel, KursortListView, DeleteKursortResult> {

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
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, "Neuer Kursort");
    createOrUpdateKursortController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndKursortIdOptional =
        createOrUpdateKursortModel.getSaveSuccessfulAndKursortIdOptional();
    Optional<Integer> kursortIdOptional =
        saveOrUpdateSuccessfulAndKursortIdOptional.entityIdOptional();
    if (saveOrUpdateSuccessfulAndKursortIdOptional.saveSuccessful()
        && kursortIdOptional.isPresent()) {
      insertCreatedObjectToList(kursortIdOptional.get());
    }
  }

  @Override
  protected void showOnBearbeitenDialog() {
    CreateOrUpdateKursortModel createOrUpdateKursortModel =
        model.createCreateOrUpdateModel(svmContext, view.convertRowIndexToModel());
    CreateOrUpdateKursortController createOrUpdateKursortController =
        new CreateOrUpdateKursortController(createOrUpdateKursortModel, "Kursort bearbeiten");
    createOrUpdateKursortController.showDialog();
    SaveSuccessfulAndEntityIdOptional saveOrUpdateSuccessfulAndKursortIdOptional =
        createOrUpdateKursortModel.getSaveSuccessfulAndKursortIdOptional();
    if (saveOrUpdateSuccessfulAndKursortIdOptional.saveSuccessful()
        && saveOrUpdateSuccessfulAndKursortIdOptional.entityIdOptional().isPresent()) {
      reloadSelectedRow();
    }
  }
}
