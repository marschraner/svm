package ch.metzenthin.svm.domain.commands;

import static ch.metzenthin.svm.common.utils.DateAndTimeUtils.getNumberOfDaysOfPeriod;

import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Lektionsgebuehren;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public class CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand implements Command {

  // input
  private final Schueler schueler;
  private final Calendar semesterBeginn;

  // output
  private boolean anmeldungsdauerErlaubtSechsJahresRabatt;

  CheckIfAnmeldungsdauerErlaubtSechsJahresRabattCommand(
      Schueler schueler, Calendar semesterBeginn) {
    this.schueler = schueler;
    this.semesterBeginn = semesterBeginn;
  }

  @Override
  public void execute() {

    int anmeldungsdauer = 0;
    for (Anmeldung anmeldung : schueler.getAnmeldungen()) {

      if (anmeldung.getAnmeldedatum().after(semesterBeginn)) {
        continue;
      }

      Calendar periodeEnde;
      if (anmeldung.getAbmeldedatum() == null
          || anmeldung.getAbmeldedatum().after(semesterBeginn)) {
        periodeEnde = semesterBeginn;
      } else {
        periodeEnde = anmeldung.getAbmeldedatum();
      }

      anmeldungsdauer += getNumberOfDaysOfPeriod(anmeldung.getAnmeldedatum(), periodeEnde);
    }

    anmeldungsdauerErlaubtSechsJahresRabatt =
        (anmeldungsdauer >= Lektionsgebuehren.MIN_ANZAHL_TAGE_SECHS_JAHRES_RABATT);
  }

  boolean isAnmeldungsdauerErlaubtSechsJahresRabatt() {
    return anmeldungsdauerErlaubtSechsJahresRabatt;
  }
}
