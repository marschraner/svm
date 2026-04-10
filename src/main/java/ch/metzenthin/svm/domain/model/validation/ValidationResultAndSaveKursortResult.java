package ch.metzenthin.svm.domain.model.validation;

import ch.metzenthin.svm.service.result.SaveKursortResult;

/**
 * @author Martin Schraner
 */
public record ValidationResultAndSaveKursortResult(
    ValidationResult validationResult, SaveKursortResult saveKursortResult) {}
