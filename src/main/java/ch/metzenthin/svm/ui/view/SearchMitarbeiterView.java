package ch.metzenthin.svm.ui.view;

import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.LehrkraftJaNeinSelected;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.StatusSelected;
import ch.metzenthin.svm.persistence.entities.MitarbeiterCode;
import ch.metzenthin.svm.ui.components.RadioButtonComponent;
import ch.metzenthin.svm.ui.components.RadioButtonGroupComponent;
import ch.metzenthin.svm.ui.components.SearchMitarbeiterPanel;
import ch.metzenthin.svm.ui.components.TextFieldWithErrorLabelComponent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author Hans Stamm
 */
public class SearchMitarbeiterView extends AbstractSearchPanelView<SearchMitarbeiterPanel> {

  private final TextFieldWithErrorLabelComponent nachnameWithErrorLabel;
  private final TextFieldWithErrorLabelComponent vornameWithErrorLabel;
  private final JComboBox<MitarbeiterCode> mitarbeiterCodeComboBox;
  private final RadioButtonGroupComponent lehrkraftRadioButtonGroup;
  private final RadioButtonGroupComponent statusRadioButtonGroup;

  public SearchMitarbeiterView(ActionListener closeListener) {
    super(new SearchMitarbeiterPanel(), closeListener);
    this.nachnameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtNachname(), panel.getErrLblNachname());
    this.vornameWithErrorLabel =
        new TextFieldWithErrorLabelComponent(panel.getTxtVorname(), panel.getErrLblVorname());
    this.mitarbeiterCodeComboBox = panel.getComboBoxMitarbeiterCode();
    this.lehrkraftRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnLehrkraftJa(), LehrkraftJaNeinSelected.JA.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnLehrkraftNein(), LehrkraftJaNeinSelected.NEIN.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnLehrkraftAlle(), LehrkraftJaNeinSelected.ALLE.toString()));
    this.statusRadioButtonGroup =
        new RadioButtonGroupComponent(
            new RadioButtonComponent(
                panel.getRadioBtnStatusAktiv(), StatusSelected.AKTIV.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnStatusNichtAktiv(), StatusSelected.NICHT_AKTIV.toString()),
            new RadioButtonComponent(
                panel.getRadioBtnStatusAlle(), StatusSelected.ALLE.toString()));
  }

  // Nachname
  public void setErrorLabelNachnameVisible(String errorMessage) {
    nachnameWithErrorLabel.setErrorLabelVisible(true);
    nachnameWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelNachnameInvisible() {
    nachnameWithErrorLabel.setErrorLabelVisible(false);
    nachnameWithErrorLabel.setToolTipText(null);
  }

  public void addTxtNachnameActionListener(ActionListener actionListener) {
    nachnameWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtNachnameFocusListener(FocusListener focusListener) {
    nachnameWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtNachnameText() {
    return nachnameWithErrorLabel.getText();
  }

  public void setTxtNachnameText(String text) {
    nachnameWithErrorLabel.setText(text);
  }

  // Vorname
  public void setErrorLabelVornameVisible(String errorMessage) {
    vornameWithErrorLabel.setErrorLabelVisible(true);
    vornameWithErrorLabel.setErrorLabelText(errorMessage);
  }

  public void setErrorLabelVornameInvisible() {
    vornameWithErrorLabel.setErrorLabelVisible(false);
    vornameWithErrorLabel.setToolTipText(null);
  }

  public void addTxtVornameActionListener(ActionListener actionListener) {
    vornameWithErrorLabel.addActionListener(actionListener);
  }

  public void addTxtVornameFocusListener(FocusListener focusListener) {
    vornameWithErrorLabel.addFocusListener(focusListener);
  }

  public String getTxtVornameText() {
    return vornameWithErrorLabel.getText();
  }

  public void setTxtVornameText(String text) {
    vornameWithErrorLabel.setText(text);
  }

  // MitarbeiterCode
  public void setComboBoxMitarbeiterCodeValues(MitarbeiterCode[] values) {
    mitarbeiterCodeComboBox.setModel(new DefaultComboBoxModel<>(values));
  }

  public MitarbeiterCode getComboBoxMitarbeiterCodeSelectedItem() {
    return (MitarbeiterCode) mitarbeiterCodeComboBox.getSelectedItem();
  }

  public void setComboBoxMitarbeiterCodeSelectedItem(MitarbeiterCode mitarbeiterCode) {
    mitarbeiterCodeComboBox.setSelectedItem(mitarbeiterCode);
  }

  // Lehrkraft
  public void setRadioButtonGroupLehrkraftSelected(
      LehrkraftJaNeinSelected lehrkraftJaNeinSelected) {
    lehrkraftRadioButtonGroup.setSelected(lehrkraftJaNeinSelected.toString());
  }

  public LehrkraftJaNeinSelected getRadioButtonGroupLehrkraftSelected() {
    String selected = lehrkraftRadioButtonGroup.getSelected();
    return LehrkraftJaNeinSelected.valueOf(selected);
  }

  // Status
  public void setRadioButtonGroupStatusSelected(StatusSelected statusSelected) {
    statusRadioButtonGroup.setSelected(statusSelected.toString());
  }

  public StatusSelected getRadioButtonGroupStatusSelected() {
    String selected = statusRadioButtonGroup.getSelected();
    return StatusSelected.valueOf(selected);
  }
}
