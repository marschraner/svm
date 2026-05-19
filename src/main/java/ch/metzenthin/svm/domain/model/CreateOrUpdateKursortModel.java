package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.KursortFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursortModel {

  boolean isNeu();

  KursortFields getKursortFields();

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSaveResult speichern(KursortFields kursortFields);
}
