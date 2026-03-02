package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveCodeResult implements SaveDialogResult {
  CODE_BEREITS_ERFASST("Kürzel bereits in Verwendung.", true, false),
  CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", false, true),
  ;

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  SaveCodeResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
    this.message = message;
    this.isErrorMessage = isErrorMessage;
    this.isCloseDialog = isCloseDialog;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isErrorMessage() {
    return isErrorMessage;
  }

  @Override
  public boolean isCloseDialog() {
    return isCloseDialog;
  }
}
