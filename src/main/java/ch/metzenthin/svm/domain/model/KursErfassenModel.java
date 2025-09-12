package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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

  Mitarbeiter getMitarbeiter1();

  Mitarbeiter getMitarbeiter2();

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

  void setMitarbeiter1(Mitarbeiter mitarbeiter1) throws SvmRequiredException;

  void setMitarbeiter2(Mitarbeiter mitarbeiter2);

  void setBemerkungen(String bemerkungen) throws SvmValidationException;

  Kurstyp[] getSelectableKurstypen(SvmModel svmModel);

  Kursort[] getSelectableKursorte(SvmModel svmModel);

  Mitarbeiter[] getSelectableLehrkraefte1(SvmModel svmModel);

  Mitarbeiter[] getSelectableLehrkraefte2(SvmModel svmModel);

  boolean checkKursBereitsErfasst(
      KurseTableModel kurseTableModel, KurseSemesterwahlModel kurseSemesterwahlModel);

  boolean checkIfLektionsgebuehrenErfasst(SvmModel svmModel);

  void speichern(
      SvmModel svmModel,
      KurseSemesterwahlModel kurseSemesterwahlModel,
      KurseTableModel kurseTableModel);
}
