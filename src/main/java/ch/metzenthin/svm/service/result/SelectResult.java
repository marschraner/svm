package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SelectResult implements SubmitDialogResult {
  ERFOLGREICH("Auswahl erfolgreich", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SelectResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.saveSuccessful = saveSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return saveSuccessful;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
