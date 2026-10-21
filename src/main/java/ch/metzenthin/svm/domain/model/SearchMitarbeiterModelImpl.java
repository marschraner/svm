package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.service.EmailService;
import ch.metzenthin.svm.service.MitarbeiterCodeService;
import ch.metzenthin.svm.service.MitarbeiterService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class SearchMitarbeiterModelImpl extends SearchPersonModelImpl
    implements SearchMitarbeiterModel {

  private final MitarbeiterService mitarbeiterService;
  private final MitarbeiterCodeService mitarbeiterCodeService;
  private final EmailService emailService;

  public SearchMitarbeiterModelImpl(
      MitarbeiterService mitarbeiterService,
      MitarbeiterCodeService mitarbeiterCodeService,
      EmailService emailService) {
    this.mitarbeiterService = mitarbeiterService;
    this.mitarbeiterCodeService = mitarbeiterCodeService;
    this.emailService = emailService;
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
    List<ValidationResult> validationResults = validateAll(mitarbeiterSearchFields);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndListModel<>(validationResults, null);
    }
    MitarbeiterListModel mitarbeiterListModel =
        new MitarbeiterListModel(mitarbeiterService, emailService, mitarbeiterSearchFields);
    return new ValidationResultsAndListModel<>(validationResults, mitarbeiterListModel);
  }

  private List<ValidationResult> validateAll(MitarbeiterSearchFields mitarbeiterSearchFields) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(validateNachname(mitarbeiterSearchFields.nachname()));
    validationResults.add(validateVorname(mitarbeiterSearchFields.vorname()));

    return validationResults;
  }
}
