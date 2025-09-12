package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.MaerchenDao;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateMaerchenCommand implements Command {

  private final MaerchenDao maerchenDao = new MaerchenDao();

  // input
  private final Maerchen maerchen;
  private final Maerchen maerchenOrigin;
  private final List<Maerchen> bereitsErfassteMaerchen;

  public SaveOrUpdateMaerchenCommand(
      Maerchen maerchen, Maerchen maerchenOrigin, List<Maerchen> bereitsErfassteMaerchen) {
    this.maerchen = maerchen;
    this.maerchenOrigin = maerchenOrigin;
    this.bereitsErfassteMaerchen = bereitsErfassteMaerchen;
  }

  @Override
  public void execute() {
    if (maerchenOrigin != null) {
      // Update von maerchenOrigin mit Werten von maerchen
      maerchenOrigin.copyAttributesFrom(maerchen);
      maerchenDao.save(maerchenOrigin);
    } else {
      Maerchen maerchenSaved = maerchenDao.save(maerchen);
      bereitsErfassteMaerchen.add(maerchenSaved);
    }
    Collections.sort(bereitsErfassteMaerchen);
  }
}
