package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public interface SubmitDialogResult {

  String getMessage();

  boolean isSubmitSuccessful();

  boolean isDialogToBeClosed();
}
