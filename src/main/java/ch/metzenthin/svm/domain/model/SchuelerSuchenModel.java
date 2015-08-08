package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.PersonSuchen;

import java.sql.Time;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends PersonModel {

    Lehrkraft LEHRKRAFT_ALLE = new Lehrkraft();
    SchuelerCode SCHUELER_CODE_ALLE = new SchuelerCode();

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
    String getSchuljahrKurs();
    Semesterbezeichnung getSemesterbezeichnung();
    Wochentag getWochentag();
    Time getZeitBeginn();
    Lehrkraft getLehrkraft();
    boolean isKursFuerSucheBeruecksichtigen();
    PersonSuchen getPerson();
    SchuelerCode getSchuelerCode();

    void setGeburtsdatumSuchperiode(String geburtsdatumSuchperiode) throws SvmValidationException;
    void setGeschlecht(GeschlechtSelected geschlecht);
    void setRolle(RolleSelected rolle);
    void setAnmeldestatus(AnmeldestatusSelected anmeldestatus);
    void setDispensation(DispensationSelected dispensation);
    void setStichtag(String stichtag) throws SvmValidationException;
    void setSchuljahrKurs(String schuljahrKurs) throws SvmValidationException;
    void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung);
    void setWochentag(Wochentag wochentag);
    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;
    void setLehrkraft(Lehrkraft lehrkraft);
    void setKursFuerSucheBeruecksichtigen(boolean isSelected);
    void setSchuelerCode(SchuelerCode schuelerCode);

    SchuelerSuchenTableData suchen(SvmModel svmModel);
    void invalidateGeburtsdatumSuchperiode();
    Lehrkraft[] getSelectableLehrkraefte(SvmModel svmModel);
    SchuelerCode[] getSelectableCodes(SvmModel svmModel);
}
