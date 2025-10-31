package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import ch.metzenthin.svm.ui.componentmodel.KursorteTableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class KursorteModelImpl extends AbstractModel implements KursorteModel {

  private final KursortService kursortService;

  public KursorteModelImpl(KursortService kursortService) {
    this.kursortService = kursortService;
  }

  @Override
  public DeleteKursortResult eintragLoeschen(
      KursorteTableModel kursorteTableModel, int indexKursortToBeRemoved) {
    Kursort kursortToBeDeleted = getSelectedKursort(kursorteTableModel, indexKursortToBeRemoved);
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
  public KursortErfassenModel createKursortErfassenModel(
      SvmContext svmContext, KursorteTableModel kursorteTableModel) {
    return svmContext.getModelFactory().createKursortErfassenModel(Optional.empty());
  }

  @Override
  public KursortErfassenModel createKursortErfassenModel(
      SvmContext svmContext, KursorteTableModel kursorteTableModel, int indexKursortToBeModified) {
    Kursort kursortToBeModified = getSelectedKursort(kursorteTableModel, indexKursortToBeModified);
    return svmContext
        .getModelFactory()
        .createKursortErfassenModel(Optional.of(kursortToBeModified));
  }

  private static Kursort getSelectedKursort(
      KursorteTableModel kursorteTableModel, int selectedIndex) {
    return kursorteTableModel.getKursorteTableData().getKursorte().get(selectedIndex);
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
