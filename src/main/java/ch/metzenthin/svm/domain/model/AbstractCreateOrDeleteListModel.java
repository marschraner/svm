package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.service.result.SubmitDialogResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;

/**
 * @param <T> Typ der Table-Data-Rows, z.B. Kursort
 * @param <U> Typ des Table-Models, z.B. TableModel<T>
 * @param <V> Typ des Resultats der Löschen-Methode, z.B. DeleteKursortResult
 * @author Hans Stamm
 */
public abstract class AbstractCreateOrDeleteListModel<
        T, U extends TableModel<? extends AbstractTableData<T>, T>, V extends SubmitDialogResult>
    extends AbstractListModel<T, U, V> {

  protected AbstractCreateOrDeleteListModel(U tableModel) {
    super(tableModel);
  }

  public abstract Object createCreateModel(SvmContext svmContext);
}
