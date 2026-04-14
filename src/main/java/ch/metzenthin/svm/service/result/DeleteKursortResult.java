package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum DeleteKursortResult implements SaveDialogResult {
  KURSORT_VON_KURS_REFERENZIERT(
      "Der Kursort wird durch mindestens einen Kurs referenziert und "
          + "kann nicht gelöscht werden.",
      false,
      false),
  KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Kursort konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
          + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  DeleteKursortResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.saveSuccessful = saveSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSaveSuccessful() {
    return saveSuccessful;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
