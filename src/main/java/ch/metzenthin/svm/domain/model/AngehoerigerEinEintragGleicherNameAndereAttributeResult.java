package ch.metzenthin.svm.domain.model;

import ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;

/**
 * @author Hans Stamm
 */
public class AngehoerigerEinEintragGleicherNameAndereAttributeResult
    extends SchuelerErfassenSaveResult {

  private final Angehoeriger angehoerigerFoundInDatabase;
  private final ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt;

  public AngehoerigerEinEintragGleicherNameAndereAttributeResult(
      Angehoeriger angehoerigerFoundInDatabase,
      ValidateSchuelerCommand.AngehoerigenArt angehoerigenArt,
      ValidateSchuelerCommand.Result result) {
    super(result);
    this.angehoerigerFoundInDatabase = angehoerigerFoundInDatabase;
    this.angehoerigenArt = angehoerigenArt;
  }

  @Override
  public void accept(SchuelerErfassenSaveResultVisitor visitor) {
    visitor.visit(this);
  }

  public Angehoeriger getAngehoerigerFoundInDatabase() {
    return angehoerigerFoundInDatabase;
  }

  public ValidateSchuelerCommand.AngehoerigenArt getAngehoerigenArt() {
    return angehoerigenArt;
  }
}
