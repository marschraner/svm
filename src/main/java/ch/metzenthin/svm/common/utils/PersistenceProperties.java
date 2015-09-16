package ch.metzenthin.svm.common.utils;

import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class PersistenceProperties {

    public static Properties getPersistenceProperties() {
        Properties svmProperties = SvmProperties.getSvmProperties();
        String dbUrlHostname = svmProperties.getProperty(SvmProperties.KEY_DB_URL_HOSTNAME);
        String dbUrlPort = svmProperties.getProperty(SvmProperties.KEY_DB_URL_PORT);
        if (dbUrlHostname == null || dbUrlPort == null) {
            throw new RuntimeException();
        }
        Properties persistenceProperties = new Properties();
        persistenceProperties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + dbUrlHostname.trim() + ":" + dbUrlPort.trim() + "/svm");
        return persistenceProperties;
    }


}
