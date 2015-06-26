package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Person;

import java.text.ParseException;
import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.strasseHausnummerGetHausnummer;
import static ch.metzenthin.svm.common.utils.Converter.strasseHausnummerGetStrasse;
import static ch.metzenthin.svm.common.utils.Converter.toCalendar;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
abstract class PersonModelImpl extends AbstractModel implements PersonModel {

    private final Adresse adresse;

    PersonModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        adresse = new Adresse();
    }

    abstract Person getPerson();

    @Override
    public Anrede getAnrede() {
        return getPerson().getAnrede();
    }

    @Override
    public void setAnrede(Anrede anrede) {
        Anrede oldValue = getPerson().getAnrede();
        getPerson().setAnrede(anrede);
        firePropertyChange(Field.ANREDE, oldValue, getPerson().getAnrede());
    }

    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return getPerson().getNachname();
                }

                @Override
                public void setValue(String value) {
                    getPerson().setNachname(value);
                }
            }
    );

    @Override
    public String getNachname() {
        return nachnameModelAttribute.getValue();
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        nachnameModelAttribute.setNewValue(isAdresseRequired(), nachname);
    }

    private final StringModelAttribute vornameModelAttribute = new StringModelAttribute(
            this,
            Field.VORNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return getPerson().getVorname();
                }

                @Override
                public void setValue(String value) {
                    getPerson().setVorname(value);
                }
            }
    );

    @Override
    public String getVorname() {
        return vornameModelAttribute.getValue();
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        vornameModelAttribute.setNewValue(isAdresseRequired(), vorname);
    }

    private final StringModelAttribute natelModelAttribute = new StringModelAttribute(
            this,
            Field.NATEL, 13, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return getPerson().getNatel();
                }

                @Override
                public void setValue(String value) {
                    getPerson().setNatel(value);
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
                    return getPerson().getEmail();
                }

                @Override
                public void setValue(String value) {
                    getPerson().setEmail(value);
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
        return getPerson().getGeburtsdatum();
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
        Calendar oldValue = getPerson().getGeburtsdatum();
        getPerson().setGeburtsdatum(geburtsdatum);
        firePropertyChange(Field.GEBURTSDATUM, oldValue, getPerson().getGeburtsdatum());
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
        strasseHausnummerModelAttribute.setNewValue(isAdresseRequired(), strasseHausnummer);
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
        plzModelAttribute.setNewValue(isAdresseRequired(), plz);
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
        ortModelAttribute.setNewValue(isAdresseRequired(), ort);
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
    public void initAdresse(AdresseModel adresseModel) {
        if (adresseModel == null) {
            strasseHausnummerModelAttribute.initValue(null);
            plzModelAttribute.initValue(null);
            ortModelAttribute.initValue(null);
            festnetzModelAttribute.initValue(null);
        } else {
            strasseHausnummerModelAttribute.initValue(adresseModel.getStrasseHausnummer());
            plzModelAttribute.initValue(adresseModel.getPlz());
            ortModelAttribute.initValue(adresseModel.getOrt());
            festnetzModelAttribute.initValue(adresseModel.getFestnetz());
        }
    }

    @Override
    public boolean isCompleted() {
        if (isAdresseRequired()) {
            return isSetName() && isSetAnschrift();
        }
        return (!isSetAnyNameElement() || isSetName()) && (!isSetAnyAnschriftElement() || (isSetName() && isSetAnschrift()));
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (isAdresseRequired()) {
            if (!isSetName()) {
                throw new SvmValidationException(2000, "Nachname und Vorname obligatorisch", Field.NACHNAME, Field.VORNAME);
            }
            if (!isSetAnschrift()) {
                throw new SvmValidationException(2001, "Anschrift ist obligatorisch", Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ);
            }
        } else {
            if (isSetAnyNameElement() && !isSetName()) {
                throw new SvmValidationException(2002, "Nachname und Vorname müssen zusammen angegeben werden", Field.NACHNAME, Field.VORNAME);
            }
            if (isSetAnyAnschriftElement()) {
                if (!isSetName()) {
                    throw new SvmValidationException(2003, "Nachname und Vorname müssen angegeben werden, wenn eine Anschrift vorhanden ist", Field.NACHNAME, Field.VORNAME);
                }
                if (!isSetAnschrift()) {
                    throw new SvmValidationException(2004, "Anschrift ist unvollständig", Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT, Field.FESTNETZ);
                }
            }
        }
    }

    private boolean isSetAnyAdresseElement() {
        return isSetAnyAnschriftElement() || isSetFestnetz();
    }

    /**
     * Anschrift: Adresse ohne Festnetz
     */
    private boolean isSetAnyAnschriftElement() {
        return checkNotEmpty(adresse.getStrasse())
                || checkNotEmpty(adresse.getHausnummer())
                || checkNotEmpty(adresse.getPlz())
                || checkNotEmpty(adresse.getOrt())
                ;
    }

    private boolean isSetAnyNameElement() {
        return checkNotEmpty(getPerson().getNachname())
                || checkNotEmpty(getPerson().getVorname())
                ;
    }

    private boolean isSetName() {
        return checkNotEmpty(getPerson().getNachname())
                && checkNotEmpty(getPerson().getVorname())
                ;
    }

    private boolean isSetFestnetz() {
        return checkNotEmpty(adresse.getFestnetz());
    }

    private boolean isSetAnschrift() {
        return checkNotEmpty(adresse.getStrasse())
                && checkNotEmpty(adresse.getPlz())
                && checkNotEmpty(adresse.getOrt())
                ;
    }

    @Override
    public Adresse getAdresse() {
        return adresse;
    }

    @Override
    public boolean isEmpty() {
        return !(isSetAnyNameElement() || isSetAnyAdresseElement());
    }

}
