package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.service.SchuelerCodeService;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class SchuelerCodeSelectionModelImpl extends AbstractCodeSelectionModel<SchuelerCode> {

  private final SchuelerCodeService schuelerCodeService;

  public SchuelerCodeSelectionModelImpl(SchuelerCodeService schuelerCodeService) {
    this.schuelerCodeService = schuelerCodeService;
  }

  @Override
  protected List<SchuelerCode> getCodesWithSelektierbarTrue() {
    return schuelerCodeService.findAllSelectableCodes();
  }

  @Override
  protected SchuelerCode[] toArray(List<SchuelerCode> codeList) {
    return codeList.toArray(new SchuelerCode[0]);
  }
}
