package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;

/**
 * @author Hans Stamm
 */
public interface KursanmeldungService {

  int calculateHoechsteAnzahlWochen(Schueler schueler, Semester semester);
}
