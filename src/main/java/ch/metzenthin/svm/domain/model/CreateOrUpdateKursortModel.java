package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursortModel {

  String formatBezeichnung(String bezeichnung);

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSaveResult speichern(KursortFields kursortFields);

  boolean isNeu();

  KursortFields getKursortFields();
}
