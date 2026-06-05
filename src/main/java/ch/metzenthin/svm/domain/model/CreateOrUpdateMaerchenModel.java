package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.entityfields.MaerchenFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;

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

  ValidationResultsAndSaveResult speichern(MaerchenFields maerchenFields);
}
