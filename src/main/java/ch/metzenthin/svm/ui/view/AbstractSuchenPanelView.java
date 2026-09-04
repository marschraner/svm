package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.AbstractSuchenPanel;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * @param <T> Panel-Typ, z.B. KurseSemesterwahlPanel
 * @author Hans Stamm
 */
public abstract class AbstractSuchenPanelView<T extends AbstractSuchenPanel> extends AbstractView<T>
    implements SuchenPanelView {

  protected final T panel;
  private final JButton buttonSubmit;
  private final JButton buttonAbbrechen;
  private final ActionListener closeListener;

  protected AbstractSuchenPanelView(T panel, ActionListener closeListener) {
    super(panel);
    this.panel = panel;
    this.buttonSubmit = panel.getSuchenButton();
    this.buttonAbbrechen = panel.getAbbrechenButton();
    this.closeListener = closeListener;
  }

  @Override
  public JComponent getRootComponent() {
    return panel.getRootComponent();
  }

  @Override
  public void addButtonSubmitActionListener(ActionListener actionListener) {
    buttonSubmit.addActionListener(actionListener);
  }

  @Override
  public void addButtonAbbrechenActionListener(ActionListener actionListener) {
    buttonAbbrechen.addActionListener(actionListener);
  }

  @Override
  public void abbrechen() {
    closeListener.actionPerformed(
        new ActionEvent(buttonAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
  }

  @Override
  public void setWaitCursorAllComponents() {
    Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    panel.getMainPanel().setCursor(waitCursor);
    // Spinner nicht verändern, da sonst Pfeile nicht mehr korrekt angezeigt werden
  }

  @Override
  public void resetCursorAllComponents() {
    panel.getMainPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    // Spinner nicht verändern, da sonst Pfeile nicht mehr korrekt angezeigt werden
  }
}
