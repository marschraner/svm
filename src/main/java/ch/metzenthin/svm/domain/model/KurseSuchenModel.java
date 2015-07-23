package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;

/**
 * @author Martin Schraner
 */
public interface KurseSuchenModel extends Model {

    String getSchuljahr();
    Semesterbezeichnung getSemesterbezeichnung();

    void setSchuljahr(String schuljahr) throws SvmValidationException;
    void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung);

    KurseTableData suchen();
}
