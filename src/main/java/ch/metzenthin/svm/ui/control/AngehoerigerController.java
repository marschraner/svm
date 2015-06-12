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
        this.angehoerigerModel.addDisableFieldsListener(this);
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

    private void setModelRechnungsempfaenger() {
        angehoerigerModel.setIsRechnungsempfaenger(checkBoxRechnungsempfaenger.isSelected());
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
    void validateFields() throws SvmValidationException {
        super.validateFields();
        System.out.println("Validate field Rechnungsempfaenger");
        setModelRechnungsempfaenger();
    }

    @Override
    void show(SvmValidationException e) {
        super.show(e);
        // todo $$$
    }

    @Override
    public void disableFields(boolean disable) {
        super.disableFields(disable);
        if (disable) {
            checkBoxRechnungsempfaenger.setEnabled(false);
            System.out.println("Disable Angehöriger Panel");
        } else {
            checkBoxRechnungsempfaenger.setEnabled(true);
            System.out.println("Enable Angehöriger Panel");
        }
    }
}
