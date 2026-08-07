package ch.metzenthin.svm.service.result;

public enum DeleteCodeResult implements SubmitDialogResult {
  CODE_REFERENZIERT(
      "Der Code wird durch mindestens ein Objekt referenziert und kann nicht gelöscht werden.",
      false,
      false),
  CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteCodeResult(String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.deleteSuccessful = deleteSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return deleteSuccessful;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
