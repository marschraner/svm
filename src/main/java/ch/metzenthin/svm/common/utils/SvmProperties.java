package ch.metzenthin.svm.common.utils;

import ch.metzenthin.svm.common.SvmRuntimeException;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;


/**
 * @author Martin Schraner
 */
public class SvmProperties {

    public static final String SVM_PROPERTIES_FILE_NAME = getUserHomeSystemProperty() + File.separator + ".svm";
    static final String KEY_DB_URL_HOSTNAME = "db_url_hostname";
    static final String KEY_DB_URL_PORT = "db_url_port";
    static final String KEY_DB_URL_OPTIONS = "db_url_options";
    static final String KEY_DB_SVM_PASSWORD = "db_svm_password";
    public static final String KEY_TEMPLATES_DIRECTORY = "templates_directory";
    public static final String KEY_DEFAULT_OUTPUT_DIRECTORY = "default_output_directory";
    public static final String KEY_ABSENZENLISTEN_TEMPLATE_GANZES_SEMESTER = "absenzenlisten_template_ganzes_semester";
    public static final String KEY_ABSENZENLISTEN_TEMPLATE_OKTOBER_FEBRUAR = "absenzenlisten_template_oktober_februar";
    public static final String KEY_SPEZIELLES_ABSENZENLISTEN_TEMPLATE = "spezielles_absenzenlisten_template";
    public static final String KEY_EMAIL_CLIENT_MULTIPLE_MAILS_SEPARATOR = "email_client_multiple_mails_separator";
    public static final String KEY_NEUSTE_ZUOBERST = "neuste_zuoberst";
    public static final String KEY_PREFERRED_LOOK_AND_FEEL = "preferred_look_and_feel";

    private SvmProperties() {
    }

    private static String getUserHomeSystemProperty() {
        return System.getProperty("user.home");
    }

    public static void createSvmPropertiesFileDefault() {

        File f = new File(SVM_PROPERTIES_FILE_NAME);

        if (f.exists()) {
            return;
        }

        Properties prop = new Properties() {
            // Alphabetische statt zufällige Sortierung der Properties-Einträge
            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(new TreeSet<>(super.keySet()));
            }
        };

        try {
            OutputStream propertiesFile = new FileOutputStream(SVM_PROPERTIES_FILE_NAME);
            String userHomeSystemProperty = getUserHomeSystemProperty();
            String templateFilesPath = "<Schuljahr>" + File.separator + "Semester_<Semester>" + File.separator;

            // set the properties value
            prop.setProperty(KEY_DB_URL_HOSTNAME, "localhost");
            prop.setProperty(KEY_DB_URL_PORT, "3306");
            prop.setProperty(KEY_DB_URL_OPTIONS, "");   // optional
            prop.setProperty(KEY_DB_SVM_PASSWORD, "svm");
            prop.setProperty(KEY_TEMPLATES_DIRECTORY, userHomeSystemProperty + File.separator + "svm" + File.separator + "Listen-Templates");
            prop.setProperty(KEY_DEFAULT_OUTPUT_DIRECTORY, userHomeSystemProperty + File.separator + "Desktop");
            prop.setProperty(KEY_ABSENZENLISTEN_TEMPLATE_GANZES_SEMESTER, templateFilesPath + "Absenzenlisten-Template_<Schuljahr>_<Semester>_<Wochentag>.docx");
            prop.setProperty(KEY_ABSENZENLISTEN_TEMPLATE_OKTOBER_FEBRUAR, templateFilesPath + "Absenzenlisten-Template_Oktober-Februar_<Schuljahr>_<Semester>_<Wochentag>.docx");
            prop.setProperty(KEY_SPEZIELLES_ABSENZENLISTEN_TEMPLATE, templateFilesPath + "Spezielles_Absenzenlisten-Template_<Schuljahr>_<Semester>_<Wochentag>.docx");
            prop.setProperty(KEY_EMAIL_CLIENT_MULTIPLE_MAILS_SEPARATOR, ";");
            prop.setProperty(KEY_NEUSTE_ZUOBERST, "true");
            prop.setProperty(KEY_PREFERRED_LOOK_AND_FEEL, "");   // optional
            prop.store(propertiesFile, null);

            propertiesFile.close();
        } catch (IOException e) {
            throw new SvmRuntimeException(
                    "Fehler beim Lesen der Datei \"" + SVM_PROPERTIES_FILE_NAME + "\"", e);
        }

    }

    public static Properties getSvmProperties() {
        Properties prop = new Properties();
        InputStream propertiesFile;
        try {
            propertiesFile = new FileInputStream(SVM_PROPERTIES_FILE_NAME);
            prop.load(propertiesFile);
            propertiesFile.close();
        } catch (IOException e) {
            throw new SvmRuntimeException(
                    "Fehler beim Lesen der Datei \"" + SVM_PROPERTIES_FILE_NAME + "\"", e);
        }
        return prop;
    }
}
