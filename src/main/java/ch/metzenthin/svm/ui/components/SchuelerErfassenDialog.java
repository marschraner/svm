package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.SchuelerErfassenSaveResult;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public abstract class SchuelerErfassenDialog extends JDialog {

    public abstract SchuelerErfassenSaveResult getResult();

}
