package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class KursortListModel
    extends AbstractListModel<
        KursortTableData, Kursort, CreateOrUpdateKursortModel, DeleteKursortResult> {

  private final KursortService kursortService;

  public KursortListModel(KursortService kursortService) {
    super(createTableModel(kursortService));
    this.kursortService = kursortService;
  }

  private static TableModel<KursortTableData, Kursort> createTableModel(
      KursortService kursortService) {
    List<Kursort> kursorte = kursortService.findAllKursorte();
    KursortTableData kursortTableData = new KursortTableData(kursorte);
    return new TableModel<>(kursortTableData, 0.75, 0.25);
  }

  @Override
  public DeleteKursortResult eintragLoeschen(int indexKursortToBeDeleted) {
    Kursort kursortToBeDeleted = getSelectedRow(indexKursortToBeDeleted);
    DeleteKursortResult deleteKursortResult;
    try {
      kursortService.deleteKursort(kursortToBeDeleted);
      deleteKursortResult = DeleteKursortResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteKursortResult = DeleteKursortResult.KURSORT_VON_KURS_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteKursortResult = DeleteKursortResult.KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteKursortResult;
  }

  @Override
  public CreateOrUpdateKursortModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateKursortModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateKursortModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexKursortToBeUpdated) {
    Kursort kursortToBeUpdated = getSelectedRow(indexKursortToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateKursortModel(Optional.of(kursortToBeUpdated));
  }

  @Override
  public void reloadData() {
    List<Kursort> kursorte = kursortService.findAllKursorte();
    tableModel.setData(kursorte);
  }

  @Override
  public String getListItemName() {
    return "Kursort";
  }
}
