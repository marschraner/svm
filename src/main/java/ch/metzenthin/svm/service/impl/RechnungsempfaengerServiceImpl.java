package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.repository.AnmeldungRepository;
import ch.metzenthin.svm.persistence.repository.SchuelerRepository;
import ch.metzenthin.svm.service.KursanmeldungService;
import ch.metzenthin.svm.service.RechnungsempfaengerService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RechnungsempfaengerServiceImpl implements RechnungsempfaengerService {

  private final KursanmeldungService kursanmeldungService;
  private final AnmeldungRepository anmeldungRepository;
  private final SchuelerRepository schuelerRepository;

  public RechnungsempfaengerServiceImpl(
      KursanmeldungService kursanmeldungService,
      AnmeldungRepository anmeldungRepository,
      SchuelerRepository schuelerRepository) {
    this.kursanmeldungService = kursanmeldungService;
    this.anmeldungRepository = anmeldungRepository;
    this.schuelerRepository = schuelerRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public int calculateHoechsteAnzahlWochen(Angehoeriger rechnungsempfaenger, Semester semester) {

    List<Schueler> schuelerList =
        schuelerRepository.findSchuelerByRechnungsempfaengerId(rechnungsempfaenger.getPersonId());
    Optional<Integer> maxAnzahlWochenOptional =
        schuelerList.stream()
            .filter(schueler -> !isAbgemeldetBeforeSemesterbeginn(schueler, semester))
            .map(schueler -> kursanmeldungService.calculateHoechsteAnzahlWochen(schueler, semester))
            .max(Integer::compareTo);

    // Default-Wert Anzahl Semesterwochen, falls noch keine Kursanmeldung
    return (maxAnzahlWochenOptional.isEmpty() || maxAnzahlWochenOptional.get() == 0)
        ? semester.getAnzahlSchulwochen()
        : maxAnzahlWochenOptional.get();
  }

  private boolean isAbgemeldetBeforeSemesterbeginn(Schueler schueler, Semester semester) {
    List<Anmeldung> anmeldungen =
        anmeldungRepository.findBySchuelerIdOrderByAnmeldedatumDesc(schueler.getPersonId());
    if (anmeldungen.isEmpty()) {
      return true;
    }

    Anmeldung anmeldung = anmeldungen.get(0);
    return anmeldung.getAbmeldedatum() != null
        && !anmeldung.getAbmeldedatum().after(semester.getSemesterbeginn());
  }
}
