package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SemesterErfassenModel extends Model {
    Semesterbezeichnung getSemesterbezeichnung();
    String getSchuljahr();
    Calendar getSemesterbeginn();
    Calendar getSemesterende();
    Integer getAnzahlSchulwochen();
    Semester getSemester();

    void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) throws SvmValidationException;
    void setSchuljahr(String text) throws SvmValidationException;
    void setSemesterbeginn(String text) throws SvmValidationException;
    void setSemesterende(String text) throws SvmValidationException;
    void setAnzahlSchulwochen(String text) throws SvmValidationException;
    void setSemesterOrigin(Semester semesterOrigin);

    boolean checkSemesterBereitsErfasst(SvmModel svmModel);
    boolean checkSemesterUeberlapptAndereSemester(SvmModel svmModel);
    void speichern(SvmModel svmModel);
}
