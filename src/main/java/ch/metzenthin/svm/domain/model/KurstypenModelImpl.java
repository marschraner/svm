package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.result.DeleteKurstypResult;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;
import java.util.Optional;

/**
 * @author Martin Schraner
 */
public class KurstypenModelImpl extends AbstractModel implements KurstypenModel {

  private final KurstypService kurstypService;

  public KurstypenModelImpl(KurstypService kurstypService) {
    this.kurstypService = kurstypService;
  }

  @Override
  public DeleteKurstypResult eintragLoeschen(
      KurstypenTableModel kurstypenTableModel, int indexKurstypToBeRemoved) {
    Kurstyp kurstypToBeDeleted = getSelectedKurstyp(kurstypenTableModel, indexKurstypToBeRemoved);
    return kurstypService.deleteKurstyp(kurstypToBeDeleted);
  }

  @Override
  public CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      SvmContext svmContext, KurstypenTableModel kurstypenTableModel) {
    return svmContext.getModelFactory().createCreateOrUpdateKurstypModel(Optional.empty());
  }

  @Override
  public CreateOrUpdateKurstypModel createCreateOrUpdateKurstypModel(
      SvmContext svmContext,
      KurstypenTableModel kurstypenTableModel,
      int indexKurstypToBeModified) {
    Kurstyp kurstypToBeModified = getSelectedKurstyp(kurstypenTableModel, indexKurstypToBeModified);
    return svmContext
        .getModelFactory()
        .createCreateOrUpdateKurstypModel(Optional.of(kurstypToBeModified));
  }

  private static Kurstyp getSelectedKurstyp(
      KurstypenTableModel kurstypenTableModel, int selectedIndex) {
    return kurstypenTableModel.getKurstypenTableData().getKurstypen().get(selectedIndex);
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
