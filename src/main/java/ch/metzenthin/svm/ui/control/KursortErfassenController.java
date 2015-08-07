package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.KursortErfassenModel;
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
public class KursortErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KursortErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private KursortErfassenModel kursortErfassenModel;
    private final SvmContext svmContext;
    private JDialog kursortErfassenDialog;
    private JTextField txtBezeichnung;
    private JLabel errLblBezeichnung;
    private JButton btnSpeichern;

    public KursortErfassenController(SvmContext svmContext, KursortErfassenModel kursortErfassenModel) {
        super(kursortErfassenModel);
        this.svmContext = svmContext;
        this.kursortErfassenModel = kursortErfassenModel;
        this.kursortErfassenModel.addPropertyChangeListener(this);
        this.kursortErfassenModel.addDisableFieldsListener(this);
        this.kursortErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kursortErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKursortErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kursortErfassenModel.initializeCompleted();
    }

    public void setKursortErfassenDialog(JDialog kursortErfassenDialog) {
        // call onCancel() when cross is clicked
        this.kursortErfassenDialog = kursortErfassenDialog;
        kursortErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kursortErfassenDialog.addWindowListener(new WindowAdapter() {
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
        LOGGER.trace("KursortErfassenController Event Bezeichnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBezeichnung.getText(), kursortErfassenModel.getBezeichnung());
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
            kursortErfassenModel.setBezeichnung(txtBezeichnung.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursortErfassenController setModelBezeichnung RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBezeichnung.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursortErfassenController setModelBezeichnung Exception=" + e.getMessage());
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
        if (kursortErfassenModel.checkKursortBezeichnungBereitsInVerwendung(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(null, "Bezeichnung bereits in Verwendung.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            kursortErfassenModel.speichern(svmContext.getSvmModel());
            kursortErfassenDialog.dispose();
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
        kursortErfassenDialog.dispose();
    }

    private void onKursortErfassenModelCompleted(boolean completed) {
        LOGGER.trace("KursortErfassenModel completed=" + completed);
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
            txtBezeichnung.setText(kursortErfassenModel.getBezeichnung());
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
    public void disableFields(boolean disable, Set<Field> fields) {}


}
