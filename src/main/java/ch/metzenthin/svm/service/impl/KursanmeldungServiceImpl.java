package ch.metzenthin.svm.service.impl;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNumberOfDaysOfPeriod;
import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNumberOfWeeksBetween;

import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.KursanmeldungRepository;
import ch.metzenthin.svm.service.KursanmeldungService;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KursanmeldungServiceImpl implements KursanmeldungService {

  private final KursanmeldungRepository kursanmeldungRepository;

  public KursanmeldungServiceImpl(KursanmeldungRepository kursanmeldungRepository) {
    this.kursanmeldungRepository = kursanmeldungRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public int calculateHoechsteAnzahlWochen(Schueler schueler, Semester semester) {

    List<Kursanmeldung> kursanmeldungen =
        kursanmeldungRepository.findKursanmeldungenBySchuelerIdAndSemesterId(
            schueler.getPersonId(), semester.getSemesterId());
    Optional<Integer> maxAnzahlWochenOptional =
        kursanmeldungen.stream()
            .map(kursanmeldung -> calculateAnzahlWochen(kursanmeldung, semester))
            .max(Integer::compare);

    return maxAnzahlWochenOptional.orElse(0);
  }

  @SuppressWarnings("java:S3776")
  static int calculateAnzahlWochen(Kursanmeldung kursanmeldung, Semester semester) {

    int anzahlWochenKursanmeldung = semester.getAnzahlSchulwochen();

    // Später angemeldet?
    if (kursanmeldung.getAnmeldedatum() != null
        && kursanmeldung.getAnmeldedatum().after(semester.getSemesterbeginn())) {

      // Klonen, da ein Anmeldedatum innerhalb Ferien auf darauffolgenden Schulanfang geändert wird
      Calendar anmeldedatum = (Calendar) kursanmeldung.getAnmeldedatum().clone();

      // Anmeldedatum innerhalb 1. Schulferien -> Anmeldedatum auf darauffolgenden Schulanfang
      // ändern
      if (semester.getFerienbeginn1() != null
          && semester.getFerienende1() != null
          && (anmeldedatum.equals(semester.getFerienbeginn1())
              || anmeldedatum.after(semester.getFerienbeginn1()))
          && (anmeldedatum.equals(semester.getFerienende1())
              || anmeldedatum.before(semester.getFerienende1()))) {
        anmeldedatum = (Calendar) semester.getFerienende1().clone();
        anmeldedatum.add(Calendar.DAY_OF_YEAR, 1);
      }

      // Anmeldedatum innerhalb 2. Schulferien -> Anmeldedatum auf darauffolgenden Schulanfang
      // ändern
      if (semester.getFerienbeginn2() != null
          && semester.getFerienende2() != null
          && (anmeldedatum.equals(semester.getFerienbeginn2())
              || anmeldedatum.after(semester.getFerienbeginn2()))
          && (anmeldedatum.equals(semester.getFerienende2())
              || anmeldedatum.before(semester.getFerienende2()))) {
        anmeldedatum = (Calendar) semester.getFerienende2().clone();
        anmeldedatum.add(Calendar.DAY_OF_YEAR, 1);
      }

      anzahlWochenKursanmeldung -=
          getNumberOfDaysOfPeriod(semester.getSemesterbeginn(), anmeldedatum) / 7;

      // Anmeldedatum nach Ende der 1. Schulferien
      if (semester.getFerienbeginn1() != null
          && semester.getFerienende1() != null
          && anmeldedatum.after(semester.getFerienende1())) {
        anzahlWochenKursanmeldung +=
            getNumberOfWeeksBetween(semester.getFerienbeginn1(), semester.getFerienende1());
      }

      // Anmeldedatum nach Ende der 2. Schulferien
      if (semester.getFerienbeginn2() != null
          && semester.getFerienende2() != null
          && anmeldedatum.after(semester.getFerienende2())) {
        anzahlWochenKursanmeldung +=
            getNumberOfWeeksBetween(semester.getFerienbeginn2(), semester.getFerienende2());
      }

      // Wochentag des Anmeldedatums nach Wochentag des Kurses?
      if (anmeldedatum.get(Calendar.DAY_OF_WEEK)
              > kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()
          // DAY_OF_WEEK von Sonntag ist 1!
          || anmeldedatum.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        anzahlWochenKursanmeldung--;
      }
    }

    // Früher abgemeldet?
    if (kursanmeldung.getAbmeldedatum() != null
        && kursanmeldung.getAbmeldedatum().before(semester.getSemesterende())) {

      // Klonen, da ein Abmeldedatum innerhalb Ferien auf vorhergehendes Schulende geändert wird
      Calendar abmeldedatum = (Calendar) kursanmeldung.getAbmeldedatum().clone();

      // Abmeldedatum innerhalb 2. Schulferien -> Abmeldedatum auf vorhergehendes Schulende ändern
      if (semester.getFerienbeginn2() != null
          && semester.getFerienende2() != null
          && (abmeldedatum.equals(semester.getFerienbeginn2())
              || abmeldedatum.after(semester.getFerienbeginn2()))
          && (abmeldedatum.equals(semester.getFerienende2())
              || abmeldedatum.before(semester.getFerienende2()))) {
        abmeldedatum = (Calendar) semester.getFerienbeginn2().clone();
        abmeldedatum.add(Calendar.DAY_OF_YEAR, -1);
      }

      // Abmeldedatum innerhalb 1. Schulferien -> Abmeldedatum auf vorhergehendes Schulende ändern
      if (semester.getFerienbeginn1() != null
          && semester.getFerienende1() != null
          && (abmeldedatum.equals(semester.getFerienbeginn1())
              || abmeldedatum.after(semester.getFerienbeginn1()))
          && (abmeldedatum.equals(semester.getFerienende1())
              || abmeldedatum.before(semester.getFerienende1()))) {
        abmeldedatum = (Calendar) semester.getFerienbeginn1().clone();
        abmeldedatum.add(Calendar.DAY_OF_YEAR, -1);
      }

      anzahlWochenKursanmeldung -=
          getNumberOfDaysOfPeriod(abmeldedatum, semester.getSemesterende()) / 7;

      // Abmeldedatum vor Beginn der 1. Schulferien
      if (semester.getFerienbeginn1() != null
          && semester.getFerienende1() != null
          && abmeldedatum.before(semester.getFerienbeginn1())) {
        anzahlWochenKursanmeldung +=
            getNumberOfWeeksBetween(semester.getFerienbeginn1(), semester.getFerienende1());
      }

      // Abmeldedatum vor Beginn der 2. Schulferien
      if (semester.getFerienbeginn2() != null
          && semester.getFerienende2() != null
          && abmeldedatum.before(semester.getFerienbeginn2())) {
        anzahlWochenKursanmeldung +=
            getNumberOfWeeksBetween(semester.getFerienbeginn2(), semester.getFerienende2());
      }

      // Wochentag des Abmeldedatums vor Wochentag des Kurses oder ein Sonntag?
      if (abmeldedatum.get(Calendar.DAY_OF_WEEK)
          < kursanmeldung.getKurs().getWochentag().getDayOfWeekCalendar()) {
        // Sonntag schon dabei, da DAY_OF_WEEK von Sonntag ist 1!
        anzahlWochenKursanmeldung--;
      }
    }

    return anzahlWochenKursanmeldung;
  }
}
