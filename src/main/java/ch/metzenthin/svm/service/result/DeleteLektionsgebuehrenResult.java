package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum DeleteLektionsgebuehrenResult implements SaveDialogResult {
  LEKTIONSGEBUEHREN_VON_KURS_REFERENZIERT(
      "Die Lektionsgebühren können nicht gelöscht werden,\n"
          + "weil Kurse mit dieser Lektionslänge existieren.",
      true,
      false),
  LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Die Lektionsgebühren können nicht gelöscht werden, da der Eintrag "
          + "unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  DeleteLektionsgebuehrenResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
