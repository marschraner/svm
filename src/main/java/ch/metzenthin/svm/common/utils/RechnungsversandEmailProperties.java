package ch.metzenthin.svm.common.utils;

import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class RechnungsversandEmailProperties {

    public static Properties getRechnungsversandEmailProperties() {
        Properties svmProperties = SvmProperties.getSvmProperties();
        String smtpServer = svmProperties.getProperty(SvmProperties.KEY_RECHNUNGSVERSAND_SMTP_SERVER);
        String smtpPort = svmProperties.getProperty(SvmProperties.KEY_RECHNUNGSVERSAND_SMTP_PORT);

        Properties emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", smtpServer);
        emailProperties.put("mail.smtp.port", smtpPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        return emailProperties;
    }
}
