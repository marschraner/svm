package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.Optional;
import lombok.Getter;

/**
 * @param <T> Typ der Table-Data-Rows, z.B. Kursort
 * @param <U> Typ des Table-Models, z.B. TableModel<T>
 * @param <V> Typ des Resultats der Löschen-Methode, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListModel<
    T, U extends TableModel<? extends AbstractTableData<T>, T>, V extends SubmitDialogResult> {

  @Getter protected final U tableModel;

  protected AbstractListModel(U tableModel) {
    this.tableModel = tableModel;
  }

  public abstract Object createCreateOrUpdateModel(SvmContext svmContext);

  public abstract Object createCreateOrUpdateModel(SvmContext svmContext, int indexToBeUpdated);

  protected T getSelectedRow(int selectedIndex) {
    return tableModel.getRowAt(selectedIndex);
  }

  /**
   * @param idOfCreatedEntity ID der Entity, die in die Liste eingefügt werden muss
   * @return Listen-Index (Position in der Model-Liste) der Entity, die eingefügt wurde
   */
  public Optional<Integer> insertCreatedObjectToTableModel(int idOfCreatedEntity) {
    Optional<T> entityOptional = loadObjectFromDatabase(idOfCreatedEntity);
    return entityOptional.map(tableModel::insertRow);
  }

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
   * @param modelIndexOfRowOfDeletedObject Listen-Index des Objekts, das aus der Model-Liste
   *     entfernt werden muss
   */
  public void removeDeletedObjectFromTableModel(int modelIndexOfRowOfDeletedObject) {
    tableModel.removeRowAt(modelIndexOfRowOfDeletedObject);
  }

  /**
   * @param idOfEntityToBeLoaded ID der Entity, die geladen werden muss
   * @return Optional des geladenen Objekts
   */
  protected abstract Optional<T> loadObjectFromDatabase(int idOfEntityToBeLoaded);

  /**
   * @param objectToBeReloaded Listen-Element, das neu geladen werden muss
   * @return Optional des neu geladenen Objekts
   */
  protected abstract Optional<T> reloadObjectFromDatabase(T objectToBeReloaded);

  public abstract void reloadData();

  public abstract V eintragLoeschen(int indexToBeDeleted);

  public abstract String getListItemName();
}
