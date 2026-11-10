package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.DispensationFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateDispensationModel {

  boolean isNeu();

  SaveSuccessfulAndEntityIdOptional getSaveSuccessfulAndDispensationIdOptional();

  DispensationFields getDispensationFields();

  ValidationResult validateDispensationsbeginn(Calendar dispensationsbeginn);

  ValidationResult validateDispensationsende(Calendar dispensationsende);

  ValidationResult validateVoraussichtlicheDauer(String voraussichtlicheDauer);

  ValidationResult validateGrund(String grund);

  ValidationResultsAndSubmitResult speichern(DispensationFields dispensationFields);
}
