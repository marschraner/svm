package ch.metzenthin.svm.common.utils;

import java.util.Properties;

/**
 * @author Martin Schraner
 */
public class PersistenceProperties {

  private PersistenceProperties() {}

  public static Properties getPersistenceProperties() {
    Properties svmProperties = SvmProperties.getSvmProperties();
    String dbUrlHostname = svmProperties.getProperty(SvmProperties.KEY_DB_URL_HOSTNAME);
    String dbUrlPort = svmProperties.getProperty(SvmProperties.KEY_DB_URL_PORT);
    String dbUrlOptions = svmProperties.getProperty(SvmProperties.KEY_DB_URL_OPTIONS);
    String dbSvmPassword = svmProperties.getProperty(SvmProperties.KEY_DB_SVM_PASSWORD);
    if (dbUrlHostname == null || dbUrlHostname.isBlank()) {
      dbUrlHostname = "localhost";
    }
    if (dbUrlPort == null || dbUrlPort.isBlank()) {
      dbUrlPort = "3306";
    }
    if (dbUrlOptions == null || dbUrlOptions.isBlank()) {
      dbUrlOptions = "";
    } else {
      dbUrlOptions = "?" + dbUrlOptions.trim();
    }
    if (dbSvmPassword == null || dbSvmPassword.isBlank()) {
      dbSvmPassword = "svm";
    }
    Properties persistenceProperties = new Properties();
    persistenceProperties.put(
        "hibernate.connection.url",
        "jdbc:mariadb://" + dbUrlHostname.trim() + ":" + dbUrlPort.trim() + "/svm" + dbUrlOptions);
    persistenceProperties.put("hibernate.connection.password", dbSvmPassword);
    return persistenceProperties;
  }
}
