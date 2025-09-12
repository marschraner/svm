package ch.metzenthin.svm;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.ui.components.SvmDesktop;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hans Stamm
 */
public class SwingExceptionHandler implements Thread.UncaughtExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger(SwingExceptionHandler.class);
  private SvmDesktop svmDesktop;

  public void uncaughtException(final Thread t, final Throwable e) {
    if (SwingUtilities.isEventDispatchThread()) {
      showMessage(t, e);
    } else {
      try {
        SwingUtilities.invokeAndWait(() -> showMessage(t, e));
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      } catch (InvocationTargetException ite) {
        // not much more we can do here except log the exception
        LOGGER.error(ite.getMessage());
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
    LOGGER.error("Uncaught Exception: ", e);
    String stackTrace = generateStackTrace(e);
    // show an error dialog
    JTextArea textArea = new JTextArea("Exception Occurred in " + t + "\n" + stackTrace);
    JScrollPane scrollPane = new JScrollPane(textArea);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    scrollPane.setPreferredSize(new Dimension(1000, 500));
    String leaveMessage =
        (svmDesktop == null)
            ? "Die Applikation wird beendet."
            : "Die Applikation wird neu initialisiert.";
    if ((e instanceof SvmRuntimeException) || (e.getCause() instanceof SvmRuntimeException)) {
      JOptionPane.showMessageDialog(
          findActiveOrVisibleFrame(),
          (e instanceof SvmRuntimeException)
              ? e.getMessage()
              : e.getCause().getMessage() + " " + leaveMessage,
          "Fehler",
          JOptionPane.ERROR_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(
          findActiveOrVisibleFrame(),
          scrollPane,
          "Ein unerwarteter Fehler ist aufgetreten. " + leaveMessage,
          JOptionPane.ERROR_MESSAGE);
    }
    if (svmDesktop != null) {
      LOGGER.info("Svm wird neu initialisiert.");
      svmDesktop.initialize();
      LOGGER.info("Svm Initialisierung nach Fehler beendet.");
    } else {
      LOGGER.info("Svm wird nach Fehler beendet.");
      System.exit(1);
    }
  }

  /** We look for an active frame and attach ourselves to that. */
  private Frame findActiveOrVisibleFrame() {
    Frame[] frames = Frame.getFrames();
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

  public void setSvmDesktop(SvmDesktop svmDesktop) {
    this.svmDesktop = svmDesktop;
  }
}
