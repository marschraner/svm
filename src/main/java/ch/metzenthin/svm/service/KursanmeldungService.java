package ch.metzenthin.svm.service;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import java.util.List;
import java.util.Map;

/**
 * @author Hans Stamm
 */
public interface KursanmeldungService {

  CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochen(
      Schueler schueler, Semester semester);

  Map<Schueler, List<Kursanmeldung>> findKursanmeldungenForSemesterAndRechnungsempfaengerBySchueler(
      Semester semester, Angehoeriger rechnungsempfaenger);
}
