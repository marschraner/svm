package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Gruppe;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.*;

import java.sql.Time;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends PersonModel {

    Mitarbeiter MITARBEITER_ALLE = new Mitarbeiter();
    SchuelerCode SCHUELER_CODE_ALLE = new SchuelerCode();
    ElternmithilfeCode ELTERNMITHILFE_CODE_ALLE = new ElternmithilfeCode();

    enum RolleSelected {
        SCHUELER,
        ELTERN,
        RECHNUNGSEMPFAENGER,
        ALLE
    }

    enum AnmeldestatusSelected {
        ANGEMELDET,
        ABGEMELDET,
        ALLE
    }

    enum DispensationSelected {
        DISPENSIERT,
        NICHT_DISPENSIERT,
        ALLE
    }

    enum GeschlechtSelected {
        WEIBLICH,
        MAENNLICH,
        ALLE
    }

    String getGeburtsdatumSuchperiode();

    Calendar getGeburtsdatumSuchperiodeBeginn();

    Calendar getGeburtsdatumSuchperiodeEnde();

    String getGeburtsdatumSuchperiodeDateFormatString();

    GeschlechtSelected getGeschlecht();

    RolleSelected getRolle();

    AnmeldestatusSelected getAnmeldestatus();

    DispensationSelected getDispensation();

    Calendar getStichtag();

    SchuelerCode getSchuelerCode();

    Semester getSemesterKurs();

    Wochentag getWochentag();

    Time getZeitBeginn();

    Mitarbeiter getMitarbeiter();

    boolean isKursFuerSucheBeruecksichtigen();

    Maerchen getMaerchen();

    Gruppe getGruppe();

    String getRollen();

    ElternmithilfeCode getElternmithilfeCode();

    Integer getKuchenVorstellung();

    String getZusatzattributMaerchen();

    boolean isMaerchenFuerSucheBeruecksichtigen();

    PersonSuchen getPerson();

    void setGeburtsdatumSuchperiode(String geburtsdatumSuchperiode) throws SvmValidationException;

    void setGeschlecht(GeschlechtSelected geschlecht);

    void setRolle(RolleSelected rolle);

    void setAnmeldestatus(AnmeldestatusSelected anmeldestatus);

    void setDispensation(DispensationSelected dispensation);

    void setStichtag(String stichtag) throws SvmValidationException;

    void setSemesterKurs(Semester semesterKurs);

    void setWochentag(Wochentag wochentag);

    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;

    void setMitarbeiter(Mitarbeiter mitarbeiter);

    void setKursFuerSucheBeruecksichtigen(boolean isSelected);

    void setSchuelerCode(SchuelerCode schuelerCode);

    void setMaerchen(Maerchen maerchen);

    void setGruppe(Gruppe gruppe);

    void setRollen(String rollen) throws SvmValidationException;

    void setElternmithilfeCode(ElternmithilfeCode elternmithilfeCode);

    void setKuchenVorstellung(String kuchenVorstellung) throws SvmValidationException;

    void setZusatzattributMaerchen(String zusatzattributMaerchen) throws SvmValidationException;

    void setMaerchenFuerSucheBeruecksichtigen(boolean isSelected);

    boolean searchForSpecificKurs();

    boolean checkIfKurseExist();

    SchuelerSuchenTableData suchen(SvmModel svmModel);

    void invalidateGeburtsdatumSuchperiode();

    Mitarbeiter[] getSelectableLehrkraefte(SvmModel svmModel);

    SchuelerCode[] getSelectableSchuelerCodes(SvmModel svmModel);

    ElternmithilfeCode[] getSelectableElternmithilfeCodes(SvmModel svmModel);

    Semester getSemesterInit(SvmModel svmModel);

    Maerchen getMaerchenInit(SvmModel svmModel);
}
