package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.SchuelerErfassenSaveResult;
import javax.swing.*;

/**
 * @author Hans Stamm
 */
public abstract class SchuelerErfassenDialog extends JDialog {

  private transient SchuelerErfassenSaveResult result;
  private boolean isAbbrechen = false;

  public SchuelerErfassenSaveResult getResult() {
    return result;
  }

  protected void setResult(SchuelerErfassenSaveResult result) {
    this.result = result;
  }

  public boolean isAbbrechen() {
    return isAbbrechen;
  }

  protected void abbrechen() {
    result = null;
    isAbbrechen = true;
  }
}
