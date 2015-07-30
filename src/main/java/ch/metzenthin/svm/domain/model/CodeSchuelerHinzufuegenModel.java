package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.persistence.entities.Code;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface CodeSchuelerHinzufuegenModel extends Model {

    Code getCode();

    void setCode(Code code) throws SvmRequiredException;

    void hinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
