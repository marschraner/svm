package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * @author Martin Schraner
 */
public class RemoveSchuelerCodeFromSchuelerCommand implements Command {

  private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

  // input
  private final SchuelerCode schuelerCodeToBeDeleted;
  private final Schueler schueler;

  // output
  private Schueler schuelerUpdated;

  public RemoveSchuelerCodeFromSchuelerCommand(
      SchuelerCode schuelerCodeToBeDeleted, Schueler schueler) {
    this.schuelerCodeToBeDeleted = schuelerCodeToBeDeleted;
    this.schueler = schueler;
  }

  @Override
  public void execute() {
    schuelerUpdated =
        schuelerCodeDao.removeFromSchuelerAndUpdate(schuelerCodeToBeDeleted, schueler);
  }

  public Schueler getSchuelerUpdated() {
    return schuelerUpdated;
  }
}
