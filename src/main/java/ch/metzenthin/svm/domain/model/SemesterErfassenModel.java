package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.SemestersTableModel;

import java.util.Calendar;

/**
 * @author Martin Schraner
 */
public interface SemesterErfassenModel extends Model {

    Semesterbezeichnung getSemesterbezeichnung();

    String getSchuljahr();

    Calendar getSemesterbeginn();

    Calendar getSemesterende();

    Calendar getFerienbeginn1();

    Calendar getFerienende1();

    Calendar getFerienbeginn2();

    Calendar getFerienende2();

    Semester getSemester();

    void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) throws SvmValidationException;

    void setSchuljahr(String text) throws SvmValidationException;

    void setSemesterbeginn(String text) throws SvmValidationException;

    void setSemesterende(String text) throws SvmValidationException;

    void setFerienbeginn1(String text) throws SvmValidationException;

    void setFerienende1(String text) throws SvmValidationException;

    void setFerienbeginn2(String text) throws SvmValidationException;

    void setFerienende2(String text) throws SvmValidationException;

    void setSemesterOrigin(Semester semesterOrigin);

    Semester getNaechstesNochNichtErfasstesSemester(SvmModel svmModel);

    boolean checkSemesterBereitsErfasst(SvmModel svmModel);

    boolean checkSemesterUeberlapptAndereSemester(SvmModel svmModel);

    boolean checkIfUpdateAffectsSemesterrechnungen();

    void updateAnzWochenSemesterrechnungen();

    void speichern(SvmModel svmModel, SemestersTableModel semestersTableModel);
}
