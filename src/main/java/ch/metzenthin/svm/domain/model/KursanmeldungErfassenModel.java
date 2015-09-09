package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.FindKursCommand;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

/**
 * @author Martin Schraner
 */
public interface KursanmeldungErfassenModel extends Model {

    void setKursanmeldungOrigin(Kursanmeldung kursanmeldungOrigin);
    Semester getSemester();
    Wochentag getWochentag();
    Time getZeitBeginn();
    Lehrkraft getLehrkraft();
    Calendar getAnmeldedatum();
    Calendar getAbmeldedatum();
    String getBemerkungen();

    void setSemester(Semester semester);
    void setWochentag(Wochentag wochentag) throws SvmRequiredException;
    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;
    void setLehrkraft(Lehrkraft lehrkraft) throws SvmRequiredException;
    void setAnmeldedatum(String anmeldedatum) throws SvmValidationException;
    void setAbmeldedatum(String abmeldedatum) throws SvmValidationException;
    void setBemerkungen(String bemerkungen) throws SvmValidationException;

    Semester[] getSelectableSemesterKursanmeldungOrigin();
    Lehrkraft[] getSelectableLehrkraftKursanmeldungOrigin();
    Semester getInitSemester(List<Semester> semesterList);
    boolean checkIfSemesterIsInPast();
    FindKursCommand.Result findKurs();
    boolean checkIfKursBereitsErfasst(SchuelerDatenblattModel schuelerDatenblattModel);
    void speichern(KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel);
}
