package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

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
public class AnmeldungenStatistikModelImpl extends AbstractModel implements AnmeldungenStatistikModel {

    private final CommandInvoker commandInvoker;
    private Calendar anAbmeldemonat;
    private AnAbmeldungenSelected anAbmeldungen;

    public AnmeldungenStatistikModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        this.commandInvoker = commandInvoker;
    }

    @Override
    public Calendar getAnAbmeldemonat() {
        return anAbmeldemonat;
    }

    @Override
    public void setAnAbmeldemonat(String anAbmeldemonat) throws SvmValidationException {
        if (!checkNotEmpty(anAbmeldemonat)) {
            invalidate();
            throw new SvmRequiredException(Field.AN_ABMELDEMONAT);
        }
        try {
            setAnAbmeldemonat(toCalendar(anAbmeldemonat));
        } catch (ParseException e) {
            invalidate();
            throw new SvmValidationException(1200, "Es wird ein gültige Datum im Format TT.MM.JJJJ erwartet", Field.AN_ABMELDEMONAT);
        }
    }

    @Override
    public void setAnAbmeldemonat(Calendar anAbmeldemonat) {
        Calendar oldValue = this.anAbmeldemonat;
        this.anAbmeldemonat = anAbmeldemonat;
        firePropertyChange(Field.STICHTAG, oldValue, this.anAbmeldemonat);
    }

    @Override
    public AnAbmeldungenSelected getAnAbmeldungen() {
        return anAbmeldungen;
    }

    @Override
    public void setAnAbmeldungen(AnAbmeldungenSelected anAbmeldungen) {
        AnAbmeldungenSelected oldValue = this.anAbmeldungen;
        this.anAbmeldungen = anAbmeldungen;
        firePropertyChange(Field.AN_ABMELDUNGEN, oldValue, this.anAbmeldungen);
    }

    @Override
    public SchuelerSuchenResult suchen() {
        //TODO
        List<Schueler> schuelerList = new ArrayList<>();
        schuelerList.add(new Schueler("Leu", "Lea", new GregorianCalendar(2010, Calendar.APRIL, 15), null, null, null, null));
        schuelerList.add(new Schueler("Keller", "Urs", new GregorianCalendar(2000, Calendar.JANUARY, 31), null, null, null, null));
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
        schueler1.addAnmeldung(new Anmeldung(new GregorianCalendar(2013, Calendar.JANUARY, 1), null));
        schuelerList.add(schueler1);
        return new SchuelerSuchenResult(schuelerList);
    }

    @Override
    public boolean isCompleted() {
        return anAbmeldemonat != null;
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (anAbmeldemonat == null) {
            throw new SvmValidationException(2000, "An-/Abmeldemonat obligatorisch", Field.STICHTAG);
        }
    }
}
