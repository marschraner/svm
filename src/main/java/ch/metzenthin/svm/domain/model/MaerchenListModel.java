package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.service.MaerchenService;
import ch.metzenthin.svm.service.result.DeleteMaerchenResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class MaerchenListModel
    extends AbstractListModel<
        MaerchenTableData,
        MaerchenAndNumberOfMaercheneinteilungen,
        CreateOrUpdateMaerchenModel,
        DeleteMaerchenResult> {

  private final MaerchenService maerchenService;

  public MaerchenListModel(MaerchenService maerchenService) {
    super(createTableModel(maerchenService));
    this.maerchenService = maerchenService;
  }

  private static TableModel<MaerchenTableData, MaerchenAndNumberOfMaercheneinteilungen>
      createTableModel(MaerchenService maerchenService) {
    List<MaerchenAndNumberOfMaercheneinteilungen> maerchenAndNumberOfMaercheneinteilungen =
        maerchenService.findAllMaerchenAndNumberOfMaercheneinteilungen();
    MaerchenTableData maerchenTableData =
        new MaerchenTableData(maerchenAndNumberOfMaercheneinteilungen);
    return new TableModel<>(maerchenTableData, 0.15, 0.55, 0.15, 0.15);
  }

  @Override
  public DeleteMaerchenResult eintragLoeschen(int indexOfMaerchenToBeDeleted) {
    Maerchen maerchenToBeDeleted = getSelectedRow(indexOfMaerchenToBeDeleted).maerchen();
    DeleteMaerchenResult deleteMaerchenResult;
    try {
      maerchenService.deleteMaerchen(maerchenToBeDeleted);
      deleteMaerchenResult = DeleteMaerchenResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteMaerchenResult = DeleteMaerchenResult.MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteMaerchenResult = DeleteMaerchenResult.MAERCHEN_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteMaerchenResult;
  }

  @Override
  public CreateOrUpdateMaerchenModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateMaerchenModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateMaerchenModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexMaerchenToBeUpdated) {
    Maerchen maerchenToBeUpdated = getSelectedRow(indexMaerchenToBeUpdated).maerchen();
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateMaerchenModel(Optional.of(maerchenToBeUpdated));
  }

  @Override
  public void reloadData() {
    List<MaerchenAndNumberOfMaercheneinteilungen> maerchenAndNumberOfMaercheneinteilungenList =
        maerchenService.findAllMaerchenAndNumberOfMaercheneinteilungen();
    tableModel.setData(maerchenAndNumberOfMaercheneinteilungenList);
  }

  @Override
  public String getListItemName() {
    return "Maerchen";
  }
}
