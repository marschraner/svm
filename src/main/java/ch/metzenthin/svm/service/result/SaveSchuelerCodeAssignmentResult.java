package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveSchuelerCodeAssignmentResult implements SubmitDialogResult {
  SCHUELER_GELOESCHT(
      "Der Code konnte nicht zugewiesen werden, da der Schüler \n unterdessen gelöscht wurde.",
      false,
      true),
  CODE_GELOESCHT(
      "Der Code konnte nicht zugewiesen werden, da der Code \n unterdessen gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", true, true),
  ;

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveSchuelerCodeAssignmentResult(
      String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
