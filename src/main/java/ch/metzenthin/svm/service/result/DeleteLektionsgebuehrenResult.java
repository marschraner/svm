package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
public enum DeleteLektionsgebuehrenResult implements SaveDialogResult {
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
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  DeleteLektionsgebuehrenResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
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
