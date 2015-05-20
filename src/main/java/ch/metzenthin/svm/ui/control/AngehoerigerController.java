package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.model.AngehoerigerModel;

import java.beans.PropertyChangeEvent;

/**
 * @author Hans Stamm
 */
public class AngehoerigerController extends PersonController {

    private AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel) {
        super(angehoerigerModel);
        this.angehoerigerModel = angehoerigerModel;
        this.angehoerigerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("AngehoerigerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        super.propertyChange(evt);
    }

}
