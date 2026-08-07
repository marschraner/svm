package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum DeleteLektionsgebuehrenResult implements SubmitDialogResult {
  LEKTIONSGEBUEHREN_VON_KURS_REFERENZIERT(
      "Die Lektionsgebühren können nicht gelöscht werden,\n"
          + "weil Kurse mit dieser Lektionslänge existieren.",
      false,
      false),
  LEKTIONSGEBUEHREN_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Die Lektionsgebühren können nicht gelöscht werden, da der Eintrag "
          + "unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  LOESCHEN_ERFOLGREICH("Löschen erfolgreich", true, true);

  private final String message;
  private final boolean deleteSuccessful;
  private final boolean dialogToBeClosed;

  DeleteLektionsgebuehrenResult(
      String message, boolean deleteSuccessful, boolean dialogToBeClosed) {
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
