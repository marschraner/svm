package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.KurstypErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class KurstypErfassenController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(KurstypErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final KurstypenTableModel kurstypenTableModel;
    private final KurstypErfassenModel kurstypErfassenModel;
    private final boolean isBearbeiten;
    private final boolean defaultButtonEnabled;
    private final SvmContext svmContext;
    private JDialog kurstypErfassenDialog;
    private JTextField txtBezeichnung;
    private JCheckBox checkBoxSelektierbar;
    private JLabel errLblBezeichnung;
    private JButton btnSpeichern;

    public KurstypErfassenController(SvmContext svmContext, KurstypenTableModel kurstypenTableModel, KurstypErfassenModel kurstypErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(kurstypErfassenModel);
        this.svmContext = svmContext;
        this.kurstypenTableModel = kurstypenTableModel;
        this.kurstypErfassenModel = kurstypErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.kurstypErfassenModel.addPropertyChangeListener(this);
        this.kurstypErfassenModel.addDisableFieldsListener(this);
        this.kurstypErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kurstypErfassenModel.addCompletedListener(this::onKurstypErfassenModelCompleted);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kurstypErfassenModel.initializeCompleted();
    }

    public void setKurstypErfassenDialog(JDialog kurstypErfassenDialog) {
        // call onCancel() when cross is clicked
        this.kurstypErfassenDialog = kurstypErfassenDialog;
        kurstypErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kurstypErfassenDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @SuppressWarnings("DuplicatedCode")
    public void setTxtBezeichnung(JTextField txtBezeichnung) {
        this.txtBezeichnung = txtBezeichnung;
        if (!defaultButtonEnabled) {
            this.txtBezeichnung.addActionListener(e -> onBezeichnungEvent(true));
        }
        this.txtBezeichnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBezeichnungEvent(false);
            }
        });
    }

    private void onBezeichnungEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KurstypErfassenController Event Bezeichnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBezeichnung.getText(), kurstypErfassenModel.getBezeichnung());
        try {
            setModelBezeichnung(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBezeichnung(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEZEICHNUNG);
        try {
            kurstypErfassenModel.setBezeichnung(txtBezeichnung.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KurstypErfassenController setModelBezeichnung RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBezeichnung.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KurstypErfassenController setModelBezeichnung Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxSelektierbar(JCheckBox checkBoxSelektierbar) {
        this.checkBoxSelektierbar = checkBoxSelektierbar;
        // Selektierbar als Default-Wert
        if (!isBearbeiten) {
            kurstypErfassenModel.setSelektierbar(true);
        }
        this.checkBoxSelektierbar.addItemListener(e -> onSelektierbarEvent());
    }

    private void setModelSelektierbar() {
        kurstypErfassenModel.setSelektierbar(checkBoxSelektierbar.isSelected());
    }

    private void onSelektierbarEvent() {
        LOGGER.trace("AngehoerigerController Event Selektierbar. Selected={}", checkBoxSelektierbar.isSelected());
        setModelSelektierbar();
    }

    public void setErrLblBezeichnung(JLabel errLblBezeichnung) {
        this.errLblBezeichnung = errLblBezeichnung;
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        if (isModelValidationMode()) {
            btnSpeichern.setEnabled(false);
        }
        this.btnSpeichern.addActionListener(e -> onSpeichern());
    }

    private void onSpeichern() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnSpeichern.setFocusPainted(false);
            return;
        }
        if (kurstypErfassenModel.checkKurstypBezeichnungBereitsInVerwendung(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(kurstypErfassenDialog, "Bezeichnung bereits in Verwendung.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            kurstypErfassenModel.speichern(svmContext.getSvmModel(), kurstypenTableModel);
            kurstypErfassenDialog.dispose();
        }
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        kurstypErfassenDialog.dispose();
    }

    private void onKurstypErfassenModelCompleted(boolean completed) {
        LOGGER.trace("KurstypErfassenModel completed={}", completed);
        if (completed) {
            btnSpeichern.setToolTipText(null);
            btnSpeichern.setEnabled(true);
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
            txtBezeichnung.setText(kurstypErfassenModel.getBezeichnung());
        } else if (checkIsFieldChange(Field.SELEKTIERBAR, evt)) {
            checkBoxSelektierbar.setSelected(kurstypErfassenModel.isSelektierbar());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtBezeichnung.isEnabled()) {
            LOGGER.trace("Validate field Bezeichnung");
            setModelBezeichnung(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
            errLblBezeichnung.setVisible(true);
            errLblBezeichnung.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.BEZEICHNUNG)) {
            txtBezeichnung.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEZEICHNUNG)) {
            errLblBezeichnung.setVisible(false);
            txtBezeichnung.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        // Keine zu deaktivierenden Felder
    }

}
