package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class SchuelerSuchenModelImpl extends PersonModelImpl implements SchuelerSuchenModel {

    private final PersonSuchen person;
    private CommandInvoker commandInvoker;
    private RolleSelected rolle;
    private AnmeldestatusSelected anmeldestatus;
    private DispensationSelected dispensation;
    private GeschlechtSelected geschlecht;
    private Calendar stichtag;

    SchuelerSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        this.commandInvoker = commandInvoker;
        this.person = new PersonSuchen();
    }

    @Override
    Person getPerson() {
        return person;
    }

    @Override
    public Calendar getGeburtsdatum() {
        return person.getGeburtsdatum();
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        try {
            setGeburtsdatum(toCalendar(geburtsdatum));
        } catch (ParseException e) {
            invalidate();
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.GEBURTSDATUM);
        }
    }

    @Override
    public void setGeburtsdatum(Calendar geburtsdatum) {
        Calendar oldValue = person.getGeburtsdatum();
        person.setGeburtsdatum(geburtsdatum);
        firePropertyChange(Field.GEBURTSDATUM, oldValue, person.getGeburtsdatum());
    }

    @Override
    public RolleSelected getRolle() {
        return rolle;
    }

    @Override
    public void setRolle(RolleSelected rolle) {
        RolleSelected oldValue = this.rolle;
        this.rolle = rolle;
        firePropertyChange(Field.ROLLE, oldValue, this.rolle);
    }

    @Override
    public AnmeldestatusSelected getAnmeldestatus() {
        return anmeldestatus;
    }
    
    public void setAnmeldestatus(AnmeldestatusSelected anmeldestatus) {
        AnmeldestatusSelected oldValue = this.anmeldestatus;
        this.anmeldestatus = anmeldestatus;
        firePropertyChange(Field.ANMELDESTATUS, oldValue, this.anmeldestatus);
    }

    @Override
    public DispensationSelected getDispensation() {
        return dispensation;
    }

    @Override
    public void setDispensation(DispensationSelected dispensation) {
        DispensationSelected oldValue = this.dispensation;
        this.dispensation = dispensation;
        firePropertyChange(Field.DISPENSATION, oldValue, this.dispensation);
    }

    @Override
    public GeschlechtSelected getGeschlecht() {
        return geschlecht;
    }

    @Override
    public void setGeschlecht(GeschlechtSelected geschlecht) {
        GeschlechtSelected oldValue = this.geschlecht;
        this.geschlecht = geschlecht;
        firePropertyChange(Field.GESCHLECHT, oldValue, this.geschlecht);
    }

    @Override
    public Calendar getStichtag() {
        return stichtag;
    }

    @Override
    public void setStichtag(String stichtag) throws SvmValidationException {
        if (!checkNotEmpty(stichtag)) {
            invalidate();
            throw new SvmRequiredException(Field.STICHTAG);
        }
        try {
            setStichtag(toCalendar(stichtag));
        } catch (ParseException e) {
            invalidate();
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.STICHTAG);
        }
    }

    @Override
    public void setStichtag(Calendar stichtag) {
        Calendar oldValue = this.stichtag;
        this.stichtag = stichtag;
        firePropertyChange(Field.STICHTAG, oldValue, this.stichtag);
    }

    @Override
    public SchuelerSuchenResult suchen() {
        //TODO
        List<Schueler> schuelerList = new ArrayList<>();
        Adresse adresse1 = new Adresse("Forchstrasse", "232", "8032", "Zürich", null);
        Adresse adresse2 = new Adresse("Hintere Bergstrasse", "15", "8942", "Oberrieden", null);
        Angehoeriger angehoeriger1 = new Angehoeriger(Anrede.FRAU, "Eva", "Juchli", null, null);
        Angehoeriger angehoeriger2 = new Angehoeriger(Anrede.HERR, "Kurt", "Juchli", null, null);
        Angehoeriger angehoeriger3 = new Angehoeriger(Anrede.FRAU, "Käthi", "Schraner", null, null);
        angehoeriger1.setAdresse(adresse1);
        angehoeriger2.setAdresse(adresse1);
        angehoeriger3.setAdresse(adresse2);
        Schueler schueler1 = new Schueler("Lilly", "Juchli", new GregorianCalendar(2008, Calendar.JANUARY, 13), null, null, Geschlecht.W, null);
        schueler1.setAdresse(adresse1);
        schueler1.setMutter(angehoeriger1);
        schueler1.setVater(angehoeriger2);
        schueler1.setRechnungsempfaenger(angehoeriger3);
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), new GregorianCalendar(2013, Calendar.JULY, 1)));
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2015, Calendar.JANUARY, 1), new GregorianCalendar(2015, Calendar.JULY, 1)));
        schuelerList.add(schueler1);
        Schueler schuelerLea = new Schueler("Lea", "Leu", new GregorianCalendar(2010, Calendar.APRIL, 15), null, null, Geschlecht.W, null);
        Adresse adresseLea = new Adresse("Seestrasse", "1", "8234", "Stetten", null);
        schuelerLea.setAdresse(adresseLea);
        Angehoeriger angehoerigerLea = new Angehoeriger(Anrede.FRAU, "Anna", "Müller", null, null);
        schuelerLea.setMutter(angehoerigerLea);
        schuelerLea.setRechnungsempfaenger(angehoerigerLea);
        schuelerList.add(schuelerLea);
        Schueler schuelerUrs = new Schueler("Urs", "Keller", new GregorianCalendar(2000, Calendar.JANUARY, 31), null, null, Geschlecht.M, null);
        Adresse adresseUrs = new Adresse("Talstrasse", "2", "8200", "Schaffhausen", null);
        Angehoeriger angehoerigerUrs = new Angehoeriger(Anrede.HERR, "Fritz", "Meier", null, null);
        schuelerUrs.setVater(angehoerigerUrs);
        schuelerUrs.setRechnungsempfaenger(angehoerigerUrs);
        schuelerUrs.setAdresse(adresseUrs);
        schuelerList.add(schuelerUrs);
        return new SchuelerSuchenResult(schuelerList);
    }

    @Override
    public boolean isCompleted() {
        return stichtag != null;
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (stichtag == null) {
            throw new SvmValidationException(2000, "Stichtag obligatorisch", Field.STICHTAG);
        }
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
