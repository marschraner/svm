package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;
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
public class LektionsgebuehrenErfassenController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(LektionsgebuehrenErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private LektionsgebuehrenTableModel lektionsgebuehrenTableModel;
    private LektionsgebuehrenErfassenModel lektionsgebuehrenErfassenModel;
    private boolean isBearbeiten;
    private boolean defaultButtonEnabled;
    private final SvmContext svmContext;
    private JDialog lektionsgebuehrenErfassenDialog;
    private JTextField txtLektionslaenge;
    private JTextField txtBetrag1Kind;
    private JTextField txtBetrag2Kinder;
    private JTextField txtBetrag3Kinder;
    private JTextField txtBetrag4Kinder;
    private JTextField txtBetrag5Kinder;
    private JTextField txtBetrag6Kinder;
    private JLabel errLblLektionslaenge;
    private JLabel errLblBetrag1Kind;
    private JLabel errLblBetrag2Kinder;
    private JLabel errLblBetrag3Kinder;
    private JLabel errLblBetrag4Kinder;
    private JLabel errLblBetrag5Kinder;
    private JLabel errLblBetrag6Kinder;
    private JButton btnSpeichern;

    public LektionsgebuehrenErfassenController(SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel, LektionsgebuehrenErfassenModel lektionsgebuehrenErfassenModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(lektionsgebuehrenErfassenModel);
        this.svmContext = svmContext;
        this.lektionsgebuehrenTableModel = lektionsgebuehrenTableModel;
        this.lektionsgebuehrenErfassenModel = lektionsgebuehrenErfassenModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.lektionsgebuehrenErfassenModel.addPropertyChangeListener(this);
        this.lektionsgebuehrenErfassenModel.addDisableFieldsListener(this);
        this.lektionsgebuehrenErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.lektionsgebuehrenErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onLektionsgebuehrenErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        lektionsgebuehrenErfassenModel.initializeCompleted();
    }

    public void setLektionsgebuehrenErfassenDialog(JDialog lektionsgebuehrenErfassenDialog) {
        // call onCancel() when cross is clicked
        this.lektionsgebuehrenErfassenDialog = lektionsgebuehrenErfassenDialog;
        lektionsgebuehrenErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        lektionsgebuehrenErfassenDialog.addWindowListener(new WindowAdapter() {
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

    public void setTxtLektionslaenge(JTextField txtLektionslaenge) {
        this.txtLektionslaenge = txtLektionslaenge;
        // ID darf nicht bearbeitet werden!
        if (isBearbeiten) {
            this.txtLektionslaenge.setEnabled(false);
        }
        if (!defaultButtonEnabled) {
            this.txtLektionslaenge.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onLektionslaengeEvent(true);
                }
            });
        }
        this.txtLektionslaenge.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onLektionslaengeEvent(false);
            }
        });
    }

    private void onLektionslaengeEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Lektionslaenge");
        boolean equalFieldAndModelValue = equalsNullSafe(txtLektionslaenge.getText(), lektionsgebuehrenErfassenModel.getLektionslaenge());
        try {
            setModelLektionslaenge(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelLektionslaenge(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.LEKTIONSLAENGE);
        try {
            lektionsgebuehrenErfassenModel.setLektionslaenge(txtLektionslaenge.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelLektionslaenge RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtLektionslaenge.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelLektionslaenge Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag1Kind(JTextField txtBetrag1Kind) {
        this.txtBetrag1Kind = txtBetrag1Kind;
        if (!defaultButtonEnabled) {
            this.txtBetrag1Kind.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag1KindEvent(true);
                }
            });
        }
        this.txtBetrag1Kind.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag1KindEvent(false);
            }
        });
    }

    private void onBetrag1KindEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag1Kind");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag1Kind.getText(), lektionsgebuehrenErfassenModel.getBetrag1Kind());
        try {
            setModelBetrag1Kind(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag1Kind(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_1_KIND);
        try {
            lektionsgebuehrenErfassenModel.setBetrag1Kind(txtBetrag1Kind.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag1Kind RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag1Kind.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag1Kind Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag2Kinder(JTextField txtBetrag2Kinder) {
        this.txtBetrag2Kinder = txtBetrag2Kinder;
        if (!defaultButtonEnabled) {
            this.txtBetrag2Kinder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag2KinderEvent(true);
                }
            });
        }
        this.txtBetrag2Kinder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag2KinderEvent(false);
            }
        });
    }

    private void onBetrag2KinderEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag2Kinder");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag2Kinder.getText(), lektionsgebuehrenErfassenModel.getBetrag2Kinder());
        try {
            setModelBetrag2Kinder(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag2Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_2_KINDER);
        try {
            lektionsgebuehrenErfassenModel.setBetrag2Kinder(txtBetrag2Kinder.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag2Kinder RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag2Kinder.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag2Kinder Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag3Kinder(JTextField txtBetrag3Kinder) {
        this.txtBetrag3Kinder = txtBetrag3Kinder;
        if (!defaultButtonEnabled) {
            this.txtBetrag3Kinder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag3KinderEvent(true);
                }
            });
        }
        this.txtBetrag3Kinder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag3KinderEvent(false);
            }
        });
    }

    private void onBetrag3KinderEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag3Kinder");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag3Kinder.getText(), lektionsgebuehrenErfassenModel.getBetrag3Kinder());
        try {
            setModelBetrag3Kinder(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag3Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_3_KINDER);
        try {
            lektionsgebuehrenErfassenModel.setBetrag3Kinder(txtBetrag3Kinder.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag3Kinder RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag3Kinder.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag3Kinder Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag4Kinder(JTextField txtBetrag4Kinder) {
        this.txtBetrag4Kinder = txtBetrag4Kinder;
        if (!defaultButtonEnabled) {
            this.txtBetrag4Kinder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag4KinderEvent(true);
                }
            });
        }
        this.txtBetrag4Kinder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag4KinderEvent(false);
            }
        });
    }

    private void onBetrag4KinderEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag4Kinder");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag4Kinder.getText(), lektionsgebuehrenErfassenModel.getBetrag4Kinder());
        try {
            setModelBetrag4Kinder(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag4Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_4_KINDER);
        try {
            lektionsgebuehrenErfassenModel.setBetrag4Kinder(txtBetrag4Kinder.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag4Kinder RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag4Kinder.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag4Kinder Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag5Kinder(JTextField txtBetrag5Kinder) {
        this.txtBetrag5Kinder = txtBetrag5Kinder;
        if (!defaultButtonEnabled) {
            this.txtBetrag5Kinder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag5KinderEvent(true);
                }
            });
        }
        this.txtBetrag5Kinder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag5KinderEvent(false);
            }
        });
    }

    private void onBetrag5KinderEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag5Kinder");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag5Kinder.getText(), lektionsgebuehrenErfassenModel.getBetrag5Kinder());
        try {
            setModelBetrag5Kinder(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag5Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_5_KINDER);
        try {
            lektionsgebuehrenErfassenModel.setBetrag5Kinder(txtBetrag5Kinder.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag5Kinder RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag5Kinder.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag5Kinder Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBetrag6Kinder(JTextField txtBetrag6Kinder) {
        this.txtBetrag6Kinder = txtBetrag6Kinder;
        if (!defaultButtonEnabled) {
            this.txtBetrag6Kinder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onBetrag6KinderEvent(true);
                }
            });
        }
        this.txtBetrag6Kinder.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBetrag6KinderEvent(false);
            }
        });
    }

    private void onBetrag6KinderEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("LektionsgebuehrenErfassenController Event Betrag6Kinder");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBetrag6Kinder.getText(), lektionsgebuehrenErfassenModel.getBetrag6Kinder());
        try {
            setModelBetrag6Kinder(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBetrag6Kinder(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BETRAG_6_KINDER);
        try {
            lektionsgebuehrenErfassenModel.setBetrag6Kinder(txtBetrag6Kinder.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag6Kinder RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBetrag6Kinder.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("LektionsgebuehrenErfassenController setModelBetrag6Kinder Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblLektionslaenge(JLabel errLblLektionslaenge) {
        this.errLblLektionslaenge = errLblLektionslaenge;
    }

    public void setErrLblBetrag1Kind(JLabel errLblBetrag1Kind) {
        this.errLblBetrag1Kind = errLblBetrag1Kind;
    }

    public void setErrLblBetrag2Kinder(JLabel errLblBetrag2Kinder) {
        this.errLblBetrag2Kinder = errLblBetrag2Kinder;
    }

    public void setErrLblBetrag3Kinder(JLabel errLblBetrag3Kinder) {
        this.errLblBetrag3Kinder = errLblBetrag3Kinder;
    }

    public void setErrLblBetrag4Kinder(JLabel errLblBetrag4Kinder) {
        this.errLblBetrag4Kinder = errLblBetrag4Kinder;
    }

    public void setErrLblBetrag5Kinder(JLabel errLblBetrag5Kinder) {
        this.errLblBetrag5Kinder = errLblBetrag5Kinder;
    }

    public void setErrLblBetrag6Kinder(JLabel errLblBetrag6Kinder) {
        this.errLblBetrag6Kinder = errLblBetrag6Kinder;
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
        if (lektionsgebuehrenErfassenModel.checkLektionslaengeBereitsErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(lektionsgebuehrenErfassenDialog, "Lektionslänge bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSpeichern.setFocusPainted(false);
        } else {
            lektionsgebuehrenErfassenModel.speichern(svmContext.getSvmModel(), lektionsgebuehrenTableModel);
            lektionsgebuehrenErfassenDialog.dispose();
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
        lektionsgebuehrenErfassenDialog.dispose();
    }

    private void onLektionsgebuehrenErfassenModelCompleted(boolean completed) {
        LOGGER.trace("LektionsgebuehrenErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.LEKTIONSLAENGE, evt)) {
            txtLektionslaenge.setText(Integer.toString(lektionsgebuehrenErfassenModel.getLektionslaenge()));
        } else if (checkIsFieldChange(Field.BETRAG_1_KIND, evt)) {
            txtBetrag1Kind.setText(lektionsgebuehrenErfassenModel.getBetrag1Kind() == null ? null : lektionsgebuehrenErfassenModel.getBetrag1Kind().toString());
        } else if (checkIsFieldChange(Field.BETRAG_2_KINDER, evt)) {
            txtBetrag2Kinder.setText(lektionsgebuehrenErfassenModel.getBetrag2Kinder() == null ? null : lektionsgebuehrenErfassenModel.getBetrag2Kinder().toString());
        } else if (checkIsFieldChange(Field.BETRAG_3_KINDER, evt)) {
            txtBetrag3Kinder.setText(lektionsgebuehrenErfassenModel.getBetrag3Kinder() == null ? null : lektionsgebuehrenErfassenModel.getBetrag3Kinder().toString());
        } else if (checkIsFieldChange(Field.BETRAG_4_KINDER, evt)) {
            txtBetrag4Kinder.setText(lektionsgebuehrenErfassenModel.getBetrag4Kinder() == null ? null : lektionsgebuehrenErfassenModel.getBetrag4Kinder().toString());
        } else if (checkIsFieldChange(Field.BETRAG_5_KINDER, evt)) {
            txtBetrag5Kinder.setText(lektionsgebuehrenErfassenModel.getBetrag5Kinder() == null ? null : lektionsgebuehrenErfassenModel.getBetrag5Kinder().toString());
        } else if (checkIsFieldChange(Field.BETRAG_6_KINDER, evt)) {
            txtBetrag6Kinder.setText(lektionsgebuehrenErfassenModel.getBetrag6Kinder() == null ? null : lektionsgebuehrenErfassenModel.getBetrag6Kinder().toString());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtLektionslaenge.isEnabled()) {
            LOGGER.trace("Validate field Lektionslaenge");
            setModelLektionslaenge(true);
        }
        if (txtBetrag1Kind.isEnabled()) {
            LOGGER.trace("Validate field Betrag 1 Kind");
            setModelBetrag1Kind(true);
        }
        if (txtBetrag2Kinder.isEnabled()) {
            LOGGER.trace("Validate field Betrag 2 Kinder");
            setModelBetrag2Kinder(true);
        }
        if (txtBetrag3Kinder.isEnabled()) {
            LOGGER.trace("Validate field Betrag 3 Kinder");
            setModelBetrag3Kinder(true);
        }
        if (txtBetrag4Kinder.isEnabled()) {
            LOGGER.trace("Validate field Betrag 4 Kinder");
            setModelBetrag4Kinder(true);
        }
        if (txtBetrag5Kinder.isEnabled()) {
            LOGGER.trace("Validate field Betrag 5 Kinder");
            setModelBetrag5Kinder(true);
        }
        if (txtBetrag6Kinder.isEnabled()) {
            LOGGER.trace("Validate field Betrag 6 Kinder");
            setModelBetrag6Kinder(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
            errLblLektionslaenge.setVisible(true);
            errLblLektionslaenge.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
            errLblBetrag1Kind.setVisible(true);
            errLblBetrag1Kind.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
            errLblBetrag2Kinder.setVisible(true);
            errLblBetrag2Kinder.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
            errLblBetrag3Kinder.setVisible(true);
            errLblBetrag3Kinder.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
            errLblBetrag4Kinder.setVisible(true);
            errLblBetrag4Kinder.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
            errLblBetrag5Kinder.setVisible(true);
            errLblBetrag5Kinder.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
            errLblBetrag6Kinder.setVisible(true);
            errLblBetrag6Kinder.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.LEKTIONSLAENGE)) {
            txtLektionslaenge.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_1_KIND)) {
            txtBetrag1Kind.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_2_KINDER)) {
            txtBetrag2Kinder.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_3_KINDER)) {
            txtBetrag3Kinder.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_4_KINDER)) {
            txtBetrag4Kinder.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_5_KINDER)) {
            txtBetrag5Kinder.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BETRAG_6_KINDER)) {
            txtBetrag6Kinder.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEKTIONSLAENGE)) {
            errLblLektionslaenge.setVisible(false);
            txtLektionslaenge.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_1_KIND)) {
            errLblBetrag1Kind.setVisible(false);
            txtBetrag1Kind.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_2_KINDER)) {
            errLblBetrag2Kinder.setVisible(false);
            txtBetrag2Kinder.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_3_KINDER)) {
            errLblBetrag3Kinder.setVisible(false);
            txtBetrag3Kinder.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_4_KINDER)) {
            errLblBetrag4Kinder.setVisible(false);
            txtBetrag4Kinder.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_5_KINDER)) {
            errLblBetrag5Kinder.setVisible(false);
            txtBetrag5Kinder.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BETRAG_6_KINDER)) {
            errLblBetrag6Kinder.setVisible(false);
            txtBetrag6Kinder.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
