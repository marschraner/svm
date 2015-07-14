package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Martin Schraner
 */
public interface CodeErfassenModel extends Model {
    void setCodeOrigin(Code codeOrigin);

    String getKuerzel();
    String getBeschreibung();
    Code getCode();

    void setKuerzel(String kuerzel) throws SvmValidationException;
    void setBeschreibung(String beschreibung) throws SvmValidationException;

    boolean checkCodeKuerzelBereitsInVerwendung(CodesModel codesTableModel);
    void speichern(CodesModel codesTableModel);
}
