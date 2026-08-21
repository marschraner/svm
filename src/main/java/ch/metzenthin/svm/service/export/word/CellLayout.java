package ch.metzenthin.svm.service.export.word;

/**
 * @author Hans Stamm
 */
@SuppressWarnings("java:S6218")
public record CellLayout(boolean bold, int merged, int[] maxLengths) {}
