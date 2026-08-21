package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum ListenExportSubmitResult implements SaveDialogResult {
  KONFIGURATION_ERFOLGREICH("Listenkonfiguration erfolgreich", true, true),
  FILE_AUSWAHL_ABGEBROCHEN("Auswahl des Exportfiles abgebrochen", true, false);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  ListenExportSubmitResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
