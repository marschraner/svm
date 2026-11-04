package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @param <T> Code-Typ, z.B. SchuelerCode
 * @author Hans Stamm
 */
public abstract class CodeListModel<T extends Code>
    extends AbstractListModel<T, TableModel<CodeTableData<T>, T>, DeleteCodeResult> {

  private final CodeService<T> service;

  protected CodeListModel(TableModel<CodeTableData<T>, T> tableModel, CodeService<T> service) {
    super(tableModel);
    this.service = service;
  }

  @Override
  public DeleteCodeResult eintragLoeschen(int indexOfCodeToBeDeleted) {
    T codeToBeDeleted = getSelectedRow(indexOfCodeToBeDeleted);
    DeleteCodeResult deleteCodeResult;
    try {
      deleteCodeResult = service.deleteCode(codeToBeDeleted);
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @Override
  protected Optional<T> loadObjectFromDatabase(int codeId) {
    return service.findCodeById(codeId);
  }

  @Override
  protected Optional<T> reloadObjectFromDatabase(T code) {
    return service.findCodeById(code.getCodeId());
  }

  @Override
  public void reloadData() {
    List<T> codeList = service.findAllCodes();
    tableModel.setData(codeList);
  }
}
