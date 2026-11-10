package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateDispensationDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateDispensationView
    extends AbstractSubmitDialogView<CreateOrUpdateDispensationDialog> {

  private final TextFieldWithErrorLabelComponent dispensationsbeginnWithErrorLabel;
  private final TextFieldWithErrorLabelComponent dispensationsendeWithErrorLabel;
  private final TextFieldWithErrorLabelComponent voraussichtlicheDauerWithErrorLabel;
  private final TextFieldWithErrorLabelComponent grundWithErrorLabel;

  public CreateOrUpdateDispensationView(String title) {
    super(new CreateOrUpdateDispensationDialog(title));
    this.dispensationsbeginnWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtDispensationsbeginn(), dialog.getErrLblDispensationsbeginn());
    this.dispensationsendeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtDispensationsende(), dialog.getErrLblDispensationsende());
    this.voraussichtlicheDauerWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtVoraussichtlicheDauer(), dialog.getErrLblVoraussichtlicheDauer());
    this.grundWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtGrund(), dialog.getErrLblGrund());
  }

  // Dispensationsbeginn
  public void setErrorLabelDispensationsbeginnVisible(String errorMessage) {
    dispensationsbeginnWithErrorLabel.setErrorLabelVisible(true);
    dispensationsbeginnWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelDispensationsbeginnInvisible() {
    dispensationsbeginnWithErrorLabel.setErrorLabelVisible(false);
    dispensationsbeginnWithErrorLabel.setToolTipText(null);
  }

  public void addTxtDispensationsbeginnActionListener(ActionListener actionListener) {
    dispensationsbeginnWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtDispensationsbeginnFocusListener(FocusListener focusListener) {
    dispensationsbeginnWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtDispensationsbeginnText() {
    return dispensationsbeginnWithErrorLabel.getText();
  }

  public void setTxtDispensationsbeginnText(String text) {
    dispensationsbeginnWithErrorLabel.setText(text);
  }

  // Dispensationsende
  public void setErrorLabelDispensationsendeVisible(String errorMessage) {
    dispensationsendeWithErrorLabel.setErrorLabelVisible(true);
    dispensationsendeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelDispensationsendeInvisible() {
    dispensationsendeWithErrorLabel.setErrorLabelVisible(false);
    dispensationsendeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtDispensationsendeActionListener(ActionListener actionListener) {
    dispensationsendeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtDispensationsendeFocusListener(FocusListener focusListener) {
    dispensationsendeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtDispensationsendeText() {
    return dispensationsendeWithErrorLabel.getText();
  }

  public void setTxtDispensationsendeText(String text) {
    dispensationsendeWithErrorLabel.setText(text);
  }

  // Voraussichtliche Dauer
  public void setErrorLabelVoraussichtlicheDauerVisible(String errorMessage) {
    voraussichtlicheDauerWithErrorLabel.setErrorLabelVisible(true);
    voraussichtlicheDauerWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelVoraussichtlicheDauerInvisible() {
    voraussichtlicheDauerWithErrorLabel.setErrorLabelVisible(false);
    voraussichtlicheDauerWithErrorLabel.setToolTipText(null);
  }

  public void addTxtVoraussichtlicheDauerActionListener(ActionListener actionListener) {
    voraussichtlicheDauerWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtVoraussichtlicheDauerFocusListener(FocusListener focusListener) {
    voraussichtlicheDauerWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtVoraussichtlicheDauerText() {
    return voraussichtlicheDauerWithErrorLabel.getText();
  }

  public void setTxtVoraussichtlicheDauerText(String text) {
    voraussichtlicheDauerWithErrorLabel.setText(text);
  }

  // Grund
  public void setErrorLabelGrundVisible(String errorMessage) {
    grundWithErrorLabel.setErrorLabelVisible(true);
    grundWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelGrundInvisible() {
    grundWithErrorLabel.setErrorLabelVisible(false);
    grundWithErrorLabel.setToolTipText(null);
  }

  public void addTxtGrundActionListener(ActionListener actionListener) {
    grundWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtGrundFocusListener(FocusListener focusListener) {
    grundWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtGrundText() {
    return grundWithErrorLabel.getText();
  }

  public void setTxtGrundText(String text) {
    grundWithErrorLabel.setText(text);
  }
}
