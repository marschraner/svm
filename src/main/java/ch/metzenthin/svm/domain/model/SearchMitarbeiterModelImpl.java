package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.MitarbeiterService;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class SearchMitarbeiterModelImpl extends SearchPersonModelImpl
    implements SearchMitarbeiterModel {

  private final MitarbeiterService mitarbeiterService;
  private final MitarbeiterCodeService mitarbeiterCodeService;

  public SearchMitarbeiterModelImpl(
      MitarbeiterService mitarbeiterService, MitarbeiterCodeService mitarbeiterCodeService) {
    this.mitarbeiterService = mitarbeiterService;
    this.mitarbeiterCodeService = mitarbeiterCodeService;
  }

  @Override
  public MitarbeiterCode[] getSelectableCodes() {
    List<MitarbeiterCode> selectableMitarbeiterCodes =
        mitarbeiterCodeService.findAllSelectableCodes();
    selectableMitarbeiterCodes.add(0, null);
    return selectableMitarbeiterCodes.toArray(new MitarbeiterCode[0]);
  }

  @Override
  public ValidationResultsAndListModel<MitarbeiterListModel> search(
      MitarbeiterSearchFields mitarbeiterSearchFields) {
    // TODO validate
    MitarbeiterListModel mitarbeiterListModel =
        new MitarbeiterListModel(mitarbeiterService, mitarbeiterSearchFields);
    return new ValidationResultsAndListModel<>(mitarbeiterListModel);
  }
}
