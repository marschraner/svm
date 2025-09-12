package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CalculateAnzWochenCommand;
import ch.metzenthin.svm.domain.commands.FindKursCommand;
import ch.metzenthin.svm.persistence.entities.Kursanmeldung;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
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

  Mitarbeiter getMitarbeiter();

  Calendar getAnmeldedatum();

  Calendar getAbmeldedatum();

  String getBemerkungen();

  void setSemester(Semester semester);

  void setWochentag(Wochentag wochentag) throws SvmRequiredException;

  void setZeitBeginn(String zeitBeginn) throws SvmValidationException;

  void setMitarbeiter(Mitarbeiter mitarbeiter) throws SvmRequiredException;

  void setAnmeldedatum(String anmeldedatum) throws SvmValidationException;

  void setAbmeldedatum(String abmeldedatum) throws SvmValidationException;

  void setBemerkungen(String bemerkungen) throws SvmValidationException;

  Semester[] getSelectableSemesterKursanmeldungOrigin();

  Mitarbeiter[] getSelectableLehrkraftKursanmeldungOrigin();

  Semester getInitSemester(List<Semester> semesterList);

  boolean checkIfSemesterIsInPast();

  Calendar getInitAnmeldedatum(KursanmeldungenTableModel kursanmeldungenTableModel);

  FindKursCommand.Result findKurs();

  boolean checkIfKursBereitsErfasst(SchuelerDatenblattModel schuelerDatenblattModel);

  boolean checkIfSchuelerIsAngemeldet(SchuelerDatenblattModel schuelerDatenblattModel);

  CalculateAnzWochenCommand.Result speichern(
      KursanmeldungenTableModel kursanmeldungenTableModel,
      SchuelerDatenblattModel schuelerDatenblattModel);
}
