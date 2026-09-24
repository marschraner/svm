package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteMitarbeiterResult implements SubmitDialogResult {
  MITARBEITER_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Mitarbeiter kann nicht gelöscht werden, da der Eintrag unterdessen durch\n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteMitarbeiterResult(String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
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
