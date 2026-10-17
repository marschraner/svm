package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public record SaveResultAndIdOfSavedEntity<T>(T saveResult, int idOfSavedEntity) {}
