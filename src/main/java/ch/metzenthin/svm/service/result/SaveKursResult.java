package ch.metzenthin.svm.service.result;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("java:S1192")
public enum SaveKursResult implements SubmitDialogResult {
  KEINE_LEHRKRAEFTE_ERFASST(
      "Der Kurs kann nicht gespeichert werden, weil für \n "
          + "den Kurs keine Lehkräfte erfasst wurden.",
      false,
      false),
  KURS_BEREITS_ERFASST(
      "Für das aktuelle Semester existiert bereits ein Kurs mit demselben Wochentag, \n"
          + "demselben Kursbeginn und derselben Lehrkraft.",
      false,
      false),
  LEKTIONSGEBUEHREN_NICHT_ERFASST(
      "Der Kurs kann nicht gespeichert werden, weil für die \n"
          + "Kurslänge noch keine Lektionsgebühren erfasst sind.",
      false,
      false),
  SEMESTER_DURCH_ANDEREN_BENUTZER_GELOESCHT(
      "Der Kurs konnte nicht gespeichert werden, da das Semester \n "
          + "unterdessen durch einen anderen Benutzer gelöscht wurde.",
      false,
      true),
  KURSTYP_DURCH_ANDEREN_BENUTZER_GELOESCHT(
      "Der Kurs konnte nicht gespeichert werden, da der selektierte Kurstyp \n"
          + "unterdessen durch einen anderen Benutzer gelöscht wurde.",
      false,
      true),
  KURSORT_DURCH_ANDEREN_BENUTZER_GELOESCHT(
      "Der Kurs konnte nicht gespeichert werden, da der selektierte Kursort \n"
          + "unterdessen durch einen anderen Benutzer gelöscht wurde.",
      false,
      true),
  LEHRKRAFT_DURCH_ANDEREN_BENUTZER_GELOESCHT(
      "Der Kurs konnte nicht gespeichert werden, da die selektierte Lehrkraft \n"
          + "unterdessen durch einen anderen Benutzer gelöscht wurde.",
      false,
      true),
  KURS_DURCH_ANDEREN_BENUTZER_VERAENDERT(
      "Der Wert konnte nicht gespeichert werden, da der Eintrag unterdessen durch \n"
          + "einen anderen Benutzer verändert oder gelöscht wurde.",
      false,
      true),
  SPEICHERN_ERFOLGREICH("Speichern erfolgreich", true, true);

  private final String message;
  private final boolean saveSuccessful;
  private final boolean dialogToBeClosed;

  SaveKursResult(String message, boolean saveSuccessful, boolean dialogToBeClosed) {
    this.message = message;
    this.saveSuccessful = saveSuccessful;
    this.dialogToBeClosed = dialogToBeClosed;
  }

  @Override
  public boolean isSubmitSuccessful() {
    return saveSuccessful;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public boolean isDialogToBeClosed() {
    return dialogToBeClosed;
  }
}
