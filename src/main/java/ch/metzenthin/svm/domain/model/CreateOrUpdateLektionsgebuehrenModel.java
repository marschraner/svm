package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.LektionsgebuehrenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import java.math.BigDecimal;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateLektionsgebuehrenModel {

  boolean isNeu();

  LektionsgebuehrenFields getLektionsgebuehrenFields();

  ValidationResult validateLektionslaenge(int lektionslaenge);

  ValidationResult validateBetrag1Kind(BigDecimal betrag1Kind);

  ValidationResult validateBetrag2Kinder(BigDecimal betrag2Kinder);

  ValidationResult validateBetrag3Kinder(BigDecimal betrag3Kinder);

  ValidationResult validateBetrag4Kinder(BigDecimal betrag4Kinder);

  ValidationResult validateBetrag5Kinder(BigDecimal betrag5Kinder);

  ValidationResult validateBetrag6Kinder(BigDecimal betrag6Kinder);

  ValidationResultsAndSubmitResult speichern(LektionsgebuehrenFields lektionsgebuehrenFields);
}
