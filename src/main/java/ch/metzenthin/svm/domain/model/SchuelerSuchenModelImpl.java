package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SchuelerSuchenCommand;
import ch.metzenthin.svm.persistence.SvmDbException;
import ch.metzenthin.svm.persistence.entities.PersonSuchen;
import ch.metzenthin.svm.persistence.entities.Schueler;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static ch.metzenthin.svm.common.utils.Converter.*;

/**
 * @author Martin Schraner
 */
final class SchuelerSuchenModelImpl extends PersonModelImpl implements SchuelerSuchenModel {

    private static final RolleSelected ROLLE_INIT = RolleSelected.SCHUELER;
    private static final AnmeldestatusSelected ANMELDESTATUS_INIT = AnmeldestatusSelected.ANGEMELDET;
    private static final DispensationSelected DISPENSATION_INIT = DispensationSelected.ALLE;
    private static final GeschlechtSelected GESCHLECHT_INIT = GeschlechtSelected.ALLE;
    private static final Calendar STICHTAG_INIT = new GregorianCalendar();

    private final PersonSuchen person;
    private RolleSelected rolle = ROLLE_INIT;
    private AnmeldestatusSelected anmeldestatus = ANMELDESTATUS_INIT;
    private DispensationSelected dispensation = DISPENSATION_INIT;
    private GeschlechtSelected geschlecht = GESCHLECHT_INIT;
    private Calendar geburtsdatumSuchperiodeBeginn;
    private Calendar geburtsdatumSuchperiodeEnde;
    private String geburtsdatumSuchperiodeDateFormatString;
    private Calendar stichtag = STICHTAG_INIT;

    SchuelerSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
        this.person = new PersonSuchen();
    }

    @Override
    public PersonSuchen getPerson() {
        return person;
    }

    @Override
    public String getGeburtsdatumSuchperiode() {
        if (geburtsdatumSuchperiodeBeginn == null && geburtsdatumSuchperiodeEnde == null) {
            return "";
        } else if (geburtsdatumSuchperiodeEnde == null) {
            return asString(getGeburtsdatumSuchperiodeBeginn(), geburtsdatumSuchperiodeDateFormatString);
        } else {
            return asString(getGeburtsdatumSuchperiodeBeginn(), geburtsdatumSuchperiodeDateFormatString) + " - " + asString(getGeburtsdatumSuchperiodeEnde(), geburtsdatumSuchperiodeDateFormatString);
        }
    }

    @Override
    public void setGeburtsdatumSuchperiode(String geburtsdatumSuchperiode) throws SvmValidationException {
        try {
            geburtsdatumSuchperiodeDateFormatString = getPeriodeDateFormatString(geburtsdatumSuchperiode);
            setGeburtsdatumSuchperiodeBeginn(getPeriodeBeginn(geburtsdatumSuchperiode));
            setGeburtsdatumSuchperiodeEnde(getPeriodeEnde(geburtsdatumSuchperiode));
        } catch (ParseException e) {
            invalidate();
            throw new SvmValidationException(1200, e.getMessage(), Field.GEBURTSDATUM_SUCHPERIODE);
        }
    }

    private final CalendarModelAttribute geburtsdatumSuchperiodeBeginnAttribute = new CalendarModelAttribute(
            this,
            Field.GEBURTSDATUM_SUCHPERIODE, new GregorianCalendar(1980, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return geburtsdatumSuchperiodeBeginn;
                }

                @Override
                public void setValue(Calendar value) {
                    geburtsdatumSuchperiodeBeginn = value;
                }
            }
    );

    @Override
    public Calendar getGeburtsdatumSuchperiodeBeginn() {
        return geburtsdatumSuchperiodeBeginnAttribute.getValue();
    }

    public void setGeburtsdatumSuchperiodeBeginn(String geburtsdatumSuchperiodeBeginn) throws SvmValidationException {
        geburtsdatumSuchperiodeBeginnAttribute.setNewValue(false, geburtsdatumSuchperiodeBeginn, geburtsdatumSuchperiodeDateFormatString);
    }

    private final CalendarModelAttribute geburtsdatumSuchperiodeEndeAttribute = new CalendarModelAttribute(
            this,
            Field.GEBURTSDATUM_SUCHPERIODE, new GregorianCalendar(1980, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return geburtsdatumSuchperiodeEnde;
                }

                @Override
                public void setValue(Calendar value) {
                    geburtsdatumSuchperiodeEnde = value;
                }
            }
    );

    @Override
    public Calendar getGeburtsdatumSuchperiodeEnde() {
        return geburtsdatumSuchperiodeEndeAttribute.getValue();
    }

    public void setGeburtsdatumSuchperiodeEnde(String geburtsdatumSuchperiodeEnde) throws SvmValidationException {
        geburtsdatumSuchperiodeEndeAttribute.setNewValue(false, geburtsdatumSuchperiodeEnde, geburtsdatumSuchperiodeDateFormatString);
    }

    @Override
    public String getGeburtsdatumSuchperiodeDateFormatString() {
        return geburtsdatumSuchperiodeDateFormatString;
    }

    @Override
    public RolleSelected getRolle() {
        return rolle;
    }

    @Override
    public RolleSelected getRolleInit() {
        return ROLLE_INIT;
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

    @Override
    public AnmeldestatusSelected getAnmeldestatusInit() {
        return ANMELDESTATUS_INIT;
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
    public DispensationSelected getDispensationInit() {
        return DISPENSATION_INIT;
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
    public GeschlechtSelected getGeschlechtInit() {
        return GESCHLECHT_INIT;
    }

    @Override
    public void setGeschlecht(GeschlechtSelected geschlecht) {
        GeschlechtSelected oldValue = this.geschlecht;
        this.geschlecht = geschlecht;
        firePropertyChange(Field.GESCHLECHT, oldValue, this.geschlecht);
    }

    private final CalendarModelAttribute stichtagModelAttribute = new CalendarModelAttribute(
            this,
            Field.STICHTAG, new GregorianCalendar(2000, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return stichtag;
                }

                @Override
                public void setValue(Calendar value) {
                    stichtag = value;
                }
            }
    );

    @Override
    public Calendar getStichtag() {
        return stichtagModelAttribute.getValue();
    }

    @Override
    public Calendar getStichtagInit() {
        return STICHTAG_INIT;
    }

    @Override
    public void setStichtag(String stichtag) throws SvmValidationException {
        stichtagModelAttribute.setNewValue(true, stichtag);
    }

    @Override
    public SchuelerSuchenResult suchen() throws SvmDbException {
        SchuelerSuchenCommand schuelerSuchenCommand = new SchuelerSuchenCommand(this);
        CommandInvoker commandInvoker = getCommandInvoker();
        try {
            commandInvoker.openSession();
            commandInvoker.executeCommandWithinSession(schuelerSuchenCommand);
        } catch (Throwable e) {
            throw new SvmDbException("Fehler beim Ausführen eines DB-Commands", e);
        }
        List<Schueler> schuelerList = schuelerSuchenCommand.getSchuelerFound();
        return new SchuelerSuchenResult(schuelerList);
    }

    @Override
    public void invalidateGeburtsdatumSuchperiode() {
        geburtsdatumSuchperiodeBeginnAttribute.initValue(null, geburtsdatumSuchperiodeDateFormatString);
    }

    @Override
    public boolean isCompleted() {
        return stichtag != null;
    }

    @Override
    public void doValidate() throws SvmValidationException {
        if (stichtag == null) {
            throw new SvmValidationException(2000, "Stichtag obligatorisch", Field.STICHTAG);
        }
    }

    @Override
    public boolean isAdresseRequired() {
        return false;
    }
}
