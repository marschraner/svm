package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.getNYearsBeforeNow;
import static ch.metzenthin.svm.common.utils.Converter.strasseHausnummerGetHausnummer;
import static ch.metzenthin.svm.common.utils.Converter.strasseHausnummerGetStrasse;
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
    public void setAnrede(Anrede anrede) throws SvmRequiredException {
        Anrede oldValue = getPerson().getAnrede();
        getPerson().setAnrede(anrede);
        firePropertyChange(Field.ANREDE, oldValue, getPerson().getAnrede());
        if (anrede == null) {
            invalidate();
            throw new SvmRequiredException(Field.ANREDE);
        }
    }

    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 1, 50,
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
        nachnameModelAttribute.setNewValue(isAdresseRequired(), nachname, isBulkUpdate());
    }

    private final StringModelAttribute vornameModelAttribute = new StringModelAttribute(
            this,
            Field.VORNAME, 1, 50,
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
        vornameModelAttribute.setNewValue(isAdresseRequired(), vorname, isBulkUpdate());
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
        natelModelAttribute.setNewValue(false, natel, isBulkUpdate());
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
        emailModelAttribute.setNewValue(false, email, isBulkUpdate());
    }

    protected Calendar getEarliestValidDateGeburtstag() {
        return getNYearsBeforeNow(80);
    }

    protected Calendar getLatestValidDateGeburtstag() {
        return getNYearsBeforeNow(2);
    }

    private CalendarModelAttribute geburtsdatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.GEBURTSDATUM, getEarliestValidDateGeburtstag(), getLatestValidDateGeburtstag(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return getPerson().getGeburtsdatum();
                }

                @Override
                public void setValue(Calendar value) {
                    getPerson().setGeburtsdatum(value);
                }
            }
    );

    @Override
    public Calendar getGeburtsdatum() {
        return geburtsdatumModelAttribute.getValue();
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        geburtsdatumModelAttribute.setNewValue(false, geburtsdatum, isBulkUpdate());
    }

    private final StringModelAttribute strasseHausnummerModelAttribute = new StringModelAttribute(
            this,
            Field.STRASSE_HAUSNUMMER, 1, 50,
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
        strasseHausnummerModelAttribute.setNewValue(isAdresseRequired(), strasseHausnummer, isBulkUpdate());
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
        plzModelAttribute.setNewValue(isAdresseRequired(), plz, isBulkUpdate());
    }

    private final StringModelAttribute ortModelAttribute = new StringModelAttribute(
            this,
            Field.ORT, 1, 50,
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
        ortModelAttribute.setNewValue(isAdresseRequired(), ort, isBulkUpdate());
    }

    private final StringModelAttribute festnetzModelAttribute = new StringModelAttribute(
            this,
            Field.FESTNETZ, 13, 20,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return getPerson().getFestnetz();
                }

                @Override
                public void setValue(String value) {
                    getPerson().setFestnetz(value);
                }
            }
    );

    @Override
    public String getFestnetz() {
        return festnetzModelAttribute.getValue();
    }

    @Override
    public void setFestnetz(String festnetz) throws SvmValidationException {
        festnetzModelAttribute.setNewValue(false, festnetz, isBulkUpdate());
    }

    @Override
    public void initAdresse(AdresseModel adresseModel) {
        if (isBulkUpdate()) {
            return;
        }
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
            return isSetName() && isSetAdresse();
        }
        return (!isSetAnyNameElement() || isSetName()) && (!isSetAnyAdresseElement() || (isSetName() && isSetAdresse()));
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (isAdresseRequired()) {
            if (!isSetName()) {
                throw new SvmValidationException(2000, "Nachname und Vorname obligatorisch", Field.NACHNAME, Field.VORNAME);
            }
            if (!isSetAdresse()) {
                throw new SvmValidationException(2001, "Adresse ist obligatorisch", Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT);
            }
        } else {
            if (isSetAnyNameElement() && !isSetName()) {
                throw new SvmValidationException(2002, "Nachname und Vorname müssen zusammen angegeben werden", Field.NACHNAME, Field.VORNAME);
            }
            if (isSetAnyAdresseElement()) {
                if (!isSetName()) {
                    throw new SvmValidationException(2003, "Nachname und Vorname müssen angegeben werden, wenn eine Adresse vorhanden ist", Field.NACHNAME, Field.VORNAME);
                }
                if (!isSetAdresse()) {
                    throw new SvmValidationException(2004, "Adresse ist unvollständig", Field.STRASSE_HAUSNUMMER, Field.PLZ, Field.ORT);
                }
            }
        }
    }

    private boolean isSetAnyAdresseElement() {
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

    private boolean isSetAdresse() {
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
