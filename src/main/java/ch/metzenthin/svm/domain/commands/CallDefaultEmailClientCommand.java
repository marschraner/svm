package ch.metzenthin.svm.domain.commands;

import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * @author Martin Schraner
 */
public class CallDefaultEmailClientCommand implements Command {

    private static final Logger LOGGER = Logger.getLogger(CallDefaultEmailClientCommand.class);

    public enum Result {
        FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT,
        EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN
    }

    // input
    private String emailAdresse;

    // output
    private Result result;

    public CallDefaultEmailClientCommand(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    @Override
    public void execute() {
        // Source: http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
        Desktop desktop = Desktop.getDesktop();
        try {
            // Open user-default mail client with the email message
            // fields information.
            //
            // mailto:dummy@domain.com?cc=test@domain.com&
            // subject=First%20Email&&body=Hello%20World
            emailAdresse = emailAdresse.replaceAll(",\\p{Blank}*", ";");   // ; als Trenner zwischen mehreren Emails verwenden
            String message = "mailto:" + emailAdresse;
            URI uri = URI.create(message);
            desktop.mail(uri);
            result = Result.EMAIL_CLIENT_ERFOLGREICH_AUFGERUFEN;
        } catch (IOException e) {
            LOGGER.trace("CallDefaultEmailClientCommand IO-Exception: " + e.getMessage());
            result = Result.FEHLER_BEIM_AUFRUF_DES_EMAIL_CLIENT;
        }
    }

    public Result getResult() {
        return result;
    }
}
