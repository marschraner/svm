package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

/**
 * @author Hans Stamm
 */
public class AngehoerigerController extends PersonController {

    private JCheckBox checkBoxRechnungsempfaenger;

    private AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel) {
        super(angehoerigerModel);
        this.angehoerigerModel = angehoerigerModel;
        this.angehoerigerModel.addPropertyChangeListener(this);
    }

    public void setCheckBoxRechnungsempfaenger(JCheckBox checkBoxRechnungsempfaenger) {
        this.checkBoxRechnungsempfaenger = checkBoxRechnungsempfaenger;
        this.checkBoxRechnungsempfaenger.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onRechnungsempfaengerEvent();
            }
        });
    }

    private void onRechnungsempfaengerEvent() {
        System.out.println("AngehoerigerController Event Rechnungsempfaenger. Selected=" + checkBoxRechnungsempfaenger.isSelected());
        setModelRechnungsempfaenger();
    }

    private boolean setModelRechnungsempfaenger() {
        angehoerigerModel.setIsRechnungsempfaenger(checkBoxRechnungsempfaenger.isSelected());
        return true;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        System.out.println("AngehoerigerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        if ("Rechnungsempfaenger".equals(evt.getPropertyName())) {
            checkBoxRechnungsempfaenger.setSelected(angehoerigerModel.isRechnungsempfaenger());
        }
        super.doPropertyChange(evt);
    }

    @Override
    boolean validateFields() {
        if (!super.validateFields()) {
            return false;
        }
        System.out.println("Validate field Rechnungsempfaenger");
        if (!setModelRechnungsempfaenger()) {
            return false;
        }
        return true;
    }

    @Override
    void show(SvmValidationException e) {
        super.show(e);
        // todo $$$
    }

}
