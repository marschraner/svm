package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

    private final Schueler schueler;
    private Schueler schuelerOrigin;
    private final Anmeldung anmeldung;

    SchuelerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
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
    public void setGeschlecht(Geschlecht geschlecht) {
        Geschlecht oldValue = schueler.getGeschlecht();
        schueler.setGeschlecht(geschlecht);
        firePropertyChange(Field.GESCHLECHT, oldValue, schueler.getGeschlecht());
    }

    @Override
    protected Calendar getEarliestValidDateGeburtstag() {
        return getNYearsBeforeNow(25);
    }

    @Override
    protected Calendar getLatestValidDateGeburtstag() {
        return getNYearsBeforeNow(2);
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        if (!checkNotEmpty(geburtsdatum)) {
            invalidate();
            throw new SvmRequiredException(Field.GEBURTSDATUM);
        }
        super.setGeburtsdatum(geburtsdatum);
    }

    private final CalendarModelAttribute anmeldedatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.ANMELDEDATUM, getNMonthsBeforeNow(3), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return anmeldung.getAnmeldedatum();
                }

                @Override
                public void setValue(Calendar value) {
                    anmeldung.setAnmeldedatum(value);
                }
            }
    );

    @Override
    public Calendar getAnmeldedatum() {
        return anmeldedatumModelAttribute.getValue();
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) throws SvmValidationException {
        anmeldedatumModelAttribute.setNewValue(true, anmeldedatum);
    }

    private final CalendarModelAttribute abmeldedatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.ABMELDEDATUM, getNMonthsBeforeNow(3), getNMonthsAfterNow(3),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return anmeldung.getAbmeldedatum();
                }

                @Override
                public void setValue(Calendar value) {
                    anmeldung.setAbmeldedatum(value);
                }
            }
    );

    @Override
    public Calendar getAbmeldedatum() {
        return abmeldedatumModelAttribute.getValue();
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) throws SvmValidationException {
        abmeldedatumModelAttribute.setNewValue(false, abmeldedatum);
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            Field.BEMERKUNGEN, 0, 1000,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schueler.getBemerkungen();
                }

                @Override
                public void setValue(String value) {
                    schueler.setBemerkungen(value);
                }
            }
    );

    @Override
    public String getBemerkungen() {
        return bemerkungenModelAttribute.getValue();
    }

    @Override
    public void setBemerkungen(String bemerkungen) throws SvmValidationException {
        bemerkungenModelAttribute.setNewValue(false, bemerkungen);
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public void doValidate() throws SvmValidationException {
        super.doValidate();
    }

    @Override
    public Schueler getSchueler() {
        return schueler;
    }

    @Override
    public void setSchueler(Schueler schueler) {
        schuelerOrigin = schueler;
    }

    @Override
    public void initializeCompleted() {
        if (schuelerOrigin != null) {
            try {
                setAnrede(schuelerOrigin.getAnrede());
                setNachname(schuelerOrigin.getNachname());
                setVorname(schuelerOrigin.getVorname());
                setGeschlecht(schuelerOrigin.getGeschlecht());
                setGeburtsdatum(asString(schuelerOrigin.getGeburtsdatum()));
                setStrasseHausnummer(schuelerOrigin.getAdresse().getStrasseHausnummer());
                setPlz(schuelerOrigin.getAdresse().getPlz());
                setOrt(schuelerOrigin.getAdresse().getOrt());
                setFestnetz(schuelerOrigin.getAdresse().getFestnetz());
                setNatel(schuelerOrigin.getNatel());
                setEmail(schuelerOrigin.getEmail());
                setBemerkungen(schuelerOrigin.getBemerkungen());
                // setAbmeldedatum(schuelerOrigin.getAnmeldungen()); // $$$ todo
                // setAnmeldedatum(schuelerOrigin.getAnmeldungen()); // $$$ todo
            } catch (SvmValidationException e) {
                throw new RuntimeException(e);
            }
        }
        super.initializeCompleted();
    }

    @Override
    public Anmeldung getAnmeldung() {
        return anmeldung;
    }

    /**
     * Für Schüler ist die Adresse obligatorisch.
     * @return true
     */
    @Override
    public boolean isAdresseRequired() {
        return true;
    }

}
