package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.FieldName;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Set;

/**
 * @author Hans Stamm
 */
public class AngehoerigerController extends PersonController {

    private JCheckBox checkBoxGleicheAdresseWieSchueler;
    private JCheckBox checkBoxRechnungsempfaenger;

    private AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel) {
        super(angehoerigerModel);
        this.angehoerigerModel = angehoerigerModel;
        this.angehoerigerModel.addPropertyChangeListener(this);
        this.angehoerigerModel.addDisableFieldsListener(this);
    }

    public void setCheckBoxGleicheAdresseWieSchueler(JCheckBox checkBoxAdresseSchuelerUebernehmen) {
        this.checkBoxGleicheAdresseWieSchueler = checkBoxAdresseSchuelerUebernehmen;
        this.checkBoxGleicheAdresseWieSchueler.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onGleicheAdresseWieSchuelerEvent();
            }
        });
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

    public void onGleicheAdresseWieSchuelerEvent() {
        System.out.println("AngehoerigerController Event GleicheAdresseWieSchueler. Selected=" + checkBoxGleicheAdresseWieSchueler.isSelected());
        setModelGleicheAdresseWieSchueler();
    }

    private void setModelGleicheAdresseWieSchueler() {
        angehoerigerModel.setIsGleicheAdresseWieSchueler(checkBoxGleicheAdresseWieSchueler.isSelected());
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
        if (checkIsFieldNameChange(FieldName.GLEICHE_ADRESSE_WIE_SCHUELER, evt)) {
            checkBoxGleicheAdresseWieSchueler.setSelected(angehoerigerModel.isGleicheAdresseWieSchueler());
        }
        else if (checkIsFieldNameChange(FieldName.RECHNUNGSEMPFAENGER, evt)) {
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
    public void disableFields(boolean disable, Set<FieldName> fieldNames) {
        super.disableFields(disable, fieldNames);
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.RECHNUNGSEMPFAENGER)) {
            checkBoxRechnungsempfaenger.setEnabled(!disable);
        }
        if (fieldNames.contains(FieldName.ALLE) || fieldNames.contains(FieldName.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            checkBoxGleicheAdresseWieSchueler.setEnabled(!disable);
        }
    }
}
