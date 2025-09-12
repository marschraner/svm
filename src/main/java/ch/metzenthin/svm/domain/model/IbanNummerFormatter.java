package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.utils.IbanNummerValidator;

/**
 * @author Martin Schraner
 */
public class IbanNummerFormatter implements Formatter<String> {

  private final IbanNummerValidator ibanNummerValidator = new IbanNummerValidator();

  @Override
  public String format(String ibanNummer) {

    if (ibanNummer == null) {
      return null;
    }

    if (!ibanNummerValidator.isValid(ibanNummer)) {
      return ibanNummer;
    }

    // Nach jeder 4. Stelle einen Leerschlag einfügen (Konvention für bessere Lesbarkeit)
    ibanNummer = ibanNummer.replace(" ", "");
    StringBuilder ibanNummerStringBuilder = new StringBuilder();
    for (int i = 0; i < ibanNummer.length(); i++) {
      ibanNummerStringBuilder.append(ibanNummer.charAt(i));
      if ((i + 1) % 4 == 0) {
        ibanNummerStringBuilder.append(" ");
      }
    }
    return ibanNummerStringBuilder.toString();
  }
}
