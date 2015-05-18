package ch.metzenthin.svm.common;

import ch.metzenthin.svm.domain.ModelFactory;

/**
 * @author Hans Stamm
 */
public class SvmContext {

    private final ModelFactory modelFactory;

    public SvmContext(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

}
