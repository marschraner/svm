package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.KursortDeletedException;
import ch.metzenthin.svm.domain.KurstypDeletedException;
import ch.metzenthin.svm.domain.SemesterDeletedException;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.KursLehrkraft;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.repository.KursLehrkraftRepository;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.KursortRepository;
import ch.metzenthin.svm.persistence.repository.KurstypRepository;
import ch.metzenthin.svm.persistence.repository.LektionsgebuehrenRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.result.SaveKursResult;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Martin Schraner
 */
@Service
public class KursServiceImpl implements KursService {

  private final KursRepository kursRepository;
  private final KursLehrkraftRepository kursLehrkraftRepository;
  private final LektionsgebuehrenRepository lektionsgebuehrenRepository;
  private final SemesterRepository semesterRepository;
  private final KurstypRepository kurstypRepository;
  private final KursortRepository kursortRepository;

  public KursServiceImpl(
      KursRepository kursRepository,
      KursLehrkraftRepository kursLehrkraftRepository,
      LektionsgebuehrenRepository lektionsgebuehrenRepository,
      SemesterRepository semesterRepository,
      KurstypRepository kurstypRepository,
      KursortRepository kursortRepository) {
    this.kursRepository = kursRepository;
    this.kursLehrkraftRepository = kursLehrkraftRepository;
    this.lektionsgebuehrenRepository = lektionsgebuehrenRepository;
    this.semesterRepository = semesterRepository;
    this.kurstypRepository = kurstypRepository;
    this.kursortRepository = kursortRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByKursortId(int kursortId) {
    return kursRepository.countByKursortId(kursortId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByKurstypId(int kurstypId) {
    return kursRepository.countByKurstypId(kurstypId) > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursByLektionslaenge(int lektionslaenge) {
    return kursRepository.findAll().stream()
        .anyMatch(
            kurs -> {
              int kurslaenge =
                  (int) ((kurs.getZeitEnde().getTime() - kurs.getZeitBeginn().getTime()) / 60000);
              return kurslaenge == lektionslaenge;
            });
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsKursBySemesterId(int semesterId) {
    return kursRepository.countBySemesterId(semesterId) > 0;
  }

  @Override
  @Transactional
  public SaveKursResult saveKurs(Kurs kurs, Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2)
      throws SemesterDeletedException, KurstypDeletedException, KursortDeletedException {

    if (lehrkraft1 == null) {
      return SaveKursResult.KEINE_LEHRKRAEFTE_ERFASST;
    }

    List<Integer> lehrkraefteIds = new ArrayList<>();
    lehrkraefteIds.add(lehrkraft1.getPersonId());
    if (lehrkraft2 != null) {
      lehrkraefteIds.add(lehrkraft2.getPersonId());
    }

    int numberOfKurse =
        (kurs.getKursId() == null)
            ? kursLehrkraftRepository
                .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdIn(
                    kurs.getSemester().getSemesterId(),
                    kurs.getWochentag(),
                    kurs.getZeitBeginn(),
                    lehrkraefteIds)
            : kursLehrkraftRepository
                .countKurseBySemesterIdAndWochentagAndZeitBeginnAndMitarbeiterIdInAndKursIdNe(
                    kurs.getSemester().getSemesterId(),
                    kurs.getWochentag(),
                    kurs.getZeitBeginn(),
                    lehrkraefteIds,
                    kurs.getKursId());
    if (numberOfKurse > 0) {
      return SaveKursResult.KURS_BEREITS_ERFASST;
    }

    int numberOfLektionsgebuehren =
        lektionsgebuehrenRepository.countByLektionslaenge(kurs.getKurslaenge());
    if (numberOfLektionsgebuehren > 0) {
      return SaveKursResult.LEKTIONSGEBUEHREN_NICHT_ERFASST;
    }

    if (semesterRepository.countBySemesterId(kurs.getSemester().getSemesterId()) > 0) {
      throw new SemesterDeletedException();
    }

    if (kurstypRepository.countByKurstypId(kurs.getKurstyp().getKurstypId()) > 0) {
      throw new KurstypDeletedException();
    }

    if (kursortRepository.countByKursortId(kurs.getKursort().getKursortId()) > 0) {
      throw new KursortDeletedException();
    }

    kurs = kursRepository.save(kurs);

    KursLehrkraft kursLehrkraft1 = new KursLehrkraft(kurs, lehrkraft1, 0);
    kursLehrkraftRepository.save(kursLehrkraft1);
    if (lehrkraft2 != null) {
      KursLehrkraft kursLehrkraft2 = new KursLehrkraft(kurs, lehrkraft2, 1);
      kursLehrkraftRepository.save(kursLehrkraft2);
    }

    return SaveKursResult.SPEICHERN_ERFOLGREICH;
  }

  @Override
  public void deleteKurs(Kurs kurs) throws EntityStillReferencedException {}
}
