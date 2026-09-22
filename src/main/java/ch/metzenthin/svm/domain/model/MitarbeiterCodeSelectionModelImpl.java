package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class MitarbeiterCodeSelectionModelImpl extends AbstractCodeSelectionModel<MitarbeiterCode> {

  private final MitarbeiterCodeService mitarbeiterCodeService;

  public MitarbeiterCodeSelectionModelImpl(MitarbeiterCodeService mitarbeiterCodeService) {
    this.mitarbeiterCodeService = mitarbeiterCodeService;
  }

  @Override
  protected List<MitarbeiterCode> getCodesWithSelektierbarTrue() {
    return mitarbeiterCodeService.findAllSelectableCodes();
  }

  @Override
  protected MitarbeiterCode[] toArray(List<MitarbeiterCode> codeList) {
    return codeList.toArray(new MitarbeiterCode[0]);
  }
}
