package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.model.formatting.FormattingUtils.formatString;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotEmpty;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotTooLong;
import static ch.metzenthin.svm.domain.model.validation.ValidationUtils.validateNotTooShort;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.EntityAlreadyExistsException;
import ch.metzenthin.svm.domain.model.validation.ValidationAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
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

  private final boolean isNeu;
  private final Kursort kursort;
  private final KursortService kursortService;

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public CreateOrUpdateKursortModelImpl(
      Optional<Kursort> kursortToBeModifiedOptional, KursortService kursortService) {
    this.isNeu = kursortToBeModifiedOptional.isEmpty();
    this.kursort = kursortToBeModifiedOptional.orElseGet(Kursort::new);
    this.kursortService = kursortService;
  }

  @Override
  public void initialiseViewFields(CreateOrUpdateKursortView createOrUpdateKursortView) {
    createOrUpdateKursortView.setTxtBezeichnungText(kursort.getBezeichnung());
    if (isNeu) {
      createOrUpdateKursortView.setCheckBoxSelektierbarSelected(true);
    } else {
      createOrUpdateKursortView.setCheckBoxSelektierbarSelected(kursort.isSelektierbar());
    }
  }

  @Override
  public String formatBezeichnung(String bezeichnung) {
    return formatString(bezeichnung);
  }

  @Override
  public ValidationResult validateBezeichnung(String bezeichnung) {
    ValidationResult validationResult = validateNotEmpty(bezeichnung, Field.BEZEICHNUNG);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validateNotTooShort(bezeichnung, MIN_KURSORT_LENGTH, Field.BEZEICHNUNG);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    validationResult = validateNotTooLong(bezeichnung, MAX_KURSORT_LENGTH, Field.BEZEICHNUNG);
    if (!validationResult.isValid()) {
      return validationResult;
    }

    return validateKursortNotAlreadyExists(bezeichnung);
  }

  private ValidationResult validateKursortNotAlreadyExists(String bezeichnung) {
    return (kursortService.doesKursortAlreadyExist(kursort.getKursortId(), bezeichnung))
        ? new ValidationResult("Bezeichnung bereits in Verwendung.", Set.of(Field.BEZEICHNUNG))
        : new ValidationResult();
  }

  @Override
  public ValidationAndSaveResult speichern(CreateOrUpdateKursortView createOrUpdateKursortView) {
    ValidationResult validationResult = validate(createOrUpdateKursortView);
    if (!validationResult.isValid()) {
      return new ValidationAndSaveResult(validationResult);
    }
    updateModel(createOrUpdateKursortView);
    SaveKursortResult saveKursortResult = saveKursort();
    return new ValidationAndSaveResult(validationResult, saveKursortResult);
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
