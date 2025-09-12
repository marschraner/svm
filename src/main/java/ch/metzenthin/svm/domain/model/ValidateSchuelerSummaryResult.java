package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class ValidateSchuelerSummaryResult extends SchuelerErfassenSaveResult {

  private final Schueler schueler;
  private final Angehoeriger mutter;
  private final Angehoeriger vater;
  private final Angehoeriger rechnungsempfaenger;
  private final boolean isRechnungsempfaengerMutter;
  private final boolean isRechnungsempfaengerVater;
  private final List<Schueler> geschwister;
  private final List<Schueler> andereSchueler;
  private final String identischeAdressen;
  private final String abweichendeAdressen;
  private final boolean isMutterNeu;
  private final boolean isVaterNeu;
  private final boolean isRechnungsempfaengerNeu;

  @SuppressWarnings("java:S107")
  public ValidateSchuelerSummaryResult(
      Schueler schueler,
      Angehoeriger mutterFoundInDatabase,
      Angehoeriger vaterFoundInDatabase,
      Angehoeriger rechnungsempfaengerFoundInDatabase,
      boolean isRechnungsempfaengerMutter,
      boolean isRechnungsempfaengerVater,
      List<Schueler> geschwister,
      List<Schueler> andereSchueler,
      String identischeAdressen,
      String abweichendeAdressen,
      boolean isMutterNeu,
      boolean isVaterNeu,
      boolean isRechnungsempfaengerNeu) {
    super(
        ValidateSchuelerCommand.Result
            .CHECK_GESCHWISTER_SCHUELER_RECHNUGSEMFPAENGER_COMMAND_FINISHED);
    this.schueler = schueler;
    mutter = (mutterFoundInDatabase != null) ? mutterFoundInDatabase : schueler.getMutter();
    vater = (vaterFoundInDatabase != null) ? vaterFoundInDatabase : schueler.getVater();
    rechnungsempfaenger =
        (rechnungsempfaengerFoundInDatabase != null)
            ? rechnungsempfaengerFoundInDatabase
            : schueler.getRechnungsempfaenger();
    this.isRechnungsempfaengerMutter = isRechnungsempfaengerMutter;
    this.isRechnungsempfaengerVater = isRechnungsempfaengerVater;
    this.geschwister = geschwister;
    this.andereSchueler = andereSchueler;
    this.identischeAdressen = identischeAdressen;
    this.abweichendeAdressen = abweichendeAdressen;
    this.isMutterNeu = isMutterNeu;
    this.isVaterNeu = isVaterNeu;
    this.isRechnungsempfaengerNeu = isRechnungsempfaengerNeu;
  }

  @Override
  public void accept(SchuelerErfassenSaveResultVisitor visitor) {
    visitor.visit(this);
  }

  public Schueler getSchueler() {
    return schueler;
  }

  public Angehoeriger getMutter() {
    return mutter;
  }

  public Angehoeriger getVater() {
    return vater;
  }

  public Angehoeriger getRechnungsempfaenger() {
    return rechnungsempfaenger;
  }

  public boolean isRechnungsempfaengerMutter() {
    return isRechnungsempfaengerMutter;
  }

  public boolean isRechnungsempfaengerVater() {
    return isRechnungsempfaengerVater;
  }

  public List<Schueler> getGeschwister() {
    return geschwister;
  }

  public List<Schueler> getAndereSchueler() {
    return andereSchueler;
  }

  public String getIdentischeAdressen() {
    return identischeAdressen;
  }

  public String getAbweichendeAdressen() {
    return abweichendeAdressen;
  }

  public boolean isMutterNeu() {
    return isMutterNeu;
  }

  public boolean isVaterNeu() {
    return isVaterNeu;
  }

  public boolean isRechnungsempfaengerNeu() {
    return isRechnungsempfaengerNeu;
  }
}
