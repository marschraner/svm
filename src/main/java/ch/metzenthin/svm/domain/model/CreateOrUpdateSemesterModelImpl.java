package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.common.utils.Converter.asString;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Schuljahre;
import ch.metzenthin.svm.common.datatypes.Semesterbezeichnung;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.EntityWithOverlappingPeriodsException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.SemesterService;
import ch.metzenthin.svm.service.result.SaveSemesterResult;
import jakarta.persistence.OptimisticLockException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CreateOrUpdateSemesterModelImpl extends AbstractModel
    implements CreateOrUpdateSemesterModel {

  private static final Logger LOGGER = LogManager.getLogger(CreateOrUpdateSemesterModelImpl.class);
  private static final String KEINE_GUELTIGE_PERIODE = "Keine gültige Periode";

  private final Semester semester;
  private final SemesterService semesterService;

  public CreateOrUpdateSemesterModelImpl(
      Optional<Semester> semesterToBeModifiedOptional, SemesterService semesterService) {
    this.semester = semesterToBeModifiedOptional.orElseGet(Semester::new);
    this.semesterService = semesterService;
  }

  @Override
  public Semester getSemester() {
    return semester;
  }

  private final StringModelAttribute schuljahrModelAttribute =
      new StringModelAttribute(
          this,
          Field.SCHULJAHR,
          9,
          9,
          new AttributeAccessor<>() {
            @Override
            public String getValue() {
              return semester.getSchuljahr();
            }

            @Override
            public void setValue(String value) {
              semester.setSchuljahr(value);
            }
          });

  @Override
  public String getSchuljahr() {
    return schuljahrModelAttribute.getValue();
  }

  @Override
  public void setSchuljahr(String schuljahr) throws SvmValidationException {
    setSchuljahr(schuljahr, false);
  }

  private void setSchuljahr(String schuljahr, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    schuljahrModelAttribute.setNewValue(
        true, schuljahr, isBulkUpdate(), enforcePropertyChangeEvent);
  }

  @Override
  public Semesterbezeichnung getSemesterbezeichnung() {
    return semester.getSemesterbezeichnung();
  }

  @Override
  public void setSemesterbezeichnung(Semesterbezeichnung semesterbezeichnung) {
    setSemesterbezeichnung(semesterbezeichnung, false);
  }

  private void setSemesterbezeichnung(
      Semesterbezeichnung semesterbezeichnung, boolean enforcePropertyChangeEvent) {
    Semesterbezeichnung oldValue =
        (enforcePropertyChangeEvent) ? null : semester.getSemesterbezeichnung();
    semester.setSemesterbezeichnung(semesterbezeichnung);
    firePropertyChange(Field.SEMESTERBEZEICHNUNG, oldValue, semester.getSemesterbezeichnung());
  }

  private final CalendarModelAttribute semesterbeginnModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.SEMESTERBEGINN,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getSemesterbeginn();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setSemesterbeginn(value);
            }
          });

  @Override
  public Calendar getSemesterbeginn() {
    return semesterbeginnModelAttribute.getValue();
  }

  @Override
  public void setSemesterbeginn(String semesterbeginn) throws SvmValidationException {
    setSemesterbeginn(semesterbeginn, false);
  }

  private void setSemesterbeginn(String semesterbeginn, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    semesterbeginnModelAttribute.setNewValue(
        true, semesterbeginn, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getSemesterbeginn() != null
        && semester.getSemesterbeginn().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      semester.setSemesterbeginn(null);
      invalidate();
      throw new SvmValidationException(
          2042, "Semesterbeginn muss ein Montag sein", Field.SEMESTERBEGINN);
    }
    if (!isBulkUpdate()
        && semester.getSemesterbeginn() != null
        && semester.getSemesterende() != null
        && semester.getSemesterbeginn().after(semester.getSemesterende())) {
      semester.setSemesterbeginn(null);
      invalidate();
      throw new SvmValidationException(2022, KEINE_GUELTIGE_PERIODE, Field.SEMESTERBEGINN);
    }
  }

  private final CalendarModelAttribute semesterendeModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.SEMESTERENDE,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getSemesterende();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setSemesterende(value);
            }
          });

  @Override
  public Calendar getSemesterende() {
    return semesterendeModelAttribute.getValue();
  }

  @Override
  public void setSemesterende(String semesterende) throws SvmValidationException {
    setSemesterende(semesterende, false);
  }

  private void setSemesterende(String semesterende, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    semesterendeModelAttribute.setNewValue(
        true, semesterende, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getSemesterende() != null
        && semester.getSemesterende().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
      semester.setSemesterende(null);
      invalidate();
      throw new SvmValidationException(
          2044, "Semesterende muss ein Samstag sein", Field.SEMESTERENDE);
    }
    if (!isBulkUpdate()
        && semester.getSemesterbeginn() != null
        && semester.getSemesterende() != null
        && semester.getSemesterbeginn().after(semester.getSemesterende())) {
      semester.setSemesterende(null);
      invalidate();
      throw new SvmValidationException(2024, KEINE_GUELTIGE_PERIODE, Field.SEMESTERENDE);
    }
  }

  private final CalendarModelAttribute ferienbeginn1ModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.FERIENBEGINN1,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getFerienbeginn1();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setFerienbeginn1(value);
            }
          });

  @Override
  public Calendar getFerienbeginn1() {
    return ferienbeginn1ModelAttribute.getValue();
  }

  @Override
  public void setFerienbeginn1(String ferienbeginn1) throws SvmValidationException {
    setFerienbeginn1(ferienbeginn1, false);
  }

  private void setFerienbeginn1(String ferienbeginn1, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    ferienbeginn1ModelAttribute.setNewValue(
        true, ferienbeginn1, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getFerienbeginn1() != null
        && semester.getFerienbeginn1().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      semester.setFerienbeginn1(null);
      invalidate();
      throw new SvmValidationException(
          2045, "Ferienbeginn muss ein Montag sein", Field.FERIENBEGINN1);
    }
    if (!isBulkUpdate()
        && semester.getFerienbeginn1() != null
        && semester.getFerienende1() != null
        && semester.getFerienbeginn1().after(semester.getFerienende1())) {
      semester.setFerienbeginn1(null);
      invalidate();
      throw new SvmValidationException(2025, KEINE_GUELTIGE_PERIODE, Field.FERIENBEGINN1);
    }
  }

  private final CalendarModelAttribute ferienende1ModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.FERIENENDE1,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getFerienende1();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setFerienende1(value);
            }
          });

  @Override
  public Calendar getFerienende1() {
    return ferienende1ModelAttribute.getValue();
  }

  @Override
  public void setFerienende1(String ferienende1) throws SvmValidationException {
    setFerienende1(ferienende1, false);
  }

  private void setFerienende1(String ferienende1, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    ferienende1ModelAttribute.setNewValue(
        true, ferienende1, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getFerienende1() != null
        && semester.getFerienende1().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
      semester.setFerienende1(null);
      invalidate();
      throw new SvmValidationException(2046, "Ferienende muss ein Samstag sein", Field.FERIENENDE1);
    }
    if (!isBulkUpdate()
        && semester.getFerienbeginn1() != null
        && semester.getFerienende1() != null
        && semester.getFerienbeginn1().after(semester.getFerienende1())) {
      semester.setFerienende1(null);
      invalidate();
      throw new SvmValidationException(2026, KEINE_GUELTIGE_PERIODE, Field.FERIENENDE1);
    }
  }

  private final CalendarModelAttribute ferienbeginn2ModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.FERIENBEGINN2,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getFerienbeginn2();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setFerienbeginn2(value);
            }
          });

  @Override
  public Calendar getFerienbeginn2() {
    return ferienbeginn2ModelAttribute.getValue();
  }

  @Override
  public void setFerienbeginn2(String ferienbeginn2) throws SvmValidationException {
    setFerienbeginn2(ferienbeginn2, false);
  }

  private void setFerienbeginn2(String ferienbeginn2, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    ferienbeginn2ModelAttribute.setNewValue(
        false, ferienbeginn2, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getFerienbeginn2() != null
        && semester.getFerienbeginn2().get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      semester.setFerienbeginn2(null);
      invalidate();
      throw new SvmValidationException(
          2047, "Ferienbeginn muss ein Montag sein", Field.FERIENBEGINN2);
    }
    if (!isBulkUpdate()
        && semester.getFerienbeginn2() != null
        && semester.getFerienende2() != null
        && semester.getFerienbeginn2().after(semester.getFerienende2())) {
      semester.setFerienbeginn2(null);
      invalidate();
      throw new SvmValidationException(2027, KEINE_GUELTIGE_PERIODE, Field.FERIENBEGINN2);
    }
  }

  private final CalendarModelAttribute ferienende2ModelAttribute =
      new CalendarModelAttribute(
          this,
          Field.FERIENENDE2,
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MIN, Calendar.JANUARY, 1),
          new GregorianCalendar(Schuljahre.SCHULJAHR_VALID_MAX + 1, Calendar.DECEMBER, 31),
          new AttributeAccessor<>() {
            @Override
            public Calendar getValue() {
              return semester.getFerienende2();
            }

            @Override
            public void setValue(Calendar value) {
              semester.setFerienende2(value);
            }
          });

  @Override
  public Calendar getFerienende2() {
    return ferienende2ModelAttribute.getValue();
  }

  @Override
  public void setFerienende2(String ferienende2) throws SvmValidationException {
    setFerienende2(ferienende2, false);
  }

  private void setFerienende2(String ferienende2, boolean enforcePropertyChangeEvent)
      throws SvmValidationException {
    ferienende2ModelAttribute.setNewValue(
        false, ferienende2, isBulkUpdate(), enforcePropertyChangeEvent);
    if (!isBulkUpdate()
        && semester.getFerienende2() != null
        && semester.getFerienende2().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
      semester.setFerienende2(null);
      invalidate();
      throw new SvmValidationException(2044, "Ferienende muss ein Samstag sein", Field.FERIENENDE2);
    }
    if (!isBulkUpdate()
        && semester.getFerienbeginn2() != null
        && semester.getFerienende2() != null
        && semester.getFerienbeginn2().after(semester.getFerienende2())) {
      semester.setFerienende2(null);
      invalidate();
      throw new SvmValidationException(2028, KEINE_GUELTIGE_PERIODE, Field.FERIENENDE2);
    }
  }

  @Override
  public boolean checkIfUpdateAffectsSemesterrechnungen() {
    return semesterService.checkIfUpdateAffectsSemesterrechnungen(semester);
  }

  @Override
  public Semester getNaechstesNochNichtErfasstesSemester() {
    return semesterService.determineNaechstesNochNichtErfasstesSemester();
  }

  @Override
  public SaveSemesterResult speichern(boolean updateSemesterrechnungen) {

    SaveSemesterResult saveSemesterResult;

    try {
      semesterService.saveSemesterAndUpdateAnzahlWochenOfSemesterrechnungen(
          semester, updateSemesterrechnungen);
      saveSemesterResult = SaveSemesterResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_BEREITS_ERFASST;
    } catch (EntityWithOverlappingPeriodsException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveSemesterResult = SaveSemesterResult.SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }

    return saveSemesterResult;
  }

  @Override
  public void initializeCompleted() {
    if (semester.getSemesterId() != null) {
      setBulkUpdate(true);
      try {
        setSchuljahr(semester.getSchuljahr(), true);
        setSemesterbezeichnung(semester.getSemesterbezeichnung(), true);
        setSemesterbeginn(asString(semester.getSemesterbeginn()), true);
        setSemesterende(asString(semester.getSemesterende()), true);
        setFerienbeginn1(asString(semester.getFerienbeginn1()), true);
        setFerienende1(asString(semester.getFerienende1()), true);
        setFerienbeginn2(asString(semester.getFerienbeginn2()), true);
        setFerienende2(asString(semester.getFerienende2()), true);
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
    return true;
  }

  @Override
  void doValidate() throws SvmValidationException {
    if (!isBulkUpdate()
        && semester.getSchuljahr() != null
        && semester.getSemesterbeginn() != null
        && !semester
            .getSchuljahr()
            .contains(Integer.toString(semester.getSemesterbeginn().get(Calendar.YEAR)))) {
      throw new SvmValidationException(
          2031, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERBEGINN);
    }
    if (!isBulkUpdate()
        && semester.getSchuljahr() != null
        && semester.getSemesterende() != null
        && !semester
            .getSchuljahr()
            .contains(Integer.toString(semester.getSemesterende().get(Calendar.YEAR)))) {
      throw new SvmValidationException(
          2032, "Datum liegt nicht im Schuljahr " + semester.getSchuljahr(), Field.SEMESTERENDE);
    }
    if (semester.getFerienbeginn1() != null
        && semester.getSemesterbeginn() != null
        && !semester.getFerienbeginn1().after(semester.getSemesterbeginn())) {
      throw new SvmValidationException(
          2061, "Ferienbeginn muss nach Semesterbeginn liegen", Field.FERIENBEGINN1);
    }
    if (semester.getFerienbeginn2() != null
        && semester.getSemesterbeginn() != null
        && !semester.getFerienbeginn2().after(semester.getSemesterbeginn())) {
      throw new SvmValidationException(
          2062, "Ferienbeginn muss nach Semesterbeginn liegen", Field.FERIENBEGINN2);
    }
    if (semester.getFerienende1() != null
        && semester.getSemesterende() != null
        && !semester.getFerienende1().before(semester.getSemesterende())) {
      throw new SvmValidationException(
          2063, "Ferienende muss vor Semesterende liegen", Field.FERIENENDE1);
    }
    if (semester.getFerienende2() != null
        && semester.getSemesterende() != null
        && !semester.getFerienende2().before(semester.getSemesterende())) {
      throw new SvmValidationException(
          2064, "Ferienende muss vor Semesterende liegen", Field.FERIENENDE2);
    }
  }
}
