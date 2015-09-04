package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.FindOrCreateSemesterrechnungenCommand;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.math.BigDecimal;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class SemesterrechnungenSuchenModelImpl extends SemesterrechnungModelImpl implements SemesterrechnungenSuchenModel {

    private Semester semester;
    private String nachname;
    private String vorname;
    private RolleSelected rolle;
    private PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected;
    private PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected;
    private PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected;
    private PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected;
    private PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected;
    private BigDecimal schulgeldVorrechnung;
    private SechsJahresRabattVorrechnungSelected sechsJahresRabattVorrechnungSelected;
    private VollstaendigkeitVorrechnungSelected vollstaendigkeitVorrechnungSelected;
    private PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected;
    private PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected;
    private PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected;
    private PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected;
    private PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected;
    private PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected;
    private BigDecimal schulgeldNachrechnung;
    private SechsJahresRabattNachrechnungSelected sechsJahresRabattNachrechnungSelected;
    private VollstaendigkeitNachrechnungSelected vollstaendigkeitNachrechnungSelected;
    private PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected;
    private BigDecimal differenzSchulgeld;
    private PraezisierungRestbetragSelected praezisierungRestbetragSelected;
    private BigDecimal restbetrag;

    SemesterrechnungenSuchenModelImpl(CommandInvoker commandInvoker) {
        super(commandInvoker);
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
    public PraezisierungRechnungsdatumVorrechnungSelected getPraezisierungRechnungsdatumVorrechnungSelected() {
        return praezisierungRechnungsdatumVorrechnungSelected;
    }

    @Override
    public void setPraezisierungRechnungsdatumVorrechnungSelected(PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected) {
        PraezisierungRechnungsdatumVorrechnungSelected oldValue = this.praezisierungRechnungsdatumVorrechnungSelected;
        this.praezisierungRechnungsdatumVorrechnungSelected = praezisierungRechnungsdatumVorrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_VORRECHNUNG, oldValue, this.praezisierungRechnungsdatumVorrechnungSelected);
    }

    @Override
    public PraezisierungErmaessigungVorrechnungSelected getPraezisierungErmaessigungVorrechnungSelected() {
        return praezisierungErmaessigungVorrechnungSelected;
    }

    @Override
    public void setPraezisierungErmaessigungVorrechnungSelected(PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected) {
        PraezisierungErmaessigungVorrechnungSelected oldValue = this.praezisierungErmaessigungVorrechnungSelected;
        this.praezisierungErmaessigungVorrechnungSelected = praezisierungErmaessigungVorrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_VORRECHNUNG, oldValue, this.praezisierungErmaessigungVorrechnungSelected);
    }

    @Override
    public PraezisierungZuschlagVorrechnungSelected getPraezisierungZuschlagVorrechnungSelected() {
        return praezisierungZuschlagVorrechnungSelected;
    }

    @Override
    public void setPraezisierungZuschlagVorrechnungSelected(PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected) {
        PraezisierungZuschlagVorrechnungSelected oldValue = this.praezisierungZuschlagVorrechnungSelected;
        this.praezisierungZuschlagVorrechnungSelected = praezisierungZuschlagVorrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_ZUSCHLAG_VORRECHNUNG, oldValue, this.praezisierungZuschlagVorrechnungSelected);
    }

    @Override
    public PraezisierungWochenbetragVorrechnungSelected getPraezisierungWochenbetragVorrechnungSelected() {
        return praezisierungWochenbetragVorrechnungSelected;
    }

    @Override
    public void setPraezisierungWochenbetragVorrechnungSelected(PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected) {
        PraezisierungWochenbetragVorrechnungSelected oldValue = this.praezisierungWochenbetragVorrechnungSelected;
        this.praezisierungWochenbetragVorrechnungSelected = praezisierungWochenbetragVorrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_WOCHENBETRAG_VORRECHNUNG, oldValue, this.praezisierungWochenbetragVorrechnungSelected);
    }

    @Override
    public PraezisierungSchulgeldVorrechnungSelected getPraezisierungSchulgeldVorrechnungSelected() {
        return praezisierungSchulgeldVorrechnungSelected;
    }

    @Override
    public void setPraezisierungSchulgeldVorrechnungSelected(PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected) {
        PraezisierungSchulgeldVorrechnungSelected oldValue = this.praezisierungSchulgeldVorrechnungSelected;
        this.praezisierungSchulgeldVorrechnungSelected = praezisierungSchulgeldVorrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_SCHULGELD_VORRECHNUNG, oldValue, this.praezisierungSchulgeldVorrechnungSelected);
    }

    private final PreisModelAttribute schulgeldVorrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.SCHULGELD_VORRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return schulgeldVorrechnung;
                }

                @Override
                public void setValue(BigDecimal value) {
                    schulgeldVorrechnung = value;
                }
            }
    );

    @Override
    public BigDecimal getSchulgeldVorrechnung() {
        return schulgeldVorrechnungModelAttribute.getValue();
    }

    @Override
    public void setSchulgeldVorrechnung(String schulgeldVorrechnung) throws SvmValidationException {
        schulgeldVorrechnungModelAttribute.setNewValue(false, schulgeldVorrechnung, isBulkUpdate());
    }

    @Override
    public SechsJahresRabattVorrechnungSelected getSechsJahresRabattVorrechnungSelected() {
        return sechsJahresRabattVorrechnungSelected;
    }

    @Override
    public void setSechsJahresRabattVorrechnungSelected(SechsJahresRabattVorrechnungSelected sechsJahresRabattVorrechnungSelected) {
        SechsJahresRabattVorrechnungSelected oldValue = this.sechsJahresRabattVorrechnungSelected;
        this.sechsJahresRabattVorrechnungSelected = sechsJahresRabattVorrechnungSelected;
        firePropertyChange(Field.SECHS_JAHRES_RABATT_VORRECHNUNG, oldValue, this.sechsJahresRabattVorrechnungSelected);
    }

    @Override
    public VollstaendigkeitVorrechnungSelected getVollstaendigkeitVorrechnungSelected() {
        return vollstaendigkeitVorrechnungSelected;
    }

    @Override
    public void setVollstaendigkeitVorrechnungSelected(VollstaendigkeitVorrechnungSelected vollstaendigkeitVorrechnungSelected) {
        VollstaendigkeitVorrechnungSelected oldValue = this.vollstaendigkeitVorrechnungSelected;
        this.vollstaendigkeitVorrechnungSelected = vollstaendigkeitVorrechnungSelected;
        firePropertyChange(Field.VOLLSTAENDIGKEIT_VORRECHNUNG, oldValue, this.vollstaendigkeitVorrechnungSelected);
    }

    @Override
    public PraezisierungRechnungsdatumNachrechnungSelected getPraezisierungRechnungsdatumNachrechnungSelected() {
        return praezisierungRechnungsdatumNachrechnungSelected;
    }

    @Override
    public void setPraezisierungRechnungsdatumNachrechnungSelected(PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected) {
        PraezisierungRechnungsdatumNachrechnungSelected oldValue = this.praezisierungRechnungsdatumNachrechnungSelected;
        this.praezisierungRechnungsdatumNachrechnungSelected = praezisierungRechnungsdatumNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_RECHNUNGSDATUM_NACHRECHNUNG, oldValue, this.praezisierungRechnungsdatumNachrechnungSelected);
    }

    @Override
    public PraezisierungErmaessigungNachrechnungSelected getPraezisierungErmaessigungNachrechnungSelected() {
        return praezisierungErmaessigungNachrechnungSelected;
    }

    @Override
    public void setPraezisierungErmaessigungNachrechnungSelected(PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected) {
        PraezisierungErmaessigungNachrechnungSelected oldValue = this.praezisierungErmaessigungNachrechnungSelected;
        this.praezisierungErmaessigungNachrechnungSelected = praezisierungErmaessigungNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_ERMAESSIGUNG_NACHRECHNUNG, oldValue, this.praezisierungErmaessigungNachrechnungSelected);
    }

    @Override
    public PraezisierungZuschlagNachrechnungSelected getPraezisierungZuschlagNachrechnungSelected() {
        return praezisierungZuschlagNachrechnungSelected;
    }

    @Override
    public void setPraezisierungZuschlagNachrechnungSelected(PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected) {
        PraezisierungZuschlagNachrechnungSelected oldValue = this.praezisierungZuschlagNachrechnungSelected;
        this.praezisierungZuschlagNachrechnungSelected = praezisierungZuschlagNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_ZUSCHLAG_NACHRECHNUNG, oldValue, this.praezisierungZuschlagNachrechnungSelected);
    }

    @Override
    public PraezisierungAnzahlWochenNachrechnungSelected getPraezisierungAnzahlWochenNachrechnungSelected() {
        return praezisierungAnzahlWochenNachrechnungSelected;
    }

    @Override
    public void setPraezisierungAnzahlWochenNachrechnungSelected(PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected) {
        PraezisierungAnzahlWochenNachrechnungSelected oldValue = this.praezisierungAnzahlWochenNachrechnungSelected;
        this.praezisierungAnzahlWochenNachrechnungSelected = praezisierungAnzahlWochenNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_ANZAHL_WOCHEN_NACHRECHNUNG, oldValue, this.praezisierungAnzahlWochenNachrechnungSelected);
    }

    @Override
    public PraezisierungWochenbetragNachrechnungSelected getPraezisierungWochenbetragNachrechnungSelected() {
        return praezisierungWochenbetragNachrechnungSelected;
    }

    @Override
    public void setPraezisierungWochenbetragNachrechnungSelected(PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected) {
        PraezisierungWochenbetragNachrechnungSelected oldValue = this.praezisierungWochenbetragNachrechnungSelected;
        this.praezisierungWochenbetragNachrechnungSelected = praezisierungWochenbetragNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_WOCHENBETRAG_NACHRECHNUNG, oldValue, this.praezisierungWochenbetragNachrechnungSelected);
    }

    @Override
    public PraezisierungSchulgeldNachrechnungSelected getPraezisierungSchulgeldNachrechnungSelected() {
        return praezisierungSchulgeldNachrechnungSelected;
    }

    @Override
    public void setPraezisierungSchulgeldNachrechnungSelected(PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected) {
        PraezisierungSchulgeldNachrechnungSelected oldValue = this.praezisierungSchulgeldNachrechnungSelected;
        this.praezisierungSchulgeldNachrechnungSelected = praezisierungSchulgeldNachrechnungSelected;
        firePropertyChange(Field.PRAEZISIERUNG_SCHULGELD_NACHRECHNUNG, oldValue, this.praezisierungSchulgeldNachrechnungSelected);
    }

    private final PreisModelAttribute schulgeldNachrechnungModelAttribute = new PreisModelAttribute(
            this,
            Field.SCHULGELD_NACHRECHNUNG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return schulgeldNachrechnung;
                }

                @Override
                public void setValue(BigDecimal value) {
                    schulgeldNachrechnung = value;
                }
            }
    );

    @Override
    public BigDecimal getSchulgeldNachrechnung() {
        return schulgeldNachrechnungModelAttribute.getValue();
    }

    @Override
    public void setSchulgeldNachrechnung(String schulgeldNachrechnung) throws SvmValidationException {
        schulgeldNachrechnungModelAttribute.setNewValue(false, schulgeldNachrechnung, isBulkUpdate());
    }

    @Override
    public SechsJahresRabattNachrechnungSelected getSechsJahresRabattNachrechnungSelected() {
        return sechsJahresRabattNachrechnungSelected;
    }

    @Override
    public void setSechsJahresRabattNachrechnungSelected(SechsJahresRabattNachrechnungSelected sechsJahresRabattNachrechnungSelected) {
        SechsJahresRabattNachrechnungSelected oldValue = this.sechsJahresRabattNachrechnungSelected;
        this.sechsJahresRabattNachrechnungSelected = sechsJahresRabattNachrechnungSelected;
        firePropertyChange(Field.SECHS_JAHRES_RABATT_NACHRECHNUNG, oldValue, this.sechsJahresRabattNachrechnungSelected);
    }

    @Override
    public VollstaendigkeitNachrechnungSelected getVollstaendigkeitNachrechnungSelected() {
        return vollstaendigkeitNachrechnungSelected;
    }

    @Override
    public void setVollstaendigkeitNachrechnungSelected(VollstaendigkeitNachrechnungSelected vollstaendigkeitNachrechnungSelected) {
        VollstaendigkeitNachrechnungSelected oldValue = this.vollstaendigkeitNachrechnungSelected;
        this.vollstaendigkeitNachrechnungSelected = vollstaendigkeitNachrechnungSelected;
        firePropertyChange(Field.VOLLSTAENDIGKEIT_NACHRECHNUNG, oldValue, this.vollstaendigkeitNachrechnungSelected);
    }

    @Override
    public PraezisierungDifferenzSchulgeldSelected getPraezisierungDifferenzSchulgeldSelected() {
        return praezisierungDifferenzSchulgeldSelected;
    }

    @Override
    public void setPraezisierungDifferenzSchulgeldSelected(PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected) {
        PraezisierungDifferenzSchulgeldSelected oldValue = this.praezisierungDifferenzSchulgeldSelected;
        this.praezisierungDifferenzSchulgeldSelected = praezisierungDifferenzSchulgeldSelected;
        firePropertyChange(Field.PRAEZISIERUNG_DIFFERENZ_SCHULGELD, oldValue, this.praezisierungDifferenzSchulgeldSelected);
    }

    private final PreisModelAttribute differenzSchulgeldModelAttribute = new PreisModelAttribute(
            this,
            Field.DIFFERENZ_SCHULGELD, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return differenzSchulgeld;
                }

                @Override
                public void setValue(BigDecimal value) {
                    differenzSchulgeld = value;
                }
            }
    );

    @Override
    public BigDecimal getDifferenzSchulgeld() {
        return differenzSchulgeldModelAttribute.getValue();
    }

    @Override
    public void setDifferenzSchulgeld(String differenzSchulgeld) throws SvmValidationException {
        differenzSchulgeldModelAttribute.setNewValue(false, differenzSchulgeld, isBulkUpdate());
    }

    @Override
    public PraezisierungRestbetragSelected getPraezisierungRestbetragSelected() {
        return praezisierungRestbetragSelected;
    }

    @Override
    public void setPraezisierungRestbetragSelected(PraezisierungRestbetragSelected praezisierungRestbetragSelected) {
        PraezisierungRestbetragSelected oldValue = this.praezisierungRestbetragSelected;
        this.praezisierungRestbetragSelected = praezisierungRestbetragSelected;
        firePropertyChange(Field.PRAEZISIERUNG_RESTBETRAG, oldValue, this.praezisierungRestbetragSelected);
    }

    private final PreisModelAttribute restbetragModelAttribute = new PreisModelAttribute(
            this,
            Field.RESTBETRAG, new BigDecimal("0.00"), new BigDecimal("9999.95"),
            new AttributeAccessor<BigDecimal>() {
                @Override
                public BigDecimal getValue() {
                    return restbetrag;
                }

                @Override
                public void setValue(BigDecimal value) {
                    restbetrag = value;
                }
            }
    );

    @Override
    public BigDecimal getRestbetrag() {
        return restbetragModelAttribute.getValue();
    }

    @Override
    public void setRestbetrag(String restbetrag) throws SvmValidationException {
        restbetragModelAttribute.setNewValue(false, restbetrag, isBulkUpdate());
    }

    @Override
    public Semester getSemesterInit(SvmModel svmModel) {
        FindSemesterForCalendarCommand findSemesterForCalendarCommand = new FindSemesterForCalendarCommand(svmModel.getSemestersAll());
        findSemesterForCalendarCommand.execute();
        Semester currentSemester = findSemesterForCalendarCommand.getCurrentSemester();
        Semester nextSemester = findSemesterForCalendarCommand.getNextSemester();
        // Innerhalb Semester
        if (currentSemester != null) {
            return currentSemester;
        }
        // Ferien zwischen 2 Semestern
        if (nextSemester != null) {
            return nextSemester;
        }
        // Kein passendes Semester erfasst
        return svmModel.getSemestersAll().get(0);
    }

    @Override
    public boolean isSuchkriterienSelected() {
        return checkNotEmpty(nachname)
                || checkNotEmpty(vorname)
                || (getSemesterrechnungCode() != null && getSemesterrechnungCode() != SEMESTERRECHNUNG_CODE_ALLE)
                || (getStipendium() != null && getStipendium() != Stipendium.KEINES)
                || isGratiskinder()
                || getRechnungsdatumVorrechnung() != null
                || getErmaessigungVorrechnung() != null
                || getZuschlagVorrechnung() != null
                || getWochenbetragVorrechnung() != null
                || schulgeldVorrechnung != null
                || (sechsJahresRabattVorrechnungSelected != null && sechsJahresRabattVorrechnungSelected != SechsJahresRabattVorrechnungSelected.ALLE)
                || (vollstaendigkeitVorrechnungSelected != null && vollstaendigkeitVorrechnungSelected != VollstaendigkeitVorrechnungSelected.ALLE)
                || getRechnungsdatumNachrechnung() != null
                || getErmaessigungNachrechnung() != null
                || getZuschlagNachrechnung() != null
                || getAnzahlWochenNachrechnung() != null
                || getWochenbetragNachrechnung() != null
                || schulgeldNachrechnung != null
                || (sechsJahresRabattNachrechnungSelected != null && sechsJahresRabattNachrechnungSelected != SechsJahresRabattNachrechnungSelected.ALLE)
                || (vollstaendigkeitNachrechnungSelected != null && vollstaendigkeitNachrechnungSelected != VollstaendigkeitNachrechnungSelected.ALLE)
                || differenzSchulgeld != null
                || restbetrag != null;
    }

    @Override
    public SemesterrechnungenTableData suchen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        FindOrCreateSemesterrechnungenCommand findOrCreateSemesterrechnungenCommand = new FindOrCreateSemesterrechnungenCommand(this);
        commandInvoker.executeCommandAsTransaction(findOrCreateSemesterrechnungenCommand);
        return new SemesterrechnungenTableData(findOrCreateSemesterrechnungenCommand.getFoundOrCreatedSemesterrechnungen());
    }
}
