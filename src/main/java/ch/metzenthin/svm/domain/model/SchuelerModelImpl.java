package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Person;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

    private Schueler schueler;

    SchuelerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        schueler = new Schueler();
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
        schueler.setGeschlecht(geschlecht);
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) {
        setAnmeldedatum(toCalendarIgnoreException(anmeldedatum));
    }

    @Override
    public void setAnmeldedatum(Calendar anmeldedatum) {
        schueler.setAnmeldedatum(anmeldedatum);
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) {
        setAbmeldedatum(toCalendarIgnoreException(abmeldedatum));
    }

    @Override
    public void setAbmeldedatum(Calendar abmeldedatum) {
        schueler.setAbmeldedatum(abmeldedatum);
    }

    @Override
    public void setBemerkungen(String bemerkungen) {
        schueler.setBemerkungen(bemerkungen);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public void save() {
        SaveSchuelerCommand saveSchuelerCommand = new SaveSchuelerCommand(schueler);
        getCommandInvoker().executeCommand(saveSchuelerCommand);
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
