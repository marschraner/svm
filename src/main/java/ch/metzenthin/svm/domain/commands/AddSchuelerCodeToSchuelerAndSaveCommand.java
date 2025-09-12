package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.Schueler;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;

/**
 * @author Martin Schraner
 */
public class AddSchuelerCodeToSchuelerAndSaveCommand implements Command {

  private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

  // input
  private final SchuelerCode schuelerCode;
  private final Schueler schueler;

  // output
  private Schueler schuelerUpdated;

  public AddSchuelerCodeToSchuelerAndSaveCommand(SchuelerCode schuelerCode, Schueler schueler) {
    this.schuelerCode = schuelerCode;
    this.schueler = schueler;
  }

  @Override
  public void execute() {
    // SchuelerCode nachladen wegen Lazy-Loading
    SchuelerCode schuelerCodeToBeAdded = schuelerCodeDao.findById(schuelerCode.getCodeId());
    schuelerUpdated = schuelerCodeDao.addToSchuelerAndSave(schuelerCodeToBeAdded, schueler);
  }

  public Schueler getSchuelerUpdated() {
    return schuelerUpdated;
  }
}
