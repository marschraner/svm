package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Stipendium;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SemesterrechnungenSuchenModel extends Model {

    SemesterrechnungCode SEMESTERRECHNUNG_CODE_ALLE = new SemesterrechnungCode();

    enum RolleSelected {
        SCHUELER,
        ELTERN,
        RECHNUNGSEMPFAENGER,
        ALLE
    }

    enum RechnungsdatumSelected {
        AM,
        VOR,
        NACH
    }

    enum RechnungsstatusSelected {
        OFFEN,
        BEZAHLT,
        ALLE
    }

    Semester getSemester();
    String getNachname();
    String getVorname();
    RolleSelected getRolle();
    SemesterrechnungCode getSemesterrechnungCode();
    Stipendium getStipendium();
    Boolean isSechsJahresRabatt();
    Boolean isGratiskinder();
    RechnungsdatumSelected getRechnungsdatumSelected();
    Calendar getRechnungsdatum();
    RechnungsstatusSelected getRechnungsstatus();
    BigDecimal getWochenbetrag();
    BigDecimal getSchulgeld();

    void setSemester(Semester semester);
    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setRolle(RolleSelected rolle);
    void setSemesterrechnungCode(SemesterrechnungCode semesterrechnungCode);
    void setStipendium(Stipendium stipendium);
    void setSechsJahresRabatt(Boolean sechsJahresRabatt);
    void setGratiskinder(Boolean gratiskind);
    void setRechnungsdatumSelected(RechnungsdatumSelected rechnungsdatumSelected);
    void setRechnungsdatum(String rechnungsdatum) throws SvmValidationException;
    void setRechnungsstatus(RechnungsstatusSelected rechnungsstatus);
    void setWochenbetrag(String wochenbetrag) throws SvmValidationException;
    void setSchulgeld(String schulgeld) throws SvmValidationException;

    Semester getSemesterInit(SvmModel svmModel);
    SemesterrechnungCode[] getSelectableSemesterrechnungCodes(SvmModel svmModel);
    SemesterrechnungenTableData suchen();
}
