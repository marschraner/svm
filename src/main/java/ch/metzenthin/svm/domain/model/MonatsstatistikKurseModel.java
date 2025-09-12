package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface MonatsstatistikKurseModel extends Model {

  String MONAT_JAHR_DATE_FORMAT_STRING = "MM.yyyy";

  Calendar getMonatJahr();

  void setMonatJahr(String anAbmeldemonat) throws SvmValidationException;

  int[] calculateMonatsstatistik();
}
