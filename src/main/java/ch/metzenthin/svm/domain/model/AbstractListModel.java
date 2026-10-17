package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import java.util.Optional;
import lombok.Getter;

/**
 * @param <T> Table-Data-Typ, z.B. KursortTableData
 * @param <U> Typ der Table-Data-Rows, z.B. Kursort
 * @param <V> Typ des Table-Models, z.B. TableModel<T, U>
 * @param <W> Typ des CreateOrUpdate-Models, z.B. CreateOrUpdateKursortModel
 * @param <X> Typ des Resultats der Löschen-Methode, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListModel<
    T extends AbstractTableData<U>, U, V extends TableModel<T, U>, W, X> {

  @Getter protected final V tableModel;

  protected AbstractListModel(V tableModel) {
    this.tableModel = tableModel;
  }

  public abstract W createCreateOrUpdateModel(SvmContext svmContext);

  public abstract W createCreateOrUpdateModel(SvmContext svmContext, int indexToBeUpdated);

  protected U getSelectedRow(int selectedIndex) {
    return tableModel.getRowAt(selectedIndex);
  }

  /**
   * @param idOfCreatedEntity ID der Entity, die in die Liste eingefügt werden muss
   * @return Listen-Index (Position in der Model-Liste) der Entity, die eingefügt wurde
   */
  public Optional<Integer> insertCreatedObjectToTableModel(int idOfCreatedEntity) {
    Optional<U> entityOptional = loadObjectFromDatabase(idOfCreatedEntity);
    return entityOptional.map(tableModel::insertRow);
  }

  /**
   * @param modelIndexOfRowOfUpdatedObject Listen-Index des Objekts, das neu geladen werden und in
   *     der Model-Liste ausgetauscht werden muss
   * @return true, wenn das neu zu ladende Objekt gefunden und ausgetauscht wurde, sonst false
   */
  public boolean replaceUpdatedObjectInTableModel(int modelIndexOfRowOfUpdatedObject) {
    U rowToBeReloaded = tableModel.getRowAt(modelIndexOfRowOfUpdatedObject);
    Optional<U> reloadedObjectOptional = reloadObjectFromDatabase(rowToBeReloaded);
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
  protected abstract Optional<U> loadObjectFromDatabase(int idOfEntityToBeLoaded);

  /**
   * @param objectToBeReloaded Listen-Element, das neu geladen werden muss
   * @return Optional des neu geladenen Objekts
   */
  protected abstract Optional<U> reloadObjectFromDatabase(U objectToBeReloaded);

  public abstract void reloadData();

  public abstract X eintragLoeschen(int indexToBeDeleted);

  public abstract String getListItemName();
}
