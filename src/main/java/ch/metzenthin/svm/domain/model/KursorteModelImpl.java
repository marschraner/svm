package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.DeleteKursortResult;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class KursorteModelImpl extends AbstractModel implements KursorteModel {

  private final KursortService kursortService;
  @Getter private final KursorteTableData kursorteTableData;

  public KursorteModelImpl(KursortService kursortService) {
    this.kursortService = kursortService;
    List<Kursort> kursorte = this.kursortService.findAllKursorte();
    this.kursorteTableData = new KursorteTableData(kursorte);
  }

  @Override
  public DeleteKursortResult eintragLoeschen(int indexKursortToBeDeleted) {
    Kursort kursortToBeDeleted = getSelectedKursort(indexKursortToBeDeleted);
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
  public CreateOrUpdateKursortModel createOrUpdateKursortModel(SvmContext svmContext) {
    return svmContext.getModelFactory().createCreateOrUpdateKursortModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateKursortModel createOrUpdateKursortModel(
      SvmContext svmContext, int indexKursortToBeModified) {
    Kursort kursortToBeModified = getSelectedKursort(indexKursortToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateKursortModel(Optional.of(kursortToBeModified));
  }

  private Kursort getSelectedKursort(int selectedIndex) {
    return kursorteTableData.getKursorte().get(selectedIndex);
  }

  @Override
  void doValidate() throws SvmValidationException {
    // Keine feldübergreifende Validierung notwendig
  }

  @Override
  public boolean isCompleted() {
    return true;
  }

  @Override
  public void reloadData() {
    List<Kursort> kursorte = kursortService.findAllKursorte();
    kursorteTableData.setKursorte(kursorte);
  }
}
