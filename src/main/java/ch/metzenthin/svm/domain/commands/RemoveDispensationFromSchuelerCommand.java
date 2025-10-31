package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.DispensationDao;
import ch.metzenthin.svm.persistence.entities.Schueler;

/**
 * @author Martin Schraner
 */
public class RemoveDispensationFromSchuelerCommand implements Command {

  private final DispensationDao dispensationDao = new DispensationDao();

  // input
  private final int indexDispensationToBeDeleted;
  private final Schueler schueler;

  // output
  private Schueler schuelerUpdated;

  public RemoveDispensationFromSchuelerCommand(
      int indexDispensationToBeDeleted, Schueler schueler) {
    this.indexDispensationToBeDeleted = indexDispensationToBeDeleted;
    this.schueler = schueler;
  }

  @Override
  public void execute() {
    schuelerUpdated =
        dispensationDao.removeFromSchuelerAndUpdate(
            schueler.getDispensationen().get(indexDispensationToBeDeleted), schueler);
  }

  public Schueler getSchuelerUpdated() {
    return schuelerUpdated;
  }
}
