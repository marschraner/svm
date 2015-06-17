package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.FieldName;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
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

    SchuelerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        schueler = new Schueler();
        schueler.setAnrede(Anrede.KEINE); // Schueler haben keine Anrede, ist aber obligatorisch in DB
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
        firePropertyChange(FieldName.GESCHLECHT, oldValue, schueler.getGeschlecht());
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        if (!checkNotEmpty(geburtsdatum)) {
            invalidate();
            throw new SvmRequiredException("Geburtsdatum");
        }
        super.setGeburtsdatum(geburtsdatum);
    }

    @Override
    public Calendar getAnmeldedatum() {
        return schueler.getAnmeldedatum();
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) throws SvmValidationException {
        if (!checkNotEmpty(anmeldedatum)) {
            invalidate();
            throw new SvmRequiredException("Anmeldedatum");
        }
        try {
            setAnmeldedatum(toCalendar(anmeldedatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1200, "Anmeldedatum ist falsch formatiert. Es wird TT.MM.JJJJ erwartet");
        }
    }

    @Override
    public void setAnmeldedatum(Calendar anmeldedatum) {
        Calendar oldValue = schueler.getAnmeldedatum();
        schueler.setAnmeldedatum(anmeldedatum);
        firePropertyChange(FieldName.ANMELDEDATUM, oldValue, schueler.getAnmeldedatum());
    }

    @Override
    public Calendar getAbmeldedatum() {
        return schueler.getAbmeldedatum();
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) throws SvmValidationException {
        try {
            setAbmeldedatum(toCalendar(abmeldedatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1201, "Abmeldedatum ist falsch formatiert. Es wird TT.MM.JJJJ erwartet");
        }
    }

    @Override
    public void setAbmeldedatum(Calendar abmeldedatum) {
        Calendar oldValue = schueler.getAbmeldedatum();
        schueler.setAbmeldedatum(abmeldedatum);
        firePropertyChange(FieldName.ABMELDEDATUM, oldValue, schueler.getAbmeldedatum());
    }

    private final StringModelAttribute bemerkungenModelAttribute = new StringModelAttribute(
            this,
            FieldName.BEMERKUNGEN, 0, 1000,
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

    /**
     * Für Schüler ist die Adresse obligatorisch.
     * @return true
     */
    @Override
    public boolean isAdresseRequired() {
        return true;
    }

}
