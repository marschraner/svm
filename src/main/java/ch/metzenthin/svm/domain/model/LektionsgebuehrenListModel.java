package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenListModel
    extends AbstractListModel<
        LektionsgebuehrenTableData,
        Lektionsgebuehren,
        CreateOrUpdateLektionsgebuehrenModel,
        DeleteLektionsgebuehrenResult> {

  private final LektionsgebuehrenService lektionsgebuehrenService;

  public LektionsgebuehrenListModel(LektionsgebuehrenService lektionsgebuehrenService) {
    super(createTableModel(lektionsgebuehrenService));
    this.lektionsgebuehrenService = lektionsgebuehrenService;
  }

  private static TableModel<LektionsgebuehrenTableData, Lektionsgebuehren> createTableModel(
      LektionsgebuehrenService lektionsgebuehrenService) {
    List<Lektionsgebuehren> lektionsgebuehrenList =
        lektionsgebuehrenService.findAllLektionsgebuehren();
    LektionsgebuehrenTableData lektionsgebuehrenTableData =
        new LektionsgebuehrenTableData(lektionsgebuehrenList);
    return new TableModel<>(lektionsgebuehrenTableData);
  }

  @Override
  public DeleteLektionsgebuehrenResult eintragLoeschen(int indexOfLektionsgebuehrenToBeDeleted) {
    Lektionsgebuehren lektionsgebuehrenToBeDeleted =
        getSelectedRow(indexOfLektionsgebuehrenToBeDeleted);
    DeleteLektionsgebuehrenResult deleteLektionsgebuehrenResult;
    try {
      lektionsgebuehrenService.deleteLektionsgebuehren(lektionsgebuehrenToBeDeleted);
      deleteLektionsgebuehrenResult = DeleteLektionsgebuehrenResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteLektionsgebuehrenResult =
          DeleteLektionsgebuehrenResult.LEKTIONSGEBUEHREN_VON_KURS_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteLektionsgebuehrenResult =
          DeleteLektionsgebuehrenResult.LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteLektionsgebuehrenResult;
  }

  @Override
  public CreateOrUpdateLektionsgebuehrenModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateLektionsgebuehrenModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateLektionsgebuehrenModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexOfLektionsgebuehrenToBeUpdated) {
    Lektionsgebuehren lektionsgebuehrenToBeUpdated =
        getSelectedRow(indexOfLektionsgebuehrenToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateLektionsgebuehrenModel(Optional.of(lektionsgebuehrenToBeUpdated));
  }

  @Override
  public void reloadData() {
    List<Lektionsgebuehren> lektionsgebuehrenList =
        lektionsgebuehrenService.findAllLektionsgebuehren();
    tableModel.setData(lektionsgebuehrenList);
  }

  @Override
  public String getListItemName() {
    return "Lektionsgebühren";
  }
}
