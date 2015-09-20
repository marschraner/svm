package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodeSpecificHinzufuegenModel extends Model {

    Code getCode();

    void setCode(Code code) throws SvmRequiredException;

    void schuelerCodeHinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
    void mitarbeiterCodeHinzufuegen(CodesTableModel codesTableModel, MitarbeiterErfassenModel mitarbeiterErfassenModel);
}
