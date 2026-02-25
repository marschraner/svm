package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum DeleteKursortResult implements SaveDialogResult {
  KURSORT_VON_KURS_REFERENZIERT(
      "Der Kursort wird durch mindestens einen Kurs referenziert und "
          + "kann nicht gelöscht werden.",
      true,
      false),
  KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Kursort konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
          + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  DeleteKursortResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
