package ch.metzenthin.svm.service.result;

/**
 * @author Hans Stamm
 */
public enum SaveDispensationResult implements SubmitDialogResult {
  DISPENSATION_UEBERLAPPT_MIT_ANDERER_DISPENSATION(
      "Dispensationen dürfen sich nicht überlappen.", false, false),
  DISPENSATION_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  SCHUELER_DURCH_ANDEREN_BENUTZER_GELOESCHT(
      "Die Dispensation konnte nicht gespeichert werden, da der Schüler \n "
          + "unterdessen durch einen anderen Benutzer gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Dispensation wurde erfolgreich gespeichert.", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveDispensationResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.saveSuccessful = saveSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return saveSuccessful;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
