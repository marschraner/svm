package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum ImportKurseResult implements SubmitDialogResult {
  IMPORT_ABGEBROCHEN_KEIN_VORHERGEHENDES_SEMESTER(
      "Import abgebrochen. Es wurde kein vorhergehendes Semester gefunden.", true, true),
  IMPORT_ERFOLGREICH("Der Import der Kurse wurde erfolgreich durchgeführt.", true, true);

  private final String message;
  private final boolean exportSuccessful;
  private final boolean dialogToBeClosed;

  ImportKurseResult(String message, boolean exportSuccessful, boolean dialogToBeClosed) {
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
