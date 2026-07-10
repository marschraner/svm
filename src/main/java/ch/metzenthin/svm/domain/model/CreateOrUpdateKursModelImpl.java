package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validatePeriod;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.entityfields.ConvertedKursFields;
import ch.metzenthin.svm.domain.model.entityfields.KursFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.persistence.entities.Kurs;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.service.KursLehrkraftService;
import ch.metzenthin.svm.service.KursService;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.KurstypService;
import ch.metzenthin.svm.service.MitarbeiterService;
import ch.metzenthin.svm.service.result.SaveKursResult;
import jakarta.persistence.OptimisticLockException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursModelImpl implements CreateOrUpdateKursModel {

  private final boolean neu;
  private final Kurs kurs;
  private final Semester semester;
  private Mitarbeiter lehrkraft1;
  private Mitarbeiter lehrkraft2;
  private final KursService kursService;
  private final KurstypService kurstypService;
  private final KursortService kursortService;
  private final MitarbeiterService mitarbeiterService;
  private final KursLehrkraftService kursLehrkraftService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateKursModelImpl(
      Optional<Kurs> kursToBeModifiedOptional,
      Semester semester,
      KursService kursService,
      KurstypService kurstypService,
      KursortService kursortService,
      MitarbeiterService mitarbeiterService,
      KursLehrkraftService kursLehrkraftService) {
    this.neu = kursToBeModifiedOptional.isEmpty();
    this.kurs = kursToBeModifiedOptional.orElseGet(Kurs::new);
    this.semester = semester;
    this.kurstypService = kurstypService;
    this.kursortService = kursortService;
    this.mitarbeiterService = mitarbeiterService;
    this.kursLehrkraftService = kursLehrkraftService;
    if (kursToBeModifiedOptional.isPresent()) {
      List<Mitarbeiter> lehrkraefte =
          kursLehrkraftService.findSortedLehrkraefteByKursId(kurs.getKursId());
      if (!lehrkraefte.isEmpty()) {
        this.lehrkraft1 = lehrkraefte.get(0);
        this.lehrkraft2 = (lehrkraefte.size() >= 2) ? lehrkraefte.get(1) : null;
      } else {
        this.lehrkraft1 = null;
        this.lehrkraft2 = null;
      }
    } else {
      this.lehrkraft1 = null;
      this.lehrkraft2 = null;
    }
    this.kursService = kursService;
  }

  @Override
  public boolean isNeu() {
    return neu;
  }

  @Override
  public KursFields getKursFields() {
    return KursFields.of(kurs);
  }

  @Override
  public Kurstyp getKurstyp() {
    return kurs.getKurstyp();
  }

  @Override
  public Kursort getKursort() {
    return kurs.getKursort();
  }

  @Override
  public Mitarbeiter getLehrkraft1() {
    return lehrkraft1;
  }

  @Override
  public Mitarbeiter getLehrkraft2() {
    return lehrkraft2;
  }

  @Override
  public Kurstyp[] getSelectableKurstypen() {
    List<Kurstyp> kurstypen = kurstypService.findSelektierbareKurstypen();
    if (!neu) {
      Kurstyp kurstypOfKursToBeModified = kurs.getKurstyp();
      if (kurstypen.stream()
          .noneMatch(
              kurstyp -> kurstyp.getKurstypId().equals(kurstypOfKursToBeModified.getKurstypId()))) {
        kurstypen.add(kurstypOfKursToBeModified);
      }
    }
    return kurstypen.toArray(new Kurstyp[0]);
  }

  @Override
  public Kursort[] getSelectableKursorte() {
    List<Kursort> kursorte = kursortService.findSelektierbareKursorte();
    if (!neu) {
      Kursort kursortOfKursToBeModified = kurs.getKursort();
      if (kursorte.stream()
          .noneMatch(
              kursort -> kursort.getKursortId().equals(kursortOfKursToBeModified.getKursortId()))) {
        kursorte.add(kursortOfKursToBeModified);
      }
    }
    return kursorte.toArray(new Kursort[0]);
  }

  @Override
  public Mitarbeiter[] getSelectableLehrkraefte1() {
    return getSelectableLehrkraefte().toArray(new Mitarbeiter[0]);
  }

  private List<Mitarbeiter> getSelectableLehrkraefte() {
    List<Mitarbeiter> lehrkraefte = mitarbeiterService.findAktiveLehrkraefte();
    if (!neu) {
      List<Mitarbeiter> lehrkraefteOfKursToBeModified =
          kursLehrkraftService.findSortedLehrkraefteByKursId(kurs.getKursId());
      if (!lehrkraefteOfKursToBeModified.isEmpty()
          && lehrkraefte.stream()
              .noneMatch(
                  mitarbeiter ->
                      mitarbeiter
                          .getPersonId()
                          .equals(lehrkraefteOfKursToBeModified.get(0).getPersonId()))) {
        lehrkraefte.add(lehrkraefteOfKursToBeModified.get(0));
      }
      if (lehrkraefteOfKursToBeModified.size() > 1
          && lehrkraefte.stream()
              .noneMatch(
                  mitarbeiter ->
                      mitarbeiter
                          .getPersonId()
                          .equals(lehrkraefteOfKursToBeModified.get(1).getPersonId()))) {
        lehrkraefte.add(lehrkraefteOfKursToBeModified.get(1));
      }
    }
    return lehrkraefte;
  }

  @Override
  public Mitarbeiter[] getSelectableLehrkraefte2() {
    List<Mitarbeiter> selectableLehrkraefte = getSelectableLehrkraefte();
    // Lehrkraft2 ist optional, deshalb als erstes Item ein leeres Objekt, damit die Auswahl
    // gelöscht werden kann.
    selectableLehrkraefte.add(0, null);
    return selectableLehrkraefte.toArray(new Mitarbeiter[0]);
  }

  @Override
  public ValidationResult validateKurstyp(Kurstyp kurstyp) {
    return ValidationUtils.validateNotNull(kurstyp, Field.KURSTYP);
  }

  @Override
  public ValidationResult validateAltersbereich(String altersbereich) {
    return ValidationUtils.validateNotEmptyAndLength(altersbereich, 2, 20, Field.ALTERSBEREICH);
  }

  @Override
  public ValidationResult validateStufe(String stufe) {
    return ValidationUtils.validateNotEmptyAndLength(stufe, 2, 30, Field.STUFE);
  }

  @Override
  public ValidationResult validateWochentag(Wochentag wochentag) {
    return ValidationUtils.validateNotNull(wochentag, Field.WOCHENTAG);
  }

  @Override
  public ValidationResult validateZeitBeginn(Time zeitBeginn) {
    return ValidationUtils.validateNotNull(zeitBeginn, Field.ZEIT_BEGINN);
  }

  @Override
  public ValidationResult validateZeitEnde(Time zeitEnde) {
    return ValidationUtils.validateNotNull(zeitEnde, Field.ZEIT_ENDE);
  }

  @Override
  public ValidationResult validateKursort(Kursort kursort) {
    return ValidationUtils.validateNotNull(kursort, Field.KURSORT);
  }

  @Override
  public ValidationResult validateLehrkraft1(Mitarbeiter lehrkraft1) {
    return ValidationUtils.validateNotNull(lehrkraft1, Field.LEHRKRAFT1);
  }

  @Override
  public ValidationResult validateBemerkungen(String bemerkungen) {
    return ValidationUtils.validateLengthWhenNotEmpty(bemerkungen, 2, 100, Field.BEMERKUNGEN);
  }

  // Übergreifende Validierungen
  private ValidationResult validateZeitBeginnZeitEndePeriod(Time zeitBeginn, Time zeitEnde) {
    return validatePeriod(zeitBeginn, zeitEnde, Field.ZEIT_BEGINN, Field.ZEIT_ENDE);
  }

  private ValidationResult validateLehrkraefte1And2NotIdentical(
      Mitarbeiter lehrkraft1, Mitarbeiter lehrkraft2) {
    if (lehrkraft1 != null
        && lehrkraft2 != null
        && lehrkraft1.getPersonId().equals(lehrkraft2.getPersonId())) {
      return new ValidationResult(
          "Lehrkräfte 1 und 2 dürfen nicht identisch sein", Set.of(Field.LEHRKRAFT2));
    } else {
      return new ValidationResult();
    }
  }

  @Override
  public ValidationResultsAndSaveResult speichern(
      KursFields kursFields,
      Kurstyp kurstyp,
      Kursort kursort,
      Mitarbeiter lehrkraft1,
      Mitarbeiter lehrkraft2) {

    ConvertedFieldsAndConversionResults<ConvertedKursFields>
        convertedKursFieldsAndConversionResults = convertAll(kursFields);
    if (!convertedKursFieldsAndConversionResults.isValid()) {
      List<ValidationResult> invalidConversionResultsAsValidationResults =
          convertedKursFieldsAndConversionResults.getInvalidConversionResultsAsValidationResults();
      return new ValidationResultsAndSaveResult(invalidConversionResultsAsValidationResults);
    }
    ConvertedKursFields convertedKursFields =
        convertedKursFieldsAndConversionResults.convertedFields();

    List<ValidationResult> validationResults =
        validateAll(convertedKursFields, kurstyp, kursort, lehrkraft1, lehrkraft2);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    updateModel(convertedKursFields, kurstyp, kursort, lehrkraft1, lehrkraft2);

    SaveKursResult saveKursResult = saveKurs();
    return new ValidationResultsAndSaveResult(validationResults, saveKursResult);
  }

  private static ConvertedFieldsAndConversionResults<ConvertedKursFields> convertAll(
      KursFields kursFields) {
    return kursFields.convert();
  }

  private List<ValidationResult> validateAll(
      ConvertedKursFields convertedKursFields,
      Kurstyp kurstyp,
      Kursort kursort,
      Mitarbeiter lehrkraft1,
      Mitarbeiter lehrkraft2) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(validateKurstyp(kurstyp));
    validationResults.add(validateAltersbereich(convertedKursFields.altersbereich()));
    validationResults.add(validateStufe(convertedKursFields.stufe()));
    validationResults.add(validateWochentag(convertedKursFields.wochentag()));
    validationResults.add(validateZeitBeginn(convertedKursFields.zeitBeginn()));
    validationResults.add(validateZeitEnde(convertedKursFields.zeitEnde()));
    validationResults.add(validateKursort(kursort));
    validationResults.add(validateLehrkraft1(lehrkraft1));
    validationResults.add(validateBemerkungen(convertedKursFields.bemerkungen()));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(
        validateZeitBeginnZeitEndePeriod(
            convertedKursFields.zeitBeginn(), convertedKursFields.zeitEnde()));
    validationResults.add(validateLehrkraefte1And2NotIdentical(lehrkraft1, lehrkraft2));

    return validationResults;
  }

  void updateModel(
      ConvertedKursFields kursFields,
      Kurstyp kurstyp,
      Kursort kursort,
      Mitarbeiter lehrkraft1,
      Mitarbeiter lehrkraft2) {
    kursFields.mergeIntoEntity(kurs);
    kurs.setSemester(semester);
    kurs.setKurstyp(kurstyp);
    kurs.setKursort(kursort);
    this.lehrkraft1 = lehrkraft1;
    this.lehrkraft2 = lehrkraft2;
  }

  private SaveKursResult saveKurs() {
    SaveKursResult saveKursResult;
    try {
      saveKursResult = kursService.saveKurs(kurs, lehrkraft1, lehrkraft2);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKursResult = SaveKursResult.KURS_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKursResult;
  }
}
