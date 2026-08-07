package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum DeleteKursResult implements SubmitDialogResult {
  KURS_VON_KURSANMELDUNGEN_REFERENZIERT(
      "Der Kurs wird durch mindestens eine Kursanmeldung referenziert. Beim Löschen des Kurses \n"
          + "werden die Kursanmeldungen ebenfalls unwiderruflich gelöscht. Fortfahren?",
      false,
      false),
  KURS_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Kurs konnte nicht gelöscht werden, da der Eintrag unterdessen \n"
          + "durch einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteKursResult(String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
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
