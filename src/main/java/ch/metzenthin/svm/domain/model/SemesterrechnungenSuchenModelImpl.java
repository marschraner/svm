package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Martin Schraner
 */
final class SemesterrechnungenenSuchenModelImpl extends AbstractModel implements SemesterrechnungenSuchenModel {

    private Semester semester;
    private String nachname;
    private String vorname;
    private RolleSelected rolle;
    private SemesterrechnungCode semesterrechnungCode;
    private Stipendium stipendium;
    private Boolean gratiskind;
    private RechnungsdatumSelected rechnungsdatumSelected;
    private Calendar rechnungsdatum;
    private RechnungsstatusSelected rechnungsstatus;
    private BigDecimal wochenbetrag;
    private BigDecimal schulgeld;

    SemesterrechnungenenSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
    }

    static {
        SEMESTERRECHNUNG_CODE_ALLE.setKuerzel("");
        SEMESTERRECHNUNG_CODE_ALLE.setBeschreibung("");
    }

    @Override
    public Semester getSemester() {
        return semester;
    }

    @Override
    public void setSemester(Semester semester) {
        Semester oldValue = this.semester;
        this.semester = semester;
        firePropertyChange(Field.SEMESTER, oldValue, this.semester);
    }

    private final StringModelAttribute nachnameModelAttribute = new StringModelAttribute(
            this,
            Field.NACHNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return nachname;
                }

                @Override
                public void setValue(String value) {
                    nachname = value;
                }
            }
    );

    @Override
    public String getNachname() {
        return nachnameModelAttribute.getValue();
    }

    @Override
    public void setNachname(String nachname) throws SvmValidationException {
        nachnameModelAttribute.setNewValue(false, nachname, isBulkUpdate());
    }

    private final StringModelAttribute vornameModelAttribute = new StringModelAttribute(
            this,
            Field.VORNAME, 2, 50,
            new AttributeAccessor<String>() {
                @Override
                public String getValue() {
                    return vorname;
                }

                @Override
                public void setValue(String value) {
                    vorname = value;
                }
            }
    );

    @Override
    public String getVorname() {
        return vornameModelAttribute.getValue();
    }

    @Override
    public void setVorname(String vorname) throws SvmValidationException {
        vornameModelAttribute.setNewValue(false, vorname, isBulkUpdate());
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
    public SemesterrechnungCode getSemesterrechnungCode() {
        return semesterrechnungCode;
    }

    @Override
    public void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode) {
        SemesterrechnungCode oldValue = this.semesterrechnungCode;
        this.semesterrechnungCode = semesterrechnungCode;
        firePropertyChange(Field.SEMESTERRECHNUNG_CODE, oldValue, this.semesterrechnungCode);
    }

    @Override
    public Stipendium getStipendium() {
        return stipendium;
    }

    @Override
    public void setStipendium(Stipendium stipendium) {
        Stipendium oldValue = this.stipendium;
        this.stipendium = stipendium;
        firePropertyChange(Field.STIPENDIUM, oldValue, this.stipendium);
    }

    @Override
    public Boolean isGratiskind() {
        return gratiskind;
    }

    @Override
    public void setGratiskind(Boolean gratiskind) {
        Boolean oldValue = this.gratiskind;
        this.gratiskind = gratiskind;
        firePropertyChange(Field.GRATISKIND, oldValue, gratiskind);
    }

    @Override
    public RechnungsdatumSelected getRechnungsdatumSelected() {
        return rechnungsdatumSelected;
    }

    @Override
    public void setRechnungsdatumSelected(RechnungsdatumSelected rechnungsdatumSelected) {
        RechnungsdatumSelected oldValue = this.rechnungsdatumSelected;
        this.rechnungsdatumSelected = rechnungsdatumSelected;
        firePropertyChange(Field.RECHNUNGSDATUM_SELECTED, oldValue, this.rechnungsdatumSelected);
    }

    private CalendarModelAttribute rechnungsdatumModelAttribute = new CalendarModelAttribute(
            this,
            Field.RECHNUNGSDATUM, new GregorianCalendar(1920, Calendar.JANUARY, 1), new GregorianCalendar(),
            new AttributeAccessor<Calendar>() {
                @Override
                public Calendar getValue() {
                    return rechnungsdatum;
                }

                @Override
                public void setValue(Calendar value) {
                    rechnungsdatum = value;
                }
            }
    );

    @Override
    public Calendar getRechnungsdatum() {
        return rechnungsdatumModelAttribute.getValue();
    }

    @Override
    public void setRechnungsdatum(String rechnungsdatum) throws SvmValidationException {
        rechnungsdatumModelAttribute.setNewValue(false, rechnungsdatum, isBulkUpdate());
    }

    @Override
    public RechnungsstatusSelected getRechnungsstatus() {
        return rechnungsstatus;
    }

    @Override
    public void setRechnungsstatus(SemesterrechnungenSuchenModel.RechnungsstatusSelected rechnungsstatus) {
        RechnungsstatusSelected oldValue = this.rechnungsstatus;
        this.rechnungsstatus = rechnungsstatus;
        firePropertyChange(Field.RECHNUNGSSTATUS, oldValue, this.rechnungsstatus);
    }

    private final PreisModelAttribute wochenbetragModelAttribute = new PreisModelAttribute(
            this,
            Field.WOCHENBETRAG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return wochenbetrag;
                }

                @Override
                public void setValue(BigDecimal value) {
                    wochenbetrag = value;
                }
            }
    );

    @Override
    public BigDecimal getWochenbetrag() {
        return wochenbetragModelAttribute.getValue();
    }

    @Override
    public void setWochenbetrag(String wochenbetrag) throws SvmValidationException {
        wochenbetragModelAttribute.setNewValue(false, wochenbetrag, isBulkUpdate());
    }

    private final PreisModelAttribute schulgeldModelAttribute = new PreisModelAttribute(
            this,
            Field.SCHULGELD, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return schulgeld;
                }

                @Override
                public void setValue(BigDecimal value) {
                    schulgeld = value;
                }
            }
    );

    @Override
    public BigDecimal getSchulgeld() {
        return schulgeldModelAttribute.getValue();
    }

    @Override
    public void setSchulgeld(String schulgeld) throws SvmValidationException {
        schulgeldModelAttribute.setNewValue(false, schulgeld, isBulkUpdate());
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }
}
