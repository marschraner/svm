package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmRuntimeException;
import ch.metzenthin.svm.common.datatypes.Field;
import ch.metzenthin.svm.common.datatypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.RechnungsdatumErfassenModel;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(RechnungsdatumErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
    private final RechnungsdatumErfassenModel rechnungsdatumErfassenModel;
    private final Rechnungstyp rechnungstyp;
    private final boolean defaultButtonEnabled;
    private JDialog rechnungsdatumErfassenDialog;
    private JTextField txtRechnungsdatum;
    private JLabel errLblRechnungsdatum;
    private JButton btnOk;
    private Exception swingWorkerException;

    public RechnungsdatumErfassenController(SemesterrechnungenTableModel semesterrechnungenTableModel, RechnungsdatumErfassenModel rechnungsdatumErfassenModel, Rechnungstyp rechnungstyp, boolean defaultButtonEnabled) {
        super(rechnungsdatumErfassenModel);
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.rechnungsdatumErfassenModel = rechnungsdatumErfassenModel;
        this.rechnungstyp = rechnungstyp;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.rechnungsdatumErfassenModel.addPropertyChangeListener(this);
        this.rechnungsdatumErfassenModel.addDisableFieldsListener(this);
        this.rechnungsdatumErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.rechnungsdatumErfassenModel.addCompletedListener(this::onRechnungsdatumErfassenModelCompleted);
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

    public void setTxtRechnungsdatum(JTextField txtRechnungsdatum) {
        this.txtRechnungsdatum = txtRechnungsdatum;
        if (!defaultButtonEnabled) {
            this.txtRechnungsdatum.addActionListener(e -> onRechnungsdatumEvent(true));
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
        } catch (SvmValidationException e) {
            LOGGER.error(e);
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
            LOGGER.trace("RechnungsdatumErfassenController setModelRechnungsdatum RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRechnungsdatum.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("RechnungsdatumErfassenController setModelRechnungsdatum Exception={}", e.getMessage());
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
        this.btnOk.addActionListener(e -> onOk());
    }

    @SuppressWarnings("TextBlockMigration")
    private void onOk() {
        // Warnung
        Object[] optionsWarnung = {"Fortfahren", "Abbrechen"};
        String warningMessage = switch (rechnungstyp) {
            case VORRECHNUNG -> """
                    Allfällige frühere Vorrechnungsdatum-Einträge werden
                    mit dem neuen Rechnungsdatum überschrieben. Fortfahren?""";
            case NACHRECHNUNG -> """
                    Allfällige frühere Nachrechnungsdatum-Einträge werden mit dem neuen
                    Rechnungsdatum überschrieben und bereits getätigte Zahlungen der
                    Vorrechnungen in die Nachrechnungen kopiert. Fortfahren?""";
        };
        int n = JOptionPane.showOptionDialog(
                null,
                warningMessage,
                "Warnung",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
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
            String infoMessage = switch (rechnungstyp) {
                case VORRECHNUNG -> "Das Rechnungsdatum wird gesetzt. Bitte warten ...";
                case NACHRECHNUNG -> "Das Rechnungsdatum wird gesetzt und Zahlungen werden kopiert. Bitte warten ...";
            };

            final JOptionPane optionPane = new JOptionPane(infoMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            dialog.setContentPane(optionPane);
            // Public method to center the dialog after calling pack()
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            swingWorkerException = null;
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        // Der Swing-Worker erzeugt einen eigenen Entity-Manager. Die selektierten Semesterrechnungen,
                        // welche nachfolgend vom Swing-Worker verarbeitet werden, befinden sich noch nicht
                        // im Persistenzkontext des Entity-Managers und müssen daher zuerst geladen werden.
                        semesterrechnungenTableModel.loadSelektierteSemesterrechnungenNotContainedInPersistenceContext();
                        rechnungsdatumErfassenModel.replaceRechnungsdatumAndUpdateSemesterrechnung(semesterrechnungenTableModel, rechnungstyp);
                    } catch (Exception e) {
                        swingWorkerException = e;
                    }
                    // DB-Session bzw. Entity-Manager des Swing-Workers wieder schliessen
                    DB db = DBFactory.getInstance();
                    db.closeSession();
                    return null;
                }

                @Override
                protected void done() {
                    // Dialog in jedem Fall schliessen
                    dialog.dispose();
                    // Exception eines Swing-Workers muss in done()-Methode geworfen werden, da sonst der
                    // SwingExceptionHandler nicht aufgerufen und kein Fehlerdialog angezeigt wird!
                    // (vgl. https://stackoverflow.com/questions/6523623/graceful-exception-handling-in-swing-worker)
                    if (swingWorkerException != null) {
                        throw new SvmRuntimeException(
                                "Fehler beim Setzen des Rechnungsdatums / Kopieren der Zahlungen",
                                swingWorkerException);
                    }
                    // Die durch den Swing-Worker veränderten Semesterrechnungen befinden sich nach dem Schliessen der
                    // DB-Session des Swing-Workers nicht mehr in dessen Persistenzkontext, welcher mitgelöscht wird.
                    // Sie müssen in den Persistenzkontext des ab nun wieder verantwortlichen Main-Threads geladen
                    // werden.
                    semesterrechnungenTableModel.reloadSemesterrechnungenNotContainedInPersistenceContext();
                }
            };
            worker.execute();
            dialog.setVisible(true);

        }
        rechnungsdatumErfassenDialog.dispose();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        rechnungsdatumErfassenDialog.dispose();
    }

    private void onRechnungsdatumErfassenModelCompleted(boolean completed) {
        LOGGER.trace("RechnungsdatumErfassenModel completed={}", completed);
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
    public void disableFields(boolean disable, Set<Field> fields) {
        // Keine zu deaktivierenden Felder
    }


}
