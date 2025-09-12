package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotNull;

import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;
import java.util.Calendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

  private static final Logger LOGGER = LogManager.getLogger(SchuelerModelImpl.class);

  private final Schueler schueler;
  private Schueler schuelerOrigin;
  private final Anmeldung anmeldung;

  SchuelerModelImpl() {
    schueler = new Schueler();
    schueler.setAnrede(Anrede.KEINE); // Schueler haben keine Anrede, ist aber obligatorisch in DB
    anmeldung = new Anmeldung();
  }

  @Override
  Person getPerson() {
    return schueler;
  }

  @Override
  public Geschlecht getGeschlecht() {
    return schueler.getGeschlecht();
  }

  @Override
  public void setGeschlecht(Geschlecht geschlecht) throws SvmRequiredException {
    Geschlecht oldValue = schueler.getGeschlecht();
    schueler.setGeschlecht(geschlecht);
    firePropertyChange(Field.GESCHLECHT, oldValue, schueler.getGeschlecht());
    if (geschlecht == null) {
      invalidate();
      throw new SvmRequiredException(Field.GESCHLECHT);
    }
  }

  @Override
  protected Calendar getEarliestValidDateGeburtstag() {
    return getNYearsBeforeNow(25);
  }

  @Override
  public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
    if (!isBulkUpdate() && !checkNotEmpty(geburtsdatum)) {
      invalidate();
      throw new SvmRequiredException(Field.GEBURTSDATUM);
    }
    super.setGeburtsdatum(geburtsdatum);
  }

  private final CalendarModelAttribute anmeldedatumModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.ANMELDEDATUM,
          getNYearsBeforeNow(20),
          getNMonthsAfterNow(1),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return anmeldung.getAnmeldedatum();
            }

            @Override
            public void setValue(Calendar value) {
              anmeldung.setAnmeldedatum(value);
            }
          });

  @Override
  public Calendar getAnmeldedatum() {
    return anmeldedatumModelAttribute.getValue();
  }

  @Override
  public void setAnmeldedatum(String anmeldedatum) throws SvmValidationException {
    anmeldedatumModelAttribute.setNewValue(true, anmeldedatum, isBulkUpdate());
  }

  private final CalendarModelAttribute abmeldedatumModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.ABMELDEDATUM,
          getNYearsBeforeNow(20),
          getNMonthsAfterNow(12),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return anmeldung.getAbmeldedatum();
            }

            @Override
            public void setValue(Calendar value) {
              anmeldung.setAbmeldedatum(value);
            }
          });

  @Override
  public Calendar getAbmeldedatum() {
    return abmeldedatumModelAttribute.getValue();
  }

  @Override
  public void setAbmeldedatum(String abmeldedatum) throws SvmValidationException {
    abmeldedatumModelAttribute.setNewValue(false, abmeldedatum, isBulkUpdate());
  }

  private final StringModelAttribute bemerkungenModelAttribute =
      new StringModelAttribute(
          this,
          Field.BEMERKUNGEN,
          0,
          1000,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return schueler.getBemerkungen();
            }

            @Override
            public void setValue(String value) {
              schueler.setBemerkungen(value);
            }
          });

  @Override
  public String getBemerkungen() {
    return bemerkungenModelAttribute.getValue();
  }

  @Override
  public void setBemerkungen(String bemerkungen) throws SvmValidationException {
    bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
  }

  @Override
  public void doValidate() throws SvmValidationException {
    super.doValidate();
    if (checkNotNull(getAnmeldedatum())
        && checkNotNull(getAbmeldedatum())
        && !getAbmeldedatum().after(getAnmeldedatum())) {
      throw new SvmValidationException(
          2101, "Abmeldedatum muss nach Anmeldedatum sein", Field.ANMELDEDATUM, Field.ABMELDEDATUM);
    }
  }

  @Override
  public Schueler getSchueler() {
    return schueler;
  }

  @Override
  public Schueler getSchuelerOrigin() {
    return schuelerOrigin;
  }

  @Override
  public void setSchuelerOrigin(Schueler schuelerOrigin) {
    this.schuelerOrigin = schuelerOrigin;
  }

  @Override
  public void initializeCompleted() {
    if (schuelerOrigin != null) {
      setBulkUpdate(true);
      try {
        setAnrede(schuelerOrigin.getAnrede());
        setNachname(schuelerOrigin.getNachname());
        setVorname(schuelerOrigin.getVorname());
        setGeschlecht(schuelerOrigin.getGeschlecht());
        setGeburtsdatum(asString(schuelerOrigin.getGeburtsdatum()));
        setStrasseHausnummer(schuelerOrigin.getAdresse().getStrasseHausnummer());
        setPlz(schuelerOrigin.getAdresse().getPlz());
        setOrt(schuelerOrigin.getAdresse().getOrt());
        setFestnetz(schuelerOrigin.getFestnetz());
        setNatel(schuelerOrigin.getNatel());
        setEmail(schuelerOrigin.getEmail());
        setBemerkungen(schuelerOrigin.getBemerkungen());
        setAnmeldedatum(asString(schuelerOrigin.getAnmeldungen().get(0).getAnmeldedatum()));
        setAbmeldedatum(asString(schuelerOrigin.getAnmeldungen().get(0).getAbmeldedatum()));
      } catch (SvmValidationException e) {
        LOGGER.error(e.getMessage());
      }
      setBulkUpdate(false);
    } else {
      super.initializeCompleted();
    }
  }

  @Override
  public Anmeldung getAnmeldung() {
    return anmeldung;
  }

  /**
   * Für Schüler ist die Adresse obligatorisch.
   *
   * @return true
   */
  @Override
  public boolean isAdresseRequired() {
    return true;
  }
}
