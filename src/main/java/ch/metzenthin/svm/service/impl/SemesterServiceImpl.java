package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.checkIfTwoPeriodsOverlap;

import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.model.IdAndCount;
import ch.metzenthin.svm.domain.model.PreviousCurrentNextSemester;
import ch.metzenthin.svm.domain.model.SemesterAndNumberOfKurse;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.KursRepository;
import ch.metzenthin.svm.persistence.repository.SemesterRepository;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.SemesterrechnungService;
import ch.metzenthin.svm.service.result.DeleteSemesterResult;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hans Stamm
 */
@Service
public class SemesterServiceImpl implements SemesterService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SemesterServiceImpl.class);

  private final SemesterrechnungService semesterrechnungService;
  private final SemesterRepository semesterRepository;
  private final KursRepository kursRepository;

  public SemesterServiceImpl(
      SemesterrechnungService semesterrechnungService,
      SemesterRepository semesterRepository,
      KursRepository kursRepository) {
    this.semesterrechnungService = semesterrechnungService;
    this.semesterRepository = semesterRepository;
    this.kursRepository = kursRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkIfUpdateAffectsSemesterrechnungen(
      Integer semesterId,
      Calendar semesterbeginn,
      Calendar semesterende,
      Calendar ferienbeginn1,
      Calendar ferienende1,
      Calendar ferienbeginn2,
      Calendar ferienende2) {

    if (semesterId != null
        && semesterrechnungService.countSemesterrechnungenBySemesterId(semesterId) > 0) {
      Optional<Semester> originalSemesterOptional = semesterRepository.findById(semesterId);
      if (originalSemesterOptional.isPresent()) {
        Semester originalSemester = originalSemesterOptional.get();
        Semester newSemester = new Semester();
        newSemester.setSemesterbeginn(semesterbeginn);
        newSemester.setSemesterende(semesterende);
        newSemester.setFerienbeginn1(ferienbeginn1);
        newSemester.setFerienende1(ferienende1);
        newSemester.setFerienbeginn2(ferienbeginn2);
        newSemester.setFerienende2(ferienende2);
        return originalSemester.getAnzahlSchulwochen() != newSemester.getAnzahlSchulwochen();
      }
    }
    return false;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Semester> findNextSemester(Semester semester) {
    String schuljahrNextSemester;
    Semesterbezeichnung semesterbezeichnungPreviousSemester;
    if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
      schuljahrNextSemester = semester.getSchuljahr();
      semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;
    } else {
      schuljahrNextSemester = Schuljahre.getNextSchuljahr(semester.getSchuljahr());
      semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
    }
    return semesterRepository.findBySchuljahrAndSemesterbezeichnung(
        schuljahrNextSemester, semesterbezeichnungPreviousSemester);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Semester> findSemesterOneYearBefore(Semester semester) {
    if (semester == null) {
      return Optional.empty();
    }

    String schuljahrOneYearBefore = Schuljahre.getPreviousSchuljahr(semester.getSchuljahr());

    return semesterRepository.findBySchuljahrAndSemesterbezeichnung(
        schuljahrOneYearBefore, semester.getSemesterbezeichnung());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Semester> findPreviousSemester(Semester semester) {
    if (semester == null) {
      return Optional.empty();
    }

    String schuljahrPreviousSemester;
    Semesterbezeichnung semesterbezeichnungPreviousSemester;
    if (semester.getSemesterbezeichnung() == Semesterbezeichnung.ERSTES_SEMESTER) {
      schuljahrPreviousSemester = Schuljahre.getPreviousSchuljahr(semester.getSchuljahr());
      semesterbezeichnungPreviousSemester = Semesterbezeichnung.ZWEITES_SEMESTER;
    } else {
      schuljahrPreviousSemester = semester.getSchuljahr();
      semesterbezeichnungPreviousSemester = Semesterbezeichnung.ERSTES_SEMESTER;
    }

    return semesterRepository.findBySchuljahrAndSemesterbezeichnung(
        schuljahrPreviousSemester, semesterbezeichnungPreviousSemester);
  }

  @SuppressWarnings("DuplicatedCode")
  @Override
  @Transactional(readOnly = true)
  public Semester determineNaechstesNochNichtErfasstesSemester() {
    List<Semester> erfassteSemester = doFindAllSemester();
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
    while (isSemesterBereitsErfasst(
            naechstesSchuljahr, naechsteSemesterbezeichnung, erfassteSemester)
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

  @SuppressWarnings("java:S3776")
  @Override
  public PreviousCurrentNextSemester determinePreviousCurrentNextSemesterFor(
      Calendar calendar, List<Semester> semesterList) {

    if (semesterList.isEmpty()) {
      return new PreviousCurrentNextSemester(Optional.empty(), Optional.empty(), Optional.empty());
    }

    Collections.sort(semesterList); // ältestes Semester zuerst

    Optional<Semester> previousSemesterOptional;
    Optional<Semester> currentSemesterOptional;
    Optional<Semester> nextSemesterOptional;

    for (int i = 0; i < semesterList.size(); i++) {
      if ((semesterList.get(i).getSemesterbeginn().before(calendar)
              || semesterList.get(i).getSemesterbeginn().equals(calendar))
          && (semesterList.get(i).getSemesterende().after(calendar)
              || semesterList.get(i).getSemesterende().equals(calendar))) {

        // calendar liegt innerhalb eines Semesters
        currentSemesterOptional = Optional.of(semesterList.get(i));
        nextSemesterOptional = (i > 0) ? Optional.of(semesterList.get(i - 1)) : Optional.empty();
        previousSemesterOptional =
            (i + 1 < semesterList.size()) ? Optional.of(semesterList.get(i + 1)) : Optional.empty();

        return new PreviousCurrentNextSemester(
            previousSemesterOptional, currentSemesterOptional, nextSemesterOptional);

      } else if (i + 1 < semesterList.size()
          && semesterList.get(i + 1).getSemesterende().before(calendar)
          && semesterList.get(i).getSemesterbeginn().after(calendar)) {

        // calendar liegt in den Ferien zwischen zwei Semestern
        currentSemesterOptional = Optional.empty();
        nextSemesterOptional = Optional.of(semesterList.get(i));
        previousSemesterOptional = Optional.of(semesterList.get(i + 1));

        return new PreviousCurrentNextSemester(
            previousSemesterOptional, currentSemesterOptional, nextSemesterOptional);
      }
    }

    // Keiner der beiden Fälle gefunden -> calendar liegt vor oder nach allen erfassten Semestern
    if (semesterList.get(0).getSemesterende().before(calendar)) {
      currentSemesterOptional = Optional.empty();
      previousSemesterOptional = Optional.of(semesterList.get(0));
      nextSemesterOptional = Optional.empty();
    } else {
      currentSemesterOptional = Optional.empty();
      previousSemesterOptional = Optional.empty();
      nextSemesterOptional = Optional.of(semesterList.get(semesterList.size() - 1));
    }

    return new PreviousCurrentNextSemester(
        previousSemesterOptional, currentSemesterOptional, nextSemesterOptional);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Semester> determineInitSemesterForSemesterSelectionComponents(
      int daysBeforeSemesterEndToShowNextSemester) {

    List<Semester> semesterList = semesterRepository.findAll();

    PreviousCurrentNextSemester previousCurrentNextSemester =
        determinePreviousCurrentNextSemesterFor(new GregorianCalendar(), semesterList);

    Optional<Semester> currentSemesterOptional =
        previousCurrentNextSemester.currentSemesterOptional();
    Optional<Semester> nextSemesterOptional = previousCurrentNextSemester.nextSemesterOptional();

    Calendar dayToShowNextSemester = new GregorianCalendar();
    dayToShowNextSemester.add(Calendar.DAY_OF_YEAR, daysBeforeSemesterEndToShowNextSemester);

    Optional<Semester> semesterInitOptional;
    if (currentSemesterOptional.isEmpty()) {
      // Ferien zwischen 2 Semestern
      semesterInitOptional = nextSemesterOptional;
    } else if (dayToShowNextSemester.after(currentSemesterOptional.get().getSemesterende())
        && nextSemesterOptional.isPresent()) {
      // weniger als daysBeforeSemesterEndToShowNextSemester Tage vor Semesterende
      semesterInitOptional = nextSemesterOptional;
    } else {
      semesterInitOptional = currentSemesterOptional;
    }

    if (semesterInitOptional.isEmpty() && !semesterList.isEmpty()) {
      semesterInitOptional = Optional.of(semesterList.get(0));
    }

    return semesterInitOptional;
  }

  private boolean isSemesterBereitsErfasst(
      String naechstesSchuljahr,
      Semesterbezeichnung naechsteSemesterbezeichnung,
      List<Semester> erfassteSemester) {
    return erfassteSemester.stream()
        .anyMatch(
            semester ->
                semester.getSchuljahr().equals(naechstesSchuljahr)
                    && semester.getSemesterbezeichnung() == naechsteSemesterbezeichnung);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Semester> findAllSemesters() {
    return doFindAllSemester();
  }

  private List<Semester> doFindAllSemester() {
    return semesterRepository.findAllOrderBySemesterbeginnAndSemesterendeDesc();
  }

  @Override
  @Transactional(readOnly = true)
  public List<SemesterAndNumberOfKurse> findAllSemesterAndNumberOfKurse() {
    List<IdAndCount> semesterIdAndNumberOfKurseList = kursRepository.countKurseGroupBySemesterId();
    Map<Integer, Long> semesterIdsAndNumberOfKurseAsMap =
        semesterIdAndNumberOfKurseList.stream()
            .collect(Collectors.toMap(IdAndCount::id, IdAndCount::count));

    List<Semester> semesters = doFindAllSemester();
    List<SemesterAndNumberOfKurse> semesterAndNumberOfKurseList = new ArrayList<>();
    for (Semester semester : semesters) {
      long numberOfKurse =
          semesterIdsAndNumberOfKurseAsMap.getOrDefault(semester.getSemesterId(), 0L);
      SemesterAndNumberOfKurse semesterAndNumberOfKurse =
          new SemesterAndNumberOfKurse(semester, numberOfKurse);
      semesterAndNumberOfKurseList.add(semesterAndNumberOfKurse);
    }
    return semesterAndNumberOfKurseList;
  }

  @Override
  @Transactional
  public SaveSemesterResult saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
      Semester semester, boolean updateSemesterrechnungen) {

    if (existsAlready(semester)) {
      LOGGER.debug("Speichern wird abgebrochen: Semester existiert bereits: {}", semester);
      return SaveSemesterResult.SEMESTER_BEREITS_ERFASST;
    }

    if (hasOverlappingPeriods(semester)) {
      LOGGER.debug("Speichern wird abgebrochen: Semester hat überlappende Perioden: {}", semester);
      return SaveSemesterResult.SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER;
    }
    LOGGER.debug("Anzahl Semester-Schulwochen: {}", semester.getAnzahlSchulwochen());

    semester = semesterRepository.save(semester);

    if (updateSemesterrechnungen) {
      semesterrechnungService.calculateAndUpdateAnzahlWochen(semester);
    }
    return SaveSemesterResult.SPEICHERN_ERFOLGREICH;
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
    return doFindAllSemester().stream()
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
  public DeleteSemesterResult deleteSemesterrechnungenAndSemester(Semester semester) {
    if (kursRepository.countBySemesterId(semester.getSemesterId()) > 0) {
      return DeleteSemesterResult.SEMESTER_VON_KURS_REFERENZIERT;
    }

    semesterrechnungService.deleteSemesterrechnungenBySemesterId(semester.getSemesterId());
    semesterRepository.delete(semester);
    return DeleteSemesterResult.LOESCHEN_ERFOLGREICH;
  }
}
