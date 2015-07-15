package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Code;

/**
 * @author Martin Schraner
 */
public interface CodeSchuelerHinzufuegenModel extends Model {

    void setCode(Code code);

    void hinzufuegen(SchuelerDatenblattModel schuelerDatenblattModel);
}
