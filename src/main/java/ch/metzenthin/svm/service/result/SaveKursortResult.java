package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum SaveKursortResult implements SaveDialogResult {
  KURSORT_BEREITS_ERFASST("Bezeichnung bereits in Verwendung.", true, false),
  KURSORT_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", false, true);

  private final boolean isErrorMessage;
  private final String message;
  private final boolean isCloseDialog;

  SaveKursortResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
    this.message = message;
    this.isErrorMessage = isErrorMessage;
    this.isCloseDialog = isCloseDialog;
  }

  @Override
  public boolean isErrorMessage() {
    return isErrorMessage;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isCloseDialog() {
    return isCloseDialog;
  }
}
