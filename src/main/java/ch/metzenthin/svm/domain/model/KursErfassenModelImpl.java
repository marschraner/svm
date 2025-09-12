package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.isTimePeriodValid;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.commands.CheckKursBereitsErfasstCommand;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.SaveOrUpdateKursCommand;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import java.sql.Time;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Martin Schraner
 */
public class KursErfassenModelImpl extends AbstractModel implements KursErfassenModel {

  private static final Logger LOGGER = LogManager.getLogger(KursErfassenModelImpl.class);
  private static final Mitarbeiter MITARBEITER_KEINE = new Mitarbeiter();

  private final Kurs kurs = new Kurs();
  private Kurs kursOrigin;
  private Kurstyp kurstyp = new Kurstyp();
  private Kursort kursort = new Kursort();
  private Mitarbeiter mitarbeiter1 = new Mitarbeiter();
  private Mitarbeiter mitarbeiter2 = new Mitarbeiter();

  @Override
  public Kurs getKurs() {
    return kurs;
  }

  @Override
  public void setKursOrigin(Kurs kursOrigin) {
    this.kursOrigin = kursOrigin;
  }

  @Override
  public Kurstyp getKurstyp() {
    return kurstyp;
  }

  @Override
  public void setKurstyp(Kurstyp kurstyp) throws SvmRequiredException {
    Kurstyp oldValue = this.kurstyp;
    this.kurstyp = kurstyp;
    firePropertyChange(Field.KURSTYP, oldValue, this.kurstyp);
    if (kurstyp == null) {
      invalidate();
      throw new SvmRequiredException(Field.KURSTYP);
    }
  }

  private final StringModelAttribute altersbereichModelAttribute =
      new StringModelAttribute(
          this,
          Field.ALTERSBEREICH,
          2,
          20,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return kurs.getAltersbereich();
            }

            @Override
            public void setValue(String value) {
              kurs.setAltersbereich(value);
            }
          });

  @Override
  public String getAltersbereich() {
    return altersbereichModelAttribute.getValue();
  }

  @Override
  public void setAltersbereich(String altersbereich) throws SvmValidationException {
    altersbereichModelAttribute.setNewValue(true, altersbereich, isBulkUpdate());
  }

  private final StringModelAttribute stufeModelAttribute =
      new StringModelAttribute(
          this,
          Field.STUFE,
          2,
          30,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return kurs.getStufe();
            }

            @Override
            public void setValue(String value) {
              kurs.setStufe(value);
            }
          });

  @Override
  public String getStufe() {
    return stufeModelAttribute.getValue();
  }

  @Override
  public void setStufe(String stufe) throws SvmValidationException {
    stufeModelAttribute.setNewValue(true, stufe, isBulkUpdate());
  }

  private final TimeModelAttribute zeitBeginnModelAttribute =
      new TimeModelAttribute(
          this,
          Field.ZEIT_BEGINN,
          new AttributeAccessor<>() {
            @Override
            public Time getValue() {
              return kurs.getZeitBeginn();
            }

            @Override
            public void setValue(Time value) {
              kurs.setZeitBeginn(value);
            }
          });

  @Override
  public Time getZeitBeginn() {
    return zeitBeginnModelAttribute.getValue();
  }

  @Override
  public void setZeitBeginn(String zeitBeginn) throws SvmValidationException {
    zeitBeginnModelAttribute.setNewValue(true, zeitBeginn, isBulkUpdate());
    if (!isBulkUpdate()
        && kurs.getZeitBeginn() != null
        && kurs.getZeitEnde() != null
        && !isTimePeriodValid(kurs.getZeitBeginn(), kurs.getZeitEnde())) {
      kurs.setZeitBeginn(null);
      invalidate();
      throw new SvmValidationException(2042, "Keine gültige Zeitperiode", Field.ZEIT_BEGINN);
    }
  }

  private final TimeModelAttribute zeitEndeModelAttribute =
      new TimeModelAttribute(
          this,
          Field.ZEIT_ENDE,
          new AttributeAccessor<>() {
            @Override
            public Time getValue() {
              return kurs.getZeitEnde();
            }

            @Override
            public void setValue(Time value) {
              kurs.setZeitEnde(value);
            }
          });

  @Override
  public Time getZeitEnde() {
    return zeitEndeModelAttribute.getValue();
  }

  @Override
  public void setZeitEnde(String zeitEnde) throws SvmValidationException {
    zeitEndeModelAttribute.setNewValue(true, zeitEnde, isBulkUpdate());
    if (!isBulkUpdate()
        && kurs.getZeitBeginn() != null
        && kurs.getZeitEnde() != null
        && !isTimePeriodValid(kurs.getZeitBeginn(), kurs.getZeitEnde())) {
      kurs.setZeitEnde(null);
      invalidate();
      throw new SvmValidationException(2043, "Keine gültige Zeitperiode", Field.ZEIT_ENDE);
    }
  }

  @Override
  public Wochentag getWochentag() {
    return kurs.getWochentag();
  }

  @Override
  public void setWochentag(Wochentag wochentag) throws SvmRequiredException {
    Wochentag oldValue = kurs.getWochentag();
    kurs.setWochentag(wochentag);
    firePropertyChange(Field.WOCHENTAG, oldValue, kurs.getWochentag());
    if (wochentag == null) {
      invalidate();
      throw new SvmRequiredException(Field.WOCHENTAG);
    }
  }

  @Override
  public Kursort getKursort() {
    return kursort;
  }

  @Override
  public void setKursort(Kursort kursort) throws SvmRequiredException {
    Kursort oldValue = this.kursort;
    this.kursort = kursort;
    firePropertyChange(Field.KURSORT, oldValue, this.kursort);
    if (kursort == null) {
      invalidate();
      throw new SvmRequiredException(Field.KURSORT);
    }
  }

  @Override
  public Kurstyp[] getSelectableKurstypen(SvmModel svmModel) {
    List<Kurstyp> kurstypenList = svmModel.getSelektierbareKurstypenAll();
    if (kursOrigin != null) {
      // Beim Bearbeiten muss ggf auch ein nicht mehr selektierbarer Kurstyp angezeigt werden können
      boolean found = false;
      for (Kurstyp kurstyp1 : kurstypenList) {
        if (kursOrigin.getKurstyp().isIdenticalWith(kurstyp1)) {
          found = true;
          break;
        }
      }
      if (!found) {
        kurstypenList.add(kursOrigin.getKurstyp());
      }
    }
    return kurstypenList.toArray(new Kurstyp[0]);
  }

  @Override
  public Kursort[] getSelectableKursorte(SvmModel svmModel) {
    List<Kursort> kursorteList = svmModel.getSelektierbareKursorteAll();
    if (kursOrigin != null) {
      // Beim Bearbeiten muss ggf auch ein nicht mehr selektierbarer Kursort angezeigt werden können
      boolean found = false;
      for (Kursort kursort1 : kursorteList) {
        if (kursOrigin.getKursort().isIdenticalWith(kursort1)) {
          found = true;
          break;
        }
      }
      if (!found) {
        kursorteList.add(kursOrigin.getKursort());
      }
    }
    return kursorteList.toArray(new Kursort[0]);
  }

  @Override
  public Mitarbeiter[] getSelectableLehrkraefte1(SvmModel svmModel) {
    List<Mitarbeiter> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
    addInaktiveLehrkraefteOrigin(lehrkraefteList);
    return lehrkraefteList.toArray(new Mitarbeiter[0]);
  }

  private void addInaktiveLehrkraefteOrigin(List<Mitarbeiter> lehrkraefteList) {
    if (kursOrigin != null) {
      for (Mitarbeiter mitarbeiterKursOrigin : kursOrigin.getLehrkraefte()) {
        // Beim Bearbeiten müssen ggf auch nicht mehr aktive Lehrkräfte angezeigt werden können
        boolean found = false;
        for (Mitarbeiter mitarbeiter : lehrkraefteList) {
          if (mitarbeiterKursOrigin.isIdenticalWith(mitarbeiter)) {
            found = true;
            break;
          }
        }
        if (!found) {
          lehrkraefteList.add(mitarbeiterKursOrigin);
        }
      }
    }
  }

  @Override
  public Mitarbeiter getMitarbeiter1() {
    return mitarbeiter1;
  }

  @Override
  public void setMitarbeiter1(Mitarbeiter mitarbeiter1) throws SvmRequiredException {
    Mitarbeiter oldValue = this.mitarbeiter1;
    this.mitarbeiter1 = mitarbeiter1;
    firePropertyChange(Field.LEHRKRAFT1, oldValue, this.mitarbeiter1);
    if (mitarbeiter1 == null) {
      invalidate();
      throw new SvmRequiredException(Field.LEHRKRAFT1);
    }
  }

  @Override
  public Mitarbeiter[] getSelectableLehrkraefte2(SvmModel svmModel) {
    List<Mitarbeiter> lehrkraefteList = svmModel.getAktiveLehrkraefteAll();
    // Lehrkraft2 kann auch leer sein
    if (lehrkraefteList.isEmpty() || !lehrkraefteList.get(0).isIdenticalWith(MITARBEITER_KEINE)) {
      lehrkraefteList.add(0, MITARBEITER_KEINE);
    }
    addInaktiveLehrkraefteOrigin(lehrkraefteList);
    return lehrkraefteList.toArray(new Mitarbeiter[0]);
  }

  @Override
  public Mitarbeiter getMitarbeiter2() {
    return mitarbeiter2;
  }

  @Override
  public void setMitarbeiter2(Mitarbeiter mitarbeiter2) {
    if (mitarbeiter2 == MITARBEITER_KEINE) {
      mitarbeiter2 = null;
    }
    Mitarbeiter oldValue = this.mitarbeiter2;
    this.mitarbeiter2 = mitarbeiter2;
    firePropertyChange(Field.LEHRKRAFT2, oldValue, this.mitarbeiter2);
  }

  private final StringModelAttribute bemerkungenModelAttribute =
      new StringModelAttribute(
          this,
          Field.BEMERKUNGEN,
          2,
          100,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return kurs.getBemerkungen();
            }

            @Override
            public void setValue(String value) {
              kurs.setBemerkungen(value);
            }
          });

  @Override
  public String getBemerkungen() {
    return bemerkungenModelAttribute.getValue();
  }

  @Override
  public void setBemerkungen(String bemerkungen) throws SvmValidationException {
    bemerkungenModelAttribute.setNewValue(false, bemerkungen, isBulkUpdate());
  }

  @Override
  public boolean checkKursBereitsErfasst(
      KurseTableModel kurseTableModel, KurseSemesterwahlModel kurseSemesterwahlModel) {
    CommandInvoker commandInvoker = getCommandInvoker();
    CheckKursBereitsErfasstCommand checkKursBereitsErfasstCommand =
        new CheckKursBereitsErfasstCommand(
            kurs,
            kurseSemesterwahlModel.getSemester(),
            mitarbeiter1,
            mitarbeiter2,
            kursOrigin,
            kurseTableModel.getKurse());
    commandInvoker.executeCommand(checkKursBereitsErfasstCommand);
    return checkKursBereitsErfasstCommand.isBereitsErfasst();
  }

  @Override
  public boolean checkIfLektionsgebuehrenErfasst(SvmModel svmModel) {
    List<Lektionsgebuehren> lektionsgebuehrenList = svmModel.getLektionsgebuehrenAllList();
    int kurslaenge = kurs.getKurslaenge();
    for (Lektionsgebuehren lektionsgebuehren : lektionsgebuehrenList) {
      if (lektionsgebuehren.getLektionslaenge() == kurslaenge) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void speichern(
      SvmModel svmModel,
      KurseSemesterwahlModel kurseSemesterwahlModel,
      KurseTableModel kurseTableModel) {
    CommandInvoker commandInvoker = getCommandInvoker();
    SaveOrUpdateKursCommand saveOrUpdateKursCommand =
        new SaveOrUpdateKursCommand(
            kurs,
            kurseSemesterwahlModel.getSemester(),
            kurstyp,
            kursort,
            mitarbeiter1,
            mitarbeiter2,
            kursOrigin,
            kurseTableModel.getKurse());
    commandInvoker.executeCommandAsTransaction(saveOrUpdateKursCommand);
  }

  @Override
  public void initializeCompleted() {
    if (kursOrigin != null) {
      setBulkUpdate(true);
      try {
        setKurstyp(kursOrigin.getKurstyp());
        setAltersbereich(kursOrigin.getAltersbereich());
        setStufe(kursOrigin.getStufe());
        setWochentag(kursOrigin.getWochentag());
        setZeitBeginn(asString(kursOrigin.getZeitBeginn()));
        setZeitEnde(asString(kursOrigin.getZeitEnde()));
        setKursort(kursOrigin.getKursort());
        setMitarbeiter1(kursOrigin.getLehrkraefte().get(0));
        if (kursOrigin.getLehrkraefte().size() > 1) {
          setMitarbeiter2(kursOrigin.getLehrkraefte().get(1));
        }
        setBemerkungen(kursOrigin.getBemerkungen());
      } catch (SvmValidationException e) {
        LOGGER.error(e.getMessage());
      }
      setBulkUpdate(false);
    } else {
      super.initializeCompleted();
    }
  }

  @Override
  public boolean isCompleted() {
    return !mitarbeiter1.isIdenticalWith(mitarbeiter2);
  }

  @Override
  void doValidate() throws SvmValidationException {
    if (!isBulkUpdate() && mitarbeiter1.isIdenticalWith(mitarbeiter2)) {
      throw new SvmValidationException(
          2033, "Lehrkräfte 1 und 2 dürfen nicht identisch sein", Field.LEHRKRAFT2);
    }
  }
}
