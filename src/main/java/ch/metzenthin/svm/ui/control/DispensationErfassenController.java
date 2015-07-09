package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.DispensationErfassenModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class DispensationErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(DispensationErfassenController.class);

    private final SvmContext svmContext;
    private DispensationErfassenModel dispensationErfassenModel;
    private JDialog dispensationErfassenDialog;
    private JTextField txtDispensationsbeginn;
    private JTextField txtDispensationsende;
    private JTextField txtVoraussichtlicheDauer;
    private JTextField txtGrund;
    private JLabel errLblDispensationsbeginn;
    private JLabel errLblDispensationsende;
    private JLabel errLblGrund;
    private JLabel errLblVoraussichtlicheDauer;
    private JButton btnSpeichern;

    public DispensationErfassenController(SvmContext svmContext, DispensationErfassenModel dispensationErfassenModel) {
        super(dispensationErfassenModel);
        this.svmContext = svmContext;
        this.dispensationErfassenModel = dispensationErfassenModel;
        this.dispensationErfassenModel.addPropertyChangeListener(this);
        this.dispensationErfassenModel.addDisableFieldsListener(this);
        this.dispensationErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.dispensationErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onDispensationErfassenModelCompleted(completed);
            }
        });
    }

    public void setDispensationErfassenDialog(JDialog dispensationErfassenDialog) {
        // call onCancel() when cross is clicked
        this.dispensationErfassenDialog = dispensationErfassenDialog;
        dispensationErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dispensationErfassenDialog.addWindowListener(new WindowAdapter() {
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

    public void setTxtDispensationsbeginn(JTextField txtDispensationsbeginn) {
        this.txtDispensationsbeginn = txtDispensationsbeginn;
        this.txtDispensationsbeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationsbeginnEvent();
            }
        });
        this.txtDispensationsbeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDispensationsbeginnEvent();
            }
        });
    }

    private void onDispensationsbeginnEvent() {
        LOGGER.trace("DispensationErfassenController Event Dispensationsbeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDispensationsbeginn.getText(), dispensationErfassenModel.getDispensationsbeginn());
        setModelDispensationsbeginn();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDispensationsbeginn() {
        makeErrorLabelInvisible(Field.DISPENSATIONSBEGINN);
        try {
            dispensationErfassenModel.setDispensationsbeginn(txtDispensationsbeginn.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("DispensationErfassenController setModelDispensationsbeginn RequiredException=" + e.getMessage());
            txtDispensationsbeginn.setToolTipText(e.getMessage());
        } catch (SvmValidationException e) {
            LOGGER.trace("DispensationErfassenController setModelDispensationsbeginn Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    public void setTxtDispensationsende(JTextField txtDispensationsende) {
        this.txtDispensationsende = txtDispensationsende;
        this.txtDispensationsende.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDispensationsendeEvent();
            }
        });
        this.txtDispensationsende.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onDispensationsendeEvent();
            }
        });
    }

    private void onDispensationsendeEvent() {
        LOGGER.trace("DispensationErfassenController Event Dispensationsende");
        boolean equalFieldAndModelValue = equalsNullSafe(txtDispensationsende.getText(), dispensationErfassenModel.getDispensationsende());
        setModelDispensationsende();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelDispensationsende() {
        makeErrorLabelInvisible(Field.DISPENSATIONSENDE);
        try {
            dispensationErfassenModel.setDispensationsende(txtDispensationsende.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("DispensationErfassenController setModelDispensationsende RequiredException=" + e.getMessage());
            txtDispensationsende.setToolTipText(e.getMessage());
        } catch (SvmValidationException e) {
            LOGGER.trace("DispensationErfassenController setModelDispensationsende Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    public void setTxtVoraussichtlicheDauer(JTextField txtVoraussichtlicheDauer) {
        this.txtVoraussichtlicheDauer = txtVoraussichtlicheDauer;
        this.txtVoraussichtlicheDauer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVoraussichtlicheDauerEvent();
            }
        });
        this.txtVoraussichtlicheDauer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVoraussichtlicheDauerEvent();
            }
        });
    }

    private void onVoraussichtlicheDauerEvent() {
        LOGGER.trace("DispensationErfassenController Event VoraussichtlicheDauer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVoraussichtlicheDauer.getText(), dispensationErfassenModel.getVoraussichtlicheDauer());
        setModelVoraussichtlicheDauer();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVoraussichtlicheDauer() {
        makeErrorLabelInvisible(Field.VORAUSSICHTLICHE_DAUER);
        try {
            dispensationErfassenModel.setVoraussichtlicheDauer(txtVoraussichtlicheDauer.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("DispensationErfassenController setModelVoraussichtlicheDauer RequiredException=" + e.getMessage());
            txtVoraussichtlicheDauer.setToolTipText(e.getMessage());
        } catch (SvmValidationException e) {
            LOGGER.trace("DispensationErfassenController setModelVoraussichtlicheDauer Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    public void setTxtGrund(JTextField txtGrund) {
        this.txtGrund = txtGrund;
        this.txtGrund.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGrundEvent();
            }
        });
        this.txtGrund.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGrundEvent();
            }
        });
    }

    private void onGrundEvent() {
        LOGGER.trace("DispensationErfassenController Event Grund");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGrund.getText(), dispensationErfassenModel.getGrund());
        setModelGrund();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGrund() {
        makeErrorLabelInvisible(Field.GRUND);
        try {
            dispensationErfassenModel.setGrund(txtGrund.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("DispensationErfassenController setModelGrund RequiredException=" + e.getMessage());
            txtGrund.setToolTipText(e.getMessage());
        } catch (SvmValidationException e) {
            LOGGER.trace("DispensationErfassenController setModelGrund Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    public void setErrLblDispensationsbeginn(JLabel errLblDispensationsbeginn) {
        this.errLblDispensationsbeginn = errLblDispensationsbeginn;
    }

    public void setErrLblDispensationsende(JLabel errLblDispensationsende) {
        this.errLblDispensationsende = errLblDispensationsende;
    }

    public void setErrLblGrund(JLabel errLblGrund) {
        this.errLblGrund = errLblGrund;
    }

    public void setErrLblVoraussichtlicheDauer(JLabel errLblVoraussichtlicheDauer) {
        this.errLblVoraussichtlicheDauer = errLblVoraussichtlicheDauer;
    }

    public void setBtnSpeichern(JButton btnSpeichern) {
        this.btnSpeichern = btnSpeichern;
        this.btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSpeichern();
            }
        });
    }

    private void onSpeichern() {
        //TODO
        dispensationErfassenDialog.dispose();
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
        dispensationErfassenDialog.dispose();
    }

    private void onDispensationErfassenModelCompleted(boolean completed) {
        LOGGER.trace("DispensationErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.DISPENSATIONSBEGINN, evt)) {
            txtDispensationsbeginn.setText(asString(dispensationErfassenModel.getDispensationsbeginn()));
        }
        if (checkIsFieldChange(Field.DISPENSATIONSENDE, evt)) {
            txtDispensationsende.setText(asString(dispensationErfassenModel.getDispensationsende()));
        }
        if (checkIsFieldChange(Field.VORAUSSICHTLICHE_DAUER, evt)) {
            txtVoraussichtlicheDauer.setText(dispensationErfassenModel.getVoraussichtlicheDauer());
        }
        if (checkIsFieldChange(Field.GRUND, evt)) {
            txtGrund.setText(dispensationErfassenModel.getGrund());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtDispensationsbeginn.isEnabled()) {
            LOGGER.trace("Validate field Dispensationsbeginn");
            setModelDispensationsbeginn();
        }
        if (txtDispensationsende.isEnabled()) {
            LOGGER.trace("Validate field Dispensationsende");
            setModelDispensationsende();
        }
        if (txtVoraussichtlicheDauer.isEnabled()) {
            LOGGER.trace("Validate field Voraussichtliche Dauer");
            setModelVoraussichtlicheDauer();
        }
        if (txtGrund.isEnabled()) {
            LOGGER.trace("Validate field Grund");
            setModelGrund();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.DISPENSATIONSBEGINN)) {
            errLblDispensationsbeginn.setVisible(true);
            errLblDispensationsbeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DISPENSATIONSENDE)) {
            errLblDispensationsende.setVisible(true);
            errLblDispensationsende.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORAUSSICHTLICHE_DAUER)) {
            errLblVoraussichtlicheDauer.setVisible(true);
            errLblVoraussichtlicheDauer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GRUND)) {
            errLblGrund.setVisible(true);
            errLblGrund.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.DISPENSATIONSBEGINN)) {
            txtDispensationsbeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.DISPENSATIONSENDE)) {
            txtDispensationsende.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.VORAUSSICHTLICHE_DAUER)) {
            txtVoraussichtlicheDauer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GRUND)) {
            txtGrund.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.DISPENSATIONSBEGINN)) {
            errLblDispensationsbeginn.setVisible(false);
            txtDispensationsbeginn.setToolTipText(null);
        }
        if (fields.contains(Field.DISPENSATIONSENDE)) {
            errLblDispensationsende.setVisible(false);
            txtDispensationsende.setToolTipText(null);
        }
        if (fields.contains(Field.VORAUSSICHTLICHE_DAUER)) {
            errLblVoraussichtlicheDauer.setVisible(false);
            txtVoraussichtlicheDauer.setToolTipText(null);
        }
        if (fields.contains(Field.GRUND)) {
            errLblGrund.setVisible(false);
            txtGrund.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.DISPENSATIONSBEGINN)) {
            errLblDispensationsbeginn.setVisible(false);
            txtDispensationsbeginn.setToolTipText(null);
        }
        if (fields.contains(Field.DISPENSATIONSENDE)) {
            errLblDispensationsende.setVisible(false);
            txtDispensationsende.setToolTipText(null);
        }
        if (fields.contains(Field.VORAUSSICHTLICHE_DAUER)) {
            errLblVoraussichtlicheDauer.setVisible(false);
            txtVoraussichtlicheDauer.setToolTipText(null);
        }
        if (fields.contains(Field.GRUND)) {
            errLblGrund.setVisible(false);
            txtGrund.setToolTipText(null);
        }
    }


}
