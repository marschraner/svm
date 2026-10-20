package ch.metzenthin.svm.service.impl;

import ch.metzenthin.svm.common.utils.EmailValidator;
import ch.metzenthin.svm.config.SvmProperties2;
import ch.metzenthin.svm.domain.model.EmailAndNachnameAndVornameIfc;
import ch.metzenthin.svm.service.EmailService;
import ch.metzenthin.svm.service.result.CallEmailClientResult;
import ch.metzenthin.svm.service.result.CallEmailClientResult.CallResult;
import java.awt.Desktop;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Martin Schraner
 */
@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  private final SvmProperties2 svmProperties2;

  private final EmailValidator emailValidator = new EmailValidator();

  public EmailServiceImpl(SvmProperties2 svmProperties2) {
    this.svmProperties2 = svmProperties2;
  }

  @Override
  public CallEmailClientResult callDefaultEmailClient(
      Iterator<? extends EmailAndNachnameAndVornameIfc> emailAndNachnameAndVornameIterator,
      boolean blindkopien) {

    Set<String> gueltigeEmailAdressen = new LinkedHashSet<>();
    Set<String> fehlendeEmailAdressen = new LinkedHashSet<>();
    Set<String> ungueltigeEmailAdressen = new LinkedHashSet<>();
    while (emailAndNachnameAndVornameIterator.hasNext()) {
      EmailAndNachnameAndVornameIfc next = emailAndNachnameAndVornameIterator.next();
      String emailOfMitarbeiter = next.getEmail();
      if (emailOfMitarbeiter == null || emailOfMitarbeiter.isEmpty()) {
        fehlendeEmailAdressen.add(next.getNachname() + " " + next.getVorname());
      } else {
        // E-Mail des Mitarbeiters enthält möglicherweise mehrere, durch Komma getrennte E-Mails
        String[] emailOfMitarbeiterArray = emailOfMitarbeiter.split("[,;][ \\t]*");
        for (String emailOfMitarbeiterElement : emailOfMitarbeiterArray) {
          String emailOfMitarbeiterElementTrimmed = emailOfMitarbeiterElement.trim();
          if (emailValidator.isValid(emailOfMitarbeiterElementTrimmed)) {
            gueltigeEmailAdressen.add(emailOfMitarbeiterElementTrimmed);
          } else {
            ungueltigeEmailAdressen.add(emailOfMitarbeiterElementTrimmed);
          }
        }
      }
    }

    CallResult callResult;
    if (gueltigeEmailAdressen.isEmpty()) {
      callResult = CallResult.KEINE_GUELTIGEN_EMAIL_ADRESSEN;
    } else {
      callResult = callDefaultEmailClient(gueltigeEmailAdressen, blindkopien);
    }

    return new CallEmailClientResult(callResult, fehlendeEmailAdressen, ungueltigeEmailAdressen);
  }

  @SuppressWarnings("java:S125")
  private CallResult callDefaultEmailClient(
      Set<String> gueltigeEmailAdressen, boolean blindkopien) {
    String emailClientMultipleMailsSeparator =
        svmProperties2.getEmailClientMultipleMailsSeparator();
    String emailAdressenAsSingleString =
        String.join(emailClientMultipleMailsSeparator, gueltigeEmailAdressen);

    // Source: http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
    Desktop desktop = Desktop.getDesktop();
    try {
      // Open user-default mail client with the email message
      // fields information.
      //
      // mailto:dummy@domain.com?cc=test@domain.com&
      // subject=First%20Email&&body=Hello%20World
      // emailAdressenAsSingleString = emailAdressenAsSingleString.replaceAll(",\\p{Blank}*", ";");
      //  // ; als Trenner zwischen mehreren Emails verwenden
      String mailtoCommand = (blindkopien ? "mailto:?bcc=" : "mailto:");
      String message = mailtoCommand + emailAdressenAsSingleString;
      URI uri = URI.create(message);
      desktop.mail(uri);
    } catch (Exception e) {
      LOGGER.trace("CallDefaultEmailClientCommand IO-Exception: {}", e.getMessage());
      return CallResult.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT;
    }

    return CallResult.EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN;
  }
}
