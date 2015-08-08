package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import ch.metzenthin.svm.ui.componentmodel.CodesTableModel;

/**
 * @author Martin Schraner
 */
public interface SchuelerCodeSchuelerHinzufuegenModel extends Model {

    SchuelerCode getSchuelerCode();

    void setSchuelerCode(SchuelerCode schuelerCode) throws SvmRequiredException;

    void hinzufuegen(CodesTableModel codesTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
