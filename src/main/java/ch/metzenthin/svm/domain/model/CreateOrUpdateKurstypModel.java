package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.KurstypFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateKurstypModel {

  boolean isNeu();

  KurstypFields getKurstypFields();

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResultsAndSubmitResult speichern(KurstypFields kurstypFields);
}
