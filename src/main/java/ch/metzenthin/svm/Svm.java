package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.CommandInvokerImpl;
import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.ModelFactoryImpl;
import ch.metzenthin.svm.domain.model.SvmModel;
import ch.metzenthin.svm.ui.components.SvmDesktop;
import org.apache.log4j.Logger;

import javax.swing.*;

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
        // Make sure we have nice window decorations.
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            String laf = lookAndFeelInfo.getName();
            switch (laf) {
                case "GTK+" :
                case "Mac" :
                case "Windows" :
                    try {
                        UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                    } catch (Exception ignore) {
                    }
                    break;
                default:
                    break;
            }
            if ("GTK+".equals(laf)) {
                GtkPlusLookAndFeelWorkaround.installGtkPopupBugWorkaround();
            }
        }

        // Create and set up the window.
        new SvmDesktop(svmContext);
        LOGGER.info("Svm wurde gestartet.");
    }

    public static void main(String[] args) {
        try {
            LOGGER.info("Svm wird gestartet ...");
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
        } catch (Exception e) {
            LOGGER.error("Fehler bei der Initialisierung der Applikation", e);
            JOptionPane.showMessageDialog(null,
                    "Bei der Initialisierung der Applikation ist ein Fehler aufgetreten.\n" +
                            "Bitte Netzwerkverbindung, Datenbank-Server und Log-Files (im Ordner \"log\") überprüfen.",
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

}