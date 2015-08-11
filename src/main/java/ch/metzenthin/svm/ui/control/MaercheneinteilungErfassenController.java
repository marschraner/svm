package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Elternteil;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Gruppe;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.MaercheneinteilungErfassenModel;
import ch.metzenthin.svm.domain.model.MaercheneinteilungenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.ElternmithilfeCode;
import ch.metzenthin.svm.persistence.entities.Maerchen;
import ch.metzenthin.svm.ui.componentmodel.MaercheneinteilungenTableModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class MaercheneinteilungErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(MaercheneinteilungErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private MaercheneinteilungErfassenModel maercheneinteilungErfassenModel;
    private final SvmContext svmContext;
    private final MaercheneinteilungenTableModel maercheneinteilungenTableModel;
    private MaercheneinteilungenModel maercheneinteilungenModel;
    private SchuelerDatenblattModel schuelerDatenblattModel;
    private boolean isBearbeiten;
    private JDialog maercheneinteilungErfassenDialog;
    private JComboBox<Maerchen> comboBoxMaerchen;
    private JComboBox<Gruppe> comboBoxGruppe;
    private JTextField txtRolle1;
    private JTextField txtBilderRolle1;
    private JTextField txtRolle2;
    private JTextField txtBilderRolle2;
    private JTextField txtRolle3;
    private JTextField txtBilderRolle3;
    private JComboBox<Elternteil> comboBoxElternmithilfe;
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

    public MaercheneinteilungErfassenController(SvmContext svmContext, MaercheneinteilungErfassenModel maercheneinteilungErfassenModel, MaercheneinteilungenTableModel maercheneinteilungenTableModel, MaercheneinteilungenModel maercheneinteilungenModel, SchuelerDatenblattModel schuelerDatenblattModel, boolean isBearbeiten) {
        super(maercheneinteilungErfassenModel);
        this.svmContext = svmContext;
        this.maercheneinteilungErfassenModel = maercheneinteilungErfassenModel;
        this.maercheneinteilungenTableModel = maercheneinteilungenTableModel;
        this.maercheneinteilungenModel = maercheneinteilungenModel;
        this.schuelerDatenblattModel = schuelerDatenblattModel;
        this.isBearbeiten = isBearbeiten;
        this.maercheneinteilungErfassenModel.addPropertyChangeListener(this);
        this.maercheneinteilungErfassenModel.addDisableFieldsListener(this);
        this.maercheneinteilungErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.maercheneinteilungErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onMaercheneinteilungErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        maercheneinteilungErfassenModel.initializeCompleted();
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
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    
    public void setComboBoxMaerchen(JComboBox<Maerchen> comboBoxMaerchen) {
        this.comboBoxMaerchen = comboBoxMaerchen;
        // Darf nicht bearbeitet werden, da Teil der ID!
        if (isBearbeiten) {
            comboBoxMaerchen.setEnabled(false);
        }
        List<Maerchen> maerchenenList = svmContext.getSvmModel().getMaerchensAll();
        Maerchen[] selectableMaerchens;
        if (isBearbeiten) {
            List<Maerchen> maerchenList = svmContext.getSvmModel().getMaerchensAll();
            selectableMaerchens = maerchenList.toArray(new Maerchen[maerchenenList.size()]);
        } else {
            selectableMaerchens = maercheneinteilungenModel.getSelectableMaerchens(svmContext.getSvmModel(), schuelerDatenblattModel);
        }
        comboBoxMaerchen.setModel(new DefaultComboBoxModel<>(selectableMaerchens));
        // Ältestes selektierbares Märchen als Initialwert
        if (selectableMaerchens.length > 0) {
            try {
                maercheneinteilungErfassenModel.setMaerchen(selectableMaerchens[selectableMaerchens.length - 1]);
            } catch (SvmRequiredException ignore) {
            }
        } else {
            comboBoxMaerchen.setSelectedItem(null);
        }
        comboBoxMaerchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMaerchenSelected();
            }
        });
    }

    private void onMaerchenSelected() {
        LOGGER.trace("PersonController Event Maerchen selected=" + comboBoxMaerchen.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxMaerchen.getSelectedItem(), maercheneinteilungErfassenModel.getMaerchen());
        try {
            setModelMaerchen();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelMaerchen() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.MAERCHEN);
        try {
            maercheneinteilungErfassenModel.setMaerchen((Maerchen) comboBoxMaerchen.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelMaerchen RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxMaerchen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setComboBoxGruppe(JComboBox<Gruppe> comboBoxGruppe) {
        this.comboBoxGruppe = comboBoxGruppe;
        comboBoxGruppe.setModel(new DefaultComboBoxModel<>(Gruppe.values()));
        // Leeren ComboBox-Wert anzeigen
        comboBoxGruppe.setSelectedItem(null);
        comboBoxGruppe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGruppeSelected();
            }
        });
    }

    private void onGruppeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event Gruppe selected=" + comboBoxGruppe.getSelectedItem());
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelGruppe RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxGruppe.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setTxtRolle1(JTextField txtRolle1) {
        this.txtRolle1 = txtRolle1;
        this.txtRolle1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRolle1Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle1 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle1(JTextField txtBilderRolle1) {
        this.txtBilderRolle1 = txtBilderRolle1;
        this.txtBilderRolle1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBilderRolle1Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle1 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle1 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRolle2(JTextField txtRolle2) {
        this.txtRolle2 = txtRolle2;
        this.txtRolle2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRolle2Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle2 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle2 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle2(JTextField txtBilderRolle2) {
        this.txtBilderRolle2 = txtBilderRolle2;
        this.txtBilderRolle2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBilderRolle2Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle2 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle2.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle2 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtRolle3(JTextField txtRolle3) {
        this.txtRolle3 = txtRolle3;
        this.txtRolle3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRolle3Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle3 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtRolle3.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelRolle3 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBilderRolle3(JTextField txtBilderRolle3) {
        this.txtBilderRolle3 = txtBilderRolle3;
        this.txtBilderRolle3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBilderRolle3Event(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle3 RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBilderRolle3.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBilderRolle3 Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxElternmithilfe(JComboBox<Elternteil> comboBoxElternmithilfe) {
        this.comboBoxElternmithilfe = comboBoxElternmithilfe;
        comboBoxElternmithilfe.setModel(new DefaultComboBoxModel<>(Elternteil.values()));
        // Leeren ComboBox-Wert anzeigen
        comboBoxElternmithilfe.setSelectedItem(null);
        // SchuelerCode in Model initialisieren mit erstem ComboBox-Wert
        maercheneinteilungErfassenModel.setElternmithilfe(Elternteil.KEINER);
        comboBoxElternmithilfe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onElternmithilfeSelected();
            }
        });
    }

    private void onElternmithilfeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event Elternmithilfe selected=" + comboBoxElternmithilfe.getSelectedItem());
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
        maercheneinteilungErfassenModel.setElternmithilfe((Elternteil) comboBoxElternmithilfe.getSelectedItem());
    }

    public void setComboBoxElternmithilfeCode(JComboBox<ElternmithilfeCode> comboBoxElternmithilfeCode) {
        this.comboBoxElternmithilfeCode = comboBoxElternmithilfeCode;
        ElternmithilfeCode[] selectableElternmithilfeCodes = maercheneinteilungErfassenModel.getSelectableElternmithilfeCodes(svmContext.getSvmModel());
        comboBoxElternmithilfeCode.setModel(new DefaultComboBoxModel<>(selectableElternmithilfeCodes));
        // Model initialisieren mit erstem ComboBox-Wert
        maercheneinteilungErfassenModel.setElternmithilfeCode(selectableElternmithilfeCodes[0]);
        comboBoxElternmithilfeCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onElternmithilfeCodeSelected();
            }
        });
    }

    private void onElternmithilfeCodeSelected() {
        LOGGER.trace("MaercheneinteilungErfassenController Event ElternmithilfeCode selected=" + comboBoxElternmithilfeCode.getSelectedItem());
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
        this.checkBoxKuchenVorstellung1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung1Event();
            }
        });
    }

    private void setModelKuchenVorstellung1() {
        maercheneinteilungErfassenModel.setKuchenVorstellung1(checkBoxKuchenVorstellung1.isSelected());
    }

    private void onKuchenVorstellung1Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung1. Selected=" + checkBoxKuchenVorstellung1.isSelected());
        setModelKuchenVorstellung1();
    }

    public void setCheckBoxKuchenVorstellung2(JCheckBox checkBoxKuchenVorstellung2) {
        this.checkBoxKuchenVorstellung2 = checkBoxKuchenVorstellung2;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung2(false);
        }
        this.checkBoxKuchenVorstellung2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung2Event();
            }
        });
    }

    private void setModelKuchenVorstellung2() {
        maercheneinteilungErfassenModel.setKuchenVorstellung2(checkBoxKuchenVorstellung2.isSelected());
    }

    private void onKuchenVorstellung2Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung2. Selected=" + checkBoxKuchenVorstellung2.isSelected());
        setModelKuchenVorstellung2();
    }

    public void setCheckBoxKuchenVorstellung3(JCheckBox checkBoxKuchenVorstellung3) {
        this.checkBoxKuchenVorstellung3 = checkBoxKuchenVorstellung3;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung3(false);
        }
        this.checkBoxKuchenVorstellung3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung3Event();
            }
        });
    }

    private void setModelKuchenVorstellung3() {
        maercheneinteilungErfassenModel.setKuchenVorstellung3(checkBoxKuchenVorstellung3.isSelected());
    }

    private void onKuchenVorstellung3Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung3. Selected=" + checkBoxKuchenVorstellung3.isSelected());
        setModelKuchenVorstellung3();
    }

    public void setCheckBoxKuchenVorstellung4(JCheckBox checkBoxKuchenVorstellung4) {
        this.checkBoxKuchenVorstellung4 = checkBoxKuchenVorstellung4;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung4(false);
        }
        this.checkBoxKuchenVorstellung4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung4Event();
            }
        });
    }

    private void setModelKuchenVorstellung4() {
        maercheneinteilungErfassenModel.setKuchenVorstellung4(checkBoxKuchenVorstellung4.isSelected());
    }

    private void onKuchenVorstellung4Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung4. Selected=" + checkBoxKuchenVorstellung4.isSelected());
        setModelKuchenVorstellung4();
    }

    public void setCheckBoxKuchenVorstellung5(JCheckBox checkBoxKuchenVorstellung5) {
        this.checkBoxKuchenVorstellung5 = checkBoxKuchenVorstellung5;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung5(false);
        }
        this.checkBoxKuchenVorstellung5.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung5Event();
            }
        });
    }

    private void setModelKuchenVorstellung5() {
        maercheneinteilungErfassenModel.setKuchenVorstellung5(checkBoxKuchenVorstellung5.isSelected());
    }

    private void onKuchenVorstellung5Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung5. Selected=" + checkBoxKuchenVorstellung5.isSelected());
        setModelKuchenVorstellung5();
    }

    public void setCheckBoxKuchenVorstellung6(JCheckBox checkBoxKuchenVorstellung6) {
        this.checkBoxKuchenVorstellung6 = checkBoxKuchenVorstellung6;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung6(false);
        }
        this.checkBoxKuchenVorstellung6.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung6Event();
            }
        });
    }

    private void setModelKuchenVorstellung6() {
        maercheneinteilungErfassenModel.setKuchenVorstellung6(checkBoxKuchenVorstellung6.isSelected());
    }

    private void onKuchenVorstellung6Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung6. Selected=" + checkBoxKuchenVorstellung6.isSelected());
        setModelKuchenVorstellung6();
    }

    public void setCheckBoxKuchenVorstellung7(JCheckBox checkBoxKuchenVorstellung7) {
        this.checkBoxKuchenVorstellung7 = checkBoxKuchenVorstellung7;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung7(false);
        }
        this.checkBoxKuchenVorstellung7.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung7Event();
            }
        });
    }

    private void setModelKuchenVorstellung7() {
        maercheneinteilungErfassenModel.setKuchenVorstellung7(checkBoxKuchenVorstellung7.isSelected());
    }

    private void onKuchenVorstellung7Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung7. Selected=" + checkBoxKuchenVorstellung7.isSelected());
        setModelKuchenVorstellung7();
    }

    public void setCheckBoxKuchenVorstellung8(JCheckBox checkBoxKuchenVorstellung8) {
        this.checkBoxKuchenVorstellung8 = checkBoxKuchenVorstellung8;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung8(false);
        }
        this.checkBoxKuchenVorstellung8.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung8Event();
            }
        });
    }

    private void setModelKuchenVorstellung8() {
        maercheneinteilungErfassenModel.setKuchenVorstellung8(checkBoxKuchenVorstellung8.isSelected());
    }

    private void onKuchenVorstellung8Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung8. Selected=" + checkBoxKuchenVorstellung8.isSelected());
        setModelKuchenVorstellung8();
    }

    public void setCheckBoxKuchenVorstellung9(JCheckBox checkBoxKuchenVorstellung9) {
        this.checkBoxKuchenVorstellung9 = checkBoxKuchenVorstellung9;
        if (!isBearbeiten) {
            // false als Default-Wert
            maercheneinteilungErfassenModel.setKuchenVorstellung9(false);
        }
        this.checkBoxKuchenVorstellung9.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKuchenVorstellung9Event();
            }
        });
    }

    private void setModelKuchenVorstellung9() {
        maercheneinteilungErfassenModel.setKuchenVorstellung9(checkBoxKuchenVorstellung9.isSelected());
    }

    private void onKuchenVorstellung9Event() {
        LOGGER.trace("AngehoerigerController Event KuchenVorstellung9. Selected=" + checkBoxKuchenVorstellung9.isSelected());
        setModelKuchenVorstellung9();
    }

    public void setTxtZusatzattribut(JTextField txtZusatzattribut) {
        this.txtZusatzattribut = txtZusatzattribut;
        this.txtZusatzattribut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZusatzattributEvent(true);
            }
        });
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
        makeErrorLabelInvisible(Field.ZUSATZATTRIBUT);
        try {
            maercheneinteilungErfassenModel.setZusatzattribut(txtZusatzattribut.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelZusatzattribut RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZusatzattribut.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelZusatzattribut Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtBemerkungen(JTextField txtBemerkungen) {
        this.txtBemerkungen = txtBemerkungen;
        this.txtBemerkungen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBemerkungenEvent(true);
            }
        });
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
            LOGGER.trace("MaercheneinteilungErfassenController setModelBemerkungen RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBemerkungen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("MaercheneinteilungErfassenController setModelBemerkungen Exception=" + e.getMessage());
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
        maercheneinteilungErfassenModel.speichern(maercheneinteilungenTableModel, schuelerDatenblattModel);
        maercheneinteilungErfassenDialog.dispose();
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
        maercheneinteilungErfassenDialog.dispose();
    }

    private void onMaercheneinteilungErfassenModelCompleted(boolean completed) {
        LOGGER.trace("MaercheneinteilungErfassenModel completed=" + completed);
        if (completed) {
            btnSpeichern.setToolTipText(null);
            btnSpeichern.setEnabled(true);
        } else {
            btnSpeichern.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSpeichern.setEnabled(false);
        }
    }

    private void makeUnusableCheckboxesInvisible() {
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 2) {
            checkBoxKuchenVorstellung2.setVisible(false);
        } else {
            checkBoxKuchenVorstellung2.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 3) {
            checkBoxKuchenVorstellung3.setVisible(false);
        } else {
            checkBoxKuchenVorstellung3.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 4) {
            checkBoxKuchenVorstellung4.setVisible(false);
        } else {
            checkBoxKuchenVorstellung4.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 5) {
            checkBoxKuchenVorstellung5.setVisible(false);
        } else {
            checkBoxKuchenVorstellung5.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 6) {
            checkBoxKuchenVorstellung6.setVisible(false);
        } else {
            checkBoxKuchenVorstellung6.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 7) {
            checkBoxKuchenVorstellung7.setVisible(false);
        } else {
            checkBoxKuchenVorstellung7.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 8) {
            checkBoxKuchenVorstellung8.setVisible(false);
        } else {
            checkBoxKuchenVorstellung8.setVisible(true);
        }
        if (maercheneinteilungErfassenModel.getMaerchen().getAnzahlVorstellungen() < 9) {
            checkBoxKuchenVorstellung9.setVisible(false);
        } else {
            checkBoxKuchenVorstellung9.setVisible(true);
        }
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        if (checkIsFieldChange(Field.MAERCHEN, evt)) {
            comboBoxMaerchen.setSelectedItem(maercheneinteilungErfassenModel.getMaerchen());
            makeUnusableCheckboxesInvisible();
        } else if (checkIsFieldChange(Field.GRUPPE, evt)) {
            comboBoxGruppe.setSelectedItem(maercheneinteilungErfassenModel.getGruppe());
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
        } else if (checkIsFieldChange(Field.ELTERNMITHILFE_CODE, evt)) {
            comboBoxElternmithilfeCode.setSelectedItem(maercheneinteilungErfassenModel.getElternmithilfeCode());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG1, evt)) {
            checkBoxKuchenVorstellung1.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung1());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG2, evt)) {
            checkBoxKuchenVorstellung2.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung2());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG3, evt)) {
            checkBoxKuchenVorstellung3.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung3());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG4, evt)) {
            checkBoxKuchenVorstellung4.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung4());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG5, evt)) {
            checkBoxKuchenVorstellung5.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung5());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG6, evt)) {
            checkBoxKuchenVorstellung6.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung6());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG7, evt)) {
            checkBoxKuchenVorstellung7.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung7());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG8, evt)) {
            checkBoxKuchenVorstellung8.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung8());
        } else if (checkIsFieldChange(Field.KUCHEN_VORSTELLUNG9, evt)) {
            checkBoxKuchenVorstellung9.setSelected(maercheneinteilungErfassenModel.isKuchenVorstellung9());
        } else if (checkIsFieldChange(Field.ZUSATZATTRIBUT, evt)) {
            txtZusatzattribut.setText(maercheneinteilungErfassenModel.getZusatzattribut());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            txtBemerkungen.setText(maercheneinteilungErfassenModel.getBemerkungen());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxMaerchen.isEnabled()) {
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
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT)) {
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
        if (e.getAffectedFields().contains(Field.MAERCHEN)) {
            comboBoxMaerchen.setToolTipText(e.getMessage());
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
        if (e.getAffectedFields().contains(Field.ZUSATZATTRIBUT)) {
            txtZusatzattribut.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            txtBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.MAERCHEN)) {
            errLblMaerchen.setVisible(false);
            comboBoxMaerchen.setToolTipText(null);
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZUSATZATTRIBUT)) {
            errLblZusatzattribut.setVisible(false);
            txtZusatzattribut.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            txtBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
