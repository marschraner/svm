package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
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
        schuelerList.add(new Schueler("Leu", "Lea", new GregorianCalendar(2010, Calendar.APRIL, 15), null, null, null, null));
        schuelerList.add(new Schueler("Keller", "Urs", new GregorianCalendar(2000, Calendar.JANUARY, 31), null, null, null, null));
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
