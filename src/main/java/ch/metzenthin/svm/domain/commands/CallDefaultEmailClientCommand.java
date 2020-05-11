package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.EmailValidator;
import ch.metzenthin.svm.common.utils.SvmProperties;
import org.apache.log4j.Logger;

import java.awt.*;
import java.net.URI;
import java.util.*;

/**
 * @author Martin Schraner
 */
public class CallDefaultEmailClientCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(CallDefaultEmailClientCommand.class);

    private final EmailValidator emailValidator = new EmailValidator();

    public enum Result {
        FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT,
        EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN
    }

    // input
    private final Collection<String> emailAdressen;
    private final boolean blindkopien;

    // output
    private Set<String> ungueltigeEmailAdressen;
    private Result result;

    public CallDefaultEmailClientCommand(Collection<String> emailAdressen, boolean blindkopien) {
        this.emailAdressen = emailAdressen;
        this.blindkopien = blindkopien;
    }

    public CallDefaultEmailClientCommand(String emailAdresse, boolean blindkopien) {
        this.emailAdressen = new ArrayList<>();
        this.emailAdressen.add(emailAdresse);
        this.blindkopien = blindkopien;
    }

    @Override
    public void execute() {

        Set<String> gueltigeEmailAdressen = new LinkedHashSet<>();
        ungueltigeEmailAdressen = new LinkedHashSet<>();

        // Validierung
        for (String emailAdresse : emailAdressen) {
            // emailAdresse enthält möglicherweise mehrere, durch Komma getrennte Emails
            String[] emailAdressenSplitted = emailAdresse.split("[,;]\\p{Blank}*");
            for (String emailAdresseSplitted : emailAdressenSplitted) {
                if (emailValidator.isValid(emailAdresseSplitted.trim())) {
                    gueltigeEmailAdressen.add(emailAdresseSplitted.trim());
                } else {
                    ungueltigeEmailAdressen.add(emailAdresseSplitted.trim());
                }
            }
        }

        if (gueltigeEmailAdressen.isEmpty()) {
            return;
        }

        // Separator für mehrere E-Mails
        Properties svmProperties = SvmProperties.getSvmProperties();
        String emailClientMultipleMailsSeparator = svmProperties.getProperty(SvmProperties.KEY_EMAIL_CLIENT_MULTIPLE_MAILS_SEPARATOR);
        if (emailClientMultipleMailsSeparator.isEmpty()) {
            emailClientMultipleMailsSeparator = ";";
        }

        // Zusammensetzen zu einzigem String mit durch ";" (oder anderem Separator gemäss .svm-Konfiguration) getrennten Emails
        StringBuilder emailAdressenAsSingleStringSb = new StringBuilder();
        for (String emailAdresse : gueltigeEmailAdressen) {
            emailAdressenAsSingleStringSb.append(emailAdresse);
            emailAdressenAsSingleStringSb.append(emailClientMultipleMailsSeparator);
        }
        // Letzten ; löschen
        emailAdressenAsSingleStringSb.setLength(emailAdressenAsSingleStringSb.length() - 1);

        // Source: http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
        String emailAdressenAsSingleString = emailAdressenAsSingleStringSb.toString();
        Desktop desktop = Desktop.getDesktop();
        try {
            // Open user-default mail client with the email message
            // fields information.
            //
            // mailto:dummy@domain.com?cc=test@domain.com&
            // subject=First%20Email&&body=Hello%20World
            //emailAdressenAsSingleString = emailAdressenAsSingleString.replaceAll(",\\p{Blank}*", ";");   // ; als Trenner zwischen mehreren Emails verwenden
            String mailtoCommand = (blindkopien ? "mailto:?bcc=" : "mailto:");
            String message = mailtoCommand + emailAdressenAsSingleString;
            URI uri = URI.create(message);
            desktop.mail(uri);
            result = Result.EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN;
        } catch (Exception e) {
            LOGGER.trace("CallDefaultEmailClientCommand IO-Exception: " + e.getMessage());
            result = Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT;
        }
    }

    public Result getResult() {
        return result;
    }

    public Set<String> getUngueltigeEmailAdressen() {
        return ungueltigeEmailAdressen;
    }
}
