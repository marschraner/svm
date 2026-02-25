package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public interface SaveDialogResult {

  String getMessage();

  boolean isErrorMessage();

  boolean isCloseDialog();
}
