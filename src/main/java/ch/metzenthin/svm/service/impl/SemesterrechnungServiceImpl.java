package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.repository.SemesterrechnungRepository;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SemesterrechnungServiceImpl implements SemesterrechnungService {

  private final RechnungsempfaengerService rechnungsempfaengerService;
  private final SemesterrechnungRepository semesterrechnungRepository;

  public SemesterrechnungServiceImpl(
      RechnungsempfaengerService rechnungsempfaengerService,
      SemesterrechnungRepository semesterrechnungRepository) {
    this.rechnungsempfaengerService = rechnungsempfaengerService;
    this.semesterrechnungRepository = semesterrechnungRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsReferencedCodeByCodeId(int codeId) {
    return semesterrechnungRepository.countBySemesterrechnungCodeId(codeId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public int countSemesterrechnungenBySemesterId(int semesterId) {
    return semesterrechnungRepository.countBySemesterId(semesterId);
  }

  @Override
  @Transactional
  public int calculateAndUpdateAnzahlWochen(Semester semester) {
    int semesterrechnungUpdatedCount = 0;
    List<Semesterrechnung> semesterrechnungen =
        semesterrechnungRepository.findSemesterrechnungenBySemesterId(semester.getSemesterId());
    for (Semesterrechnung semesterrechnung : semesterrechnungen) {

      if (semesterrechnung.getRechnungsdatumVorrechnung() != null
          && semesterrechnung.getRechnungsdatumNachrechnung() != null) {
        continue;
      }

      int hoechsteAnzahlWochen =
          rechnungsempfaengerService.calculateHoechsteAnzahlWochen(
              semesterrechnung.getRechnungsempfaenger(), semester);

      boolean semesterrechnungUpdated = false;
      if (semesterrechnung.getRechnungsdatumVorrechnung() == null
          && !semesterrechnung.getAnzahlWochenVorrechnung().equals(hoechsteAnzahlWochen)) {
        semesterrechnungUpdated = true;
        semesterrechnung.setAnzahlWochenVorrechnung(hoechsteAnzahlWochen);
      }
      if (semesterrechnung.getRechnungsdatumNachrechnung() == null
          && !semesterrechnung.getAnzahlWochenNachrechnung().equals(hoechsteAnzahlWochen)) {
        semesterrechnungUpdated = true;
        semesterrechnung.setAnzahlWochenNachrechnung(hoechsteAnzahlWochen);
      }

      if (semesterrechnungUpdated) {
        semesterrechnungUpdatedCount++;
        semesterrechnungRepository.save(semesterrechnung);
      }
    }

    return semesterrechnungUpdatedCount;
  }

  @Override
  @Transactional
  public void deleteSemesterrechnungenBySemesterId(int semesterId) {
    semesterrechnungRepository.deleteBySemesterId(semesterId);
  }
}
