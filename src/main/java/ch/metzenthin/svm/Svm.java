package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.Command;
import ch.metzenthin.svm.domain.commands.CommandInvoker;
import ch.metzenthin.svm.domain.commands.CommandInvokerImpl;
import ch.metzenthin.svm.domain.model.ModelFactoryImpl;
import ch.metzenthin.svm.ui.components.SvmDesktop;

import javax.persistence.Persistence;
import javax.swing.*;

/*
 * SVM Applikation
 */
public class Svm {

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
    }

    public static void main(String[] args) {
        final CommandInvoker commandInvoker = createCommandInvoker();
        final SvmContext svmContext = new SvmContext(createModelFactory(commandInvoker), commandInvoker);
        // Fängt alle unbehandelten Exceptions und beendet die Applikation.
        Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler());
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(svmContext);
            }
        });
    }

    private static ModelFactoryImpl createModelFactory(CommandInvoker commandInvoker) {
        return new ModelFactoryImpl(commandInvoker);
    }

    private static CommandInvoker createCommandInvoker() {
        return new CommandInvokerImpl(Persistence.createEntityManagerFactory("svm"));
    }

}