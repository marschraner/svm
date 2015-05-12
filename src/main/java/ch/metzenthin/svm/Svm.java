package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.components.SvmDesktop;

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
        // TODO JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        new SvmDesktop(svmContext);
    }

    public static void main(String[] args) {
        final SvmContext svmContext = new SvmContext();
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(svmContext);
            }
        });
    }

}