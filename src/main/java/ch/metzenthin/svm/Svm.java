package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.utils.SvmProperties;
import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.ModelFactoryImpl;
import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.ui.components.SvmDesktop;
import java.awt.*;
import java.util.Properties;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * SVM Applikation
 */
public class Svm {

  private static final Logger LOGGER = LogManager.getLogger(Svm.class);

  public static void main(String[] args) {
    try {
      LOGGER.info("Svm wird gestartet ...");
      splashScreenInit();
      SvmProperties.createSvmPropertiesFileDefault();
      ModelFactory modelFactory = new ModelFactoryImpl();
      SvmModel svmModel = modelFactory.createSvmModel();
      SvmContext svmContext = new SvmContext(modelFactory, svmModel);
      // Fängt alle unbehandelten Exceptions und beendet die Applikation.
      Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
      // Schedule a job for the event-dispatching thread:
      // creating and showing this application's GUI.
      SwingUtilities.invokeLater(() -> createAndShowGUI(svmContext));
      // splashScreenClose(splash); -> No need to close it here.
      // This way the splash screen disappears when GUI appears.
    } catch (Exception e) {
      LOGGER.error("Fehler bei der Initialisierung der Applikation", e);
      JOptionPane.showMessageDialog(
          null,
          "Bei der Initialisierung der Applikation ist ein Fehler "
              + "aufgetreten.\n"
              + "Bitte Netzwerkverbindung, Datenbank-Server, Log-Files (im Ordner "
              + "\"log\") und .svm-File (im home) überprüfen.",
          "Fehler bei der Initialisierung der Applikation",
          JOptionPane.ERROR_MESSAGE);
    }
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

  /**
   * Create the GUI and show it. For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI(SvmContext svmContext) {

    // Optionaler Preferred Look and Feel-Wert aus SVM Properties abfragen
    Properties svmProperties = SvmProperties.getSvmProperties();
    String svmPropertyPreferredLookAndFeel =
        svmProperties.getProperty(SvmProperties.KEY_PREFERRED_LOOK_AND_FEEL);

    String loadedLookAndFeel = loadLookAndFeel(svmPropertyPreferredLookAndFeel);

    if (svmPropertyPreferredLookAndFeel != null
        && !svmPropertyPreferredLookAndFeel.isEmpty()
        && !svmPropertyPreferredLookAndFeel.equalsIgnoreCase(loadedLookAndFeel)) {
      LOGGER.warn(
          "'{}' ist kein gültiger Wert für SVM-Property '{}'. Verwende "
              + "stattdessen Look and Feel '{}'.",
          svmPropertyPreferredLookAndFeel,
          SvmProperties.KEY_PREFERRED_LOOK_AND_FEEL,
          loadedLookAndFeel);
    }

    // Notwendige Workarounds für einige Look-And-Feels
    if ("GTK+".equals(loadedLookAndFeel)) {
      GtkPlusLookAndFeelWorkaround.installGtkPopupBugWorkaround();
    }

    // Create and set up the window.
    SvmDesktop svmDesktop = new SvmDesktop(svmContext);
    ((SwingExceptionHandler) Thread.getDefaultUncaughtExceptionHandler()).setSvmDesktop(svmDesktop);
    LOGGER.info("Svm wurde gestartet.");
  }

  private static String loadLookAndFeel(String svmPropertyPreferredLookAndFeel) {
    // Nach absteigender Präferenz geordnet
    String[] preferredLookAndFeels =
        new String[] {svmPropertyPreferredLookAndFeel, "Mac OS X", "GTK+", "Windows", "Nimbus"};

    for (String preferredLookAndFeel : preferredLookAndFeels) {
      if (preferredLookAndFeel == null || preferredLookAndFeel.isEmpty()) {
        continue;
      }
      for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
        if (preferredLookAndFeel.equalsIgnoreCase(lookAndFeelInfo.getName())) {
          try {
            UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
          } catch (Exception e) {
            LOGGER.error(e.getMessage());
          }
          return lookAndFeelInfo.getName();
        }
      }
    }
    return "";
  }
}
