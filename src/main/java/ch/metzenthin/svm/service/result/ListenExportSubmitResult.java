package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum ListenExportSubmitResult implements SubmitDialogResult {
  KONFIGURATION_ERFOLGREICH("Listenkonfiguration erfolgreich", true, true),
  FILE_AUSWAHL_ABGEBROCHEN("Auswahl des Exportfiles abgebrochen", true, false);

  private final String message;
  private final boolean submitSuccessful;
  private final boolean dialogToBeClosed;

  ListenExportSubmitResult(String message, boolean submitSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.submitSuccessful = submitSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return submitSuccessful;
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
