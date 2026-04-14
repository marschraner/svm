package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKurstypModel {

  String formatBezeichnung(String bezeichnung);

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSaveResult speichern(KurstypFields kurstypFields);

  boolean isNeu();

  KurstypFields getKurstypFields();
}
