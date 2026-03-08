package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteSemesterResult implements SaveDialogResult {
  SEMESTER_VON_KURS_REFERENZIERT(
      "Das Semester wird durch mindestens einen Kurs "
          + "referenziert und kann nicht gelöscht werden.",
      true,
      false),
  SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Das Semester kann nicht gelöscht werden, da der Eintrag unterdessen durch\n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  DeleteSemesterResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
