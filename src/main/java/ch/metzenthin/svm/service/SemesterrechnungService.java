package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import java.util.Optional;

/**
 * @author Hans Stamm
 */
public interface SemesterrechnungService extends ReferencedCodeService {

  int countSemesterrechnungenBySemesterId(int semesterId);

  int calculateAndUpdateAnzahlWochen(Semester semester);

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  int calculateAndUpdateAnzahlWochenAndWochenbetrag(
      Semester currentSemester,
      Optional<Semester> nextSemesterOptional,
      Angehoeriger rechnungsempfaenger);

  void deleteSemesterrechnungenBySemesterId(int semesterId);
}
