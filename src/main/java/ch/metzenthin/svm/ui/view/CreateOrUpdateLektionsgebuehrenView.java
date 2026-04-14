package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.ui.components.CreateOrUpdateLektionsgebuehrenDialog;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

/**
 * @author Hans Stamm
 */
public class CreateOrUpdateLektionsgebuehrenView
    extends SpeichernAbbrechenDialogView<CreateOrUpdateLektionsgebuehrenDialog> {

  private final TextFieldWithErrorLabelComponent lektionslaengeWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag1KindWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag2KinderWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag3KinderWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag4KinderWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag5KinderWithErrorLabel;
  private final TextFieldWithErrorLabelComponent betrag6KinderWithErrorLabel;

  public CreateOrUpdateLektionsgebuehrenView(String title) {
    super(new CreateOrUpdateLektionsgebuehrenDialog(title));
    this.lektionslaengeWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtLektionslaenge(), dialog.getErrLblLektionslaenge());
    this.betrag1KindWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag1Kind(), dialog.getErrLblBetrag1Kind());
    this.betrag2KinderWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag2Kinder(), dialog.getErrLblBetrag2Kinder());
    this.betrag3KinderWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag3Kinder(), dialog.getErrLblBetrag3Kinder());
    this.betrag4KinderWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag4Kinder(), dialog.getErrLblBetrag4Kinder());
    this.betrag5KinderWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag5Kinder(), dialog.getErrLblBetrag5Kinder());
    this.betrag6KinderWithErrorLabel =
        new TextFieldWithErrorLabelComponent(
            dialog.getTxtBetrag6Kinder(), dialog.getErrLblBetrag6Kinder());
  }

  // Lektionslaenge
  public void setErrorLabelLektionslaengeVisible(String errorMessage) {
    lektionslaengeWithErrorLabel.setErrorLabelVisible(true);
    lektionslaengeWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelLektionslaengeInvisible() {
    lektionslaengeWithErrorLabel.setErrorLabelVisible(false);
    lektionslaengeWithErrorLabel.setToolTipText(null);
  }

  public void addTxtLektionslaengeActionListener(ActionListener actionListener) {
    lektionslaengeWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtLektionslaengeFocusListener(FocusListener focusListener) {
    lektionslaengeWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtLektionslaengeText() {
    return lektionslaengeWithErrorLabel.getText();
  }

  public void setTxtLektionslaengeText(String text) {
    lektionslaengeWithErrorLabel.setText(text);
  }

  public void setTxtLektionslaengeToolTipText(String text) {
    lektionslaengeWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtLektionslaengeEnabled() {
    return lektionslaengeWithErrorLabel.isEnabled();
  }

  // Betrag1Kind
  public void setErrorLabelBetrag1KindVisible(String errorMessage) {
    betrag1KindWithErrorLabel.setErrorLabelVisible(true);
    betrag1KindWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag1KindInvisible() {
    betrag1KindWithErrorLabel.setErrorLabelVisible(false);
    betrag1KindWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag1KindActionListener(ActionListener actionListener) {
    betrag1KindWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag1KindFocusListener(FocusListener focusListener) {
    betrag1KindWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag1KindText() {
    return betrag1KindWithErrorLabel.getText();
  }

  public void setTxtBetrag1KindText(String text) {
    betrag1KindWithErrorLabel.setText(text);
  }

  public void setTxtBetrag1KindToolTipText(String text) {
    betrag1KindWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag1KindEnabled() {
    return betrag1KindWithErrorLabel.isEnabled();
  }

  // Betrag2Kinder
  public void setErrorLabelBetrag2KinderVisible(String errorMessage) {
    betrag2KinderWithErrorLabel.setErrorLabelVisible(true);
    betrag2KinderWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag2KinderInvisible() {
    betrag2KinderWithErrorLabel.setErrorLabelVisible(false);
    betrag2KinderWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag2KinderActionListener(ActionListener actionListener) {
    betrag2KinderWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag2KinderFocusListener(FocusListener focusListener) {
    betrag2KinderWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag2KinderText() {
    return betrag2KinderWithErrorLabel.getText();
  }

  public void setTxtBetrag2KinderText(String text) {
    betrag2KinderWithErrorLabel.setText(text);
  }

  public void setTxtBetrag2KinderToolTipText(String text) {
    betrag2KinderWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag2KinderEnabled() {
    return betrag2KinderWithErrorLabel.isEnabled();
  }

  // Betrag3Kinder
  public void setErrorLabelBetrag3KinderVisible(String errorMessage) {
    betrag3KinderWithErrorLabel.setErrorLabelVisible(true);
    betrag3KinderWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag3KinderInvisible() {
    betrag3KinderWithErrorLabel.setErrorLabelVisible(false);
    betrag3KinderWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag3KinderActionListener(ActionListener actionListener) {
    betrag3KinderWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag3KinderFocusListener(FocusListener focusListener) {
    betrag3KinderWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag3KinderText() {
    return betrag3KinderWithErrorLabel.getText();
  }

  public void setTxtBetrag3KinderText(String text) {
    betrag3KinderWithErrorLabel.setText(text);
  }

  public void setTxtBetrag3KinderToolTipText(String text) {
    betrag3KinderWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag3KinderEnabled() {
    return betrag3KinderWithErrorLabel.isEnabled();
  }

  // Betrag4Kinder
  public void setErrorLabelBetrag4KinderVisible(String errorMessage) {
    betrag4KinderWithErrorLabel.setErrorLabelVisible(true);
    betrag4KinderWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag4KinderInvisible() {
    betrag4KinderWithErrorLabel.setErrorLabelVisible(false);
    betrag4KinderWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag4KinderActionListener(ActionListener actionListener) {
    betrag4KinderWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag4KinderFocusListener(FocusListener focusListener) {
    betrag4KinderWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag4KinderText() {
    return betrag4KinderWithErrorLabel.getText();
  }

  public void setTxtBetrag4KinderText(String text) {
    betrag4KinderWithErrorLabel.setText(text);
  }

  public void setTxtBetrag4KinderToolTipText(String text) {
    betrag4KinderWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag4KinderEnabled() {
    return betrag4KinderWithErrorLabel.isEnabled();
  }

  // Betrag5Kinder
  public void setErrorLabelBetrag5KinderVisible(String errorMessage) {
    betrag5KinderWithErrorLabel.setErrorLabelVisible(true);
    betrag5KinderWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag5KinderInvisible() {
    betrag5KinderWithErrorLabel.setErrorLabelVisible(false);
    betrag5KinderWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag5KinderActionListener(ActionListener actionListener) {
    betrag5KinderWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag5KinderFocusListener(FocusListener focusListener) {
    betrag5KinderWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag5KinderText() {
    return betrag5KinderWithErrorLabel.getText();
  }

  public void setTxtBetrag5KinderText(String text) {
    betrag5KinderWithErrorLabel.setText(text);
  }

  public void setTxtBetrag5KinderToolTipText(String text) {
    betrag5KinderWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag5KinderEnabled() {
    return betrag5KinderWithErrorLabel.isEnabled();
  }

  // Betrag6Kinder
  public void setErrorLabelBetrag6KinderVisible(String errorMessage) {
    betrag6KinderWithErrorLabel.setErrorLabelVisible(true);
    betrag6KinderWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelBetrag6KinderInvisible() {
    betrag6KinderWithErrorLabel.setErrorLabelVisible(false);
    betrag6KinderWithErrorLabel.setToolTipText(null);
  }

  public void addTxtBetrag6KinderActionListener(ActionListener actionListener) {
    betrag6KinderWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtBetrag6KinderFocusListener(FocusListener focusListener) {
    betrag6KinderWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtBetrag6KinderText() {
    return betrag6KinderWithErrorLabel.getText();
  }

  public void setTxtBetrag6KinderText(String text) {
    betrag6KinderWithErrorLabel.setText(text);
  }

  public void setTxtBetrag6KinderToolTipText(String text) {
    betrag6KinderWithErrorLabel.setToolTipText(text);
  }

  public boolean isTxtBetrag6KinderEnabled() {
    return betrag6KinderWithErrorLabel.isEnabled();
  }
}
