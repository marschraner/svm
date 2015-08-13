package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Codetyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * @author Martin Schraner
 */
public interface CodeErfassenModel extends Model {
    void setSchuelerCodeOrigin(SchuelerCode schuelerCodeOrigin);
    void setElternmithilfeCodeOrigin(ElternmithilfeCode elternmithilfeCodeOrigin);

    String getKuerzel();
    String getBeschreibung();

    void setKuerzel(String kuerzel) throws SvmValidationException;
    void setBeschreibung(String beschreibung) throws SvmValidationException;

    boolean checkCodeKuerzelBereitsInVerwendung(SvmModel svmModel, Codetyp codetyp);
    void speichern(SvmModel svmModel, Codetyp codetyp);
}
