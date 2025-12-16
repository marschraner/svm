package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;

/**
 * @author Hans Stamm
 */
public interface RechnungsempfaengerService {

  int calculateHoechsteAnzahlWochen(Angehoeriger rechnungsempfaenger, Semester semester);
}
