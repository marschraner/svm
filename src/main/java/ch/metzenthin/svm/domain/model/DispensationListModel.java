package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.persistence.entities.Dispensation;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.service.DispensationService;
import ch.metzenthin.svm.service.result.DeleteDispensationResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class DispensationListModel
    extends AbstractListModel<
        Dispensation, TableModel<DispensationTableData, Dispensation>, DeleteDispensationResult> {

  private final Schueler schueler;
  private final DispensationService dispensationService;

  public DispensationListModel(DispensationService dispensationService, Schueler schueler) {
    super(createTableModel(schueler, dispensationService));
    this.schueler = schueler;
    this.dispensationService = dispensationService;
  }

  private static TableModel<DispensationTableData, Dispensation> createTableModel(
      Schueler schueler, DispensationService dispensationService) {
    List<Dispensation> dispensationen =
        dispensationService.findDispensationenBySchuelerId(schueler.getPersonId());
    DispensationTableData dispensationTableData = new DispensationTableData(dispensationen);
    return new TableModel<>(dispensationTableData, 0.2, 0.2, 0.2, 0.4);
  }

  @Override
  public CreateOrUpdateDispensationModel createCreateOrUpdateModel(SvmContext svmContext) {
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateDispensationModel(schueler, Optional.empty());
  }

  @Override
  public CreateOrUpdateDispensationModel createCreateOrUpdateModel(
      SvmContext svmContext, int indexDispensationToBeUpdated) {
    Dispensation dispensationToBeUpdated = getSelectedRow(indexDispensationToBeUpdated);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateDispensationModel(schueler, Optional.of(dispensationToBeUpdated));
  }

  @Override
  public DeleteDispensationResult eintragLoeschen(int indexDispensationToBeDeleted) {
    Dispensation dispensationToBeDeleted = getSelectedRow(indexDispensationToBeDeleted);
    DeleteDispensationResult deleteDispensationResult;
    try {
      deleteDispensationResult = dispensationService.deleteDispensation(dispensationToBeDeleted);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteDispensationResult =
          DeleteDispensationResult.DISPENSATION_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteDispensationResult;
  }

  @Override
  public void reloadData() {
    List<Dispensation> dispensationen =
        dispensationService.findDispensationenBySchuelerId(schueler.getPersonId());
    tableModel.setData(dispensationen);
  }

  @Override
  protected Optional<Dispensation> loadObjectFromDatabase(int dispensationId) {
    return dispensationService.findDispensationById(dispensationId);
  }

  @Override
  protected Optional<Dispensation> reloadObjectFromDatabase(Dispensation dispensation) {
    return dispensationService.findDispensationById(dispensation.getDispensationId());
  }

  @Override
  public String getListItemName() {
    return "Dispensationen";
  }
}
