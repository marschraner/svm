package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateCodeDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateCodeView extends SpeichernAbbrechenDialogView<CreateOrUpdateCodeDialog> {

  private final TextFieldWithErrorLabelComponent kuerzelWithErrorLabel;
  private final TextFieldWithErrorLabelComponent beschreibungWithErrorLabel;
  private final JCheckBox checkBoxSelektierbar;

  public CreateOrUpdateCodeView(String title) {
    super(new CreateOrUpdateCodeDialog(title));
    this.kuerzelWithErrorLabel =
        new TextFieldWithErrorLabelComponent(dialog.getTxtKuerzel(), dialog.getErrLblKuerzel());
    this.beschreibungWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBeschreibung(), dialog.getErrLblBeschreibung());
    this.checkBoxSelektierbar = dialog.getCheckBoxSelektierbar();
  }

  // Kuerzel

  public void setErrLblKuerzelVisible(String errorMessage) {
    kuerzelWithErrorLabel.setErrorLabelVisible(true);
    kuerzelWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblKuerzelInvisible() {
    kuerzelWithErrorLabel.setErrorLabelVisible(false);
    kuerzelWithErrorLabel.setErrorLabelText(null);
  }

  public void addTxtKuerzelActionListener(ActionListener actionListener) {
    kuerzelWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtKuerzelFocusListener(FocusListener focusListener) {
    kuerzelWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtKuerzelText() {
    return kuerzelWithErrorLabel.getText();
  }

  public void setTxtKuerzelText(String text) {
    kuerzelWithErrorLabel.setText(text);
  }

  public void setTxtKuerzelToolTipText(String text) {
    kuerzelWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtKuerzelEnabled() {
    return kuerzelWithErrorLabel.isEnabled();
  }

  // Beschreibung

  public void setErrLblBeschreibungVisible(String errorMessage) {
    beschreibungWithErrorLabel.setErrorLabelVisible(true);
    beschreibungWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrLblBeschreibungInvisible() {
    beschreibungWithErrorLabel.setErrorLabelVisible(false);
    beschreibungWithErrorLabel.setErrorLabelText(null);
  }

  public void addTxtBeschreibungActionListener(ActionListener actionListener) {
    beschreibungWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBeschreibungFocusListener(FocusListener focusListener) {
    beschreibungWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBeschreibungText() {
    return beschreibungWithErrorLabel.getText();
  }

  public void setTxtBeschreibungText(String text) {
    beschreibungWithErrorLabel.setText(text);
  }

  public void setTxtBeschreibungToolTipText(String text) {
    beschreibungWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBeschreibungEnabled() {
    return beschreibungWithErrorLabel.isEnabled();
  }

  // Selektierbar

  public void addCheckBoxSelektierbarItemListener(ItemListener itemListener) {
    checkBoxSelektierbar.addItemListener(itemListener);
  }

  public boolean isCheckBoxSelektierbarSelected() {
    return checkBoxSelektierbar.isSelected();
  }

  public void setCheckBoxSelektierbarSelected(boolean selected) {
    checkBoxSelektierbar.setSelected(selected);
  }
}
