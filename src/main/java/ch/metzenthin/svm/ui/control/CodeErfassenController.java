package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CodeErfassenModel;
import ch.metzenthin.svm.domain.model.CompletedListener;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class CodeErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(CodeErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private CodeErfassenModel codeErfassenModel;
    private final SvmContext svmContext;
    private JDialog codeErfassenDialog;
    private JTextField txtKuerzel;
    private JTextField txtBeschreibung;
    private JLabel errLblKuerzel;
    private JLabel errLblBeschreibung;
    private JButton btnSpeichern;

    public CodeErfassenController(SvmContext svmContext, CodeErfassenModel codeErfassenModel) {
        super(codeErfassenModel);
        this.svmContext = svmContext;
        this.codeErfassenModel = codeErfassenModel;
        this.codeErfassenModel.addPropertyChangeListener(this);
        this.codeErfassenModel.addDisableFieldsListener(this);
        this.codeErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.codeErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onCodeErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        codeErfassenModel.initializeCompleted();
    }

    public void setCodeErfassenDialog(JDialog codeErfassenDialog) {
        // call onCancel() when cross is clicked
        this.codeErfassenDialog = codeErfassenDialog;
        codeErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        codeErfassenDialog.addWindowListener(new WindowAdapter() {
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

    public void setTxtKuerzel(JTextField txtKuerzel) {
        this.txtKuerzel = txtKuerzel;
        this.txtKuerzel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKuerzelEvent(true);
            }
        });
        this.txtKuerzel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onKuerzelEvent(false);
            }
        });
    }

    private void onKuerzelEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("CodeErfassenController Event Kuerzel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtKuerzel.getText(), codeErfassenModel.getKuerzel());
        try {
            setModelKuerzel(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelKuerzel(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.KUERZEL);
        try {
            codeErfassenModel.setKuerzel(txtKuerzel.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("CodeErfassenController setModelKuerzel RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtKuerzel.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("CodeErfassenController setModelKuerzel Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBeschreibung(JTextField txtBeschreibung) {
        this.txtBeschreibung = txtBeschreibung;
        this.txtBeschreibung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBeschreibungEvent(true);
            }
        });
        this.txtBeschreibung.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBeschreibungEvent(false);
            }
        });
    }

    private void onBeschreibungEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("CodeErfassenController Event Beschreibung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBeschreibung.getText(), codeErfassenModel.getBeschreibung());
        try {
            setModelBeschreibung(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBeschreibung(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BESCHREIBUNG);
        try {
            codeErfassenModel.setBeschreibung(txtBeschreibung.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("CodeErfassenController setModelBeschreibung RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBeschreibung.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("CodeErfassenController setModelBeschreibung Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblKuerzel(JLabel errLblKuerzel) {
        this.errLblKuerzel = errLblKuerzel;
    }

    public void setErrLblBeschreibung(JLabel errLblBeschreibung) {
        this.errLblBeschreibung = errLblBeschreibung;
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
        if (codeErfassenModel.checkCodeKuerzelBereitsInVerwendung(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(null, "Kürzel bereits in Verwendung.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            codeErfassenModel.speichern(svmContext.getSvmModel());
            codeErfassenDialog.dispose();
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
        codeErfassenDialog.dispose();
    }

    private void onCodeErfassenModelCompleted(boolean completed) {
        LOGGER.trace("CodeErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.KUERZEL, evt)) {
            txtKuerzel.setText(codeErfassenModel.getKuerzel());
        }
        else if (checkIsFieldChange(Field.BESCHREIBUNG, evt)) {
            txtBeschreibung.setText(codeErfassenModel.getBeschreibung());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtKuerzel.isEnabled()) {
            LOGGER.trace("Validate field Kuerzel");
            setModelKuerzel(true);
        }
        if (txtBeschreibung.isEnabled()) {
            LOGGER.trace("Validate field Beschreibung");
            setModelBeschreibung(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.KUERZEL)) {
            errLblKuerzel.setVisible(true);
            errLblKuerzel.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
            errLblBeschreibung.setVisible(true);
            errLblBeschreibung.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.KUERZEL)) {
            txtKuerzel.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BESCHREIBUNG)) {
            txtBeschreibung.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.KUERZEL)) {
            errLblKuerzel.setVisible(false);
            txtKuerzel.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BESCHREIBUNG)) {
            errLblBeschreibung.setVisible(false);
            txtBeschreibung.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
