package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Codetyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.persistence.entities.SemesterrechnungCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodeErfassenModel extends Model {

    void setSchuelerCodeOrigin(SchuelerCode schuelerCodeOrigin);

    void setMitarbeiterCodeOrigin(MitarbeiterCode mitarbeiterCodeOrigin);

    void setElternmithilfeCodeOrigin(ElternmithilfeCode elternmithilfeCodeOrigin);

    void setSemesterrechnungCodeOrigin(SemesterrechnungCode semesterrechnungCodeOrigin);

    String getKuerzel();

    String getBeschreibung();

    Boolean isSelektierbar();

    void setKuerzel(String kuerzel) throws SvmValidationException;

    void setBeschreibung(String beschreibung) throws SvmValidationException;

    void setSelektierbar(Boolean isSelected);

    boolean checkCodeKuerzelBereitsInVerwendung(SvmModel svmModel, Codetyp codetyp);

    void speichern(SvmModel svmModel, CodesTableModel codesTableModel, Codetyp codetyp);
}
