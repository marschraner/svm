package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Martin Schraner
 */
public interface MitarbeitersSuchenModel extends Model {

    enum LehrkraftJaNeinSelected {
        JA,
        NEIN,
        ALLE
    }

    enum AktivJaNeinSelected {
        JA,
        NEIN,
        ALLE
    }

    String getNachname();
    String getVorname();
    String getMitarbeiterCodes();
    LehrkraftJaNeinSelected getLehrkraftJaNeinSelected();
    AktivJaNeinSelected getAktivJaNeinSelected();

    void setNachname(String nachname) throws SvmValidationException;
    void setVorname(String vorname) throws SvmValidationException;
    void setMitarbeiterCodes(String mitarbeiterCodes) throws SvmValidationException;
    void setLehrkraftJaNeinSelected(LehrkraftJaNeinSelected lehrkraftJaNeinSelected);
    void setAktivJaNeinSelected(AktivJaNeinSelected aktivJaNeinSelected);

    String checkIfCodeKuerzelsExist(SvmModel svmModel);
    MitarbeitersTableData suchen();
}
