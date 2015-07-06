package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.MonatsstatistikSchuelerSuchenCommand;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.entities.Adresse;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Anmeldung;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class MonatsstatistikModelImpl extends AbstractModel implements MonatsstatistikModel {

    private static Calendar MONAT_JAHR_INIT;
    private static final AnAbmeldungenDispensationenSelected AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT = AnAbmeldungenDispensationenSelected.ANMELDUNGEN;

    private final CommandInvoker commandInvoker;
    private Calendar monatJahr = MONAT_JAHR_INIT;
    private AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen = AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT;

    public MonatsstatistikModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        this.commandInvoker = commandInvoker;
    }

    private final CalendarModelAttribute monatJahrModelAttribute = new CalendarModelAttribute(
            this,
            Field.MONAT_JAHR, new GregorianCalendar(2000, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return monatJahr;
                }

                @Override
                public void setValue(Calendar value) {
                    monatJahr = value;
                }
            }
    );

    static {
        MONAT_JAHR_INIT = new GregorianCalendar();
        MONAT_JAHR_INIT.add(Calendar.MONTH, -1);
    }

    @Override
    public Calendar getMonatJahr() {
        return monatJahrModelAttribute.getValue();
    }

    @Override
    public Calendar getMonatJahrInit() {
        return MONAT_JAHR_INIT;
    }

    @Override
    public void setMonatJahr(String monatJahr) throws SvmValidationException {
        monatJahrModelAttribute.setNewValue(true, monatJahr, MONAT_JAHR_DATE_FORMAT_STRING);
    }

    @Override
    public AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationen() {
        return anAbmeldungenDispensationen;
    }

    @Override
    public AnAbmeldungenDispensationenSelected getAnAbmeldungenDispensationenInit() {
        return AN_ABMELDUNGEN_DISPENSATIONEN_SELECTED_INIT;
    }

    @Override
    public void setAnAbmeldungenDispensationen(AnAbmeldungenDispensationenSelected anAbmeldungenDispensationen) {
        AnAbmeldungenDispensationenSelected oldValue = this.anAbmeldungenDispensationen;
        this.anAbmeldungenDispensationen = anAbmeldungenDispensationen;
        firePropertyChange(Field.AN_ABMELDUNGEN_DISPENSATIONEN, oldValue, this.anAbmeldungenDispensationen);
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
        MonatsstatistikSchuelerSuchenCommand monatsstatistikSchuelerSuchenCommand = new MonatsstatistikSchuelerSuchenCommand(this);
        try {
            commandInvoker.executeCommand(monatsstatistikSchuelerSuchenCommand);
        } catch (SvmDbException e) {
            //TODO
            e.printStackTrace();
        }
        //schuelerList = monatsstatistikSchuelerSuchenCommand.getSchuelerFound();
        return new SchuelerSuchenResult(schuelerList);
    }

    @Override
    public boolean isCompleted() {
        return monatJahr != null;
    }

    @Override
    void doValidate() throws SvmValidationException {
        if (monatJahr == null) {
            throw new SvmValidationException(2000, "Monat/Jahr obligatorisch", Field.MONAT_JAHR);
        }
    }
}
