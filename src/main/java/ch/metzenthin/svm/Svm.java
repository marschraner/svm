package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.ModelFactoryImpl;
import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.ui.components.SvmDesktop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;

/*
 * SVM Applikation
 */
public class Svm {

    private static final Logger LOGGER = LogManager.getLogger(Svm.class);

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI(SvmContext svmContext) {

        // Optionaler Preferred Look and Feel-Wert aus SVM Properties abfragen
        Properties svmProperties = SvmProperties.getSvmProperties();
        String svmPropertyPreferredLookAndFeel = svmProperties.getProperty(SvmProperties.KEY_PREFERRED_LOOK_AND_FEEL);

        // Nach absteigender Präferenz geordnet
        String[] preferredLookAndFeels = new String[]{svmPropertyPreferredLookAndFeel, "Mac OS X", "GTK+", "Windows", "Nimbus"};
        String selectedLookAndFeel = "";

        preferredLookAndFeelsLoop:
        for (String preferredLookAndFeel : preferredLookAndFeels) {
            if (preferredLookAndFeel == null || preferredLookAndFeel.isEmpty()) {
                continue;
            }
            for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                if (preferredLookAndFeel.equalsIgnoreCase(lookAndFeelInfo.getName())) {
                    try {
                        UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                    } catch (Exception ignore) {
                    }
                    selectedLookAndFeel = lookAndFeelInfo.getName();
                    break preferredLookAndFeelsLoop;
                }
            }
        }

        if (svmPropertyPreferredLookAndFeel != null && !svmPropertyPreferredLookAndFeel.isEmpty()
                && !svmPropertyPreferredLookAndFeel.equalsIgnoreCase(selectedLookAndFeel)) {
            LOGGER.warn("'" + svmPropertyPreferredLookAndFeel + "' ist kein gültiger Wert für SVM-Property '" +
                    SvmProperties.KEY_PREFERRED_LOOK_AND_FEEL + "'. Verwende stattdessen Default-Look and Feel '" +
                    selectedLookAndFeel + "'.");
        }

        // Notwendige Workarounds für einige Look-And-Feels
        if ("GTK+".equals(selectedLookAndFeel)) {
            GtkPlusLookAndFeelWorkaround.installGtkPopupBugWorkaround();
        }

        // Create and set up the window.
        SvmDesktop svmDesktop = new SvmDesktop(svmContext);
       ((SwingExceptionHandler) Thread.getDefaultUncaughtExceptionHandler()).setSvmDesktop(svmDesktop);
        LOGGER.info("Svm wurde gestartet.");
    }

    public static void main(String[] args) {
        try {
            LOGGER.info("Svm wird gestartet ...");
            splashScreenInit();
            createSvmPropertiesFileDefault();
            final ModelFactory modelFactory = createModelFactory();
            final SvmModel svmModel = modelFactory.createSvmModel();
            final SvmContext svmContext = new SvmContext(modelFactory, svmModel);
            // Fängt alle unbehandelten Exceptions und beendet die Applikation.
            Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
            // Schedule a job for the event-dispatching thread:
            // creating and showing this application's GUI.
            SwingUtilities.invokeLater(() -> createAndShowGUI(svmContext));
            // splashScreenClose(splash); -> No need to close it here. This way the splash screen disappears when GUI appears.
        } catch (Exception e) {
            LOGGER.error("Fehler bei der Initialisierung der Applikation", e);
            JOptionPane.showMessageDialog(null,
                    "Bei der Initialisierung der Applikation ist ein Fehler aufgetreten.\n" +
                            "Bitte Netzwerkverbindung, Datenbank-Server, Log-Files (im Ordner \"log\") und .svm-File (im home) überprüfen.",
                    "Fehler bei der Initialisierung der Applikation",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static ModelFactoryImpl createModelFactory() {
        return new ModelFactoryImpl();
    }

    private static void splashScreenInit() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            LOGGER.warn("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            LOGGER.warn("SplashScreen: g is null");
        }
    }
}