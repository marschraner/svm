package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.CreateAndFindSemesterrechnungenCommand;
import ch.metzenthin.svm.domain.commands.FindSemesterForCalendarCommand;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.Semesterrechnung;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;

import java.math.BigDecimal;
import java.util.List;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Martin Schraner
 */
final class SemesterrechnungenSuchenModelImpl extends SemesterrechnungModelImpl implements SemesterrechnungenSuchenModel {

    private Semester semester;
    private String nachname;
    private String vorname;
    private RolleSelected rolle;
    private SemesterrechnungCodeJaNeinSelected semesterrechnungCodeJaNeinSelected;
    private StipendiumJaNeinSelected stipendiumJaNeinSelected;
    private RechnungsdatumGesetztVorrechnungSelected rechnungsdatumGesetztVorrechnungSelected;
    private PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected;
    private PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected;
    private PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected;
    private PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected;
    private PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected;
    private BigDecimal schulgeldVorrechnung;
    private SechsJahresRabattJaNeinVorrechnungSelected sechsJahresRabattJaNeinVorrechnungSelected;
    private RechnungsdatumGesetztNachrechnungSelected rechnungsdatumGesetztNachrechnungSelected;
    private PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected;
    private PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected;
    private PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected;
    private PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected;
    private PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected;
    private PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected;
    private BigDecimal schulgeldNachrechnung;
    private SechsJahresRabattJaNeinNachrechnungSelected sechsJahresRabattJaNeinNachrechnungSelected;
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
            Field.NACHNAME, 1, 50,
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
            Field.VORNAME, 1, 50,
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
    public SemesterrechnungCodeJaNeinSelected getSemesterrechnungCodeJaNeinSelected() {
        return semesterrechnungCodeJaNeinSelected;
    }

    @Override
    public void setSemesterrechnungCodeJaNeinSelected(SemesterrechnungCodeJaNeinSelected semesterrechnungCodeJaNeinSelected) {
        SemesterrechnungCodeJaNeinSelected oldValue = this.semesterrechnungCodeJaNeinSelected;
        this.semesterrechnungCodeJaNeinSelected = semesterrechnungCodeJaNeinSelected;
        firePropertyChange(Field.SEMESTERRECHNUNG_CODE_JA_NEIN, oldValue, this.semesterrechnungCodeJaNeinSelected);
    }

    @Override
    public StipendiumJaNeinSelected getStipendiumJaNeinSelected() {
        return stipendiumJaNeinSelected;
    }

    @Override
    public void setStipendiumJaNeinSelected(StipendiumJaNeinSelected stipendiumJaNeinSelected) {
        StipendiumJaNeinSelected oldValue = this.stipendiumJaNeinSelected;
        this.stipendiumJaNeinSelected = stipendiumJaNeinSelected;
        firePropertyChange(Field.STIPENDIUM_JA_NEIN, oldValue, this.stipendiumJaNeinSelected);
    }

    @Override
    public void setRechnungsdatumGesetztVorrechnungSelected(RechnungsdatumGesetztVorrechnungSelected rechnungsdatumGesetztVorrechnungSelected) {
        RechnungsdatumGesetztVorrechnungSelected oldValue = this.rechnungsdatumGesetztVorrechnungSelected;
        this.rechnungsdatumGesetztVorrechnungSelected = rechnungsdatumGesetztVorrechnungSelected;
        firePropertyChange(Field.RECHNUNGSDATUM_GESETZT_VORRECHNUNG, oldValue, this.rechnungsdatumGesetztVorrechnungSelected);
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
    public SechsJahresRabattJaNeinVorrechnungSelected getSechsJahresRabattJaNeinVorrechnungSelected() {
        return sechsJahresRabattJaNeinVorrechnungSelected;
    }

    @Override
    public void setSechsJahresRabattJaNeinVorrechnungSelected(SechsJahresRabattJaNeinVorrechnungSelected sechsJahresRabattJaNeinVorrechnungSelected) {
        SechsJahresRabattJaNeinVorrechnungSelected oldValue = this.sechsJahresRabattJaNeinVorrechnungSelected;
        this.sechsJahresRabattJaNeinVorrechnungSelected = sechsJahresRabattJaNeinVorrechnungSelected;
        firePropertyChange(Field.SECHS_JAHRES_RABATT_VORRECHNUNG, oldValue, this.sechsJahresRabattJaNeinVorrechnungSelected);
    }

    @Override
    public RechnungsdatumGesetztVorrechnungSelected getRechnungsdatumGesetztVorrechnungSelected() {
        return rechnungsdatumGesetztVorrechnungSelected;
    }

    @Override
    public void setRechnungsdatumGesetztNachrechnungSelected(RechnungsdatumGesetztNachrechnungSelected rechnungsdatumGesetztNachrechnungSelected) {
        RechnungsdatumGesetztNachrechnungSelected oldValue = this.rechnungsdatumGesetztNachrechnungSelected;
        this.rechnungsdatumGesetztNachrechnungSelected = rechnungsdatumGesetztNachrechnungSelected;
        firePropertyChange(Field.RECHNUNGSDATUM_GESETZT_NACHRECHNUNG, oldValue, this.rechnungsdatumGesetztNachrechnungSelected);
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
    public SechsJahresRabattJaNeinNachrechnungSelected getSechsJahresRabattJaNeinNachrechnungSelected() {
        return sechsJahresRabattJaNeinNachrechnungSelected;
    }

    @Override
    public void setSechsJahresRabattJaNeinNachrechnungSelected(SechsJahresRabattJaNeinNachrechnungSelected sechsJahresRabattJaNeinNachrechnungSelected) {
        SechsJahresRabattJaNeinNachrechnungSelected oldValue = this.sechsJahresRabattJaNeinNachrechnungSelected;
        this.sechsJahresRabattJaNeinNachrechnungSelected = sechsJahresRabattJaNeinNachrechnungSelected;
        firePropertyChange(Field.SECHS_JAHRES_RABATT_NACHRECHNUNG, oldValue, this.sechsJahresRabattJaNeinNachrechnungSelected);
    }

    @Override
    public RechnungsdatumGesetztNachrechnungSelected getRechnungsdatumGesetztNachrechnungSelected() {
        return rechnungsdatumGesetztNachrechnungSelected;
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
    public SemesterrechnungCode[] getSelectableSemesterrechnungCodes(SvmModel svmModel) {
        List<SemesterrechnungCode> codesList = svmModel.getSelektierbareSemesterrechnungCodesAll();
        // SemesterrechnungCode alle auch erlaubt
        if (codesList.isEmpty() || !codesList.get(0).isIdenticalWith(SEMESTERRECHNUNG_CODE_ALLE)) {
            codesList.add(0, SEMESTERRECHNUNG_CODE_ALLE);
        }
        return codesList.toArray(new SemesterrechnungCode[codesList.size()]);
    }

    @Override
    public Stipendium[] getSelectableStipendien() {
        Stipendium[] selectableStipendien = new Stipendium[Stipendium.values().length - 1];
        int j = 0;
        for (int i = 0; i < Stipendium.values().length; i++) {
            if (Stipendium.values()[i] != Stipendium.KEINES) {
                selectableStipendien[j] = Stipendium.values()[i];
                j++;
            }
        }
        return selectableStipendien;
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
                || (semesterrechnungCodeJaNeinSelected != null && semesterrechnungCodeJaNeinSelected != SemesterrechnungCodeJaNeinSelected.ALLE)
                || (getSemesterrechnungCode() != null && getSemesterrechnungCode() != SEMESTERRECHNUNG_CODE_ALLE)
                || (stipendiumJaNeinSelected != null && stipendiumJaNeinSelected != StipendiumJaNeinSelected.ALLE)
                || isGratiskinder()
                || getErmaessigungVorrechnung() != null
                || getZuschlagVorrechnung() != null
                || getAnzahlWochenVorrechnung() != null
                || getWochenbetragVorrechnung() != null
                || schulgeldVorrechnung != null
                || (sechsJahresRabattJaNeinVorrechnungSelected != null && sechsJahresRabattJaNeinVorrechnungSelected != SechsJahresRabattJaNeinVorrechnungSelected.ALLE)
                || (rechnungsdatumGesetztVorrechnungSelected != null && rechnungsdatumGesetztVorrechnungSelected != RechnungsdatumGesetztVorrechnungSelected.ALLE)
                || getErmaessigungNachrechnung() != null
                || getZuschlagNachrechnung() != null
                || getAnzahlWochenNachrechnung() != null
                || getWochenbetragNachrechnung() != null
                || schulgeldNachrechnung != null
                || (sechsJahresRabattJaNeinNachrechnungSelected != null && sechsJahresRabattJaNeinNachrechnungSelected != SechsJahresRabattJaNeinNachrechnungSelected.ALLE)
                || (rechnungsdatumGesetztNachrechnungSelected != null && rechnungsdatumGesetztNachrechnungSelected != RechnungsdatumGesetztNachrechnungSelected.ALLE)
                || differenzSchulgeld != null
                || restbetrag != null;
    }

    @Override
    public SemesterrechnungenTableData suchen() {
        CommandInvoker commandInvoker = getCommandInvoker();
        CreateAndFindSemesterrechnungenCommand createAndFindSemesterrechnungenCommand = new CreateAndFindSemesterrechnungenCommand(this);
        commandInvoker.executeCommandAsTransaction(createAndFindSemesterrechnungenCommand);
        return new SemesterrechnungenTableData(createAndFindSemesterrechnungenCommand.getSemesterrechnungenFound(), semester);
    }

    @Override
    public SemesterrechnungBearbeitenModel getSemesterrechnungBearbeitenModel(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel) {
        SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel = svmContext.getModelFactory().createSemesterrechnungBearbeitenModel();
        Semesterrechnung semesterrechnungSelected = semesterrechnungenTableModel.getSemesterrechnungSelected(0);
        semesterrechnungBearbeitenModel.setSemesterrechnungOrigin(semesterrechnungSelected);
        return semesterrechnungBearbeitenModel;
    }

    @Override
    void doValidate() throws SvmValidationException {
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
