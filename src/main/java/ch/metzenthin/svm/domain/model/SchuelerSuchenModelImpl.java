package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.ui.control.CompletedListener;
import ch.metzenthin.svm.ui.control.DisableFieldsListener;
import ch.metzenthin.svm.ui.control.MakeErrorLabelsInvisibleListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.*;

import static ch.metzenthin.svm.common.utils.Converter.*;

/**
 * @author Martin Schraner
 */
final class SchuelerSuchenModelImpl implements SchuelerSuchenModel {

    private final Schueler schueler;
    private final Angehoeriger angehoeriger;
    private final Adresse adresse;
    private final Anmeldung anmeldung;
    private Calendar stichtag;

    SchuelerSuchenModelImpl(CommandInvoker commandInvoker) {
        schueler = new Schueler();
        angehoeriger = new Angehoeriger();
        adresse = new Adresse();
        anmeldung = new Anmeldung();
    }

    @Override
    public void setNachname(String nachname) {
        schueler.setNachname(nachname);
        angehoeriger.setNachname(nachname);
    }

    @Override
    public void setVorname(String vorname) {
        schueler.setVorname(vorname);
        angehoeriger.setVorname(vorname);
    }

    @Override
    public void setStrasseHausnummer(String strasseHausnummer) {
        adresse.setStrasse(strasseHausnummerGetStrasse(strasseHausnummer));
        adresse.setHausnummer(strasseHausnummerGetHausnummer(strasseHausnummer));
    }

    @Override
    public void setPlz(String plz) {
        adresse.setPlz(plz);
    }

    @Override
    public void setOrt(String ort) {
        adresse.setOrt(ort);
    }

    @Override
    public void setFestnetz(String festnetz) {
        adresse.setFestnetz(festnetz);
    }

    @Override
    public void setNatel(String natel) {
        schueler.setNatel(natel);
        angehoeriger.setNatel(natel);
    }

    @Override
    public void setEmail(String email) {
        schueler.setEmail(email);
        angehoeriger.setEmail(email);
    }

    @Override
    public void setGeschlecht(Geschlecht geschlecht) {
        schueler.setGeschlecht(geschlecht);
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
        schueler.setGeburtsdatum(geburtsdatum);
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
        this.stichtag = stichtag;
    }

    @Override
    public void setAnmeldemonat(String anmeldemonat) throws SvmValidationException {
        try {
            setAnmeldemonat(toCalendar(anmeldemonat));
        } catch (ParseException e) {
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.ANMELDEDATUM);
        }
    }

    @Override
    public void setAnmeldemonat(Calendar anmeldemonat) {
        anmeldung.setAnmeldedatum(anmeldemonat);
    }


    @Override
    public void setAbmeldemonat(String abmeldedatum) throws SvmValidationException {
        try {
            setAbmeldemonat(toCalendar(abmeldedatum));
        } catch (ParseException e) {
            throw new SvmValidationException(1201, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.ABMELDEDATUM);
        }
    }

    @Override
    public void setAbmeldemonat(Calendar abmeldedatum) {
        anmeldung.setAbmeldedatum(abmeldedatum);
    }

    @Override
    public Schueler getSchueler() {
        return schueler;
    }

    @Override
    public Angehoeriger getAngehoeriger() {
        return angehoeriger;
    }

    @Override
    public Adresse getAdresse() {
        return adresse;
    }

    @Override
    public Anmeldung getAnmeldung() {
        return anmeldung;
    }

    @Override
    public Calendar getStichtag() {
        return stichtag;
    }

    @Override
    public SchuelerSuchenResult suchen() {
        List<Schueler> schuelerList = new ArrayList<>();
        schuelerList.add(new Schueler("Leu", "Lea", new GregorianCalendar(2010, Calendar.APRIL, 15), null, null, null, null));
        schuelerList.add(new Schueler("Keller", "Urs", new GregorianCalendar(2000, Calendar.JANUARY, 31), null, null, null, null));
        SchuelerSuchenResult schuelerSuchenResult = new SchuelerSuchenResult(schuelerList);
        return schuelerSuchenResult;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @Override
    public void addCompletedListener(CompletedListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public boolean checkIsFieldChange(Field field, PropertyChangeEvent evt) {
        return false;
    }

    @Override
    public void addDisableFieldsListener(DisableFieldsListener listener) {

    }

    @Override
    public void removeDisableFieldsListener(DisableFieldsListener listener) {

    }

    @Override
    public void disableFields() {

    }

    @Override
    public void disableFields(Set<Field> fields) {

    }

    @Override
    public void enableFields() {

    }

    @Override
    public void enableFields(Set<Field> fields) {

    }

    @Override
    public void addMakeErrorLabelsInvisibleListener(MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {

    }

    @Override
    public void removeMakeErrorLabelsInvisibleListener(MakeErrorLabelsInvisibleListener makeErrorLabelsInvisibleListener) {

    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {

    }

    @Override
    public void initializeCompleted() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void validate() throws SvmValidationException {

    }
}
