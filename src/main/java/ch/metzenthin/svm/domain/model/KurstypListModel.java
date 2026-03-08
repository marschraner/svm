package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class KurstypListModel
    extends AbstractListModel<
        KurstypTableData, Kurstyp, CreateOrUpdateKurstypModel, DeleteKurstypResult> {

  private final KurstypService kurstypService;

  public KurstypListModel(KurstypService kurstypService) {
    super(createTableModel(kurstypService));
    this.kurstypService = kurstypService;
  }

  private static TableModel<KurstypTableData, Kurstyp> createTableModel(
      KurstypService kurstypService) {
    List<Kurstyp> kurstypen = kurstypService.findAllKurstypen();
    KurstypTableData kurstypTableData = new KurstypTableData(kurstypen);
    return new TableModel<>(kurstypTableData, 0.75, 0.25);
  }

  @Override
  public DeleteKurstypResult eintragLoeschen(int indexOfKurstypToBeDeleted) {
    Kurstyp kurstypToBeDeleted = getSelectedRow(indexOfKurstypToBeDeleted);
    DeleteKurstypResult deleteKurstypResult;
    try {
      kurstypService.deleteKurstyp(kurstypToBeDeleted);
      deleteKurstypResult = DeleteKurstypResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteKurstypResult = DeleteKurstypResult.KURSTYP_VON_KURS_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteKurstypResult = DeleteKurstypResult.KURSTYP_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteKurstypResult;
  }

  @Override
  public CreateOrUpdateKurstypModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateKurstypModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateKurstypModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexKurstypToBeUpdated) {
    Kurstyp kurstypToBeUpdated = getSelectedRow(indexKurstypToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateKurstypModel(Optional.of(kurstypToBeUpdated));
  }

  @Override
  public void reloadData() {
    List<Kurstyp> kurstypen = kurstypService.findAllKurstypen();
    tableModel.setData(kurstypen);
  }

  @Override
  public String getListItemName() {
    return "Kurstyp";
  }
}
