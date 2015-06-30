package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class SchuelerSuchenModelImpl extends AbstractModel implements SchuelerSuchenModel {

    private final Schueler schueler;
    private final Angehoeriger angehoeriger;
    private final Adresse adresse;
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
        schueler = new Schueler();
        angehoeriger = new Angehoeriger();
        adresse = new Adresse();
    }

    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schueler.getNachname();
                }

                @Override
                public void setValue(String value) {
                    schueler.setNachname(value);
                }
            }
    );

    @Override
    public String getNachname() {
        return nachnameModelAttribute.getValue();
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        nachnameModelAttribute.setNewValue(false, nachname);
    }

    private final StringModelAttribute vornameModelAttribute = new StringModelAttribute(
            this,
            Field.VORNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schueler.getVorname();
                }

                @Override
                public void setValue(String value) {
                    schueler.setVorname(value);
                }
            }
    );

    @Override
    public String getVorname() {
        return vornameModelAttribute.getValue();
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        vornameModelAttribute.setNewValue(false, vorname);
    }

    private final StringModelAttribute natelModelAttribute = new StringModelAttribute(
            this,
            Field.NATEL, 13, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schueler.getNatel();
                }

                @Override
                public void setValue(String value) {
                    schueler.setNatel(value);
                }
            }
    );

    @Override
    public String getNatel() {
        return natelModelAttribute.getValue();
    }

    @Override
    public void setNatel(String natel) throws SvmValidationException {
        natelModelAttribute.setNewValue(false, natel);
    }

    private final StringModelAttribute emailModelAttribute = new StringModelAttribute(
            this,
            Field.EMAIL, 5, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return schueler.getEmail();
                }

                @Override
                public void setValue(String value) {
                    schueler.setEmail(value);
                }
            }
    );

    @Override
    public String getEmail() {
        return emailModelAttribute.getValue();
    }

    @Override
    public void setEmail(String email) throws SvmValidationException {
        emailModelAttribute.setNewValue(false, email);
    }

    @Override
    public Calendar getGeburtsdatum() {
        return schueler.getGeburtsdatum();
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
        Calendar oldValue = schueler.getGeburtsdatum();
        schueler.setGeburtsdatum(geburtsdatum);
        firePropertyChange(Field.GEBURTSDATUM, oldValue, schueler.getGeburtsdatum());
    }

    private final StringModelAttribute strasseHausnummerModelAttribute = new StringModelAttribute(
            this,
            Field.STRASSE_HAUSNUMMER, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    if (!checkNotEmpty(adresse.getStrasse()) && !checkNotEmpty(adresse.getHausnummer())) {
                        return "";
                    } else if (!checkNotEmpty(adresse.getHausnummer())) {
                        return adresse.getStrasse();
                    } else if (!checkNotEmpty(adresse.getStrasse())) {
                        return adresse.getHausnummer();
                    } else {
                        return adresse.getStrasse() + " " + adresse.getHausnummer();
                    }
                }

                @Override
                public void setValue(String strasseHausnummer) {
                    adresse.setStrasse(strasseHausnummerGetStrasse(strasseHausnummer));
                    adresse.setHausnummer(strasseHausnummerGetHausnummer(strasseHausnummer));
                }
            },
            new StrasseFormatter()
    );

    @Override
    public String getStrasseHausnummer() {
        return strasseHausnummerModelAttribute.getValue();
    }

    @Override
    public void setStrasseHausnummer(String strasseHausnummer) throws SvmValidationException {
        strasseHausnummerModelAttribute.setNewValue(false, strasseHausnummer);
    }

    private final StringModelAttribute plzModelAttribute = new StringModelAttribute(
            this,
            Field.PLZ, 4, 10,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return adresse.getPlz();
                }

                @Override
                public void setValue(String value) {
                    adresse.setPlz(value);
                }
            }
    );

    @Override
    public String getPlz() {
        return plzModelAttribute.getValue();
    }

    @Override
    public void setPlz(String plz) throws SvmValidationException {
        plzModelAttribute.setNewValue(false, plz);
    }

    private final StringModelAttribute ortModelAttribute = new StringModelAttribute(
            this,
            Field.ORT, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return adresse.getOrt();
                }

                @Override
                public void setValue(String value) {
                    adresse.setOrt(value);
                }
            }
    );

    @Override
    public String getOrt() {
        return ortModelAttribute.getValue();
    }

    @Override
    public void setOrt(String ort) throws SvmValidationException {
        ortModelAttribute.setNewValue(false, ort);
    }

    private final StringModelAttribute festnetzModelAttribute = new StringModelAttribute(
            this,
            Field.FESTNETZ, 13, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return adresse.getFestnetz();
                }

                @Override
                public void setValue(String value) {
                    adresse.setFestnetz(value);
                }
            }
    );

    @Override
    public String getFestnetz() {
        return festnetzModelAttribute.getValue();
    }

    @Override
    public void setFestnetz(String festnetz) throws SvmValidationException {
        festnetzModelAttribute.setNewValue(false, festnetz);
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
        //TODO
        return false;
    }

    @Override
    void doValidate() throws SvmValidationException {

    }
}
