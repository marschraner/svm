package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteMaerchenResult implements SubmitDialogResult {
  MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT(
      "Das Maerchen wird durch mindestens eine Märcheneinteilung referenziert \n "
          + "und kann nicht gelöscht werden.",
      false,
      false),
  MAERCHEN_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Maerchen konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
          + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteMaerchenResult(String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
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
