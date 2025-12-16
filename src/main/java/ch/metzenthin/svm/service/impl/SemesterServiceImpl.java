package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.checkIfTwoPeriodsOverlap;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class SemesterServiceImpl implements SemesterService {

  private static final Logger LOGGER = LogManager.getLogger(SemesterServiceImpl.class);

  private final KursService kursService;
  private final SemesterrechnungService semesterrechnungService;
  private final SemesterRepository semesterRepository;
  private final KursRepository kursRepository;

  public SemesterServiceImpl(
      KursService kursService,
      SemesterRepository semesterRepository,
      SemesterrechnungService semesterrechnungService,
      KursRepository kursRepository) {
    this.kursService = kursService;
    this.semesterRepository = semesterRepository;
    this.semesterrechnungService = semesterrechnungService;
    this.kursRepository = kursRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkIfUpdateAffectsSemesterrechnungen(Semester semester) {
    if (semester.getSemesterId() != null
        && semesterrechnungService.countSemesterrechnungenBySemesterId(semester.getSemesterId())
            > 0) {
      Optional<Semester> originalSemesterOptional =
          semesterRepository.findById(semester.getSemesterId());
      if (originalSemesterOptional.isPresent()) {
        Semester originalSemester = originalSemesterOptional.get();
        return originalSemester.getAnzahlSchulwochen() != semester.getAnzahlSchulwochen();
      }
    }
    return false;
  }

  @Override
  @Transactional(readOnly = true)
  public Semester determineNaechstesNochNichtErfasstesSemester() {
    Calendar today = new GregorianCalendar();
    int schuljahr1;
    if (today.get(Calendar.MONTH) <= Calendar.MAY) {
      schuljahr1 = today.get(Calendar.YEAR) - 1;
    } else {
      schuljahr1 = today.get(Calendar.YEAR);
    }
    int schuljahr2 = schuljahr1 + 1;
    String naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
    Semesterbezeichnung naechsteSemesterbezeichnung;
    if (today.get(Calendar.MONTH) >= Calendar.FEBRUARY
        && today.get(Calendar.MONTH) <= Calendar.MAY) {
      naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
    } else {
      naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
    }
    while (isSemesterBereitsErfasst(naechstesSchuljahr, naechsteSemesterbezeichnung)
        && schuljahr1 < Schuljahre.SCHULJAHR_VALID_MAX) {
      if (naechsteSemesterbezeichnung == Semesterbezeichnung.ERSTES_SEMESTER) {
        naechsteSemesterbezeichnung = Semesterbezeichnung.ZWEITES_SEMESTER;
      } else {
        naechsteSemesterbezeichnung = Semesterbezeichnung.ERSTES_SEMESTER;
        schuljahr1++;
        schuljahr2++;
        naechstesSchuljahr = schuljahr1 + "/" + schuljahr2;
      }
    }
    return new Semester(
        naechstesSchuljahr, naechsteSemesterbezeichnung, null, null, null, null, null, null);
  }

  private boolean isSemesterBereitsErfasst(
      String naechstesSchuljahr, Semesterbezeichnung naechsteSemesterbezeichnung) {
    return semesterRepository.countBySchuljahrAndSemesterbezeichnung(
            naechstesSchuljahr, naechsteSemesterbezeichnung)
        > 0;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Semester> findAllSemesters() {
    return doFindAllSemesters();
  }

  private List<Semester> doFindAllSemesters() {
    return semesterRepository.findAllOrderBySemesterbeginnAndSemesterendeDesc();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SemesterAndNumberOfKurse> findAllSemestersAndNumberOfKurse() {
    List<Semester> semesters = doFindAllSemesters();
    List<Integer> semesterIds = semesters.stream().map(Semester::getSemesterId).toList();
    List<IdAndCount> semesterIdAndNumberOfKurseList =
        kursRepository.countKurseGroupBySemesterId(semesterIds);
    Map<Integer, Long> semesterIdsAndNumberOfKurseAsMap =
        semesterIdAndNumberOfKurseList.stream()
            .collect(Collectors.toMap(IdAndCount::id, IdAndCount::count));

    List<SemesterAndNumberOfKurse> semesterAndNumberOfKurses = new ArrayList<>();
    for (Semester semester : semesters) {
      long numberOfKurse =
          semesterIdsAndNumberOfKurseAsMap.getOrDefault(semester.getSemesterId(), 0L);
      SemesterAndNumberOfKurse semesterAndNumberOfKurse =
          new SemesterAndNumberOfKurse(semester, numberOfKurse);
      semesterAndNumberOfKurses.add(semesterAndNumberOfKurse);
    }
    return semesterAndNumberOfKurses;
  }

  @Override
  @Transactional
  public void saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
      Semester semester, boolean updateSemesterrechnungen)
      throws EntityAlreadyExistsException, EntityWithOverlappingPeriodsException {

    if (existsAlready(semester)) {
      LOGGER.debug("Speichern wird abgebrochen: Semester existiert bereits: {}", semester);
      throw new EntityAlreadyExistsException();
    }

    if (hasOverlappingPeriods(semester)) {
      LOGGER.debug("Speichern wird abgebrochen: Semester hat überlappende Perioden: {}", semester);
      throw new EntityWithOverlappingPeriodsException("Semester");
    }
    LOGGER.debug("Anzahl Semester-Schulwochen: {}", semester.getAnzahlSchulwochen());

    semester = semesterRepository.save(semester);

    if (updateSemesterrechnungen) {
      semesterrechnungService.calculateAndUpdateAnzahlWochen(semester);
    }
  }

  private boolean existsAlready(Semester semester) {
    long numberOfAlreadyExistingSemesters =
        (semester.getSemesterId() != null)
            ? semesterRepository.countBySchuljahrAndSemesterbezeichnungAndIdNe(
                semester.getSchuljahr(),
                semester.getSemesterbezeichnung(),
                semester.getSemesterId())
            : semesterRepository.countBySchuljahrAndSemesterbezeichnung(
                semester.getSchuljahr(), semester.getSemesterbezeichnung());
    return numberOfAlreadyExistingSemesters > 0;
  }

  private boolean hasOverlappingPeriods(Semester semester) {
    return doFindAllSemesters().stream()
        .filter(s -> !s.getSemesterId().equals(semester.getSemesterId()))
        .anyMatch(
            s ->
                checkIfTwoPeriodsOverlap(
                    s.getSemesterbeginn(),
                    s.getSemesterende(),
                    semester.getSemesterbeginn(),
                    semester.getSemesterende()));
  }

  @Override
  @Transactional
  public void deleteSemesterrechnungenAndSemester(Semester semester)
      throws EntityStillReferencedException {
    if (kursService.existsKursBySemesterId(semester.getSemesterId())) {
      throw new EntityStillReferencedException();
    }

    semesterrechnungService.deleteSemesterrechnungenBySemesterId(semester.getSemesterId());
    semesterRepository.delete(semester);
  }
}
