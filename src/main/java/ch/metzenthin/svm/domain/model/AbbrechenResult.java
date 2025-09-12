package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.ABBRECHEN;

/**
 * @author Martin Schraner
 */
public class AbbrechenResult extends SchuelerErfassenSaveResult {

  public AbbrechenResult() {
    super(ABBRECHEN);
  }

  @Override
  public void accept(SchuelerErfassenSaveResultVisitor visitor) {
    visitor.visit(this);
  }
}
