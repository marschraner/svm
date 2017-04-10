package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.CommandInvokerImpl;
import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.ModelFactoryImpl;
import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.ui.components.SvmDesktop;
import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.*;
import java.util.Properties;

import static ch.metzenthin.svm.common.utils.SvmProperties.createSvmPropertiesFileDefault;

/*
 * SVM Applikation
 */
public class Svm {

    private static final Logger LOGGER = Logger.getLogger(Svm.class);

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI(SvmContext svmContext) {

        // Look and Feel
        String lookAndFeelName = null;
        String lookAndFeelClassName = null;

        // Wert aus SVM-Properties-File verwenden (falls dort gesetzt)
        Properties svmProperties = SvmProperties.getSvmProperties();
        String lookAndFeelNameSvmProperty = svmProperties.getProperty(SvmProperties.KEY_JAVA_LOOK_AND_FEEL);
        if (lookAndFeelNameSvmProperty != null && !lookAndFeelNameSvmProperty.isEmpty()) {
            for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                if (lookAndFeelNameSvmProperty.toLowerCase().equals(lookAndFeelInfo.getName().toLowerCase())) {
                    lookAndFeelName = lookAndFeelInfo.getName();
                    lookAndFeelClassName = lookAndFeelInfo.getClassName();
                    break;
                }
            }
            if (lookAndFeelName == null) {
                LOGGER.warn("'" + lookAndFeelNameSvmProperty + "' ist kein gültiger Wert für SVM-Property  '" +
                        SvmProperties.KEY_JAVA_LOOK_AND_FEEL + "'. Verwende Default-Wert (Mac, GTK+ oder Windows).");
            }
        }

        // Look and Feel in Properties-File nicht gesetzt oder ungültig -> Default-Wert verwenden
        if (lookAndFeelName == null) {
            for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                switch (lookAndFeelInfo.getName()) {
                    case "Mac":
                    case "GTK+":
                    case "Windows":
                        lookAndFeelName = lookAndFeelInfo.getName();
                        lookAndFeelClassName = lookAndFeelInfo.getClassName();
                        break;
                    default:
                        break;
                }

            }
        }

        if (lookAndFeelName == null) {
            LOGGER.error("Kein gültiges Look and Feel gefunden!");
            System.exit(1);
        }

        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception ignore) {
        }

        if ("GTK+".equals(lookAndFeelName)) {
            GtkPlusLookAndFeelWorkaround.installGtkPopupBugWorkaround();
        }
        if ("Mac".equals(lookAndFeelName)) {
            svmContext.getDialogIcons().createDialogIcons();
        }

        // Create and set up the window.
        ((SwingExceptionHandler) Thread.getDefaultUncaughtExceptionHandler()).setSvmDesktop(new SvmDesktop(svmContext));
        LOGGER.info("Svm wurde gestartet.");
    }

    public static void main(String[] args) {
        try {
            LOGGER.info("Svm wird gestartet ...");
            SplashScreen splash = splashScreenInit();
            createSvmPropertiesFileDefault();
            final CommandInvoker commandInvoker = createCommandInvoker();
            commandInvoker.openSession();
            final ModelFactory modelFactory = createModelFactory(commandInvoker);
            final SvmModel svmModel = modelFactory.createSvmModel();
            final SvmContext svmContext = new SvmContext(modelFactory, commandInvoker, svmModel);
            // Fängt alle unbehandelten Exceptions und beendet die Applikation.
            Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
            // Schedule a job for the event-dispatching thread:
            // creating and showing this application's GUI.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI(svmContext);
                }
            });
            splashScreenClose(splash);
        } catch (Exception e) {
            LOGGER.error("Fehler bei der Initialisierung der Applikation", e);
            JOptionPane.showMessageDialog(null,
                    "Bei der Initialisierung der Applikation ist ein Fehler aufgetreten.\n" +
                            "Bitte Netzwerkverbindung, Datenbank-Server, Log-Files (im Ordner \"log\") und .svm-File (im home) überprüfen.",
                    "Fehler bei der Initialisierung der Applikation",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static ModelFactoryImpl createModelFactory(CommandInvoker commandInvoker) {
        return new ModelFactoryImpl(commandInvoker);
    }

    private static CommandInvoker createCommandInvoker() {
        return new CommandInvokerImpl();
    }

    private static SplashScreen splashScreenInit() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            LOGGER.warn("SplashScreen.getSplashScreen() returned null");
            return null;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null) {
            LOGGER.warn("SplashScreen: g is null");
            return null;
        }
        return splash;
    }

    private static void splashScreenClose(SplashScreen splash) {
        if (splash != null) {
            splash.close();
        }
    }

}