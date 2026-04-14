package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveSemesterResult implements SaveDialogResult {
  SEMESTER_BEREITS_ERFASST("Semester bereits erfasst.", false, false),
  SEMESTER_UEBERLAPPT_MIT_ANDEREM_SEMESTER("Semester dürfen sich nicht überlappen.", false, false),
  SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Semester wurde erfolgreich gespeichert.", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveSemesterResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
