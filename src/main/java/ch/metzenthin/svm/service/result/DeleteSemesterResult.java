package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteSemesterResult implements SubmitDialogResult {
  SEMESTER_VON_KURS_REFERENZIERT(
      "Das Semester wird durch mindestens einen Kurs "
          + "referenziert und kann nicht gelöscht werden.",
      false,
      false),
  SEMESTER_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Das Semester kann nicht gelöscht werden, da der Eintrag unterdessen durch\n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteSemesterResult(String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
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
