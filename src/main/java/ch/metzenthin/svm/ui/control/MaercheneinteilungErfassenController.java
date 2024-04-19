package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Elternmithilfe;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.MaercheneinteilungErfassenModel;
import ch.metzenthin.svm.domain.model.MaercheneinteilungenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.persistence.entities.Maercheneinteilung;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.checkNotEmpty;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
@SuppressWarnings("LoggingSimilarMessage")
public class MaercheneinteilungErfassenController extends PersonController {

    private static final Logger LOGGER = LogManager.getLogger(MaercheneinteilungErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private final MaercheneinteilungErfassenModel maercheneinteilungErfassenModel;
    private final SvmContext svmContext;
    private final MaercheneinteilungenTableModel maercheneinteilungenTableModel;
    private final MaercheneinteilungenModel maercheneinteilungenModel;
    private final SchuelerDatenblattModel schuelerDatenblattModel;
    private final boolean isBearbeiten;
    private final boolean defaultButtonEnabled;
    private JDialog maercheneinteilungErfassenDialog;
    private JSpinner spinnerMaerchen;
    private JComboBox<Gruppe> comboBoxGruppe;
    private JTextField txtRolle1;
    private JTextField txtBilderRolle1;
    private JTextField txtRolle2;
    private JTextField txtBilderRolle2;
    private JTextField txtRolle3;
    private JTextField txtBilderRolle3;
    private JComboBox<Elternmithilfe> comboBoxElternmithilfe;
    private JComboBox<ElternmithilfeCode> comboBoxElternmithilfeCode;
    private JCheckBox checkBoxKuchenVorstellung1;
    private JCheckBox checkBoxKuchenVorstellung2;
    private JCheckBox checkBoxKuchenVorstellung3;
    private JCheckBox checkBoxKuchenVorstellung4;
    private JCheckBox checkBoxKuchenVorstellung5;
    private JCheckBox checkBoxKuchenVorstellung6;
    private JCheckBox checkBoxKuchenVorstellung7;
    private JCheckBox checkBoxKuchenVorstellung8;
    private JCheckBox checkBoxKuchenVorstellung9;
    private JTextField txtZusatzattribut;
    private JTextField txtBemerkungen;
    private JLabel errLblMaerchen;
    private JLabel errLblGruppe;
    private JLabel errLblRolle1;
    private JLabel errLblBilderRolle1;
    private JLabel errLblRolle2;
    private JLabel errLblBilderRolle2;
    private JLabel errLblRolle3;
    private JLabel errLblBilderRolle3;
    private JLabel errLblElternmithilfe;
    private JLabel errLblElternmithilfeCode;
    private JLabel errLblZusatzattribut;
    private JLabel errLblBemerkungen;
    private JButton btnSpeichern;

    List<Maercheneinteilung> maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe
            = new ArrayList<>();

    public MaercheneinteilungErfassenController(SvmContext svmContext, MaercheneinteilungErfassenModel maercheneinteilungErfassenModel, MaercheneinteilungenTableModel maercheneinteilungenTableModel, MaercheneinteilungenModel maercheneinteilungenModel, SchuelerDatenblattModel schuelerDatenblattModel, boolean isBearbeiten, boolean defaultButtonEnabled) {
        super(maercheneinteilungErfassenModel, defaultButtonEnabled);
        this.svmContext = svmContext;
        this.maercheneinteilungErfassenModel = maercheneinteilungErfassenModel;
        this.maercheneinteilungenTableModel = maercheneinteilungenTableModel;
        this.maercheneinteilungenModel = maercheneinteilungenModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.isBearbeiten = isBearbeiten;
        this.defaultButtonEnabled = defaultButtonEnabled;
        this.maercheneinteilungErfassenModel.addPropertyChangeListener(this);
        this.maercheneinteilungErfassenModel.addDisableFieldsListener(this);
        this.maercheneinteilungErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.maercheneinteilungErfassenModel.addCompletedListener(this::onMaercheneinteilungErfassenModelCompleted);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        maercheneinteilungErfassenModel.initializeCompleted();
        maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe
                = maercheneinteilungErfassenModel
                .findMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe(
                        schuelerDatenblattModel);
        enableOrDisableElternmithilfeFields();
        if (!maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty()) {
            writeElternhilfeBereitsErfasstBemerkung();
        }
    }

    private void enableOrDisableElternmithilfeFields() {
        if (!maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty()) {
            boolean elternmithilfeFieldsHaveToBeEnabled = doElternmithilfeFieldsHaveToBeEnabled();
            disableElternmithilfeFields(!elternmithilfeFieldsHaveToBeEnabled);
        } else {
            disableElternmithilfeFields(false);
        }
    }

    private boolean doElternmithilfeFieldsHaveToBeEnabled() {

        // Schüler hat kein Geschwister mit bereits erfassten Elternmithilfe
        // -> Elternmithilfe muss erfassbar sein
        if (maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty()) {
            return true;
        }

        // Bereits erfasste Elternmithilfe für den Schüler
        // -> bereits erfasste Elternmithilfe muss editierbar sein
        if (maercheneinteilungErfassenModel.getElternmithilfe() != null
                || maercheneinteilungErfassenModel.getElternmithilfeCode() != null
                || (maercheneinteilungErfassenModel.isKuchenVorstellung1() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung1())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung2() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung2())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung3() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung3())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung4() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung4())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung5() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung5())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung6() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung6())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung7() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung7())
                || (maercheneinteilungErfassenModel.isKuchenVorstellung8() != null
                && maercheneinteilungErfassenModel.isKuchenVorstellung8())) {
            return true;
        }

        // Schüler hat Geschwister mit bereits erfassten Elternmithilfe, aber Geschwister ist nicht
        // in gleicher Gruppe wie Schüler (und es gibt nicht ein drittes Geschwister mit ebenfalls
        // erfasster Elternmithilfe in gleicher Gruppe)
        // -> weitere Elternmithilfe muss erfassbar sein
        Gruppe gruppeSchueler = maercheneinteilungErfassenModel.getGruppe();
        if (gruppeSchueler != null) {
            boolean geschwisterInVerschiedenerGruppeFound = false;
            boolean geschwisterInGleicherGruppeFound = false;
            for (Maercheneinteilung maercheneinteilungGeschwister
                    : maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe) {
                if (gruppeSchueler != maercheneinteilungGeschwister.getGruppe()) {
                    geschwisterInVerschiedenerGruppeFound = true;
                } else {
                    geschwisterInGleicherGruppeFound = true;
                }
            }
            return geschwisterInVerschiedenerGruppeFound && !geschwisterInGleicherGruppeFound;
        }

        return false;
    }

    public void setMaercheneinteilungErfassenDialog(JDialog maercheneinteilungErfassenDialog) {
        // call onCancel() when cross is clicked
        this.maercheneinteilungErfassenDialog = maercheneinteilungErfassenDialog;
        maercheneinteilungErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        maercheneinteilungErfassenDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setContentPane(JPanel contentPane) {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setSpinnerMaerchen(JSpinner spinnerMaerchen) {
        this.spinnerMaerchen = spinnerMaerchen;
        // Darf nicht bearbeitet werden, da Teil der ID!
        if (isBearbeiten) {
            spinnerMaerchen.setEnabled(false);
        }
        List<Maerchen> selectableMaerchenList = maercheneinteilungenModel.getSelectableMaerchens(svmContext.getSvmModel(), schuelerDatenblattModel);
        Maerchen[] selectableMaerchens;
        if (isBearbeiten) {
            selectableMaerchens = maercheneinteilungErfassenModel.getSelectableMaerchenMaercheneinteilungOrigin();
        } else {
            selectableMaerchens = selectableMaerchenList.toArray(new Maerchen[0]);
        }
        SpinnerModel spinnerModelMaerchen = new SpinnerListModel(selectableMaerchens);
        spinnerMaerchen.setModel(spinnerModelMaerchen);
        spinnerMaerchen.addChangeListener(e -> onMaerchenSelected());
        if (!isBearbeiten) {
            Maerchen maerchenInit = maercheneinteilungErfassenModel.getMaerchenInit(selectableMaerchenList);
            maercheneinteilungErfassenModel.setMaerchen(maerchenInit);
        }
    }

    private void onMaerchenSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event Maerchen selected ={}", spinnerMaerchen.getValue());
        boolean equalFieldAndModelValue = equalsNullSafe(spinnerMaerchen.getValue(), maercheneinteilungErfassenModel.getMaerchen());
        setModelMaerchen();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelMaerchen() {
        makeErrorLabelInvisible(Field.MAERCHEN);
        maercheneinteilungErfassenModel.setMaerchen((Maerchen) spinnerMaerchen.getValue());
    }

    public void setComboBoxGruppe(JComboBox<Gruppe> comboBoxGruppe) {
        this.comboBoxGruppe = comboBoxGruppe;
        comboBoxGruppe.setModel(new DefaultComboBoxModel<>(Gruppe.values()));
        // Alle nicht anzeigen:
        comboBoxGruppe.removeItem(Gruppe.ALLE);
        // Leeren ComboBox-Wert anzeigen
        comboBoxGruppe.setSelectedItem(null);
        comboBoxGruppe.addActionListener(e -> onGruppeSelected());
    }

    private void onGruppeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event Gruppe selected={}", comboBoxGruppe.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxGruppe.getSelectedItem(), maercheneinteilungErfassenModel.getGruppe());
        try {
            setModelGruppe();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGruppe() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.GRUPPE);
        try {
            maercheneinteilungErfassenModel.setGruppe((Gruppe) comboBoxGruppe.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelGruppe RequiredException={}", e.getMessage());
            if (isModelValidationMode()) {
                comboBoxGruppe.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setTxtRolle1(JTextField txtRolle1) {
        this.txtRolle1 = txtRolle1;
        if (!defaultButtonEnabled) {
            this.txtRolle1.addActionListener(e -> onRolle1Event(true));
        }
        this.txtRolle1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRolle1Event(false);
            }
        });
    }

    private void onRolle1Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event Rolle1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRolle1.getText(), maercheneinteilungErfassenModel.getRolle1());
        try {
            setModelRolle1(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRolle1(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ROLLE1);
        try {
            maercheneinteilungErfassenModel.setRolle1(txtRolle1.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle1 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle1 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle1(JTextField txtBilderRolle1) {
        this.txtBilderRolle1 = txtBilderRolle1;
        if (!defaultButtonEnabled) {
            this.txtBilderRolle1.addActionListener(e -> onBilderRolle1Event(true));
        }
        this.txtBilderRolle1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBilderRolle1Event(false);
            }
        });
    }

    private void onBilderRolle1Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event BilderRolle1");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBilderRolle1.getText(), maercheneinteilungErfassenModel.getBilderRolle1());
        try {
            setModelBilderRolle1(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBilderRolle1(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BILDER_ROLLE1);
        try {
            maercheneinteilungErfassenModel.setBilderRolle1(txtBilderRolle1.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle1 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle1 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRolle2(JTextField txtRolle2) {
        this.txtRolle2 = txtRolle2;
        if (!defaultButtonEnabled) {
            this.txtRolle2.addActionListener(e -> onRolle2Event(true));
        }
        this.txtRolle2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRolle2Event(false);
            }
        });
    }

    private void onRolle2Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event Rolle2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRolle2.getText(), maercheneinteilungErfassenModel.getRolle2());
        try {
            setModelRolle2(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRolle2(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ROLLE2);
        try {
            maercheneinteilungErfassenModel.setRolle2(txtRolle2.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle2 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle2 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle2(JTextField txtBilderRolle2) {
        this.txtBilderRolle2 = txtBilderRolle2;
        if (!defaultButtonEnabled) {
            this.txtBilderRolle2.addActionListener(e -> onBilderRolle2Event(true));
        }
        this.txtBilderRolle2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBilderRolle2Event(false);
            }
        });
    }

    private void onBilderRolle2Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event BilderRolle2");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBilderRolle2.getText(), maercheneinteilungErfassenModel.getBilderRolle2());
        try {
            setModelBilderRolle2(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBilderRolle2(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BILDER_ROLLE2);
        try {
            maercheneinteilungErfassenModel.setBilderRolle2(txtBilderRolle2.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle2 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle2 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRolle3(JTextField txtRolle3) {
        this.txtRolle3 = txtRolle3;
        if (!defaultButtonEnabled) {
            this.txtRolle3.addActionListener(e -> onRolle3Event(true));
        }
        this.txtRolle3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onRolle3Event(false);
            }
        });
    }

    private void onRolle3Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event Rolle3");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRolle3.getText(), maercheneinteilungErfassenModel.getRolle3());
        try {
            setModelRolle3(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelRolle3(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ROLLE3);
        try {
            maercheneinteilungErfassenModel.setRolle3(txtRolle3.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle3 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle3.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle3 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle3(JTextField txtBilderRolle3) {
        this.txtBilderRolle3 = txtBilderRolle3;
        if (!defaultButtonEnabled) {
            this.txtBilderRolle3.addActionListener(e -> onBilderRolle3Event(true));
        }
        this.txtBilderRolle3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBilderRolle3Event(false);
            }
        });
    }

    private void onBilderRolle3Event(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event BilderRolle3");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBilderRolle3.getText(), maercheneinteilungErfassenModel.getBilderRolle3());
        try {
            setModelBilderRolle3(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBilderRolle3(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BILDER_ROLLE3);
        try {
            maercheneinteilungErfassenModel.setBilderRolle3(txtBilderRolle3.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle3 RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle3.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle3 Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxElternmithilfe(JComboBox<Elternmithilfe> comboBoxElternmithilfe) {
        this.comboBoxElternmithilfe = comboBoxElternmithilfe;
        Elternmithilfe[] selectableElternmithilfen = maercheneinteilungErfassenModel.getSelectableElternmithilfen(schuelerDatenblattModel);
        comboBoxElternmithilfe.setModel(new DefaultComboBoxModel<>(selectableElternmithilfen));
        // Elternmithilfe in Model initialisieren mit erstem ComboBox-Wert
        maercheneinteilungErfassenModel.setElternmithilfe(Elternmithilfe.KEINER);
        comboBoxElternmithilfe.addActionListener(e -> onElternmithilfeSelected());
    }

    private void onElternmithilfeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event Elternmithilfe selected={}", comboBoxElternmithilfe.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxElternmithilfe.getSelectedItem(), maercheneinteilungErfassenModel.getElternmithilfe());
        setModelElternmithilfe();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelElternmithilfe() {
        makeErrorLabelInvisible(Field.ELTERNMITHILFE);
        maercheneinteilungErfassenModel.setElternmithilfe((Elternmithilfe) comboBoxElternmithilfe.getSelectedItem());
    }

    public void setComboBoxElternmithilfeCode(JComboBox<ElternmithilfeCode> comboBoxElternmithilfeCode) {
        this.comboBoxElternmithilfeCode = comboBoxElternmithilfeCode;
        ElternmithilfeCode[] selectableElternmithilfeCodes = maercheneinteilungErfassenModel.getSelectableElternmithilfeCodes(svmContext.getSvmModel());
        comboBoxElternmithilfeCode.setModel(new DefaultComboBoxModel<>(selectableElternmithilfeCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        maercheneinteilungErfassenModel.setElternmithilfeCode(selectableElternmithilfeCodes[0]);
        comboBoxElternmithilfeCode.addActionListener(e -> onElternmithilfeCodeSelected());
    }

    private void onElternmithilfeCodeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event ElternmithilfeCode selected={}", comboBoxElternmithilfeCode.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxElternmithilfeCode.getSelectedItem(), maercheneinteilungErfassenModel.getElternmithilfeCode());
        setModelElternmithilfeCode();
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelElternmithilfeCode() {
        makeErrorLabelInvisible(Field.ELTERNMITHILFE_CODE);
        maercheneinteilungErfassenModel.setElternmithilfeCode((ElternmithilfeCode) comboBoxElternmithilfeCode.getSelectedItem());
    }

    public void setCheckBoxKuchenVorstellung1(JCheckBox checkBoxKuchenVorstellung1) {
        this.checkBoxKuchenVorstellung1 = checkBoxKuchenVorstellung1;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung1(false);
        }
        this.checkBoxKuchenVorstellung1.addItemListener(e -> onKuchenVorstellung1Event());
    }

    private void setModelKuchenVorstellung1() {
        maercheneinteilungErfassenModel.setKuchenVorstellung1(checkBoxKuchenVorstellung1.isSelected());
    }

    private void onKuchenVorstellung1Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung1. Selected={}", checkBoxKuchenVorstellung1.isSelected());
        setModelKuchenVorstellung1();
    }

    public void setCheckBoxKuchenVorstellung2(JCheckBox checkBoxKuchenVorstellung2) {
        this.checkBoxKuchenVorstellung2 = checkBoxKuchenVorstellung2;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung2(false);
        }
        this.checkBoxKuchenVorstellung2.addItemListener(e -> onKuchenVorstellung2Event());
    }

    private void setModelKuchenVorstellung2() {
        maercheneinteilungErfassenModel.setKuchenVorstellung2(checkBoxKuchenVorstellung2.isSelected());
    }

    private void onKuchenVorstellung2Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung2. Selected={}", checkBoxKuchenVorstellung2.isSelected());
        setModelKuchenVorstellung2();
    }

    public void setCheckBoxKuchenVorstellung3(JCheckBox checkBoxKuchenVorstellung3) {
        this.checkBoxKuchenVorstellung3 = checkBoxKuchenVorstellung3;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung3(false);
        }
        this.checkBoxKuchenVorstellung3.addItemListener(e -> onKuchenVorstellung3Event());
    }

    private void setModelKuchenVorstellung3() {
        maercheneinteilungErfassenModel.setKuchenVorstellung3(checkBoxKuchenVorstellung3.isSelected());
    }

    private void onKuchenVorstellung3Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung3. Selected={}", checkBoxKuchenVorstellung3.isSelected());
        setModelKuchenVorstellung3();
    }

    public void setCheckBoxKuchenVorstellung4(JCheckBox checkBoxKuchenVorstellung4) {
        this.checkBoxKuchenVorstellung4 = checkBoxKuchenVorstellung4;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung4(false);
        }
        this.checkBoxKuchenVorstellung4.addItemListener(e -> onKuchenVorstellung4Event());
    }

    private void setModelKuchenVorstellung4() {
        maercheneinteilungErfassenModel.setKuchenVorstellung4(checkBoxKuchenVorstellung4.isSelected());
    }

    private void onKuchenVorstellung4Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung4. Selected={}", checkBoxKuchenVorstellung4.isSelected());
        setModelKuchenVorstellung4();
    }

    public void setCheckBoxKuchenVorstellung5(JCheckBox checkBoxKuchenVorstellung5) {
        this.checkBoxKuchenVorstellung5 = checkBoxKuchenVorstellung5;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung5(false);
        }
        this.checkBoxKuchenVorstellung5.addItemListener(e -> onKuchenVorstellung5Event());
    }

    private void setModelKuchenVorstellung5() {
        maercheneinteilungErfassenModel.setKuchenVorstellung5(checkBoxKuchenVorstellung5.isSelected());
    }

    private void onKuchenVorstellung5Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung5. Selected={}", checkBoxKuchenVorstellung5.isSelected());
        setModelKuchenVorstellung5();
    }

    public void setCheckBoxKuchenVorstellung6(JCheckBox checkBoxKuchenVorstellung6) {
        this.checkBoxKuchenVorstellung6 = checkBoxKuchenVorstellung6;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung6(false);
        }
        this.checkBoxKuchenVorstellung6.addItemListener(e -> onKuchenVorstellung6Event());
    }

    private void setModelKuchenVorstellung6() {
        maercheneinteilungErfassenModel.setKuchenVorstellung6(checkBoxKuchenVorstellung6.isSelected());
    }

    private void onKuchenVorstellung6Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung6. Selected={}", checkBoxKuchenVorstellung6.isSelected());
        setModelKuchenVorstellung6();
    }

    public void setCheckBoxKuchenVorstellung7(JCheckBox checkBoxKuchenVorstellung7) {
        this.checkBoxKuchenVorstellung7 = checkBoxKuchenVorstellung7;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung7(false);
        }
        this.checkBoxKuchenVorstellung7.addItemListener(e -> onKuchenVorstellung7Event());
    }

    private void setModelKuchenVorstellung7() {
        maercheneinteilungErfassenModel.setKuchenVorstellung7(checkBoxKuchenVorstellung7.isSelected());
    }

    private void onKuchenVorstellung7Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung7. Selected={}", checkBoxKuchenVorstellung7.isSelected());
        setModelKuchenVorstellung7();
    }

    public void setCheckBoxKuchenVorstellung8(JCheckBox checkBoxKuchenVorstellung8) {
        this.checkBoxKuchenVorstellung8 = checkBoxKuchenVorstellung8;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung8(false);
        }
        this.checkBoxKuchenVorstellung8.addItemListener(e -> onKuchenVorstellung8Event());
    }

    private void setModelKuchenVorstellung8() {
        maercheneinteilungErfassenModel.setKuchenVorstellung8(checkBoxKuchenVorstellung8.isSelected());
    }

    private void onKuchenVorstellung8Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung8. Selected={}", checkBoxKuchenVorstellung8.isSelected());
        setModelKuchenVorstellung8();
    }

    public void setCheckBoxKuchenVorstellung9(JCheckBox checkBoxKuchenVorstellung9) {
        this.checkBoxKuchenVorstellung9 = checkBoxKuchenVorstellung9;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung9(false);
        }
        this.checkBoxKuchenVorstellung9.addItemListener(e -> onKuchenVorstellung9Event());
    }

    private void setModelKuchenVorstellung9() {
        maercheneinteilungErfassenModel.setKuchenVorstellung9(checkBoxKuchenVorstellung9.isSelected());
    }

    private void onKuchenVorstellung9Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung9. Selected={}", checkBoxKuchenVorstellung9.isSelected());
        setModelKuchenVorstellung9();
    }

    public void setTxtZusatzattribut(JTextField txtZusatzattribut) {
        this.txtZusatzattribut = txtZusatzattribut;
        if (!defaultButtonEnabled) {
            this.txtZusatzattribut.addActionListener(e -> onZusatzattributEvent(true));
        }
        this.txtZusatzattribut.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZusatzattributEvent(false);
            }
        });
    }

    private void onZusatzattributEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event Zusatzattribut");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZusatzattribut.getText(), maercheneinteilungErfassenModel.getZusatzattribut());
        try {
            setModelZusatzattribut(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZusatzattribut(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZUSATZATTRIBUT_MAERCHEN);
        try {
            maercheneinteilungErfassenModel.setZusatzattribut(txtZusatzattribut.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelZusatzattribut RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZusatzattribut.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelZusatzattribut Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public void setTxtBemerkungen(JTextField txtBemerkungen) {
        this.txtBemerkungen = txtBemerkungen;
        if (!defaultButtonEnabled) {
            this.txtBemerkungen.addActionListener(e -> onBemerkungenEvent(true));
        }
        this.txtBemerkungen.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onBemerkungenEvent(false);
            }
        });
    }

    private void onBemerkungenEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("MaercheneinteilungErfassenController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBemerkungen.getText(), maercheneinteilungErfassenModel.getBemerkungen());
        try {
            setModelBemerkungen(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelBemerkungen(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.BEMERKUNGEN);
        try {
            maercheneinteilungErfassenModel.setBemerkungen(txtBemerkungen.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBemerkungen RequiredException={}", e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBemerkungen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut, nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBemerkungen Exception={}", e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblMaerchen(JLabel errLblMaerchen) {
        this.errLblMaerchen = errLblMaerchen;
    }

    public void setErrLblGruppe(JLabel errLblGruppe) {
        this.errLblGruppe = errLblGruppe;
    }

    public void setErrLblRolle1(JLabel errLblRolle1) {
        this.errLblRolle1 = errLblRolle1;
    }

    public void setErrLblBilderRolle1(JLabel errLblBilderRolle1) {
        this.errLblBilderRolle1 = errLblBilderRolle1;
    }

    public void setErrLblRolle2(JLabel errLblRolle2) {
        this.errLblRolle2 = errLblRolle2;
    }

    public void setErrLblBilderRolle2(JLabel errLblBilderRolle2) {
        this.errLblBilderRolle2 = errLblBilderRolle2;
    }

    public void setErrLblRolle3(JLabel errLblRolle3) {
        this.errLblRolle3 = errLblRolle3;
    }

    public void setErrLblBilderRolle3(JLabel errLblBilderRolle3) {
        this.errLblBilderRolle3 = errLblBilderRolle3;
    }

    public void setErrLblElternmithilfe(JLabel errLblElternmithilfe) {
        this.errLblElternmithilfe = errLblElternmithilfe;
    }

    public void setErrLblElternmithilfeCode(JLabel errLblElternmithilfeCode) {
        this.errLblElternmithilfeCode = errLblElternmithilfeCode;
    }

    public void setErrLblZusatzattribut(JLabel errLblZusatzattribut) {
        this.errLblZusatzattribut = errLblZusatzattribut;
    }

    public void setErrLblBemerkungen(JLabel errLblBemerkungen) {
        this.errLblBemerkungen = errLblBemerkungen;
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
        int n = 0;
        if (!isBearbeiten && maercheneinteilungErfassenModel.checkIfMaerchenIsInPast()) {
            Object[] options = {"Ja", "Nein"};
            n = JOptionPane.showOptionDialog(
                    null,
                    "Das selektierte Märchen liegt in der Vergangenheit. Märcheneinteilung trotzdem speichern?",
                    "Märchen in Vergangenheit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,  //the titles of buttons
                    options[1]); //default button title
        }
        if (n == 0) {
            if (!maercheneinteilungErfassenModel.checkIfElternmithilfeHasTelefon(schuelerDatenblattModel)) {
                JOptionPane.showMessageDialog(maercheneinteilungErfassenDialog, "Für die Elternmithilfe sind weder Festnetz noch Natel erfasst. Bitte Schüler-Stammdaten ergänzen.", "Elternmithilfe ohne Telefon", JOptionPane.WARNING_MESSAGE);
            }
            if (!maercheneinteilungErfassenModel.checkIfElternmithilfeHasEmail(schuelerDatenblattModel)) {
                JOptionPane.showMessageDialog(maercheneinteilungErfassenDialog, "Für die Elternmithilfe ist keine E-Mail erfasst. Bitte Schüler-Stammdaten ergänzen.", "Elternmithilfe ohne E-Mail", JOptionPane.WARNING_MESSAGE);
            }
            maercheneinteilungErfassenModel.speichern(maercheneinteilungenTableModel, schuelerDatenblattModel);
        }
        maercheneinteilungErfassenDialog.dispose();
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        btnAbbrechen.addActionListener(e -> onAbbrechen());
    }

    private void onAbbrechen() {
        maercheneinteilungErfassenDialog.dispose();
    }

    private void onMaercheneinteilungErfassenModelCompleted(boolean completed) {
        LOGGER.trace("MaercheneinteilungErfassenModel completed={}", completed);
        if (completed) {
            btnSpeichern.setToolTipText(null);
            btnSpeichern.setEnabled(true);
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    private void makeUnusableCheckboxesInvisible() {
        checkBoxKuchenVorstellung2.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 2);
        checkBoxKuchenVorstellung3.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 3);
        checkBoxKuchenVorstellung4.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 4);
        checkBoxKuchenVorstellung5.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 5);
        checkBoxKuchenVorstellung6.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 6);
        checkBoxKuchenVorstellung7.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 7);
        checkBoxKuchenVorstellung8.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 8);
        checkBoxKuchenVorstellung9.setVisible(
                maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() >= 9);
    }

    @SuppressWarnings("DuplicatedCode")
    private void disableElternmithilfeFields(boolean disable) {
        boolean enabled = !disable;
        if (comboBoxElternmithilfe != null) {
            comboBoxElternmithilfe.setEnabled(enabled);
        }
        if (comboBoxElternmithilfeCode != null) {
            comboBoxElternmithilfeCode.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung1 != null) {
            checkBoxKuchenVorstellung1.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung2 != null) {
            checkBoxKuchenVorstellung2.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung3 != null) {
            checkBoxKuchenVorstellung3.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung4 != null) {
            checkBoxKuchenVorstellung4.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung5 != null) {
            checkBoxKuchenVorstellung5.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung6 != null) {
            checkBoxKuchenVorstellung6.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung7 != null) {
            checkBoxKuchenVorstellung7.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung8 != null) {
            checkBoxKuchenVorstellung8.setEnabled(enabled);
        }
        if (checkBoxKuchenVorstellung9 != null) {
            checkBoxKuchenVorstellung9.setEnabled(enabled);
        }
    }

    private void writeElternhilfeBereitsErfasstBemerkung() {
        if (!checkNotEmpty(maercheneinteilungErfassenModel.getBemerkungen())
                && !maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty()) {
            StringBuilder geschwisterStringBuilder = new StringBuilder();
            for (Maercheneinteilung maercheneinteilungGeschwister
                    : maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe) {
                geschwisterStringBuilder.append(maercheneinteilungGeschwister.getSchueler().getVorname())
                        .append(" ")
                        .append(maercheneinteilungGeschwister.getSchueler().getNachname())
                        .append(", ");
            }
            // Letztes ", " weglassen
            String geschwister = geschwisterStringBuilder.substring(0, geschwisterStringBuilder.length() - 2);
            try {
                maercheneinteilungErfassenModel.setBemerkungen("Eltern-Mithilfe bei " + geschwister + " erfasst.");
            } catch (SvmValidationException ignore) {
            }
        }
    }

    private void enableDisableFields() {
        if (maercheneinteilungErfassenModel.getElternmithilfe() == null || maercheneinteilungErfassenModel.getElternmithilfe() != Elternmithilfe.DRITTPERSON) {
            disableEltermithilfeDrittperson();
        } else {
            enableElternmithilfeDrittperson();
        }
    }

    private void enableElternmithilfeDrittperson() {
        maercheneinteilungErfassenModel.enableFields(getElternmithilfeDrittpersonFields());
    }

    private void disableEltermithilfeDrittperson() {
        maercheneinteilungErfassenModel.disableFields(getElternmithilfeDrittpersonFields());
        makeErrorLabelsInvisible(getElternmithilfeDrittpersonFields());
    }

    private Set<Field> getElternmithilfeDrittpersonFields() {
        Set<Field> zahlungenFields = new HashSet<>();
        zahlungenFields.add(Field.ANREDE);
        zahlungenFields.add(Field.NACHNAME);
        zahlungenFields.add(Field.VORNAME);
        zahlungenFields.add(Field.STRASSE_HAUSNUMMER);
        zahlungenFields.add(Field.PLZ);
        zahlungenFields.add(Field.ORT);
        zahlungenFields.add(Field.FESTNETZ);
        zahlungenFields.add(Field.NATEL);
        zahlungenFields.add(Field.EMAIL);
        return zahlungenFields;
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.MAERCHEN, evt)) {
            spinnerMaerchen.setValue(maercheneinteilungErfassenModel.getMaerchen());
            makeUnusableCheckboxesInvisible();
            maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe
                    = maercheneinteilungErfassenModel
                    .findMaercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe(
                            schuelerDatenblattModel);
            enableOrDisableElternmithilfeFields();
            if (!maercheneinteilungenVonGeschwisternMitBereitsErfassterElternmithilfe.isEmpty()) {
                writeElternhilfeBereitsErfasstBemerkung();
            }
        } else if (checkIsFieldChange(Field.GRUPPE, evt)) {
            comboBoxGruppe.setSelectedItem(maercheneinteilungErfassenModel.getGruppe());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.ROLLE1, evt)) {
            txtRolle1.setText(maercheneinteilungErfassenModel.getRolle1());
        } else if (checkIsFieldChange(Field.BILDER_ROLLE1, evt)) {
            txtBilderRolle1.setText(maercheneinteilungErfassenModel.getBilderRolle1());
        } else if (checkIsFieldChange(Field.ROLLE2, evt)) {
            txtRolle2.setText(maercheneinteilungErfassenModel.getRolle2());
        } else if (checkIsFieldChange(Field.BILDER_ROLLE2, evt)) {
            txtBilderRolle2.setText(maercheneinteilungErfassenModel.getBilderRolle2());
        } else if (checkIsFieldChange(Field.ROLLE3, evt)) {
            txtRolle3.setText(maercheneinteilungErfassenModel.getRolle3());
        } else if (checkIsFieldChange(Field.BILDER_ROLLE3, evt)) {
            txtBilderRolle3.setText(maercheneinteilungErfassenModel.getBilderRolle3());
        } else if (checkIsFieldChange(Field.ELTERNMITHILFE, evt)) {
            comboBoxElternmithilfe.setSelectedItem(maercheneinteilungErfassenModel.getElternmithilfe());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.ELTERNMITHILFE_CODE, evt)) {
            comboBoxElternmithilfeCode.setSelectedItem(maercheneinteilungErfassenModel.getElternmithilfeCode());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG1, evt)) {
            checkBoxKuchenVorstellung1.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung1());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG2, evt)) {
            checkBoxKuchenVorstellung2.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung2());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG3, evt)) {
            checkBoxKuchenVorstellung3.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung3());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG4, evt)) {
            checkBoxKuchenVorstellung4.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung4());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG5, evt)) {
            checkBoxKuchenVorstellung5.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung5());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG6, evt)) {
            checkBoxKuchenVorstellung6.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung6());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG7, evt)) {
            checkBoxKuchenVorstellung7.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung7());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG8, evt)) {
            checkBoxKuchenVorstellung8.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung8());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG9, evt)) {
            checkBoxKuchenVorstellung9.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung9());
            enableOrDisableElternmithilfeFields();
        } else if (checkIsFieldChange(Field.ZUSATZATTRIBUT_MAERCHEN, evt)) {
            txtZusatzattribut.setText(maercheneinteilungErfassenModel.getZusatzattribut());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            txtBemerkungen.setText(maercheneinteilungErfassenModel.getBemerkungen());
        }
        enableDisableFields();
    }

    @Override
    void validateFields() throws SvmValidationException {
        super.validateFields();
        if (spinnerMaerchen.isEnabled()) {
            LOGGER.trace("Validate field Maerchen");
            setModelMaerchen();
        }
        if (comboBoxGruppe.isEnabled()) {
            LOGGER.trace("Validate field Gruppe");
            setModelGruppe();
        }
        if (txtRolle1.isEnabled()) {
            LOGGER.trace("Validate field Rolle1");
            setModelRolle1(true);
        }
        if (txtBilderRolle1.isEnabled()) {
            LOGGER.trace("Validate field BilderRolle1");
            setModelBilderRolle1(true);
        }
        if (txtRolle2.isEnabled()) {
            LOGGER.trace("Validate field Rolle2");
            setModelRolle2(true);
        }
        if (txtBilderRolle2.isEnabled()) {
            LOGGER.trace("Validate field BilderRolle2");
            setModelBilderRolle2(true);
        }
        if (txtRolle3.isEnabled()) {
            LOGGER.trace("Validate field Rolle3");
            setModelRolle3(true);
        }
        if (txtBilderRolle3.isEnabled()) {
            LOGGER.trace("Validate field BilderRolle3");
            setModelBilderRolle3(true);
        }
        if (comboBoxElternmithilfe.isEnabled()) {
            LOGGER.trace("Validate field Elternmithilfe");
            setModelElternmithilfe();
        }
        if (comboBoxElternmithilfeCode.isEnabled()) {
            LOGGER.trace("Validate field ElternmithilfeCode");
            setModelElternmithilfeCode();
        }
        if (txtZusatzattribut.isEnabled()) {
            LOGGER.trace("Validate field Zusatzattribut");
            setModelZusatzattribut(true);
        }
        if (txtBemerkungen.isEnabled()) {
            LOGGER.trace("Validate field Bemerkungen");
            setModelBemerkungen(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        super.showErrMsg(e);
        if (e.getAffectedFields().contains(Field.MAERCHEN)) {
            errLblMaerchen.setVisible(true);
            errLblMaerchen.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GRUPPE)) {
            errLblGruppe.setVisible(true);
            errLblGruppe.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE1)) {
            errLblRolle1.setVisible(true);
            errLblRolle1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE1)) {
            errLblBilderRolle1.setVisible(true);
            errLblBilderRolle1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE2)) {
            errLblRolle2.setVisible(true);
            errLblRolle2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE2)) {
            errLblBilderRolle2.setVisible(true);
            errLblBilderRolle2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE3)) {
            errLblRolle3.setVisible(true);
            errLblRolle3.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE3)) {
            errLblBilderRolle3.setVisible(true);
            errLblBilderRolle3.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ELTERNMITHILFE)) {
            errLblElternmithilfe.setVisible(true);
            errLblElternmithilfe.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ELTERNMITHILFE_CODE)) {
            errLblElternmithilfeCode.setVisible(true);
            errLblElternmithilfeCode.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            errLblZusatzattribut.setVisible(true);
            errLblZusatzattribut.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        super.showErrMsgAsToolTip(e);
        if (e.getAffectedFields().contains(Field.MAERCHEN)) {
            spinnerMaerchen.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GRUPPE)) {
            comboBoxGruppe.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE1)) {
            txtRolle1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE1)) {
            txtBilderRolle1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE2)) {
            txtRolle2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE2)) {
            txtBilderRolle2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ROLLE3)) {
            txtRolle3.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BILDER_ROLLE3)) {
            txtBilderRolle3.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ELTERNMITHILFE)) {
            comboBoxElternmithilfe.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ELTERNMITHILFE_CODE)) {
            comboBoxElternmithilfeCode.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            txtZusatzattribut.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            txtBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        super.makeErrorLabelsInvisible(fields);
        if (fields.contains(Field.ALLE) || fields.contains(Field.MAERCHEN)) {
            errLblMaerchen.setVisible(false);
            spinnerMaerchen.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.GRUPPE)) {
            errLblGruppe.setVisible(false);
            comboBoxGruppe.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE1)) {
            errLblRolle1.setVisible(false);
            txtRolle1.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BILDER_ROLLE1)) {
            errLblBilderRolle1.setVisible(false);
            txtBilderRolle1.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE2)) {
            errLblRolle2.setVisible(false);
            txtRolle2.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BILDER_ROLLE2)) {
            errLblBilderRolle2.setVisible(false);
            txtBilderRolle2.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE3)) {
            errLblRolle3.setVisible(false);
            txtRolle3.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BILDER_ROLLE3)) {
            errLblBilderRolle3.setVisible(false);
            txtBilderRolle3.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ELTERNMITHILFE)) {
            errLblElternmithilfe.setVisible(false);
            comboBoxElternmithilfe.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ELTERNMITHILFE_CODE)) {
            errLblElternmithilfeCode.setVisible(false);
            comboBoxElternmithilfeCode.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSATZATTRIBUT_MAERCHEN)) {
            errLblZusatzattribut.setVisible(false);
            txtZusatzattribut.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            txtBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        super.disableFields(disable, fields);
    }

}
