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

    enum SechsJahresRabattVorrechnungSelected {
        VORHANDEN,
        NICHT_VORHANDEN,
        ALLE
    }

    enum VollstaendigkeitVorrechnungSelected {
        VOLLSTAENDIG,
        UNVOLLSTAENDIG,
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

    enum SechsJahresRabattNachrechnungSelected {
        VORHANDEN,
        NICHT_VORHANDEN,
        ALLE
    }

    enum VollstaendigkeitNachrechnungSelected {
        VOLLSTAENDIG,
        UNVOLLSTAENDIG,
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
    PraezisierungRechnungsdatumVorrechnungSelected getPraezisierungRechnungsdatumVorrechnungSelected();
    PraezisierungErmaessigungVorrechnungSelected getPraezisierungErmaessigungVorrechnungSelected();
    PraezisierungZuschlagVorrechnungSelected getPraezisierungZuschlagVorrechnungSelected();
    PraezisierungWochenbetragVorrechnungSelected getPraezisierungWochenbetragVorrechnungSelected();
    PraezisierungSchulgeldVorrechnungSelected getPraezisierungSchulgeldVorrechnungSelected();
    BigDecimal getSchulgeldVorrechnung();
    SechsJahresRabattVorrechnungSelected getSechsJahresRabattVorrechnungSelected();
    VollstaendigkeitVorrechnungSelected getVollstaendigkeitVorrechnungSelected();
    PraezisierungRechnungsdatumNachrechnungSelected getPraezisierungRechnungsdatumNachrechnungSelected();
    PraezisierungErmaessigungNachrechnungSelected getPraezisierungErmaessigungNachrechnungSelected();
    PraezisierungZuschlagNachrechnungSelected getPraezisierungZuschlagNachrechnungSelected();
    PraezisierungAnzahlWochenNachrechnungSelected getPraezisierungAnzahlWochenNachrechnungSelected();
    PraezisierungWochenbetragNachrechnungSelected getPraezisierungWochenbetragNachrechnungSelected();
    PraezisierungSchulgeldNachrechnungSelected getPraezisierungSchulgeldNachrechnungSelected();
    BigDecimal getSchulgeldNachrechnung();
    SechsJahresRabattNachrechnungSelected getSechsJahresRabattNachrechnungSelected();
    VollstaendigkeitNachrechnungSelected getVollstaendigkeitNachrechnungSelected();
    PraezisierungDifferenzSchulgeldSelected getPraezisierungDifferenzSchulgeldSelected();
    BigDecimal getDifferenzSchulgeld();
    PraezisierungRestbetragSelected getPraezisierungRestbetragSelected();
    BigDecimal getRestbetrag();

    void setSemester(Semester semester);
    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setRolle(RolleSelected rolle);
    void setPraezisierungRechnungsdatumVorrechnungSelected(PraezisierungRechnungsdatumVorrechnungSelected praezisierungRechnungsdatumVorrechnungSelected);
    void setPraezisierungErmaessigungVorrechnungSelected(PraezisierungErmaessigungVorrechnungSelected praezisierungErmaessigungVorrechnungSelected);
    void setPraezisierungZuschlagVorrechnungSelected(PraezisierungZuschlagVorrechnungSelected praezisierungZuschlagVorrechnungSelected);
    void setPraezisierungWochenbetragVorrechnungSelected(PraezisierungWochenbetragVorrechnungSelected praezisierungWochenbetragVorrechnungSelected);
    void setPraezisierungSchulgeldVorrechnungSelected(PraezisierungSchulgeldVorrechnungSelected praezisierungSchulgeldVorrechnungSelected);
    void setSchulgeldVorrechnung(String schulgeldVorrechnung) throws SvmValidationException;
    void setSechsJahresRabattVorrechnungSelected(SechsJahresRabattVorrechnungSelected sechsJahresRabattVorrechnungSelected);
    void setVollstaendigkeitVorrechnungSelected(VollstaendigkeitVorrechnungSelected vollstaendigkeitVorrechnungSelected);
    void setPraezisierungRechnungsdatumNachrechnungSelected(PraezisierungRechnungsdatumNachrechnungSelected praezisierungRechnungsdatumNachrechnungSelected);
    void setPraezisierungErmaessigungNachrechnungSelected(PraezisierungErmaessigungNachrechnungSelected praezisierungErmaessigungNachrechnungSelected);
    void setPraezisierungZuschlagNachrechnungSelected(PraezisierungZuschlagNachrechnungSelected praezisierungZuschlagNachrechnungSelected);
    void setPraezisierungAnzahlWochenNachrechnungSelected(PraezisierungAnzahlWochenNachrechnungSelected praezisierungAnzahlWochenNachrechnungSelected);
    void setPraezisierungWochenbetragNachrechnungSelected(PraezisierungWochenbetragNachrechnungSelected praezisierungWochenbetragNachrechnungSelected);
    void setPraezisierungSchulgeldNachrechnungSelected(PraezisierungSchulgeldNachrechnungSelected praezisierungSchulgeldNachrechnungSelected);
    void setSchulgeldNachrechnung(String schulgeldNachrechnung) throws SvmValidationException;
    void setSechsJahresRabattNachrechnungSelected(SechsJahresRabattNachrechnungSelected sechsJahresRabattNachrechnungSelected);
    void setVollstaendigkeitNachrechnungSelected(VollstaendigkeitNachrechnungSelected vollstaendigkeitNachrechnungSelected);
    void setPraezisierungDifferenzSchulgeldSelected(PraezisierungDifferenzSchulgeldSelected praezisierungDifferenzSchulgeldSelected);
    void setDifferenzSchulgeld(String differenzSchulgeld) throws SvmValidationException;
    void setPraezisierungRestbetragSelected(PraezisierungRestbetragSelected praezisierungRestbetragSelected);
    void setRestbetrag(String restbetrag) throws SvmValidationException;

    Semester getSemesterInit(SvmModel svmModel);
    boolean isSuchkriterienSelected();
    SemesterrechnungenTableData suchen();
}
