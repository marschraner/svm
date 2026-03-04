package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.EntityStillReferencedException;
import ch.metzenthin.svm.persistence.entities.*;
import ch.metzenthin.svm.service.CodeService;
import ch.metzenthin.svm.service.result.DeleteCodeResult;
import ch.metzenthin.svm.ui.componentmodel.TableModel;
import jakarta.persistence.OptimisticLockException;
import java.util.List;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * @param <T> Code-Typ, z.B. SchuelerCode
 * @author Hans Stamm
 */
public abstract class CodeListModel<T extends Code>
    extends AbstractListModel<CodeTableData<T>, T, CreateOrUpdateCodeModel, DeleteCodeResult> {

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
      service.deleteCode(codeToBeDeleted);
      deleteCodeResult = DeleteCodeResult.LOESCHEN_ERFOLGREICH;
    } catch (EntityStillReferencedException e) {
      deleteCodeResult = DeleteCodeResult.CODE_REFERENZIERT;
    } catch (OptimisticLockException | OptimisticLockingFailureException e) {
      deleteCodeResult = DeleteCodeResult.CODE_DURCH_ANDEREN_BENUTZER_VERAENDERT;
    }
    return deleteCodeResult;
  }

  @Override
  public void reloadData() {
    List<T> codeList = service.findAllCodes();
    tableModel.setData(codeList);
  }
}
