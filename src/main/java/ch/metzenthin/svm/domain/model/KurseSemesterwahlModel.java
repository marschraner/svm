package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;

/**
 * @author Martin Schraner
 */
public interface KurseSemesterwahlModel extends Model {

    String getSchuljahr();
    Semesterbezeichnung getSemesterbezeichnung();
    Semester getSemester(SvmModel svmModel);

    void setSchuljahr(String schuljahr) throws SvmValidationException;
    void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung);

    boolean checkSemesterBereitsErfasst(SvmModel svmModel);
    KurseTableData suchen();
}
