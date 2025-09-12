package ch.metzenthin.svm.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Martin Schraner
 */
public class IbanNummerValidator {

  // Quelle: https://www.moneytoday.ch/lexikon/iban/

  private static final Pattern VALID_IBAN_REGEX = Pattern.compile("^[A-Z]{2}[A-Za-z0-9]+$");

  public boolean isValid(final String ibanNummer) {

    if (ibanNummer == null || ibanNummer.isEmpty()) {
      return true;
    }

    String ibanNummerWithoutSpaces = ibanNummer.replaceAll("\\s", "");

    if (ibanNummer.startsWith("CH")) {
      // In der Schweiz muss eine IBAN-Nummer genau 21 Stellen haben
      if (ibanNummerWithoutSpaces.length() != 21) {
        return false;
      }
    } else {
      if (ibanNummerWithoutSpaces.length() < 15 || ibanNummerWithoutSpaces.length() > 34) {
        return false;
      }
    }

    Matcher matcher = VALID_IBAN_REGEX.matcher(ibanNummerWithoutSpaces);
    return matcher.matches();
  }
}
