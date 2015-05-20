package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Person;

import java.util.Calendar;

/**
 * @author Hans Stamm
 */
abstract class PersonModelImpl extends AbstractModel implements PersonModel {

    Adresse adresse;

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
        getPerson().setAnrede(anrede);
    }

    @Override
    public void setNachname(String nachname) {
        getPerson().setNachname(nachname);
    }

    private boolean checkNachname() {
        return checkNotEmpty(getPerson().getNachname());
    }

    @Override
    public void setVorname(String vorname) {
        getPerson().setVorname(vorname);
    }

    private boolean checkVorname() {
        return checkNotEmpty(getPerson().getVorname());
    }

    @Override
    public void setNatel(String natel) {
        getPerson().setNatel(natel);
    }

    @Override
    public void setEmail(String email) {
        getPerson().setEmail(email);
    }

    @Override
    public void setGeburtsdatum(String geburtsdatum) {
        setGeburtsdatum(toCalendarIgnoreException(geburtsdatum));
    }

    @Override
    public void setGeburtsdatum(Calendar geburtsdatum) {
        getPerson().setGeburtsdatum(geburtsdatum);
    }

    @Override
    public String getStrasse() {
        return adresse.getStrasse();
    }

    @Override
    public Integer getHausnummer() {
        return adresse.getHausnummer();
    }

    @Override
    public Integer getPlz() {
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
    public void setStrasse(String strasse) {
        adresse.setStrasse(strasse);
    }

    private boolean checkStrasse() {
        return checkNotEmpty(adresse.getStrasse());
    }

    @Override
    public void setHausnummer(String hausnummerString) {
        setHausnummer(toIntegerOrNull(hausnummerString));
    }

    @Override
    public void setHausnummer(Integer hausnummer) {
        adresse.setHausnummer(hausnummer);
    }

    @Override
    public void setPlz(Integer plz) {
        adresse.setPlz(plz);
    }

    @Override
    public void setPlz(String plzString) {
        setPlz(toIntegerOrNull(plzString));
    }

    private boolean checkPlz() {
        return (adresse.getPlz() != null) && (adresse.getPlz() > 0);
    }

    @Override
    public void setOrt(String ort) {
        adresse.setOrt(ort);
    }

    private boolean checkOrt() {
        return checkNotEmpty(adresse.getOrt());
    }

    @Override
    public void setFestnetz(String festnetz) {
        adresse.setFestnetz(festnetz);
    }

    @Override
    public boolean isValid() {
        return checkNachname() && checkVorname() && checkStrasse() && checkPlz() && checkOrt();
    }

    @Override
    public Adresse getAdresse() {
        return adresse;
    }

}
