package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.model.ModelFactory;
import ch.metzenthin.svm.domain.model.SvmModel;

import javax.swing.*;

/**
 * @author Hans Stamm
 */
public class SvmContext {

    private final ModelFactory modelFactory;
    private final SvmModel svmModel;
    private JRootPane rootPaneJFrame;

    public SvmContext(ModelFactory modelFactory, SvmModel svmModel) {
        this.modelFactory = modelFactory;
        this.svmModel = svmModel;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public SvmModel getSvmModel() {
        return svmModel;
    }

    public JRootPane getRootPaneJFrame() {
        return rootPaneJFrame;
    }

    public void setRootPaneJFrame(JRootPane rootPaneJFrame) {
        this.rootPaneJFrame = rootPaneJFrame;
    }
}
