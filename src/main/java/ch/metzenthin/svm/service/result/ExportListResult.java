package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum ExportListResult implements SaveDialogResult {
  LISTE_ERFOLGREICH_ERSTELLT("Liste erfolgreich erstellt", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  ExportListResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.saveSuccessful = saveSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public boolean isSaveSuccessful() {
    return saveSuccessful;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
