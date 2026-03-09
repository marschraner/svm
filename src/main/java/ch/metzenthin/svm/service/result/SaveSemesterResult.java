package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveSemesterResult implements SaveDialogResult {
  SEMESTER_BEREITS_ERFASST("Semester bereits erfasst.", true, false),
  SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER("Semester dürfen sich nicht überlappen.", true, false),
  SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      true,
      true),
  SPEICHERN_ERFOLGREICH("Semester wurde erfolgreich gespeichert.", false, true);

  private final String message;
  private final boolean isErrorMessage;
  private final boolean isCloseDialog;

  SaveSemesterResult(String message, boolean isErrorMessage, boolean isCloseDialog) {
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
