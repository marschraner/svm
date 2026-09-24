package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.AbstractSearchPanel;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * @param <T> Panel-Typ, z.B. KurseSemesterwahlPanel
 * @author Hans Stamm
 */
public abstract class AbstractSearchPanelView<T extends AbstractSearchPanel>
    extends AbstractView<T> {

  protected final T panel;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;
  private final ActionListener closeListener;

  protected AbstractSearchPanelView(T panel, ActionListener closeListener) {
    super(panel);
    this.panel = panel;
    this.buttonSubmit = panel.getSuchenButton();
    this.buttonAbbrechen = panel.getAbbrechenButton();
    this.closeListener = closeListener;
  }

  public JComponent getRootComponent() {
    return panel.getRootComponent();
  }

  public void addButtonSubmitActionListener(ActionListener actionListener) {
    buttonSubmit.addActionListener(actionListener);
  }

  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }

  public void abbrechen() {
    closeListener.actionPerformed(
        new ActionEvent(buttonAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
  }

  public void setWaitCursorAllComponents() {
    Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    panel.getRootComponent().setCursor(waitCursor);
    // Spinner nicht verändern, da sonst Pfeile nicht mehr korrekt angezeigt werden
  }

  public void resetCursorAllComponents() {
    panel.getRootComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    // Spinner nicht verändern, da sonst Pfeile nicht mehr korrekt angezeigt werden
  }
}
