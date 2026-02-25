package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteKurstypResult implements SaveDialogResult {
  KURSTYP_VON_KURS_REFERENZIERT(
      "Der Kurstyp wird durch mindestens einen Kurs referenziert und "
          + "kann nicht gelöscht werden.",
      true,
      false),
  KURSTYP_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Kurstyp konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
          + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  DeleteKurstypResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
