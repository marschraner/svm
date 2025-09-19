package ch.metzenthin.svm.domain.commands;

import ch.metzenthin.svm.persistence.daos.SchuelerCodeDao;
import ch.metzenthin.svm.persistence.entities.SchuelerCode;
import java.util.Collections;
import java.util.List;

/**
 * @author Martin Schraner
 */
public class SaveOrUpdateSchuelerCodeCommand implements Command {

  private final SchuelerCodeDao schuelerCodeDao = new SchuelerCodeDao();

  // input
  private final SchuelerCode schuelerCode;
  private final SchuelerCode schuelerCodeOrigin;
  private final List<SchuelerCode> bereitsErfassteSchuelerCodes;

  public SaveOrUpdateSchuelerCodeCommand(
      SchuelerCode schuelerCode,
      SchuelerCode schuelerCodeOrigin,
      List<SchuelerCode> bereitsErfassteSchuelerCodes) {
    this.schuelerCode = schuelerCode;
    this.schuelerCodeOrigin = schuelerCodeOrigin;
    this.bereitsErfassteSchuelerCodes = bereitsErfassteSchuelerCodes;
  }

  @Override
  public void execute() {
    if (schuelerCodeOrigin != null) {
      // Update von schuelerCodeOrigin mit Werten von schuelerCode
      schuelerCodeOrigin.copyAttributesFrom(schuelerCode);
      schuelerCodeDao.save(schuelerCodeOrigin);
    } else {
      SchuelerCode schuelerCodeSaved = schuelerCodeDao.save(schuelerCode);
      bereitsErfassteSchuelerCodes.add(schuelerCodeSaved);
    }
    Collections.sort(bereitsErfassteSchuelerCodes);
  }
}
