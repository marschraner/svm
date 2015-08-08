package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MaerchenErfassenModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MaerchenErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MaerchenErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private MaerchenErfassenModel maerchenErfassenModel;
    private final SvmContext svmContext;
    private boolean isBearbeiten;
    private JDialog maerchenErfassenDialog;
    private JSpinner spinnerSchuljahre;
    private JTextField txtBezeichnung;
    private JLabel errLblBezeichnung;
    private JButton btnSpeichern;

    public MaerchenErfassenController(SvmContext svmContext, MaerchenErfassenModel maerchenErfassenModel, boolean isBearbeiten) {
        super(maerchenErfassenModel);
        this.svmContext = svmContext;
        this.maerchenErfassenModel = maerchenErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.maerchenErfassenModel.addPropertyChangeListener(this);
        this.maerchenErfassenModel.addDisableFieldsListener(this);
        this.maerchenErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.maerchenErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMaerchenErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        maerchenErfassenModel.initializeCompleted();
    }

    public void setMaerchenErfassenDialog(JDialog maerchenErfassenDialog) {
        // call onCancel() when cross is clicked
        this.maerchenErfassenDialog = maerchenErfassenDialog;
        maerchenErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        maerchenErfassenDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setSpinnerSchuljahre(JSpinner spinnerSchuljahre) {
        this.spinnerSchuljahre = spinnerSchuljahre;
        spinnerSchuljahre.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onSchuljahrSelected();
            }
        });
        if (!isBearbeiten) {
            initSchuljahr();
        }
    }

    private void initSchuljahr() {
        Calendar today = new GregorianCalendar();
        int schuljahr1;
        if (today.get(Calendar.MONTH) <= Calendar.JANUARY) {
            schuljahr1 = today.get(Calendar.YEAR) - 1;
        } else {
            schuljahr1 = today.get(Calendar.YEAR);
        }
        int schuljahr2 = schuljahr1 + 1;
        String initSchuljahr = schuljahr1 + "/" + schuljahr2;
        try {
            maerchenErfassenModel.setSchuljahr(initSchuljahr);
        } catch (SvmValidationException ignore) {
        }
    }

    private void onSchuljahrSelected() {
        LOGGER.trace("PersonController Event Schuljahre selected =" + spinnerSchuljahre.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerSchuljahre.getValue(), maerchenErfassenModel.getSchuljahr());
        try {
            setModelSchuljahr();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelSchuljahr() throws SvmValidationException {
        makeErrorLabelInvisible(Field.SCHULJAHR);
        try {
            maerchenErfassenModel.setSchuljahr((String) spinnerSchuljahre.getValue());
        } catch (SvmValidationException e) {
            LOGGER.trace("PersonController setModelSchuljahr Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBezeichnung(JTextField txtBezeichnung) {
        this.txtBezeichnung = txtBezeichnung;
        this.txtBezeichnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBezeichnungEvent(true);
            }
        });
        this.txtBezeichnung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBezeichnungEvent(false);
            }
        });
    }

    private void onBezeichnungEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MaerchenErfassenController Event Bezeichnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBezeichnung.getText(), maerchenErfassenModel.getBezeichnung());
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
            maerchenErfassenModel.setBezeichnung(txtBezeichnung.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaerchenErfassenController setModelBezeichnung RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBezeichnung.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaerchenErfassenController setModelBezeichnung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblBezeichnung(JLabel errLblBezeichnung) {
        this.errLblBezeichnung = errLblBezeichnung;
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        if (isModelValidationMode()) {
            btnSpeichern.setEnabled(false);
        }
        this.btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    private void onSpeichern() {
        if (!isModelValidationMode() && !validateOnSpeichern()) {
            btnSpeichern.setFocusPainted(false);
            return;
        }

        if (maerchenErfassenModel.checkMaerchenBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(maerchenErfassenDialog, "Märchen für Schuljahr " + maerchenErfassenModel.getMaerchen().getSchuljahr() + " bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            maerchenErfassenModel.speichern(svmContext.getSvmModel());
            maerchenErfassenDialog.dispose();
        }
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    private void onAbbrechen() {
        maerchenErfassenDialog.dispose();
    }

    private void onMaerchenErfassenModelCompleted(boolean completed) {
        LOGGER.trace("MaerchenErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.SCHULJAHR, evt)) {
            spinnerSchuljahre.setValue(maerchenErfassenModel.getSchuljahr());
        } else if (checkIsFieldChange(Field.BEZEICHNUNG, evt)) {
            txtBezeichnung.setText(maerchenErfassenModel.getBezeichnung());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtBezeichnung.isEnabled()) {
            LOGGER.trace("Validate field Maerchenbeginn");
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
    public void disableFields(boolean disable, Set<Field> fields) {}

}
