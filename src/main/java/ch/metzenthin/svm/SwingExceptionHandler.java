package ch.metzenthin.svm;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Hans Stamm
 */
public class SwingExceptionHandler implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(final Thread t, final Throwable e) {
        if (SwingUtilities.isEventDispatchThread()) {
            showMessage(t, e);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        showMessage(t, e);
                    }
                });
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException ite) {
                // not much more we can do here except log the exception
                ite.getCause().printStackTrace();
            }
        }
    }

    private String generateStackTrace(Throwable e) {
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        return writer.toString();
    }

    private void showMessage(Thread t, Throwable e) {
        String stackTrace = generateStackTrace(e);
        // show an error dialog
        JOptionPane.showMessageDialog(findActiveOrVisibleFrame(),
                "Exception Occurred in " + t + "\n" + stackTrace,
                "Ein unerwarteter Fehler ist aufgetreten. Die Applikation wird beendet!",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    /**
     * We look for an active frame and attach ourselves to that.
     */
    private Frame findActiveOrVisibleFrame() {
        Frame[] frames = JFrame.getFrames();
        for (Frame frame : frames) {
            if (frame.isActive()) {
                return frame;
            }
        }
        for (Frame frame : frames) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
}