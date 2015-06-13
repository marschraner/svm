package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Person;

import java.text.ParseException;
import java.util.Calendar;

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
    public String getNachname() {
        return getPerson().getNachname();
    }

    @Override
    public String getVorname() {
        return getPerson().getVorname();
    }

    @Override
    public String getNatel() {
        return getPerson().getNatel();
    }

    @Override
    public String getEmail() {
        return getPerson().getEmail();
    }

    @Override
    public Calendar getGeburtsdatum() {
        return getPerson().getGeburtsdatum();
    }

    @Override
    public void setAnrede(Anrede anrede) {
        Anrede oldValue = getPerson().getAnrede();
        getPerson().setAnrede(anrede);
        firePropertyChange("Anrede", oldValue, getPerson().getAnrede());
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        if (isAdresseRequired() && !checkNotEmpty(nachname)) {
            invalidate();
            throw new SvmRequiredException("Nachname");
        }
        if (checkNotEmpty(nachname) && nachname.length() < 2) {
            invalidate();
            throw new SvmValidationException(1100, "Länge muss mindestens " + 2 + " sein");
        }
        String oldValue = getPerson().getNachname();
        getPerson().setNachname(nachname);
        firePropertyChange("Nachname", oldValue, getPerson().getNachname());
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        if (isAdresseRequired() && !checkNotEmpty(vorname)) {
            invalidate();
            throw new SvmRequiredException("Vorname");
        }
        if (checkNotEmpty(vorname) && vorname.length() < 2) {
            invalidate();
            throw new SvmValidationException(1100, "Länge muss mindestens " + 2 + " sein");
        }
        String oldValue = getPerson().getVorname();
        getPerson().setVorname(vorname);
        firePropertyChange("Vorname", oldValue, getPerson().getVorname());
    }

    @Override
    public void setNatel(String natel) {
        String oldValue = getPerson().getNatel();
        getPerson().setNatel(natel);
        firePropertyChange("Natel", oldValue, getPerson().getNatel());
    }

    @Override
    public void setEmail(String email) {
        String oldValue = getPerson().getEmail();
        getPerson().setEmail(email);
        firePropertyChange("Email", oldValue, getPerson().getEmail());
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) throws SvmValidationException {
        try {
            setGeburtsdatum(toCalendar(geburtsdatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1200, "Geburtsdatum ist falsch formatiert. Es wird TT.MM.JJJJ erwartet");
        }
    }

    @Override
    public void setGeburtsdatum(Calendar geburtsdatum) {
        Calendar oldValue = getPerson().getGeburtsdatum();
        getPerson().setGeburtsdatum(geburtsdatum);
        firePropertyChange("Geburtsdatum", oldValue, getPerson().getGeburtsdatum());
    }

    @Override
    public String getStrasse() {
        return adresse.getStrasse();
    }

    @Override
    public String getHausnummer() {
        return adresse.getHausnummer();
    }

    @Override
    public String getPlz() {
        return adresse.getPlz();
    }

    @Override
    public String getOrt() {
        return adresse.getOrt();
    }

    @Override
    public String getFestnetz() {
        return adresse.getFestnetz();
    }

    @Override
    public void setStrasse(String strasse) throws SvmRequiredException {
        if (isAdresseRequired() && !checkNotEmpty(strasse)) {
            invalidate();
            throw new SvmRequiredException("Strasse");
        }
        String oldValue = adresse.getStrasse();
        adresse.setStrasse(strasse);
        firePropertyChange("Strasse", oldValue, adresse.getStrasse());
    }

    @Override
    public void setHausnummer(String hausnummer) {
        String oldValue = adresse.getHausnummer();
        adresse.setHausnummer(hausnummer);
        firePropertyChange("Hausnummer", oldValue, adresse.getHausnummer());
    }

    @Override
    public void setPlz(String plz) throws SvmRequiredException {
        if (isAdresseRequired() && !checkNotEmpty(plz)) {
            invalidate();
            throw new SvmRequiredException("Plz");
        }
        String oldValue = adresse.getPlz();
        adresse.setPlz(plz);
        firePropertyChange("Plz", oldValue, adresse.getPlz());
    }

    @Override
    public void setOrt(String ort) throws SvmRequiredException {
        if (isAdresseRequired() && !checkNotEmpty(ort)) {
            invalidate();
            throw new SvmRequiredException("Ort");
        }
        String oldValue = adresse.getOrt();
        adresse.setOrt(ort);
        firePropertyChange("Ort", oldValue, adresse.getOrt());
    }

    @Override
    public void setFestnetz(String festnetz) {
        String oldValue = adresse.getFestnetz();
        adresse.setFestnetz(festnetz);
        firePropertyChange("Festnetz", oldValue, adresse.getFestnetz());
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
                throw new SvmValidationException(2000, "Nachname und Vorname obligatorisch", "Nachname", "Vorname");
            }
            if (!isSetAnschrift()) {
                throw new SvmValidationException(2001, "Anschrift ist obligatorisch", "Strasse", "Hausnummer", "Plz", "Ort", "Festnetz");
            }
        } else {
            if (isSetAnyNameElement() && !isSetName()) {
                throw new SvmValidationException(2002, "Nachname und Vorname müssen zusammen angegeben werden", "Nachname", "Vorname");
            }
            if (isSetAnyAnschriftElement()) {
                if (!isSetName()) {
                    throw new SvmValidationException(2003, "Nachname und Vorname müssen angegeben werden, wenn eine Anschrift vorhanden ist", "Nachname", "Vorname");
                }
                if (!isSetAnschrift()) {
                    throw new SvmValidationException(2004, "Anschrift ist unvollständig", "Strasse", "Hausnummer", "Plz", "Ort", "Festnetz");
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
