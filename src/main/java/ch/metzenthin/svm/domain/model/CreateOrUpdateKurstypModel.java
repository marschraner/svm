package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.KurstypFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKurstypModel {

  boolean isNeu();

  KurstypFields getKurstypFields();

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSaveResult speichern(KurstypFields kurstypFields);
}
