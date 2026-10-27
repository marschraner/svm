package ch.metzenthin.svm.service.result;

import java.util.Set;

/**
 * @author Hans Stamm
 */
public record CheckEmailResult(
    Set<String> gueltigeEmailAdressen,
    Set<String> fehlendeEmailAdressen,
    Set<String> ungueltigeEmailAdressen) {

  public boolean isSuccessful() {
    return !gueltigeEmailAdressen.isEmpty()
        && fehlendeEmailAdressen.isEmpty()
        && ungueltigeEmailAdressen.isEmpty();
  }
}
