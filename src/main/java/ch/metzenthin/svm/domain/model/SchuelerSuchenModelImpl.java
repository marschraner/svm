package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.toCalendar;

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
    private Calendar anAbmeldemonat;
    private AnAbmeldungenSelected anAbmeldungen;
    private boolean stammdatenBeruecksichtigen;
    private boolean kursBeruecksichtigen;
    private boolean codesBeruecksichtigen;
    private boolean anAbmeldestatistikBeruecksichtigen;

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
        try {
            setStichtag(toCalendar(stichtag));
        } catch (ParseException e) {
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
    public Calendar getAnAbmeldemonat() {
        return anAbmeldemonat;
    }

    @Override
    public void setAnAbmeldemonat(String anmeldemonat) throws SvmValidationException {
        try {
            setAnAbmeldemonat(toCalendar(anmeldemonat));
        } catch (ParseException e) {
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.ANMELDEDATUM);
        }
    }

    @Override
    public AnAbmeldungenSelected getAnAbmeldungen() {
        return anAbmeldungen;
    }

    @Override
    public void setAnAbmeldemonat(Calendar anAbmeldemonat) {
        Calendar oldValue = this.anAbmeldemonat;
        this.anAbmeldemonat = anAbmeldemonat;
        firePropertyChange(Field.AN_ABMELDEMONAT, oldValue, this.anAbmeldemonat);
    }

    @Override
    public void setAnAbmeldungen(AnAbmeldungenSelected anAbmeldungen) {
        AnAbmeldungenSelected oldValue = this.anAbmeldungen;
        this.anAbmeldungen = anAbmeldungen;
        firePropertyChange(Field.AN_ABMELDUNGEN, oldValue, this.anAbmeldungen);
    }

    @Override
    public boolean isStammdatenBeruecksichtigen() {
        return stammdatenBeruecksichtigen;
    }

    @Override
    public void setStammdatenBeruecksichtigen(boolean stammdatenBeruecksichtigen) {
        boolean oldValue = this.stammdatenBeruecksichtigen;
        this.stammdatenBeruecksichtigen = stammdatenBeruecksichtigen;
        firePropertyChange(Field.STAMMDATEN_BERUECKSICHTIGEN, oldValue, this.stammdatenBeruecksichtigen);
    }

    @Override
    public boolean isKursBeruecksichtigen() {
        return kursBeruecksichtigen;
    }

    @Override
    public void setKursBeruecksichtigen(boolean kursBeruecksichtigen) {
        boolean oldValue = this.kursBeruecksichtigen;
        this.kursBeruecksichtigen = kursBeruecksichtigen;
        firePropertyChange(Field.KURS_BERUECKSICHTIGEN, oldValue, this.kursBeruecksichtigen);
    }

    @Override
    public boolean isCodesBeruecksichtigen() {
        return codesBeruecksichtigen;
    }

    @Override
    public void setCodesBeruecksichtigen(boolean codesBeruecksichtigen) {
        boolean oldValue = this.codesBeruecksichtigen;
        this.codesBeruecksichtigen = codesBeruecksichtigen;
        firePropertyChange(Field.CODES_BERUECKSICHTIGEN, oldValue, this.codesBeruecksichtigen);
    }

    @Override
    public boolean isAnAbmeldestatistikBeruecksichtigen() {
        return anAbmeldestatistikBeruecksichtigen;
    }

    @Override
    public void setAnAbmeldestatistikBeruecksichtigen(boolean anAbmeldestatistikBeruecksichtigen) {
        boolean oldValue = this.anAbmeldestatistikBeruecksichtigen;
        this.anAbmeldestatistikBeruecksichtigen = anAbmeldestatistikBeruecksichtigen;
        firePropertyChange(Field.AN_ABMELDESTATISTIK_BERUECKSICHTIGEN, oldValue, this.anAbmeldestatistikBeruecksichtigen);
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
        // wird nicht verwendet
        return true;
    }

    @Override
    public void doValidate() throws SvmValidationException {
        //TODO
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
