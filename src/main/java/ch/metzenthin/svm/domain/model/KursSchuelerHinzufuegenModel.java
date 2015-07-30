package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.AddKursToSchuelerAndSaveCommand;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.sql.Time;

/**
 * @author Martin Schraner
 */
public interface KursSchuelerHinzufuegenModel extends Model {

    Semester getSemester();
    Wochentag getWochentag();
    Time getZeitBeginn();
    Lehrkraft getLehrkraft();

    void setSemester(Semester semester);
    void setWochentag(Wochentag wochentag) throws SvmRequiredException;
    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;
    void setLehrkraft(Lehrkraft lehrkraft) throws SvmRequiredException;

    Semester getDefaultSemester(SvmModel svmModel, Semester[] selectableSemesters);
    AddKursToSchuelerAndSaveCommand.Result hinzufuegen(KurseTableModel kurseTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
