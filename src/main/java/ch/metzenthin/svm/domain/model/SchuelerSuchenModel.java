package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.PersonSuchen;

import java.sql.Time;
import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SchuelerSuchenModel extends PersonModel {

    Lehrkraft LEHRKRAFT_ALLE = new Lehrkraft();
    Code CODE_ALLE = new Code();

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
    Code getCode();

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
    void setCode(Code code);

    SchuelerSuchenTableData suchen(SvmModel svmModel);
    void invalidateGeburtsdatumSuchperiode();
    Lehrkraft[] getSelectableLehrkraefte(SvmModel svmModel);
    Code[] getSelectableCodes(SvmModel svmModel);
}
