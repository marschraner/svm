package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Semester;

/**
 * @author Hans Stamm
 */
public interface SemesterrechnungService extends ReferencedCodeService {

  int countSemesterrechnungenBySemesterId(int semesterId);

  int calculateAndUpdateAnzahlWochen(Semester semester);

  void deleteSemesterrechnungenBySemesterId(int semesterId);
}
