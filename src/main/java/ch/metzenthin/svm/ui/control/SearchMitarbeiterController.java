package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.model.MitarbeiterListModel;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.LehrkraftJaNeinSelected;
import ch.metzenthin.svm.domain.model.SearchMitarbeiterModel.StatusSelected;
import ch.metzenthin.svm.domain.model.searchfields.MitarbeiterSearchFields;
import ch.metzenthin.svm.domain.model.validation.ValidationResult;
import ch.metzenthin.svm.domain.model.validation.ValidationResultsAndListModel;
import ch.metzenthin.svm.ui.view.SearchMitarbeiterView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class SearchMitarbeiterController
    extends AbstractSearchPanelController<SearchMitarbeiterView, MitarbeiterListModel> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchMitarbeiterController.class);

  private final SvmContext svmContext;
  private final SearchMitarbeiterModel model;
  private final ActionListener closeListener;
  private final ActionListener nextPanelListener;

  public SearchMitarbeiterController(
      SvmContext svmContext,
      SearchMitarbeiterModel searchMitarbeiterModel,
      ActionListener closeListener,
      ActionListener nextPanelListener) {
    super(createView(closeListener));
    this.svmContext = svmContext;
    this.model = searchMitarbeiterModel;
    this.closeListener = closeListener;
    this.nextPanelListener = nextPanelListener;
    configTxtNachname();
    configTxtVorname();
    configComboBoxMitarbeiterCode();
    initialiseViewFields();
  }

  private static SearchMitarbeiterView createView(ActionListener closeListener) {
    return new SearchMitarbeiterView(closeListener);
  }

  private void configTxtNachname() {
    view.addTxtNachnameActionListener(e -> onNachnameEvent());
    view.addTxtNachnameFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onNachnameEvent();
          }
        });
  }

  private void onNachnameEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Nachname");
    formatAndValidateString(
        view.getTxtNachnameText(),
        model::validateNachname,
        view::setTxtNachnameText,
        view::setErrorLabelNachnameVisible,
        view::setErrorLabelNachnameInvisible);
  }

  private void configTxtVorname() {
    view.addTxtVornameActionListener(e -> onVornameEvent());
    view.addTxtVornameFocusListener(
        new FocusAdapter() {
          @Override
          public void focusLost(FocusEvent e) {
            onVornameEvent();
          }
        });
  }

  private void onVornameEvent() {
    LOGGER.trace("CreateOrUpdateMitarbeiterController Event Vorname");
    formatAndValidateString(
        view.getTxtVornameText(),
        model::validateVorname,
        view::setTxtVornameText,
        view::setErrorLabelVornameVisible,
        view::setErrorLabelVornameInvisible);
  }

  private void configComboBoxMitarbeiterCode() {
    view.setComboBoxMitarbeiterCodeValues(model.getSelectableCodes());
    // ComboBox auf leeren Wert setzen, vor der Registrierung des Listeners, sonst gibt es einen
    // Validierungsfehler!
    view.setComboBoxMitarbeiterCodeSelectedItem(null);
  }

  private void initialiseViewFields() {
    view.setRadioButtonGroupLehrkraftSelected(LehrkraftJaNeinSelected.ALLE);
    view.setRadioButtonGroupStatusSelected(StatusSelected.AKTIV);
  }

  //  private void onSuchen() {
  //    LOGGER.trace("SearchMitarbeiterPanel Suchen gedrückt");
  //    if (!validateOnSpeichern()) {
  //      btnSuchen.setFocusPainted(false);
  //      return;
  //    }
  //    setWaitCursorAllComponents();
  //    MitarbeitersTableData mitarbeitersTableData = model.suchen();
  //    MitarbeitersTableModel mitarbeitersTableModel =
  //        new MitarbeitersTableModel(mitarbeitersTableData);
  //    // Auch bei einem Suchresultat Liste anzeigen, da nur von dort gelöscht werden kann
  //    if (mitarbeitersTableData.size() > 0 || !model.isSuchkriterienSelected()) {
  //      MitarbeitersPanel mitarbeitersPanel =
  //          new MitarbeitersPanel(svmContext, mitarbeitersTableModel);
  //      mitarbeitersPanel.addCloseListener(closeListener);
  //      mitarbeitersPanel.addZurueckListener(zurueckListener);
  //      resetCursorAllComponents();
  //      nextPanelListener.actionPerformed(
  //          new ActionEvent(
  //              new Object[] {mitarbeitersPanel.$$$getRootComponent$$$(), "Suchresultat"},
  //              ActionEvent.ACTION_PERFORMED,
  //              "Suchresultat verfügbar"));
  //    } else {
  //      resetCursorAllComponents();
  //      JOptionPane.showMessageDialog(
  //          null,
  //          "Es wurden keine Mitarbeiter gefunden, welche auf die Suchabfrage passen.",
  //          "Keine Mitarbeiter gefunden",
  //          JOptionPane.INFORMATION_MESSAGE);
  //      btnSuchen.setFocusPainted(false);
  //    }
  //  }

  //  private void setWaitCursorAllComponents() {
  //    Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
  //    mainPanel.setCursor(waitCursor);
  //    // TextFields müssen separat gesetzt werden
  //    txtNachname.setCursor(waitCursor);
  //    txtVorname.setCursor(waitCursor);
  //  }
  //
  //  private void resetCursorAllComponents() {
  //    mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  //    // TextFields müssen separat gesetzt werden
  //    Cursor textCursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
  //    txtNachname.setCursor(textCursor);
  //    txtVorname.setCursor(textCursor);
  //  }

  @Override
  protected ValidationResultsAndListModel<MitarbeiterListModel> suchen() {
    view.setWaitCursorAllComponents();
    ValidationResultsAndListModel<MitarbeiterListModel> searchResult =
        model.search(
            new MitarbeiterSearchFields(
                view.getTxtNachnameText(),
                view.getTxtVornameText(),
                view.getComboBoxMitarbeiterCodeSelectedItem(),
                view.getRadioButtonGroupLehrkraftSelected(),
                view.getRadioButtonGroupStatusSelected()));
    view.resetCursorAllComponents();
    return searchResult;
  }

  @Override
  protected void showNextPanel(MitarbeiterListModel mitarbeiterListModel) {
    if (mitarbeiterListModel.getTableModel().getRowCount() == 0) {
      view.showInfoMessageDialog(
          "Es wurden keine Mitarbeiter gefunden, welche auf die Suchabfrage passen.",
          "Keine Mitarbeiter gefunden");
    } else {
      MitarbeiterListController mitarbeiterListController =
          new MitarbeiterListController(
              svmContext, mitarbeiterListModel, closeListener, e -> onZurueck());
      nextPanelListener.actionPerformed(
          new ActionEvent(
              new Object[] {mitarbeiterListController.getView().getRootComponent(), "Suchresultat"},
              ActionEvent.ACTION_PERFORMED,
              "Suchresultat verfügbar"));
    }
  }

  private void onZurueck() {
    nextPanelListener.actionPerformed(
        new ActionEvent(
            new Object[] {view.getRootComponent(), "Mitarbeiter suchen", Boolean.TRUE},
            ActionEvent.ACTION_PERFORMED,
            "Zurück zu Mitarbeiter suchen"));
  }

  @Override
  protected void setErrorLabelVisible(ValidationResult validationResult, Field field) {
    switch (field) {
      case NACHNAME ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelNachnameVisible);
      case VORNAME ->
          setErrorLabelVisibleIfRequired(
              validationResult, field, view::setErrorLabelVornameVisible);
      default -> throw new IllegalStateException("Unexpected value: " + field);
    }
  }

  @Override
  protected void setAllErrorLabelsInvisible() {
    view.setErrorLabelNachnameInvisible();
    view.setErrorLabelVornameInvisible();
  }
}
