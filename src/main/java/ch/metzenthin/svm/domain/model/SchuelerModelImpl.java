package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.toCalendarIgnoreException;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

    private Schueler schueler;

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
    public Calendar getAnmeldedatum() {
        return schueler.getAnmeldedatum();
    }

    @Override
    public Calendar getAbmeldedatum() {
        return schueler.getAbmeldedatum();
    }

    @Override
    public String getBemerkungen() {
        return schueler.getBemerkungen();
    }

    @Override
    public void setGeschlecht(Geschlecht geschlecht) {
        Geschlecht oldValue = schueler.getGeschlecht();
        schueler.setGeschlecht(geschlecht);
        firePropertyChange("Geschlecht", oldValue, schueler.getGeschlecht());
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) {
        setAnmeldedatum(toCalendarIgnoreException(anmeldedatum));
    }

    @Override
    public void setAnmeldedatum(Calendar anmeldedatum) {
        Calendar oldValue = schueler.getAnmeldedatum();
        schueler.setAnmeldedatum(anmeldedatum);
        firePropertyChange("Anmeldedatum", oldValue, schueler.getAnmeldedatum());
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) {
        setAbmeldedatum(toCalendarIgnoreException(abmeldedatum));
    }

    @Override
    public void setAbmeldedatum(Calendar abmeldedatum) {
        Calendar oldValue = schueler.getAbmeldedatum();
        schueler.setAbmeldedatum(abmeldedatum);
        firePropertyChange("Abmeldedatum", oldValue, schueler.getAbmeldedatum());
    }

    @Override
    public void setBemerkungen(String bemerkungen) {
        String oldValue = schueler.getBemerkungen();
        schueler.setBemerkungen(bemerkungen);
        firePropertyChange("Bemerkungen", oldValue, schueler.getBemerkungen());
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
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
