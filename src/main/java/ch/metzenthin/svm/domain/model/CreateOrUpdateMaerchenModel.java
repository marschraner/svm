package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.MaerchenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSubmitResult;
import java.util.function.BooleanSupplier;

/**
 * @author Martin Schraner
 */
public interface CreateOrUpdateMaerchenModel {

  boolean isNeu();

  MaerchenFields getMaerchenFields();

  String getNaechstesNochNichtErfasstesSchuljahr();

  ValidationResult validateSchuljahr(String schuljahr);

  ValidationResult validateBezeichnung(String bezeichnung);

  ValidationResult validateAnzahlVorstellungen(int anzahlVorstellungen);

  ValidationResultsAndSubmitResult speichern(
      MaerchenFields maerchenFields, BooleanSupplier checkForMaerchenInPastWarningSupplier);
}
