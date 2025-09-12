package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteSchuelerCodeCommand implements Command {

  public enum Result {
    CODE_VON_SCHUELER_REFERENZIERT,
    LOESCHEN_ERFOLGREICH
  }

  private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

  // input
  private final List<SchuelerCode> schuelerCodes;
  private final int indexCodeToBeDeleted;

  // output
  private Result result;

  public DeleteSchuelerCodeCommand(List<SchuelerCode> schuelerCodes, int indexCodeToBeDeleted) {
    this.schuelerCodes = schuelerCodes;
    this.indexCodeToBeDeleted = indexCodeToBeDeleted;
  }

  @Override
  public void execute() {
    SchuelerCode schuelerCodeToBeDeleted = schuelerCodes.get(indexCodeToBeDeleted);
    if (!schuelerCodeToBeDeleted.getSchueler().isEmpty()) {
      result = Result.CODE_VON_SCHUELER_REFERENZIERT;
      return;
    }
    schuelerCodeDao.remove(schuelerCodeToBeDeleted);
    schuelerCodes.remove(indexCodeToBeDeleted);
    result = Result.LOESCHEN_ERFOLGREICH;
  }

  public Result getResult() {
    return result;
  }
}
