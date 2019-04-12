package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.RechnungsdatumErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.GregorianCalendar;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class RechnungsdatumErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(RechnungsdatumErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private SemesterrechnungenTableModel semesterrechnungenTableModel;
    private RechnungsdatumErfassenModel rechnungsdatumErfassenModel;
    private Rechnungstyp rechnungstyp;
    private boolean defaultButtonEnabled;
    private final SvmContext svmContext;
    private JDialog rechnungsdatumErfassenDialog;
    private JTextField txtRechnungsdatum;
    private JLabel errLblRechnungsdatum;
    private JButton btnOk;

    public RechnungsdatumErfassenController(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, RechnungsdatumErfassenModel rechnungsdatumErfassenModel, Rechnungstyp rechnungstyp, boolean defaultButtonEnabled) {
        super(rechnungsdatumErfassenModel);
        this.svmContext = svmContext;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.rechnungsdatumErfassenModel = rechnungsdatumErfassenModel;
        this.rechnungstyp = rechnungstyp;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.rechnungsdatumErfassenModel.addPropertyChangeListener(this);
        this.rechnungsdatumErfassenModel.addDisableFieldsListener(this);
        this.rechnungsdatumErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.rechnungsdatumErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onRechnungsdatumErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        rechnungsdatumErfassenModel.initializeCompleted();
    }

    public void setRechnungsdatumErfassenDialog(JDialog rechnungsdatumErfassenDialog) {
        // call onCancel() when cross is clicked
        this.rechnungsdatumErfassenDialog = rechnungsdatumErfassenDialog;
        rechnungsdatumErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        rechnungsdatumErfassenDialog.addWindowListener(new WindowAdapter() {
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
    
    public void setTxtRechnungsdatum(JTextField txtRechnungsdatum) {
        this.txtRechnungsdatum = txtRechnungsdatum;
        if (!defaultButtonEnabled) {
            this.txtRechnungsdatum.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onRechnungsdatumEvent(true);
                }
            });
        }
        this.txtRechnungsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRechnungsdatumEvent(false);
            }
        });
        // Initialisierung mit heutigem Datum
        try {
            rechnungsdatumErfassenModel.setRechnungsdatum(asString(new GregorianCalendar()));
        } catch (SvmValidationException ignore) {
        }
    }

    private void onRechnungsdatumEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("RechnungsdatumErfassenController Event Rechnungsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsdatum.getText(), rechnungsdatumErfassenModel.getRechnungsdatum());
        try {
            setModelRechnungsdatum(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRechnungsdatum(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.RECHNUNGSDATUM);
        try {
            rechnungsdatumErfassenModel.setRechnungsdatum(txtRechnungsdatum.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("RechnungsdatumErfassenController setModelRechnungsdatum RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRechnungsdatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("RechnungsdatumErfassenController setModelRechnungsdatum Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblRechnungsdatum(JLabel errLblRechnungsdatum) {
        this.errLblRechnungsdatum = errLblRechnungsdatum;
    }

    public void setBtnOk(JButton btnOk) {
        this.btnOk = btnOk;
        if (isModelValidationMode()) {
            btnOk.setEnabled(false);
        }
        this.btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
    }

    private void onOk() {
        // Warnung
        Object[] optionsWarnung = {"Fortfahren", "Abbrechen"};
        String warningMessage = "";
        switch (rechnungstyp) {
            case VORRECHNUNG:
                warningMessage = "Allfällige frühere Vorrechnungsdatum-Einträge werden \n" +
                        "mit dem neuen Rechnungsdatum überschrieben. Fortfahren?";
                break;
            case NACHRECHNUNG:
                warningMessage = "Allfällige frühere Nachrechnungsdatum-Einträge werden mit dem neuen \n" +
                        "Rechnungsdatum überschrieben und bereits getätigte Zahlungen der \n" +
                        "Vorrechnungen in die Nachrechnungen kopiert. Fortfahren?";
                break;
        }
        int n = JOptionPane.showOptionDialog(
                null,
                warningMessage,
                "Warnung",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                svmContext.getDialogIcons().getWarningIcon(),
                optionsWarnung,  //the titles of buttons
                optionsWarnung[1]); //default button title
        if (n == 0) {
            // Wait-Cursor funktioniert hier nicht (wegen vorhergehendem Dialog)
            final JDialog dialog = new JDialog();
            dialog.setModal(true);
            // Kein Maximierung-Button
            dialog.setResizable(false);
            // Schliessen soll keinen Effekt haben
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialog.setTitle("Rechnungsdatum wird gesetzt");
            String infoMessage = "";
            switch (rechnungstyp) {
                case VORRECHNUNG:
                    infoMessage = "Das Rechnungsdatum wird gesetzt. Bitte warten ...";
                    break;
                case NACHRECHNUNG:
                    infoMessage = "Das Rechnungsdatum wird gesetzt und Zahlungen werden kopiert. Bitte warten ...";
                    break;
            }

            final JOptionPane optionPane = new JOptionPane(infoMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, svmContext.getDialogIcons().getInformationIcon(), new Object[]{}, null);
            dialog.setContentPane(optionPane);
            // Public method to center the dialog after calling pack()
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    rechnungsdatumErfassenModel.replaceRechnungsdatumAndUpdateSemesterrechnung(semesterrechnungenTableModel, rechnungstyp);
                    return null;
                }
                @Override
                protected void done() {
                    dialog.dispose();
                }
            };
            worker.execute();
            dialog.setVisible(true);

        }
        rechnungsdatumErfassenDialog.dispose();
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
        rechnungsdatumErfassenDialog.dispose();
    }

    private void onRechnungsdatumErfassenModelCompleted(boolean completed) {
        LOGGER.trace("RechnungsdatumErfassenModel completed=" + completed);
        if (completed) {
            btnOk.setToolTipText(null);
            btnOk.setEnabled(true);
        } else {
            btnOk.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnOk.setEnabled(false);
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.RECHNUNGSDATUM, evt)) {
            txtRechnungsdatum.setText(asString(rechnungsdatumErfassenModel.getRechnungsdatum()));
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtRechnungsdatum.isEnabled()) {
            LOGGER.trace("Validate field Rechnungsdatum");
            setModelRechnungsdatum(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM)) {
            errLblRechnungsdatum.setVisible(true);
            errLblRechnungsdatum.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.RECHNUNGSDATUM)) {
            txtRechnungsdatum.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSDATUM)) {
            errLblRechnungsdatum.setVisible(false);
            txtRechnungsdatum.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}


}
