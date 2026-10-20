package ch.metzenthin.svm.service.result;

import java.util.Set;
import lombok.Getter;

/**
 * @author Hans Stamm
 */
public record CallEmailClientResult(
    CallResult callResult, Set<String> fehlendeEmailAdressen, Set<String> ungueltigeEmailAdressen) {

  @Getter
  public enum CallResult {
    EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN("E-Mail-Client erfolgreich aufgerufen.", true),
    KEINE_GUELTIGEN_EMAIL_ADRESSEN("Es sind keine gültigen E-Mail-Adresse vorhanden.", false),
    FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT(
        "Beim Aufruf des Email-Client ist ein Fehler aufgetreten.", false);

    private final String message;
    private final boolean successful;

    CallResult(String message, boolean successful) {
      this.message = message;
      this.successful = successful;
    }
  }

  public String getMessage() {
    return callResult.getMessage();
  }

  public boolean isSuccessful() {
    return callResult.isSuccessful();
  }
}
