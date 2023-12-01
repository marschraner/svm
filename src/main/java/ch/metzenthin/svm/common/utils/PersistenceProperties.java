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
        String dbUrlOptions = svmProperties.getProperty(SvmProperties.KEY_DB_URL_OPTIONS);
        if (dbUrlHostname == null || dbUrlHostname.trim().isEmpty()) {
            dbUrlHostname = "localhost";
        }
        if (dbUrlPort == null || dbUrlPort.trim().isEmpty()) {
            dbUrlPort = "3306";
        }
        if (dbUrlOptions == null || dbUrlOptions.trim().isEmpty()) {
            dbUrlOptions = "";
        } else {
            dbUrlOptions = "?" + dbUrlOptions.trim();
        }
        Properties persistenceProperties = new Properties();
        persistenceProperties.put("hibernate.connection.url", "jdbc:mysql://" + dbUrlHostname.trim() + ":" + dbUrlPort.trim() + "/svm" + dbUrlOptions);
        return persistenceProperties;
    }


}
