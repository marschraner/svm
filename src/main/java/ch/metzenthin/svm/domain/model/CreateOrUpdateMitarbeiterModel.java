package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import java.util.Set;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateMitarbeiterModel extends CreateOrUpdatePersonModel {

  MitarbeiterFieldsAndAdresseFields getMitarbeiterFieldsAndAdresseFields();

  String getMitarbeiterCodesAsStr();

  void setMitarbeiterCodes(Set<MitarbeiterCode> mitarbeiterCodes);

  AddOrRemoveMitarbeiterCodeAssignmentListModel createMitarbeiterCodeAssignmentListModel();

  ValidationResult validateAhvNummer(String ahvNummer);

  ValidationResult validateIbanNummer(String ibanNummer);

  ValidationResult validateVertretungsmoeglichkeiten(String vertretungsmoeglichkeiten);

  ValidationResult validateBemerkungen(String bemerkungen);

  ValidationResultsAndSubmitResult speichern(
      MitarbeiterFieldsAndAdresseFields mitarbeiterFieldsAndAdresseFields);
}
