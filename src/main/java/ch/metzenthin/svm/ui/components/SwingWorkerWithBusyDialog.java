package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmRuntimeException;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 * @param <T> Resultat-Typ, z.B. DeleteKursResult
 * @author Hans Stamm
 */
public class SwingWorkerWithBusyDialog<T> {

  private final JDialog busyDialog;

  public SwingWorkerWithBusyDialog(JDialog busyDialog) {
    this.busyDialog = busyDialog;
  }

  public T executeAndGetResult(Supplier<T> taskSupplier) {
    SwingWorker<T, Void> worker =
        new SwingWorker<>() {
          @Override
          protected T doInBackground() {
            return taskSupplier.get();
          }

          @Override
          protected void done() {
            busyDialog.dispose();
          }
        };

    // Worker muss ausgeführt werden bevor der Busy-Dialog visible wird, da mit setVisible der
    // nachfolgende Code blockiert ist.
    worker.execute();
    busyDialog.setVisible(true);
    // Die get()-Methode sollte eigentlich in der done()-Methode  aufgerufen werden. Ist hier aber
    // ok, da der Busy-Dialog blockiert und gewartet wird, bis die Background-Task beendet ist.
    try {
      return worker.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SvmRuntimeException(e.getMessage(), e);
    } catch (ExecutionException e) {
      if (e.getCause() instanceof SvmRuntimeException svmRuntimeException) {
        throw svmRuntimeException;
      }
      throw new SvmRuntimeException(e.getCause().getMessage(), e);
    }
  }
}
