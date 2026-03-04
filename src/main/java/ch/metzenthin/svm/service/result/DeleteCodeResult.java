package ch.metzenthin.svm.service.result;

public enum DeleteCodeResult implements SaveDialogResult {
  CODE_REFERENZIERT(
      "Der Code wird durch mindestens ein Objekt referenziert und kann nicht gelöscht werden.",
      true,
      false),
  CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  DeleteCodeResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
