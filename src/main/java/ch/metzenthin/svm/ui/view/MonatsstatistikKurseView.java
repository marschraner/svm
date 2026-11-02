package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.MonatsstatistikKursePanel;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

/**
 * @author Hans Stamm
 */
public class MonatsstatistikKurseView extends AbstractSubmitPanelView<MonatsstatistikKursePanel> {

  private final TextFieldWithErrorLabelComponent txtMonatJahr;

  public MonatsstatistikKurseView(ActionListener closeListener) {
    super(new MonatsstatistikKursePanel(), closeListener);
    this.txtMonatJahr =
        new TextFieldWithErrorLabelComponent(panel.getTxtMonatJahr(), panel.getErrLblMonatJahr());
  }

  // Monat/Jahr
  public void setErrorLabelMonatJahrVisible(String errorMessage) {
    txtMonatJahr.setErrorLabelVisible(true);
    txtMonatJahr.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelMonatJahrInvisible() {
    txtMonatJahr.setErrorLabelVisible(false);
    txtMonatJahr.setToolTipText(null);
  }

  public void addTxtMonatJahrActionListener(ActionListener actionListener) {
    txtMonatJahr.addActionListener(actionListener);
  }

  public void addTxtMonatJahrFocusListener(FocusListener focusListener) {
    txtMonatJahr.addFocusListener(focusListener);
  }

  public String getTxtMonatJahrText() {
    return txtMonatJahr.getText();
  }

  public void setTxtMonatJahrText(String text) {
    txtMonatJahr.setText(text);
  }
}
