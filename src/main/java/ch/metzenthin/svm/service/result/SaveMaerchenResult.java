package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum SaveMaerchenResult implements SaveDialogResult {
  MAERCHEN_BEREITS_ERFASST("Märchen für selektiertes Schuljahr bereits erfasst.", false, false),
  SPEICHERN_ABBRECHEN_NACH_WARNUNG(
      "Speichern des Märchens wurde nach Warnung abgebrochen.", true, false),
  MAERCHEN_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveMaerchenResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
