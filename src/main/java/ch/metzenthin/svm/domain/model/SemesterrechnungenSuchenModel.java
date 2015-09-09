package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.math.BigDecimal;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungenSuchenModel extends SemesterrechnungModel {

    enum RolleSelected {
        SCHUELER,
        ELTERN,
        RECHNUNGSEMPFAENGER,
        ALLE
    }

    enum SemesterrechnungCodeJaNeinSelected {
        JA,
        NEIN,
        ALLE
    }

    enum StipendiumJaNeinSelected {
        JA,
        NEIN,
        ALLE
    }

    enum RechnungsdatumGesetztVorrechnungSelected {
        GESETZT,
        NICHT_GESETZT,
        ALLE
    }

    enum PraezisierungRechnungsdatumVorrechnungSelected {
        AM,
        VOR,
        NACH
    }

    enum PraezisierungErmaessigungVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungZuschlagVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungWochenbetragVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungSchulgeldVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum SechsJahresRabattJaNeinVorrechnungSelected {
        JA,
        NEIN,
        ALLE
    }

    enum RechnungsdatumGesetztNachrechnungSelected {
        GESETZT,
        NICHT_GESETZT,
        ALLE
    }

    enum PraezisierungRechnungsdatumNachrechnungSelected {
        AM,
        VOR,
        NACH
    }

    enum PraezisierungErmaessigungNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungZuschlagNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungAnzahlWochenNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungWochenbetragNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungSchulgeldNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum SechsJahresRabattJaNeinNachrechnungSelected {
        JA,
        NEIN,
        ALLE
    }

    enum PraezisierungDifferenzSchulgeldSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungRestbetragSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    Semester getSemester();
    String getNachname();
    String getVorname();
    RolleSelected getRolle();
    SemesterrechnungCodeJaNeinSelected getSemesterrechnungCodeJaNeinSelected();
    StipendiumJaNeinSelected getStipendiumJaNeinSelected();
    RechnungsdatumGesetztVorrechnungSelected getRechnungsdatumGesetztVorrechnungSelected();
    PraezisierungRechnungsdatumVorrechnungSelected getPraezisierungRechnungsdatumVorrechnungSelected();
    PraezisierungErmaessigungVorrechnungSelected getPraezisierungErmaessigungVorrechnungSelected();
    PraezisierungZuschlagVorrechnungSelected getPraezisierungZuschlagVorrechnungSelected();
    PraezisierungWochenbetragVorrechnungSelected getPraezisierungWochenbetragVorrechnungSelected();
    PraezisierungSchulgeldVorrechnungSelected getPraezisierungSchulgeldVorrechnungSelected();
    BigDecimal getSchulgeldVorrechnung();
    SechsJahresRabattJaNeinVorrechnungSelected getSechsJahresRabattJaNeinVorrechnungSelected();
    RechnungsdatumGesetztNachrechnungSelected getRechnungsdatumGesetztNachrechnungSelected();
    PraezisierungRechnungsdatumNachrechnungSelected getPraezisierungRechnungsdatumNachrechnungSelected();
    PraezisierungErmaessigungNachrechnungSelected getPraezisierungErmaessigungNachrechnungSelected();
    PraezisierungZuschlagNachrechnungSelected getPraezisierungZuschlagNachrechnungSelected();
    PraezisierungAnzahlWochenNachrechnungSelected getPraezisierungAnzahlWochenNachrechnungSelected();
    PraezisierungWochenbetragNachrechnungSelected getPraezisierungWochenbetragNachrechnungSelected();
    PraezisierungSchulgeldNachrechnungSelected getPraezisierungSchulgeldNachrechnungSelected();
    BigDecimal getSchulgeldNachrechnung();
    SechsJahresRabattJaNeinNachrechnungSelected getSechsJahresRabattJaNeinNachrechnungSelected();
    PraezisierungDifferenzSchulgeldSelected getPraezisierungDifferenzSchulgeldSelected();
    BigDecimal getDifferenzSchulgeld();
    PraezisierungRestbetragSelected getPraezisierungRestbetragSelected();
    BigDecimal getRestbetrag();

    void setSemester(Semester semester);
    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setRolle(RolleSelected rolle);
    void setSemesterrechnungCodeJaNeinSelected(SemesterrechnungCodeJaNeinSelected semesterrechnungCodeJaNeinSelected);
    void setStipendiumJaNeinSelected(StipendiumJaNeinSelected stipendiumJaNeinSelected);
    void setRechnungsdatumGesetztVorrechnungSelected(RechnungsdatumGesetztVorrechnungSelected rechnungsdatumGesetztVorrechnungSelected);
    void setPraezisierungRechnungsdatumVorrechnungSelected(PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected);
    void setPraezisierungErmaessigungVorrechnungSelected(PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected);
    void setPraezisierungZuschlagVorrechnungSelected(PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected);
    void setPraezisierungWochenbetragVorrechnungSelected(PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected);
    void setPraezisierungSchulgeldVorrechnungSelected(PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected);
    void setSchulgeldVorrechnung(String schulgeldVorrechnung) throws SvmValidationException;
    void setSechsJahresRabattJaNeinVorrechnungSelected(SechsJahresRabattJaNeinVorrechnungSelected sechsJahresRabattJaNeinVorrechnungSelected);
    void setRechnungsdatumGesetztNachrechnungSelected(RechnungsdatumGesetztNachrechnungSelected rechnungsdatumGesetztNachrechnungSelected);
    void setPraezisierungRechnungsdatumNachrechnungSelected(PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected);
    void setPraezisierungErmaessigungNachrechnungSelected(PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected);
    void setPraezisierungZuschlagNachrechnungSelected(PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected);
    void setPraezisierungAnzahlWochenNachrechnungSelected(PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected);
    void setPraezisierungWochenbetragNachrechnungSelected(PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected);
    void setPraezisierungSchulgeldNachrechnungSelected(PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected);
    void setSchulgeldNachrechnung(String schulgeldNachrechnung) throws SvmValidationException;
    void setSechsJahresRabattJaNeinNachrechnungSelected(SechsJahresRabattJaNeinNachrechnungSelected sechsJahresRabattJaNeinNachrechnungSelected);
    void setPraezisierungDifferenzSchulgeldSelected(PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected);
    void setDifferenzSchulgeld(String differenzSchulgeld) throws SvmValidationException;
    void setPraezisierungRestbetragSelected(PraezisierungRestbetragSelected praezisierungRestbetragSelected);
    void setRestbetrag(String restbetrag) throws SvmValidationException;

    Semester getSemesterInit(SvmModel svmModel);
    boolean isSuchkriterienSelected();
    SemesterrechnungenTableData suchen();
}
