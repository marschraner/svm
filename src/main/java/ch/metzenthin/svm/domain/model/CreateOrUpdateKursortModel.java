package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKursortModel {

  String formatBezeichnung(String bezeichnung);

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationAndSaveResult speichern(KursortFields kursortFields);

  boolean isNeu();

  KursortFields getKursortFields();
}
