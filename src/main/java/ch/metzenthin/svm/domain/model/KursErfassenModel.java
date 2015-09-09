package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;

import java.sql.Time;

/**
 * @author Martin Schraner
 */
public interface KursErfassenModel extends Model {

    Kurstyp getKurstyp();
    String getAltersbereich();
    String getStufe();
    Wochentag getWochentag();
    Time getZeitBeginn();
    Time getZeitEnde();
    Kursort getKursort();
    Lehrkraft getLehrkraft1();
    Lehrkraft getLehrkraft2();
    String getBemerkungen();
    Kurs getKurs();

    void setKursOrigin(Kurs kursOrigin);
    void setKurstyp(Kurstyp kurstyp) throws SvmRequiredException;
    void setAltersbereich(String altersbereich) throws SvmValidationException;
    void setStufe(String stufe) throws SvmValidationException;
    void setWochentag(Wochentag wochentag) throws SvmRequiredException;
    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;
    void setZeitEnde(String zeitEnde) throws SvmValidationException;
    void setKursort(Kursort kursort) throws SvmRequiredException;
    void setLehrkraft1(Lehrkraft lehrkraft1) throws SvmRequiredException;
    void setLehrkraft2(Lehrkraft lehrkraft2);
    void setBemerkungen(String bemerkungen) throws SvmValidationException;
    Kurstyp[] getSelectableKurstypen(SvmModel svmModel);
    Kursort[] getSelectableKursorte(SvmModel svmModel);
    Lehrkraft[] getSelectableLehrkraefte1(SvmModel svmModel);
    Lehrkraft[] getSelectableLehrkraefte2(SvmModel svmModel);
    boolean checkKursBereitsErfasst(KurseTableModel kurseTableModel);
    boolean checkIfLektionsgebuehrenErfasst(SvmModel svmModel);
    void speichern(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel);
}
