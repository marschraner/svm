package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;

/**
 * @author Martin Schraner
 */
public interface MitarbeiterSuchenModel extends Model {

    MitarbeiterCode MITARBEITER_CODE_ALLE = new MitarbeiterCode();

    enum LehrkraftJaNeinSelected {
        JA,
        NEIN,
        ALLE
    }

    enum StatusSelected {
        AKTIV,
        NICHT_AKTIV,
        ALLE
    }

    String getNachname();

    String getVorname();

    MitarbeiterCode getMitarbeiterCode();

    LehrkraftJaNeinSelected getLehrkraftJaNeinSelected();

    StatusSelected getStatusSelected();

    void setNachname(String nachname) throws SvmValidationException;

    void setVorname(String vorname) throws SvmValidationException;

    void setMitarbeiterCode(MitarbeiterCode mitarbeiterCode);

    void setLehrkraftJaNeinSelected(LehrkraftJaNeinSelected lehrkraftJaNeinSelected);

    void setStatusSelected(StatusSelected statusSelected);

    MitarbeiterCode[] getSelectableMitarbeiterCodes(SvmModel svmModel);

    boolean isSuchkriterienSelected();

    MitarbeitersTableData suchen();
}
