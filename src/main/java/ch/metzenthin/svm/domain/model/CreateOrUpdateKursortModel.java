package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultAndSaveKursortResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursortModel {

  void initialiseViewFields(CreateOrUpdateKursortView createOrUpdateKursortView);

  String formatBezeichnung(String bezeichnung);

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultAndSaveKursortResult speichern(
      CreateOrUpdateKursortView createOrUpdateKursortView);
}
