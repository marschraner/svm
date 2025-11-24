package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.service.LektionsgebuehrenService;
import ch.metzenthin.svm.service.result.DeleteLektionsgebuehrenResult;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class LektionsgebuehrenModelImpl extends AbstractModel implements LektionsgebuehrenModel {

  private final LektionsgebuehrenService lektionsgebuehrenService;

  public LektionsgebuehrenModelImpl(LektionsgebuehrenService lektionsgebuehrenService) {
    this.lektionsgebuehrenService = lektionsgebuehrenService;
  }

  @Override
  public DeleteLektionsgebuehrenResult eintragLoeschen(
      LektionsgebuehrenTableModel lektionsgebuehrenTableModel,
      int indexLektionsgebuehrenToBeRemoved) {
    Lektionsgebuehren lektionsgebuehrenToBeDeleted =
        getSelectedLektionsgebuehren(
            lektionsgebuehrenTableModel, indexLektionsgebuehrenToBeRemoved);
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
  public LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel(
      SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel) {
    return svmContext.getModelFactory().createLektionsgebuehrenErfassenModel(Optional.empty());
  }

  @Override
  public LektionsgebuehrenErfassenModel createLektionsgebuehrenErfassenModel(
      SvmContext svmContext,
      LektionsgebuehrenTableModel lektionsgebuehrenTableModel,
      int indexLektionsgebuehrenToBeModified) {
    Lektionsgebuehren lektionsgebuehrenToBeModified =
        getSelectedLektionsgebuehren(
            lektionsgebuehrenTableModel, indexLektionsgebuehrenToBeModified);
    return svmContext
        .getModelFactory()
        .createLektionsgebuehrenErfassenModel(Optional.of(lektionsgebuehrenToBeModified));
  }

  private static Lektionsgebuehren getSelectedLektionsgebuehren(
      LektionsgebuehrenTableModel lektionsgebuehrenTableModel, int selectedIndex) {
    return lektionsgebuehrenTableModel
        .getLektionsgebuehrenTableData()
        .getLektionsgebuehrenList()
        .get(selectedIndex);
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }

  @Override
  public boolean isCompleted() {
    return true;
  }
}
