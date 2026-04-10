package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultAndSaveKursortResult;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.service.KursortService;
import ch.metzenthin.svm.service.result.SaveKursortResult;
import ch.metzenthin.svm.ui.view.CreateOrUpdateKursortView;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @author Martin Schraner
 */
public class CreateOrUpdateKursortModelImpl implements CreateOrUpdateKursortModel {

  private static final int MIN_KURSORT_LENGTH = 2;
  private static final int MAX_KURSORT_LENGTH = 50;

  private final Kursort kursort;
  private final KursortService kursortService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateKursortModelImpl(
      Optional<Kursort> kursortToBeModifiedOptional, KursortService kursortService) {
    boolean isNeu = kursortToBeModifiedOptional.isEmpty();
    this.kursort = kursortToBeModifiedOptional.orElseGet(Kursort::new);
    this.kursortService = kursortService;
    if (isNeu) {
      initialiseModelValuesOnNeu();
    }
  }

  private void initialiseModelValuesOnNeu() {
    kursort.setSelektierbar(true);
  }

  @Override
  public void initialiseViewFields(CreateOrUpdateKursortView createOrUpdateKursortView) {
    createOrUpdateKursortView.setTxtBezeichnungText(kursort.getBezeichnung());
    createOrUpdateKursortView.setCheckBoxSelektierbarSelected(kursort.isSelektierbar());
  }

  @Override
  public String formatBezeichnung(String bezeichnung) {
    return (bezeichnung != null) ? bezeichnung.trim() : null;
  }

  @Override
  public ValidationResult validateBezeichnung(String bezeichnung) {
    ValidationResult validationResult = validateNotEmpty(bezeichnung);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validateNotTooShort(bezeichnung);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validateNotTooLong(bezeichnung);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateKursortNotAlreadyExists(bezeichnung);
  }

  private ValidationResult validateNotEmpty(String bezeichnung) {
    return (bezeichnung != null && !bezeichnung.isBlank())
        ? new ValidationResult(true, null, null)
        : new ValidationResult(false, "Eintrag ist obligatorisch!", Set.of(Field.BEZEICHNUNG));
  }

  private ValidationResult validateNotTooShort(String bezeichnung) {
    return (bezeichnung.length() < MIN_KURSORT_LENGTH)
        ? new ValidationResult(
            false,
            "Länge muss mindestens " + MIN_KURSORT_LENGTH + " sein!",
            Set.of(Field.BEZEICHNUNG))
        : new ValidationResult(true, null, null);
  }

  private ValidationResult validateNotTooLong(String bezeichnung) {
    return (bezeichnung.length() > MAX_KURSORT_LENGTH)
        ? new ValidationResult(
            false,
            "Länge darf höchstens " + MAX_KURSORT_LENGTH + " sein!",
            Set.of(Field.BEZEICHNUNG))
        : new ValidationResult(true, null, null);
  }

  private ValidationResult validateKursortNotAlreadyExists(String bezeichnung) {
    return (kursortService.doesKursortAlreadyExist(kursort.getKursortId(), bezeichnung))
        ? new ValidationResult(
            false, "Bezeichnung bereits in Verwendung.", Set.of(Field.BEZEICHNUNG))
        : new ValidationResult(true, null, null);
  }

  @Override
  public ValidationResultAndSaveKursortResult speichern(
      CreateOrUpdateKursortView createOrUpdateKursortView) {

    ValidationResult validationResult = validate(createOrUpdateKursortView);
    if (!validationResult.isValid()) {
      return new ValidationResultAndSaveKursortResult(validationResult, null);
    }

    updateModel(createOrUpdateKursortView);

    SaveKursortResult saveKursortResult = saveKursort();

    return new ValidationResultAndSaveKursortResult(validationResult, saveKursortResult);
  }

  private ValidationResult validate(CreateOrUpdateKursortView createOrUpdateKursortView) {
    return validateBezeichnung(createOrUpdateKursortView.getTxtBezeichnungText());
  }

  void updateModel(CreateOrUpdateKursortView createOrUpdateKursortView) {
    kursort.setBezeichnung(createOrUpdateKursortView.getTxtBezeichnungText());
    kursort.setSelektierbar(createOrUpdateKursortView.isCheckBoxSelektierbarSelected());
  }

  private SaveKursortResult saveKursort() {
    SaveKursortResult saveKursortResult;
    try {
      kursortService.saveKursort(kursort);
      saveKursortResult = SaveKursortResult.SPEICHERN_ERFOLGREICH;
    } catch (EntityAlreadyExistsException e) {
      saveKursortResult = SaveKursortResult.KURSORT_BEREITS_ERFASST;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      saveKursortResult = SaveKursortResult.KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return saveKursortResult;
  }
}
