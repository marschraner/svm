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

    enum PraezisierungAnzahlWochenVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungWochenbetragVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungRechnungsbetragVorrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungRestbetragVorrechnungSelected {
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

    enum PraezisierungRechnungsbetragNachrechnungSelected {
        GLEICH,
        KLEINER,
        GROESSER
    }

    enum PraezisierungRestbetragNachrechnungSelected {
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
    PraezisierungAnzahlWochenVorrechnungSelected getPraezisierungAnzahlWochenVorrechnungSelected();
    PraezisierungWochenbetragVorrechnungSelected getPraezisierungWochenbetragVorrechnungSelected();
    PraezisierungRechnungsbetragVorrechnungSelected getPraezisierungRechnungsbetragVorrechnungSelected();
    BigDecimal getRechnungsbetragVorrechnung();
    PraezisierungRestbetragVorrechnungSelected getPraezisierungRestbetragVorrechnungSelected();
    BigDecimal getRestbetragVorrechnung();
    SechsJahresRabattJaNeinVorrechnungSelected getSechsJahresRabattJaNeinVorrechnungSelected();
    RechnungsdatumGesetztNachrechnungSelected getRechnungsdatumGesetztNachrechnungSelected();
    PraezisierungRechnungsdatumNachrechnungSelected getPraezisierungRechnungsdatumNachrechnungSelected();
    PraezisierungErmaessigungNachrechnungSelected getPraezisierungErmaessigungNachrechnungSelected();
    PraezisierungZuschlagNachrechnungSelected getPraezisierungZuschlagNachrechnungSelected();
    PraezisierungAnzahlWochenNachrechnungSelected getPraezisierungAnzahlWochenNachrechnungSelected();
    PraezisierungWochenbetragNachrechnungSelected getPraezisierungWochenbetragNachrechnungSelected();
    PraezisierungRechnungsbetragNachrechnungSelected getPraezisierungRechnungsbetragNachrechnungSelected();
    BigDecimal getRechnungsbetragNachrechnung();
    PraezisierungRestbetragNachrechnungSelected getPraezisierungRestbetragNachrechnungSelected();
    BigDecimal getRestbetragNachrechnung();
    SechsJahresRabattJaNeinNachrechnungSelected getSechsJahresRabattJaNeinNachrechnungSelected();
    PraezisierungDifferenzSchulgeldSelected getPraezisierungDifferenzSchulgeldSelected();
    BigDecimal getDifferenzSchulgeld();

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
    void setPraezisierungAnzahlWochenVorrechnungSelected(PraezisierungAnzahlWochenVorrechnungSelected praezisierungAnzahlWochenVorrechnungSelected);
    void setPraezisierungWochenbetragVorrechnungSelected(PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected);
    void setPraezisierungRechnungsbetragVorrechnungSelected(PraezisierungRechnungsbetragVorrechnungSelected praezisierungRechnungsbetragVorrechnungSelected);
    void setRechnungsbetragVorrechnung(String rechnungsbetragVorrechnung) throws SvmValidationException;
    void setPraezisierungRestbetragVorrechnungSelected(PraezisierungRestbetragVorrechnungSelected praezisierungRestbetragVorrechnungSelected);
    void setRestbetragVorrechnung(String restbetragVorrechnung) throws SvmValidationException;
    void setSechsJahresRabattJaNeinVorrechnungSelected(SechsJahresRabattJaNeinVorrechnungSelected sechsJahresRabattJaNeinVorrechnungSelected);
    void setRechnungsdatumGesetztNachrechnungSelected(RechnungsdatumGesetztNachrechnungSelected rechnungsdatumGesetztNachrechnungSelected);
    void setPraezisierungRechnungsdatumNachrechnungSelected(PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected);
    void setPraezisierungErmaessigungNachrechnungSelected(PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected);
    void setPraezisierungZuschlagNachrechnungSelected(PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected);
    void setPraezisierungAnzahlWochenNachrechnungSelected(PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected);
    void setPraezisierungWochenbetragNachrechnungSelected(PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected);
    void setPraezisierungRechnungsbetragNachrechnungSelected(PraezisierungRechnungsbetragNachrechnungSelected praezisierungRechnungsbetragNachrechnungSelected);
    void setRechnungsbetragNachrechnung(String rechnungsbetragNachrechnung) throws SvmValidationException;
    void setPraezisierungRestbetragNachrechnungSelected(PraezisierungRestbetragNachrechnungSelected praezisierungRestbetragNachrechnungSelected);
    void setRestbetragNachrechnung(String restbetragNachrechnung) throws SvmValidationException;
    void setSechsJahresRabattJaNeinNachrechnungSelected(SechsJahresRabattJaNeinNachrechnungSelected sechsJahresRabattJaNeinNachrechnungSelected);
    void setPraezisierungDifferenzSchulgeldSelected(PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected);
    void setDifferenzSchulgeld(String differenzSchulgeld) throws SvmValidationException;

    Semester getSemesterInit(SvmModel svmModel);
    SemesterrechnungenTableData suchen();
}
