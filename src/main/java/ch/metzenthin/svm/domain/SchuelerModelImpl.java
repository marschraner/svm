package ch.metzenthin.svm.domain;

import ch.metzenthin.svm.commands.CommandInvoker;
import ch.metzenthin.svm.commands.SaveAdresseCommand;
import ch.metzenthin.svm.model.entities.Adresse;
import ch.metzenthin.svm.model.entities.Angehoeriger;
import ch.metzenthin.svm.model.entities.Person;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
final class SchuelerModelImpl extends PersonModelImpl implements SchuelerModel {

    private Person schueler; // todo $$$ Schueler

    SchuelerModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        schueler = new Angehoeriger(); // todo $$$ Schueler
    }

    @Override
    Person getPerson() {
        return schueler;
    }

    @Override
    public Calendar getAnmeldedatum() {
        return null; // todo $$$ schueler.getAnmeldedatum();
    }

    @Override
    public Calendar getAbmeldedatum() {
        return null; // todo $$$ schueler.getAbmeldedatum();
    }

    @Override
    public Calendar getDispensationsbeginn() {
        return null; // todo $$$ schueler.getDispensationsbeginn();
    }

    @Override
    public Calendar getDispensationsende() {
        return null; // todo $$$ schueler.getDispensationsende();
    }

    @Override
    public String getBemerkungen() {
        return null; // todo $$$ schueler.getBemerkungen();
    }

    @Override
    public void setAnmeldedatum(String anmeldedatum) {
        setAnmeldedatum(toCalendarIgnoreException(anmeldedatum));
    }

    @Override
    public void setAnmeldedatum(Calendar anmeldedatum) {
        // todo $$$ schueler.setAnmeldedatum(anmeldedatum);
    }

    @Override
    public void setAbmeldedatum(String abmeldedatum) {
        setAbmeldedatum(toCalendarIgnoreException(abmeldedatum));
    }

    @Override
    public void setAbmeldedatum(Calendar abmeldedatum) {
        // todo $$$ schueler.setAbmeldedatum(abmeldedatum);
    }

    @Override
    public void setDispensationsbeginn(String dispensationsbeginn) {
        setDispensationsbeginn(toCalendarIgnoreException(dispensationsbeginn));
    }

    @Override
    public void setDispensationsbeginn(Calendar dispensationsbeginn) {
        // todo $$$ schueler.setDispensationsbeginn(dispensationsbeginn);
    }

    @Override
    public void setDispensationsende(String dispensationsende) {
        setDispensationsende(toCalendarIgnoreException(dispensationsende));
    }

    @Override
    public void setDispensationsende(Calendar dispensationsende) {
        // todo $$$ schueler.setDispensationsende(dispensationsende);
    }

    @Override
    public void setBemerkungen(String bemerkungen) {
        // todo $$$ schueler.setBemerkungen(bemerkungen);
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public void save() {
        SaveAdresseCommand adresseInsertCommand = new SaveAdresseCommand(adresse); // todo $$$ SaveSchuelerCommand?
        getCommandInvoker().executeCommand(adresseInsertCommand);
    }

}
