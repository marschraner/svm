package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.LehrkraftErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.LehrkraefteTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class LehrkraftErfassenController extends PersonController {

    private static final Logger LOGGER = Logger.getLogger(LehrkraftErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private LehrkraefteTableModel lehrkraefteTableModel;
    private LehrkraftErfassenModel lehrkraftErfassenModel;
    private final boolean isBearbeiten;
    private final SvmContext svmContext;
    private JDialog lehrkraftErfassenDialog;
    private JTextField txtAhvNummer;
    private JTextField txtVertretungsmoeglichkeiten;
    private JLabel errLblAhvNummer;
    private JLabel errLblVertretungsmoeglichkeiten;
    private JCheckBox checkBoxAktiv;
    private JButton btnSpeichern;

    public LehrkraftErfassenController(SvmContext svmContext, LehrkraefteTableModel lehrkraefteTableModel, LehrkraftErfassenModel lehrkraftErfassenModel, boolean isBearbeiten) {
        super(lehrkraftErfassenModel);
        this.svmContext = svmContext;
        this.lehrkraefteTableModel = lehrkraefteTableModel;
        this.lehrkraftErfassenModel = lehrkraftErfassenModel;
        this.lehrkraftErfassenModel.addPropertyChangeListener(this);
        this.lehrkraftErfassenModel.addDisableFieldsListener(this);
        this.lehrkraftErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.lehrkraftErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onLehrkraftErfassenModelCompleted(completed);
            }
        });
        this.isBearbeiten = isBearbeiten;
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        lehrkraftErfassenModel.initializeCompleted();
    }

    public void setLehrkraftErfassenDialog(JDialog lehrkraftErfassenDialog) {
        // call onCancel() when cross is clicked
        this.lehrkraftErfassenDialog = lehrkraftErfassenDialog;
        lehrkraftErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        lehrkraftErfassenDialog.addWindowListener(new WindowAdapter() {
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

    public void setTxtAhvNummer(JTextField txtAhvNummer) {
        this.txtAhvNummer = txtAhvNummer;
        this.txtAhvNummer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAhvNummerEvent(true);
            }
        });
        this.txtAhvNummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAhvNummerEvent(false);
            }
        });
    }

    private void onAhvNummerEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LehrkraftErfassenController Event AhvNummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAhvNummer.getText(), lehrkraftErfassenModel.getAhvNummer());
        try {
            setModelAhvNummer(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAhvNummer(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.AHV_NUMMER);
        try {
            lehrkraftErfassenModel.setAhvNummer(txtAhvNummer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LehrkraftErfassenController setModelAhvNummer RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAhvNummer.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LehrkraftErfassenController setModelAhvNummer Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtVertretungsmoeglichkeiten(JTextField txtVertretungsmoeglichkeiten) {
        this.txtVertretungsmoeglichkeiten = txtVertretungsmoeglichkeiten;
        this.txtVertretungsmoeglichkeiten.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVertretungsmoeglichkeitenEvent(true);
            }
        });
        this.txtVertretungsmoeglichkeiten.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVertretungsmoeglichkeitenEvent(false);
            }
        });
    }

    private void onVertretungsmoeglichkeitenEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LehrkraftErfassenController Event Vertretungsmoeglichkeiten");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVertretungsmoeglichkeiten.getText(), lehrkraftErfassenModel.getVertretungsmoeglichkeiten());
        try {
            setModelVertretungsmoeglichkeiten(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVertretungsmoeglichkeiten(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.VERTRETUNGSMOEGLICHKEITEN);
        try {
            lehrkraftErfassenModel.setVertretungsmoeglichkeiten(txtVertretungsmoeglichkeiten.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LehrkraftErfassenController setModelVertretungsmoeglichkeiten RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtVertretungsmoeglichkeiten.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LehrkraftErfassenController setModelVertretungsmoeglichkeiten Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setCheckBoxAktiv(JCheckBox checkBoxAktiv) {
        this.checkBoxAktiv = checkBoxAktiv;
        // Aktiv als Default-Wert
        if (!isBearbeiten) {
            lehrkraftErfassenModel.setAktiv(true);
        }
        this.checkBoxAktiv.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onAktivEvent();
            }
        });
    }

    private void setModelAktiv() {
        lehrkraftErfassenModel.setAktiv(checkBoxAktiv.isSelected());
    }

    private void onAktivEvent() {
        LOGGER.trace("AngehoerigerController Event Aktiv. Selected=" + checkBoxAktiv.isSelected());
        setModelAktiv();
    }

    public void setErrLblAhvNummer(JLabel errLblAhvNummer) {
        this.errLblAhvNummer = errLblAhvNummer;
    }

    public void setErrLblVertretungsmoeglichkeiten(JLabel errLblVertretungsmoeglichkeiten) {
        this.errLblVertretungsmoeglichkeiten = errLblVertretungsmoeglichkeiten;
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
        if (lehrkraftErfassenModel.checkLehrkraftBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(lehrkraftErfassenDialog, "Die Lehrkraft ist bereits in der Datenbank gespeichert und kann nicht ein weiteres Mal erfasst werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            btnSpeichern.setFocusPainted(false);
        } else {
            lehrkraftErfassenModel.speichern(svmContext.getSvmModel(), lehrkraefteTableModel);
            lehrkraftErfassenDialog.dispose();
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
        lehrkraftErfassenDialog.dispose();
    }

    private void onLehrkraftErfassenModelCompleted(boolean completed) {
        LOGGER.trace("LehrkraftErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.AHV_NUMMER, evt)) {
            txtAhvNummer.setText(lehrkraftErfassenModel.getAhvNummer());
        }
        else if (checkIsFieldChange(Field.VERTRETUNGSMOEGLICHKEITEN, evt)) {
            txtVertretungsmoeglichkeiten.setText(lehrkraftErfassenModel.getVertretungsmoeglichkeiten());
        }
        else if (checkIsFieldChange(Field.AKTIV, evt)) {
            checkBoxAktiv.setSelected(lehrkraftErfassenModel.isAktiv());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (txtAhvNummer.isEnabled()) {
            LOGGER.trace("Validate field AhvNummer");
            setModelAhvNummer(true);
        }
        if (txtVertretungsmoeglichkeiten.isEnabled()) {
            LOGGER.trace("Validate field Vertretungsmoeglichkeiten");
            setModelVertretungsmoeglichkeiten(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.AHV_NUMMER)) {
            errLblAhvNummer.setVisible(true);
            errLblAhvNummer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            errLblVertretungsmoeglichkeiten.setVisible(true);
            errLblVertretungsmoeglichkeiten.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.AHV_NUMMER)) {
            txtAhvNummer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            txtVertretungsmoeglichkeiten.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.AHV_NUMMER)) {
            errLblAhvNummer.setVisible(false);
            txtAhvNummer.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VERTRETUNGSMOEGLICHKEITEN)) {
            errLblVertretungsmoeglichkeiten.setVisible(false);
            txtVertretungsmoeglichkeiten.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
