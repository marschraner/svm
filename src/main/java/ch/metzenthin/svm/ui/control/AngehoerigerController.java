package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
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
    private JLabel errLblGleicheAdresseWieSchueler;
    private JLabel errLblRechnungsempfaenger;
    private AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel) {
        super(angehoerigerModel);
        this.angehoerigerModel = angehoerigerModel;
        this.angehoerigerModel.addPropertyChangeListener(this);
        this.angehoerigerModel.addDisableFieldsListener(this);
        this.angehoerigerModel.addMakeErrorLabelsInvisibleListener(this);
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

    public void setErrLblGleicheAdresseWieSchueler(JLabel errLblGleicheAdresseWieSchueler) {
        this.errLblGleicheAdresseWieSchueler = errLblGleicheAdresseWieSchueler;
    }

    public void setErrLblRechnungsempfaenger(JLabel errLblRechnungsempfaenger) {
        this.errLblRechnungsempfaenger = errLblRechnungsempfaenger;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        System.out.println("AngehoerigerController PropertyChangeEvent '" + evt.getPropertyName() + "', oldValue='" + evt.getOldValue() + "', newValue='" + evt.getNewValue() + "'");
        if (checkIsFieldChange(Field.GLEICHE_ADRESSE_WIE_SCHUELER, evt)) {
            checkBoxGleicheAdresseWieSchueler.setSelected(angehoerigerModel.isGleicheAdresseWieSchueler());
        }
        else if (checkIsFieldChange(Field.RECHNUNGSEMPFAENGER, evt)) {
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
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            errLblGleicheAdresseWieSchueler.setVisible(true);
            errLblGleicheAdresseWieSchueler.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSEMPFAENGER)) {
            errLblRechnungsempfaenger.setVisible(true);
            errLblRechnungsempfaenger.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            checkBoxGleicheAdresseWieSchueler.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.RECHNUNGSEMPFAENGER)) {
            checkBoxRechnungsempfaenger.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            errLblGleicheAdresseWieSchueler.setVisible(false);
            checkBoxGleicheAdresseWieSchueler.setToolTipText(null);
        }
        if (fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            errLblRechnungsempfaenger.setVisible(false);
            checkBoxRechnungsempfaenger.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            checkBoxRechnungsempfaenger.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            checkBoxGleicheAdresseWieSchueler.setEnabled(!disable);
        }
    }

}
