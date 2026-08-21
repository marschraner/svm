package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndSaveResult;
import ch.metzenthin.svm.domain.model.validation.ValidationUtils;
import ch.metzenthin.svm.service.result.ListenExportSubmitResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans Stamm
 */
public class ListenExportModelImpl implements ListenExportModel {

  private Listentyp listentyp;
  private String titel;
  private File exportFile;

  @Override
  public ValidationResult validateListentyp(Listentyp listentyp) {
    return ValidationUtils.validateNotNull(listentyp, Field.LISTENTYP);
  }

  @Override
  public ValidationResult validateTitel(String titel) {
    return ValidationUtils.validateLengthWhenNotEmpty(titel, 2, 110, Field.TITEL);
  }

  // Übergreifende Validierungen
  private ValidationResult validateTitelNotEmpty(Listentyp listentyp, String titel) {
    if (listentyp.isListenTitelRequired()) {
      return ValidationUtils.validateNotEmpty(titel, Field.TITEL);
    }
    return new ValidationResult();
  }

  @Override
  public void setExportFile(File exportFile) {
    this.exportFile = exportFile;
  }

  @Override
  public ValidationResultsAndSaveResult submit(Listentyp listentyp, String titel) {
    List<ValidationResult> validationResults = validateAll(listentyp, titel);
    if (!ValidationResult.allValidationResultsValid(validationResults)) {
      return new ValidationResultsAndSaveResult(validationResults);
    }

    this.listentyp = listentyp;
    this.titel = titel;

    return new ValidationResultsAndSaveResult(
        validationResults, ListenExportSubmitResult.KONFIGURATION_ERFOLGREICH);
  }

  private List<ValidationResult> validateAll(Listentyp listentyp, String titel) {
    List<ValidationResult> validationResults = new ArrayList<>();

    // Einzelne Felder validieren
    validationResults.add(validateListentyp(listentyp));
    validationResults.add(validateTitel(titel));

    boolean errorsFound =
        validationResults.stream().anyMatch(validationResult -> !validationResult.isValid());
    if (errorsFound) {
      return validationResults;
    }

    // Alle Felder sind validiert, jetzt die übergreifenden Validierungen durchführen
    validationResults.add(validateTitelNotEmpty(listentyp, titel));

    return validationResults;
  }

  @Override
  public Listentyp getListentyp() {
    return listentyp;
  }

  @Override
  public String getTitel() {
    return titel;
  }

  @Override
  public File getExportFile() {
    return exportFile;
  }
}
