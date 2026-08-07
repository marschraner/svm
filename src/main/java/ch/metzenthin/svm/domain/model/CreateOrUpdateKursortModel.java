package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.KursortFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursortModel {

  boolean isNeu();

  KursortFields getKursortFields();

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSubmitResult speichern(KursortFields kursortFields);
}
