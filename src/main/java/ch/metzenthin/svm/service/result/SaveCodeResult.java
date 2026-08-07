package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveCodeResult implements SubmitDialogResult {
  CODE_BEREITS_ERFASST("Kürzel bereits in Verwendung.", false, false),
  CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", true, true),
  ;

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveCodeResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
