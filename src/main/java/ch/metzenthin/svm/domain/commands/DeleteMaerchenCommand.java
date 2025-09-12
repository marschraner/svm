package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class DeleteMaerchenCommand implements Command {

  public enum Result {
    MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT,
    LOESCHEN_ERFOLGREICH
  }

  private final MaerchenDao maerchenDao = new MaerchenDao();

  // input
  private final List<Maerchen> maerchens;
  private final int indexMaerchenToBeDeleted;

  // output
  private Result result;

  public DeleteMaerchenCommand(List<Maerchen> maerchens, int indexMaerchenToBeDeleted) {
    this.maerchens = maerchens;
    this.indexMaerchenToBeDeleted = indexMaerchenToBeDeleted;
  }

  @Override
  public void execute() {
    Maerchen maerchenToBeDeleted = maerchens.get(indexMaerchenToBeDeleted);
    if (!maerchenToBeDeleted.getMaercheneinteilungen().isEmpty()) {
      result = Result.MAERCHEN_VON_MAERCHENEINTEILUNGEN_REFERENZIERT;
      return;
    }
    maerchenDao.remove(maerchenToBeDeleted);
    maerchens.remove(indexMaerchenToBeDeleted);
    result = Result.LOESCHEN_ERFOLGREICH;
  }

  public Result getResult() {
    return result;
  }
}
