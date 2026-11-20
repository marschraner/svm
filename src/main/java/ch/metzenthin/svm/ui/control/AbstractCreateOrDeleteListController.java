package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.AbstractCreateOrDeleteListModel;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.view.AbstractCreateOrDeleteListPanelView;

/**
 * @param <T> List-Model-Typ, z.B. KursortListModel
 * @param <U> List-Panel-View-Typ, z.B. KursortListView
 * @param <V> Delete-Result-Typ, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractCreateOrDeleteListController<
        T extends AbstractCreateOrDeleteListModel<?, ?, V>,
        U extends AbstractCreateOrDeleteListPanelView<?>,
        V extends SubmitDialogResult>
    extends AbstractListController<T, U, V> {

  protected AbstractCreateOrDeleteListController(SvmContext svmContext, T model, U view) {
    super(svmContext, model, view);
  }

  @Override
  protected void configView() {
    view.configListeners(() -> {}, this::onListSelection);
  }

  private void onListSelection() {
    int selectedRowIndex = view.getIndexOfSelectedRow();
    view.setButtonLoeschenEnabled(selectedRowIndex >= 0);
  }

  @Override
  protected void configButtons() {
    configBtnNeu();
    configBtnLoeschen();
  }

  private void configBtnNeu() {
    view.addButtonNeuActionListener(e -> onNeu());
  }

  protected abstract void showOnNeuDialog();

  @Override
  protected void onLoeschenDialog() {
    int n =
        view.showYesNoDialog(
            "Soll der Eintrag aus der Datenbank gelöscht werden?",
            model.getListItemName() + " löschen");
    if (n == 0) {
      model.eintragLoeschen(view.convertRowIndexToModel());
      removeSelectedRow();
    }
  }
}
