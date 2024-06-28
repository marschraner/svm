package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;

/**
 * @author Hans Stamm
 */
public class AngehoerigerController extends PersonController {

    private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

    private JCheckBox checkBoxGleicheAdresseWieSchueler;
    private JCheckBox checkBoxRechnungsempfaenger;
    private JCheckBox checkBoxWuenschtEmails;
    private JLabel errLblGleicheAdresseWieSchueler;
    private JLabel errLblRechnungsempfaenger;
    private JLabel errLblWuenschtEmails;
    private final AngehoerigerModel angehoerigerModel;

    public AngehoerigerController(AngehoerigerModel angehoerigerModel, boolean defaultButtonEnabled) {
        super(angehoerigerModel, defaultButtonEnabled);
        this.angehoerigerModel = angehoerigerModel;
        this.angehoerigerModel.addPropertyChangeListener(this);
        this.angehoerigerModel.addDisableFieldsListener(this);
        this.angehoerigerModel.addMakeErrorLabelsInvisibleListener(this);
    }

    public void setCheckBoxGleicheAdresseWieSchueler(JCheckBox checkBoxAdresseSchuelerUebernehmen) {
        this.checkBoxGleicheAdresseWieSchueler = checkBoxAdresseSchuelerUebernehmen;
        this.checkBoxGleicheAdresseWieSchueler.addItemListener(e -> onGleicheAdresseWieSchuelerEvent());
    }

    public void setCheckBoxWuenschtEmails(JCheckBox checkBoxWuenschtEmails) {
        this.checkBoxWuenschtEmails = checkBoxWuenschtEmails;
        this.checkBoxWuenschtEmails.addItemListener(e -> onWuenschtEmailsEvent());
    }

    public void setCheckBoxRechnungsempfaenger(JCheckBox checkBoxRechnungsempfaenger) {
        this.checkBoxRechnungsempfaenger = checkBoxRechnungsempfaenger;
        this.checkBoxRechnungsempfaenger.addItemListener(e -> onRechnungsempfaengerEvent());
    }

    public void onGleicheAdresseWieSchuelerEvent() {
        LOGGER.trace("AngehoerigerController Event GleicheAdresseWieSchueler. Selected={}", checkBoxGleicheAdresseWieSchueler.isSelected());
        setModelGleicheAdresseWieSchueler();
    }

    private void setModelGleicheAdresseWieSchueler() {
        angehoerigerModel.setIsGleicheAdresseWieSchueler(checkBoxGleicheAdresseWieSchueler.isSelected());
    }

    public void setModelGleicheAdresseWieSchueler(boolean isGleicheAdresseWieSchueler) {
        angehoerigerModel.setIsGleicheAdresseWieSchueler(isGleicheAdresseWieSchueler);
    }

    private void onWuenschtEmailsEvent() {
        LOGGER.trace("AngehoerigerController Event WuenschtEmails. Selected={}", checkBoxWuenschtEmails.isSelected());
        setModelWuenschtEmails();
    }

    private void setModelWuenschtEmails() {
        angehoerigerModel.setWuenschtEmails(checkBoxWuenschtEmails.isSelected());
    }

    public void setModelWuenschtEmails(Boolean isWuenschtEmails) {
        angehoerigerModel.setWuenschtEmails(isWuenschtEmails);
    }

    private void onRechnungsempfaengerEvent() {
        LOGGER.trace("AngehoerigerController Event Rechnungsempfaenger. Selected={}", checkBoxRechnungsempfaenger.isSelected());
        setModelRechnungsempfaenger();
    }

    private void setModelRechnungsempfaenger() {
        angehoerigerModel.setIsRechnungsempfaenger(checkBoxRechnungsempfaenger.isSelected());
    }

    public void setErrLblGleicheAdresseWieSchueler(JLabel errLblGleicheAdresseWieSchueler) {
        this.errLblGleicheAdresseWieSchueler = errLblGleicheAdresseWieSchueler;
    }

    public void setErrLblWuenschtEmails(JLabel errLblWuenschtEmails) {
        this.errLblWuenschtEmails = errLblWuenschtEmails;
    }

    public void setErrLblRechnungsempfaenger(JLabel errLblRechnungsempfaenger) {
        this.errLblRechnungsempfaenger = errLblRechnungsempfaenger;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        LOGGER.trace("AngehoerigerController PropertyChangeEvent '{}', oldValue='{}', newValue='{}'", evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        if (checkIsFieldChange(Field.GLEICHE_ADRESSE_WIE_SCHUELER, evt)) {
            checkBoxGleicheAdresseWieSchueler.setSelected(angehoerigerModel.isGleicheAdresseWieSchueler());
        } else if (checkIsFieldChange(Field.WUENSCHT_EMAILS, evt) && checkBoxWuenschtEmails.isVisible()) {
            checkBoxWuenschtEmails.setSelected(angehoerigerModel.getWuenschtEmails() != null
                    && angehoerigerModel.getWuenschtEmails());
        } else if (checkIsFieldChange(Field.RECHNUNGSEMPFAENGER, evt)) {
            checkBoxRechnungsempfaenger.setSelected(angehoerigerModel.isRechnungsempfaenger());
        }
        super.doPropertyChange(evt);
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        LOGGER.trace("Validate field Rechnungsempfaenger");
        setModelRechnungsempfaenger();
        if (checkBoxWuenschtEmails.isVisible()) {
            LOGGER.trace("Validate field WuenschtEmails");
            setModelWuenschtEmails();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            errLblGleicheAdresseWieSchueler.setVisible(true);
            errLblGleicheAdresseWieSchueler.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WUENSCHT_EMAILS)
                && checkBoxWuenschtEmails.isVisible()) {
            // Error 3001 nur anzeigen, falls Nachname nicht leer (d.h. Person wurde erfasst)
            if (e.getErrorId() != 3001 || checkNotEmpty(angehoerigerModel.getNachname())) {
                errLblWuenschtEmails.setVisible(true);
                errLblWuenschtEmails.setText(e.getMessage());
            }
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
        if (e.getAffectedFields().contains(Field.WUENSCHT_EMAILS)
                && checkBoxWuenschtEmails.isVisible()) {
            checkBoxWuenschtEmails.setToolTipText(e.getMessage());
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
        if (fields.contains(Field.WUENSCHT_EMAILS)
                && checkBoxWuenschtEmails.isVisible()) {
            errLblWuenschtEmails.setVisible(false);
            checkBoxWuenschtEmails.setToolTipText(null);
        }
        if (fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            errLblRechnungsempfaenger.setVisible(false);
            checkBoxRechnungsempfaenger.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.GLEICHE_ADRESSE_WIE_SCHUELER)) {
            checkBoxGleicheAdresseWieSchueler.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WUENSCHT_EMAILS)) {
            checkBoxWuenschtEmails.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            checkBoxRechnungsempfaenger.setEnabled(!disable);
        }
    }

}
