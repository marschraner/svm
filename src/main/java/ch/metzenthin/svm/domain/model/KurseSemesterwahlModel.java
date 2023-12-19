package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.Semester;

/**
 * @author Martin Schraner
 */
public interface KurseSemesterwahlModel extends Model {

    Semester getSemester();

    void setSemester(Semester semesterg);

    Semester getInitSemester(SvmModel svmModel);

    KurseTableData suchen();
}
