package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum ExportListResult implements SubmitDialogResult {
  LISTE_ERFOLGREICH_ERSTELLT("Liste erfolgreich erstellt", true, true);

  private final String message;
  private final boolean exportSuccessful;
  private final boolean dialogToBeClosed;

  ExportListResult(String message, boolean exportSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.exportSuccessful = exportSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return exportSuccessful;
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
