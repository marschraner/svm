package ch.metzenthin.svm.service;

import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.service.result.CalculateMaxAnzahlWochenKursanmeldungenResult;
import ch.metzenthin.svm.service.result.CalculateWochenbetragKurseResult;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Hans Stamm
 */
public interface RechnungsempfaengerService {

  CalculateMaxAnzahlWochenKursanmeldungenResult calculateMaxAnzahlWochen(
      Angehoeriger rechnungsempfaenger, Semester semester);

  CalculateWochenbetragKurseResult calculateWochenbetrag(
      Semesterrechnung semesterrechnung,
      Semester relevantesSemester,
      Rechnungstyp rechnungstyp,
      Map<Integer, BigDecimal[]> lektionsgebuehrenMap);
}
