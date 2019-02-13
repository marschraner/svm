package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.utils.EmailValidator;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Calendar;

import static ch.metzenthin.svm.common.utils.Converter.*;
import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
abstract class PersonModelImpl extends AbstractModel implements PersonModel {

    private final Adresse adresse;

    private EmailValidator emailValidator = new EmailValidator();

    PersonModelImpl() {
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
            },
            new BindestrichLeerzeichenFormatter()
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
            },
            new BindestrichLeerzeichenFormatter()
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

    final StringModelAttribute emailModelAttribute = new StringModelAttribute(
            this,
            Field.EMAIL, 1, 150,
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
        if (!isBulkUpdate() && checkNotEmpty(email) && !isEmailValid(email)) {
            invalidate();
            String errMsg = "Keine gültige E-Mail";
            if (email.trim().contains(" ") && email.contains("@") && !email.contains(",") && !email.contains(";")) {
                errMsg = "Mehrere E-Mails müssen durch Kommas getrennt werden";
            }
            throw new SvmValidationException(10001, errMsg, Field.EMAIL);
        }
        emailModelAttribute.setNewValue(false, email, isBulkUpdate());
    }

    private boolean isEmailValid(String email) {
        // emailAdresse enthält möglicherweise mehrere, durch Komma getrennte Emails
        String[] emailAdressenSplitted = email.split("[,;]\\p{Blank}*");
        for (String emailAdresseSplitted : emailAdressenSplitted) {
            if (!emailValidator.isValid(emailAdresseSplitted.trim())) {
                return false;
            }
        }
        return true;
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
        strasseHausnummerModelAttribute.setNewValue(false, strasseHausnummer, isBulkUpdate());
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
            },
            new BindestrichLeerzeichenFormatter()
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
                throw new SvmValidationException(2000, "Nachname und Vorname obligatorisch", Field.NACHNAME);
            }
            if (!isSetAdresse()) {
                throw new SvmValidationException(2001, "Adresse ist obligatorisch", Field.PLZ);
            }
        } else {
            if (isSetAnyNameElement() && !isSetName()) {
                throw new SvmValidationException(2002, "Nachname und Vorname müssen zusammen angegeben werden", Field.NACHNAME);
            }
            if (isSetAnyAdresseElement()) {
                if (!isSetName()) {
                    throw new SvmValidationException(2003, "Nachname und Vorname müssen angegeben werden, wenn eine Adresse vorhanden ist", Field.NACHNAME);
                }
                if (!isSetAdresse()) {
                    throw new SvmValidationException(2004, "Adresse ist unvollständig", Field.PLZ);
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
        return checkNotEmpty(adresse.getPlz())
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
