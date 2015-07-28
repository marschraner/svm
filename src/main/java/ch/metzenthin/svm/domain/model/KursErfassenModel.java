package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.dataTypes.Wochentag;
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
    void setKurstyp(Kurstyp kurstyp);
    void setAltersbereich(String altersbereich) throws SvmValidationException;
    void setStufe(String stufe) throws SvmValidationException;
    void setWochentag(Wochentag wochentag);
    void setZeitBeginn(String zeitBeginn) throws SvmValidationException;
    void setZeitEnde(String zeitEnde) throws SvmValidationException;
    void setKursort(Kursort kursort);
    void setLehrkraft1(Lehrkraft lehrkraft1);
    void setLehrkraft2(Lehrkraft lehrkraft2);
    void setBemerkungen(String bemerkungen) throws SvmValidationException;

    Lehrkraft[] getSelectableLehrkraefte1(SvmModel svmModel);
    Lehrkraft[] getSelectableLehrkraefte2(SvmModel svmModel);
    boolean checkKursBereitsErfasst(KurseTableModel kurseTableModel);
    void speichern(SvmModel svmModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel);
}