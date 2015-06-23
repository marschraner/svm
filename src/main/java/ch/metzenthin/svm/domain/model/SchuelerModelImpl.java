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

import java.text.ParseException;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

    private final Schueler schueler;
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
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        if (!checkNotEmpty(geburtsdatum)) {
            invalidate();
            throw new SvmRequiredException(Field.GEBURTSDATUM);
        }
        super.setGeburtsdatum(geburtsdatum);
    }

    @Override
    public Calendar getAnmeldedatum() {
        return anmeldung.getAnmeldedatum();
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) throws SvmValidationException {
        if (!checkNotEmpty(anmeldedatum)) {
            invalidate();
            throw new SvmRequiredException(Field.ANMELDEDATUM);
        }
        try {
            setAnmeldedatum(toCalendar(anmeldedatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.ANMELDEDATUM);
        }
    }

    @Override
    public void setAnmeldedatum(Calendar anmeldedatum) {
        Calendar oldValue = anmeldung.getAnmeldedatum();
        anmeldung.setAnmeldedatum(anmeldedatum);
        firePropertyChange(Field.ANMELDEDATUM, oldValue, anmeldung.getAnmeldedatum());
    }

    @Override
    public Calendar getAbmeldedatum() {
        return anmeldung.getAbmeldedatum();
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) throws SvmValidationException {
        try {
            setAbmeldedatum(toCalendar(abmeldedatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1201, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.ABMELDEDATUM);
        }
    }

    @Override
    public void setAbmeldedatum(Calendar abmeldedatum) {
        Calendar oldValue = anmeldung.getAbmeldedatum();
        anmeldung.setAbmeldedatum(abmeldedatum);
        firePropertyChange(Field.ABMELDEDATUM, oldValue, anmeldung.getAbmeldedatum());
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
