package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import lombok.Getter;

/**
 * @param <T> Table-Data-Typ, z.B. KursortTableData
 * @param <U> Typ der Table-Data-Rows, z.B. Kursort
 * @param <V> Typ des CreateOrUpdate-Models, z.B. CreateOrUpdateKursortModel
 * @param <W> Typ des Resultats der Löschen-Methode, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractListModel<T extends AbstractTableData<U>, U, V, W> {

  @Getter protected final TableModel<T, U> tableModel;

  protected AbstractListModel(TableModel<T, U> tableModel) {
    this.tableModel = tableModel;
  }

  public abstract V createCreateOrUpdateModel(SvmContext svmContext);

  public abstract V createCreateOrUpdateModel(SvmContext svmContext, int indexToBeUpdated);

  public abstract W eintragLoeschen(int indexToBeDeleted);

  public abstract void reloadData();

  protected U getSelectedRow(int selectedIndex) {
    return tableModel.getRowAt(selectedIndex);
  }

  public abstract String getListItemName();
}
