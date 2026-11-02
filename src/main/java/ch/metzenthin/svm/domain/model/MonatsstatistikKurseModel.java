package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndServiceReturnValue;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface MonatsstatistikKurseModel {

  String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

  ValidationResult validateMonatJahr(Calendar monatJahr);

  ValidationResultsAndServiceReturnValue<MonatsstatistikKurse> calculateMonatsstatistik(
      String monatJahr);
}
