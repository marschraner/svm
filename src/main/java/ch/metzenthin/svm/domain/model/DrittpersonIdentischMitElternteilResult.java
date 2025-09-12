package ch.metzenthin.svm.domain.model;

import static ch.metzenthin.svm.domain.commands.ValidateSchuelerCommand.Result.DRITTPERSON_IDENTISCH_MIT_ELTERNTEIL;

/**
 * @author Hans Stamm
 */
public class DrittpersonIdentischMitElternteilResult extends SchuelerErfassenSaveResult {

  private final String errorMessage;

  public DrittpersonIdentischMitElternteilResult(String errorMessage) {
    super(DRITTPERSON_IDENTISCH_MIT_ELTERNTEIL);
    this.errorMessage = errorMessage;
  }

  @Override
  public void accept(SchuelerErfassenSaveResultVisitor visitor) {
    visitor.visit(this);
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
