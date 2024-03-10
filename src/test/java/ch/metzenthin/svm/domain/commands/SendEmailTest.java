package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.common.utils.RechnungsversandEmailProperties;
import ch.metzenthin.svm.common.utils.SvmProperties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class SendEmailTest {

    @Test
    public void testSendEmail() {

        Properties svmProperties = SvmProperties.getSvmProperties();

        String rechnungsversandEmail = svmProperties.getProperty(SvmProperties.KEY_RECHNUNGSVERSAND_EMAIL);
        String rechnungsversandEmailOderAppPasswort
                = svmProperties.getProperty(SvmProperties.KEY_RECHNUNGSVERSAND_EMAIL_ODER_APP_PASSWORT);
        String rechnungsversandEmailTestEmpfaenger
                = svmProperties.getProperty(SvmProperties.KEY_RECHNUNGSVERSAND_TEST_EMPFAENGER);

        Properties emailProperties = RechnungsversandEmailProperties.getRechnungsversandEmailProperties();

        String subject = "Test-Mail SVM";
        List<String> messages = List.of("Dies ist der erste Abschnitt.", "Und das der zweite mit den Umlauten äöü.");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(rechnungsversandEmail, rechnungsversandEmailOderAppPasswort);
            }
        };

        Session session = Session.getInstance(emailProperties, authenticator);
        session.setDebug(true);

        try {

            // Create a message with headers
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(rechnungsversandEmail));
            InternetAddress[] address = {new InternetAddress(rechnungsversandEmailTestEmpfaenger)};
            mimeMessage.setRecipients(Message.RecipientType.TO, address);
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());

            // Create message body
            Multipart mimeMultipart = new MimeMultipart();
            for (String message : messages) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(message, StandardCharsets.UTF_8.name());
                mimeMultipart.addBodyPart(mimeBodyPart);
            }
            mimeMessage.setContent(mimeMultipart);

            // Send the message
            Transport.send(mimeMessage);

        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
            Exception ex;
            if ((ex = messagingException.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }

}
