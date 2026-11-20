package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.Optional;

/**
 * @param <T> Typ der Table-Data-Rows, z.B. Kursort
 * @param <U> Typ des Table-Models, z.B. TableModel<T>
 * @param <V> Typ des Resultats der Löschen-Methode, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractCreateOrUpdateOrDeleteListModel<
        T, U extends TableModel<? extends AbstractTableData<T>, T>, V extends SubmitDialogResult>
    extends AbstractListModel<T, U, V> {

  protected AbstractCreateOrUpdateOrDeleteListModel(U tableModel) {
    super(tableModel);
  }

  public abstract Object createCreateOrUpdateModel(SvmContext svmContext);

  public abstract Object createCreateOrUpdateModel(SvmContext svmContext, int indexToBeUpdated);

  /**
   * @param modelIndexOfRowOfUpdatedObject Listen-Index des Objekts, das neu geladen werden und in
   *     der Model-Liste ausgetauscht werden muss
   * @return true, wenn das neu zu ladende Objekt gefunden und ausgetauscht wurde, sonst false
   */
  public boolean replaceUpdatedObjectInTableModel(int modelIndexOfRowOfUpdatedObject) {
    T rowToBeReloaded = tableModel.getRowAt(modelIndexOfRowOfUpdatedObject);
    Optional<T> reloadedObjectOptional = reloadObjectFromDatabase(rowToBeReloaded);
    if (reloadedObjectOptional.isPresent()) {
      tableModel.replaceRowAt(modelIndexOfRowOfUpdatedObject, reloadedObjectOptional.get());
      return true;
    }
    return false;
  }

  /**
   * @param objectToBeReloaded Listen-Element, das neu geladen werden muss
   * @return Optional des neu geladenen Objekts
   */
  protected abstract Optional<T> reloadObjectFromDatabase(T objectToBeReloaded);
}
