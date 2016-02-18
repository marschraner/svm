package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmRequiredException;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.CompletedListener;
import ch.metzenthin.svm.domain.model.KursErfassenModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
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
public class KursErfassenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(KursErfassenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private KursErfassenModel kursErfassenModel;
    private final SvmContext svmContext;
    private final KurseSemesterwahlModel kurseSemesterwahlModel;
    private final KurseTableModel kurseTableModel;
    private JDialog kursErfassenDialog;
    private JComboBox<Kurstyp> comboBoxKurstyp;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Kursort> comboBoxKursort;
    private JComboBox<Mitarbeiter> comboBoxLehrkraft1;
    private JComboBox<Mitarbeiter> comboBoxLehrkraft2;
    private JTextField txtAltersbereich;
    private JTextField txtStufe;
    private JTextField txtZeitBeginn;
    private JTextField txtZeitEnde;
    private JTextField txtBemerkungen;
    private JLabel errLblKurstyp;
    private JLabel errLblAltersbereich;
    private JLabel errLblStufe;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblZeitEnde;
    private JLabel errLblKursort;
    private JLabel errLblLehrkraft1;
    private JLabel errLblLehrkraft2;
    private JLabel errLblBemerkungen;
    private JButton btnSpeichern;

    public KursErfassenController(SvmContext svmContext, KursErfassenModel kursErfassenModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel) {
        super(kursErfassenModel);
        this.svmContext = svmContext;
        this.kursErfassenModel = kursErfassenModel;
        this.kurseSemesterwahlModel = kurseSemesterwahlModel;
        this.kurseTableModel = kurseTableModel;
        this.kursErfassenModel.addPropertyChangeListener(this);
        this.kursErfassenModel.addDisableFieldsListener(this);
        this.kursErfassenModel.addMakeErrorLabelsInvisibleListener(this);
        this.kursErfassenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onKursErfassenModelCompleted(completed);
            }
        });
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        kursErfassenModel.initializeCompleted();
    }

    public void setKursErfassenDialog(JDialog kursErfassenDialog) {
        // call onCancel() when cross is clicked
        this.kursErfassenDialog = kursErfassenDialog;
        kursErfassenDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        kursErfassenDialog.addWindowListener(new WindowAdapter() {
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
    
    public void setComboBoxKurstyp(JComboBox<Kurstyp> comboBoxKurstyp) {
        this.comboBoxKurstyp = comboBoxKurstyp;
        Kurstyp[] selectableKurstypen = kursErfassenModel.getSelectableKurstypen(svmContext.getSvmModel());
        comboBoxKurstyp.setModel(new DefaultComboBoxModel<>(selectableKurstypen));
        // Leeren ComboBox-Wert anzeigen
        comboBoxKurstyp.setSelectedItem(null);
        comboBoxKurstyp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKurstypSelected();
            }
        });
    }

    private void onKurstypSelected() {
        LOGGER.trace("PersonController Event Kurstyp selected=" + comboBoxKurstyp.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxKurstyp.getSelectedItem(), kursErfassenModel.getKurstyp());
        try {
            setModelKurstyp();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelKurstyp() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.KURSTYP);
        try {
            kursErfassenModel.setKurstyp((Kurstyp) comboBoxKurstyp.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelKurstyp RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxKurstyp.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setTxtAltersbereich(JTextField txtAltersbereich) {
        this.txtAltersbereich = txtAltersbereich;
        this.txtAltersbereich.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAltersbereichEvent(true);
            }
        });
        this.txtAltersbereich.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAltersbereichEvent(false);
            }
        });
    }

    private void onAltersbereichEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KursErfassenController Event Altersbereich");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAltersbereich.getText(), kursErfassenModel.getAltersbereich());
        try {
            setModelAltersbereich(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAltersbereich(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ALTERSBEREICH);
        try {
            kursErfassenModel.setAltersbereich(txtAltersbereich.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelAltersbereich RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtAltersbereich.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursErfassenController setModelAltersbereich Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtStufe(JTextField txtStufe) {
        this.txtStufe = txtStufe;
        this.txtStufe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStufeEvent(true);
            }
        });
        this.txtStufe.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStufeEvent(false);
            }
        });
    }

    private void onStufeEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KursErfassenController Event Stufe");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStufe.getText(), kursErfassenModel.getStufe());
        try {
            setModelStufe(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStufe(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.STUFE);
        try {
            kursErfassenModel.setStufe(txtStufe.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelStufe RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtStufe.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursErfassenController setModelStufe Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
        comboBoxWochentag.setModel(new DefaultComboBoxModel<>(Wochentag.values()));
        comboBoxWochentag.removeItem(Wochentag.ALLE);
        comboBoxWochentag.removeItem(Wochentag.SONNTAG);
        // Leeren ComboBox-Wert anzeigen
        comboBoxWochentag.setSelectedItem(null);
        comboBoxWochentag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochentagSelected();
            }
        });
    }

    private void onWochentagSelected() {
        LOGGER.trace("KursErfassenController Event Wochentag selected=" + comboBoxWochentag.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxWochentag.getSelectedItem(), kursErfassenModel.getWochentag());
        try {
            setModelWochentag();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelWochentag() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.WOCHENTAG);
        try {
            kursErfassenModel.setWochentag((Wochentag) comboBoxWochentag.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelWochentag RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxWochentag.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setTxtZeitBeginn(JTextField txtZeitBeginn) {
        this.txtZeitBeginn = txtZeitBeginn;
        this.txtZeitBeginn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZeitBeginnEvent(true);
            }
        });
        this.txtZeitBeginn.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZeitBeginnEvent(false);
            }
        });
    }

    private void onZeitBeginnEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KursErfassenController Event ZeitBeginn");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitBeginn.getText(), kursErfassenModel.getZeitBeginn());
        try {
            setModelZeitBeginn(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZeitBeginn(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZEIT_BEGINN);
        try {
            kursErfassenModel.setZeitBeginn(txtZeitBeginn.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelZeitBeginn RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZeitBeginn.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursErfassenController setModelZeitBeginn Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setTxtZeitEnde(JTextField txtZeitEnde) {
        this.txtZeitEnde = txtZeitEnde;
        this.txtZeitEnde.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZeitEndeEvent(true);
            }
        });
        this.txtZeitEnde.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onZeitEndeEvent(false);
            }
        });
    }

    private void onZeitEndeEvent(boolean showRequiredErrMsg) {
        LOGGER.trace("KursErfassenController Event ZeitEnde");
        boolean equalFieldAndModelValue = equalsNullSafe(txtZeitEnde.getText(), kursErfassenModel.getZeitEnde());
        try {
            setModelZeitEnde(showRequiredErrMsg);
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelZeitEnde(boolean showRequiredErrMsg) throws SvmValidationException {
        makeErrorLabelInvisible(Field.ZEIT_ENDE);
        try {
            kursErfassenModel.setZeitEnde(txtZeitEnde.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelZeitEnde RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtZeitEnde.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursErfassenController setModelZeitEnde Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setComboBoxKursort(JComboBox<Kursort> comboBoxKursort) {
        this.comboBoxKursort = comboBoxKursort;
        Kursort[] selectableKursorte = kursErfassenModel.getSelectableKursorte(svmContext.getSvmModel());
        comboBoxKursort.setModel(new DefaultComboBoxModel<>(selectableKursorte));
        // Leeren ComboBox-Wert anzeigen
        comboBoxKursort.setSelectedItem(null);
        comboBoxKursort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKursortSelected();
            }
        });
    }

    private void onKursortSelected() {
        LOGGER.trace("KursErfassenController Event Kursort selected=" + comboBoxKursort.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxKursort.getSelectedItem(), kursErfassenModel.getKursort());
        try {
            setModelKursort();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelKursort() throws SvmRequiredException {
        makeErrorLabelInvisible(Field.KURSORT);
        try {
            kursErfassenModel.setKursort((Kursort) comboBoxKursort.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelKursort RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxKursort.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setComboBoxLehrkraft1(JComboBox<Mitarbeiter> comboBoxLehrkraft1) {
        this.comboBoxLehrkraft1 = comboBoxLehrkraft1;
        Mitarbeiter[] selectableLehrkraefte1 = kursErfassenModel.getSelectableLehrkraefte1(svmContext.getSvmModel());
        comboBoxLehrkraft1.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte1));
        // Leeren ComboBox-Wert anzeigen
        comboBoxLehrkraft1.setSelectedItem(null);
        comboBoxLehrkraft1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraft1Selected();
            }
        });
    }

    private void onLehrkraft1Selected() {
        LOGGER.trace("KursErfassenController Event Lehrkraft1 selected=" + comboBoxLehrkraft1.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft1.getSelectedItem(), kursErfassenModel.getMitarbeiter1());
        try {
            setModelLehrkraft1();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelLehrkraft1() throws SvmValidationException {
        makeErrorLabelInvisible(Field.LEHRKRAFT1);
        try {
            kursErfassenModel.setMitarbeiter1((Mitarbeiter) comboBoxLehrkraft1.getSelectedItem());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelLehrkraft1 RequiredException=" + e.getMessage());
            if (isModelValidationMode()) {
                comboBoxLehrkraft1.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        }
    }

    public void setComboBoxLehrkraft2(JComboBox<Mitarbeiter> comboBoxLehrkraft2) {
        this.comboBoxLehrkraft2 = comboBoxLehrkraft2;
        Mitarbeiter[] selectableLehrkraefte2 = kursErfassenModel.getSelectableLehrkraefte2(svmContext.getSvmModel());
        comboBoxLehrkraft2.setModel(new DefaultComboBoxModel<>(selectableLehrkraefte2));
        // Model initialisieren mit erstem ComboBox-Wert
        kursErfassenModel.setMitarbeiter2(selectableLehrkraefte2[0]);
        comboBoxLehrkraft2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLehrkraft2Selected();
            }
        });
    }

    private void onLehrkraft2Selected() {
        LOGGER.trace("KursErfassenController Event Lehrkraft2 selected=" + comboBoxLehrkraft2.getSelectedItem());
        boolean equalFieldAndModelValue = equalsNullSafe(comboBoxLehrkraft2.getSelectedItem(), kursErfassenModel.getMitarbeiter2());
        try {
            setModelLehrkraft2();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelLehrkraft2() throws SvmValidationException {
        makeErrorLabelInvisible(Field.LEHRKRAFT2);
        kursErfassenModel.setMitarbeiter2((Mitarbeiter) comboBoxLehrkraft2.getSelectedItem());
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
        LOGGER.trace("KursErfassenController Event Bemerkungen");
        boolean equalFieldAndModelValue = equalsNullSafe(txtBemerkungen.getText(), kursErfassenModel.getBemerkungen());
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
            kursErfassenModel.setBemerkungen(txtBemerkungen.getText());
        } catch (SvmRequiredException e) {
            LOGGER.trace("KursErfassenController setModelBemerkungen RequiredException=" + e.getMessage());
            if (isModelValidationMode() || !showRequiredErrMsg) {
                txtBemerkungen.setToolTipText(e.getMessage());
                // Keine weitere Aktion. Die Required-Prüfung erfolgt erneut nachdem alle Field-Prüfungen bestanden sind.
            } else {
                showErrMsg(e);
            }
            throw e;
        } catch (SvmValidationException e) {
            LOGGER.trace("KursErfassenController setModelBemerkungen Exception=" + e.getMessage());
            showErrMsg(e);
            throw e;
        }
    }

    public void setErrLblKurstyp(JLabel errLblKurstyp) {
        this.errLblKurstyp = errLblKurstyp;
    }

    public void setErrLblAltersbereich(JLabel errLblAltersbereich) {
        this.errLblAltersbereich = errLblAltersbereich;
    }

    public void setErrLblStufe(JLabel errLblStufe) {
        this.errLblStufe = errLblStufe;
    }

    public void setErrLblWochentag(JLabel errLblWochentag) {
        this.errLblWochentag = errLblWochentag;
    }

    public void setErrLblZeitBeginn(JLabel errLblZeitBeginn) {
        this.errLblZeitBeginn = errLblZeitBeginn;
    }

    public void setErrLblZeitEnde(JLabel errLblZeitEnde) {
        this.errLblZeitEnde = errLblZeitEnde;
    }

    public void setErrLblKursort(JLabel errLblKursort) {
        this.errLblKursort = errLblKursort;
    }

    public void setErrLblLehrkraft1(JLabel errLblLehrkraft1) {
        this.errLblLehrkraft1 = errLblLehrkraft1;
    }

    public void setErrLblLehrkraft2(JLabel errLblLehrkraft2) {
        this.errLblLehrkraft2 = errLblLehrkraft2;
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
        if (kursErfassenModel.checkKursBereitsErfasst(kurseTableModel)) {
            JOptionPane.showMessageDialog(kursErfassenDialog, "Kurs bereits erfasst.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSpeichern.setFocusPainted(false);
            return;
        }
        if (!kursErfassenModel.checkIfLektionsgebuehrenErfasst(svmContext.getSvmModel())) {
            JOptionPane.showMessageDialog(kursErfassenDialog, "Der Kurs kann nicht gespeichert werden, weil für die \n" +
                    "Kurslänge noch keine Lektionsgebühren erfasst sind.", "Fehler", JOptionPane.ERROR_MESSAGE, svmContext.getDialogIcons().getErrorIcon());
            btnSpeichern.setFocusPainted(false);
            return;
        }
        kursErfassenModel.speichern(svmContext.getSvmModel(), kurseSemesterwahlModel, kurseTableModel);
        kursErfassenDialog.dispose();
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
        kursErfassenDialog.dispose();
    }

    private void onKursErfassenModelCompleted(boolean completed) {
        LOGGER.trace("KursErfassenModel completed=" + completed);
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
        if (checkIsFieldChange(Field.KURSTYP, evt)) {
            comboBoxKurstyp.setSelectedItem(kursErfassenModel.getKurstyp());
        } else if (checkIsFieldChange(Field.ALTERSBEREICH, evt)) {
            txtAltersbereich.setText(kursErfassenModel.getAltersbereich());
        } else if (checkIsFieldChange(Field.STUFE, evt)) {
            txtStufe.setText(kursErfassenModel.getStufe());
        } else if (checkIsFieldChange(Field.WOCHENTAG, evt)) {
            comboBoxWochentag.setSelectedItem(kursErfassenModel.getWochentag());
        } else if (checkIsFieldChange(Field.ZEIT_BEGINN, evt)) {
            txtZeitBeginn.setText(asString(kursErfassenModel.getZeitBeginn()));
        } else if (checkIsFieldChange(Field.ZEIT_ENDE, evt)) {
            txtZeitEnde.setText(asString(kursErfassenModel.getZeitEnde()));
        } else if (checkIsFieldChange(Field.KURSORT, evt)) {
            comboBoxKursort.setSelectedItem(kursErfassenModel.getKursort());
        } else if (checkIsFieldChange(Field.LEHRKRAFT1, evt)) {
            comboBoxLehrkraft1.setSelectedItem(kursErfassenModel.getMitarbeiter1());
        } else if (checkIsFieldChange(Field.LEHRKRAFT2, evt)) {
            comboBoxLehrkraft2.setSelectedItem(kursErfassenModel.getMitarbeiter2());
        } else if (checkIsFieldChange(Field.BEMERKUNGEN, evt)) {
            txtBemerkungen.setText(kursErfassenModel.getBemerkungen());
        }
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (comboBoxKurstyp.isEnabled()) {
            LOGGER.trace("Validate field Kurstyp");
            setModelKurstyp();
        }
        if (txtZeitBeginn.isEnabled()) {
            LOGGER.trace("Validate field Altersbereich");
            setModelAltersbereich(true);
        }
        if (txtZeitEnde.isEnabled()) {
            LOGGER.trace("Validate field Stufe");
            setModelStufe(true);
        }
        if (comboBoxWochentag.isEnabled()) {
            LOGGER.trace("Validate field Wochentag");
            setModelWochentag();
        }
        if (txtZeitBeginn.isEnabled()) {
            LOGGER.trace("Validate field ZeitBeginn");
            setModelZeitBeginn(true);
        }
        if (txtZeitEnde.isEnabled()) {
            LOGGER.trace("Validate field ZeitEnde");
            setModelZeitEnde(true);
        }
        if (comboBoxKursort.isEnabled()) {
            LOGGER.trace("Validate field Kursort");
            setModelKursort();
        }
        if (comboBoxLehrkraft1.isEnabled()) {
            LOGGER.trace("Validate field Lehrkraft1");
            setModelLehrkraft1();
        }
        if (comboBoxLehrkraft2.isEnabled()) {
            LOGGER.trace("Validate field Lehrkraft2");
            setModelLehrkraft2();
        }
        if (txtBemerkungen.isEnabled()) {
            LOGGER.trace("Validate field Bemerkungen");
            setModelBemerkungen(true);
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.KURSTYP)) {
            errLblKurstyp.setVisible(true);
            errLblKurstyp.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ALTERSBEREICH)) {
            errLblAltersbereich.setVisible(true);
            errLblAltersbereich.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STUFE)) {
            errLblStufe.setVisible(true);
            errLblStufe.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENTAG)) {
            errLblWochentag.setVisible(true);
            errLblWochentag.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(true);
            errLblZeitBeginn.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_ENDE)) {
            errLblZeitEnde.setVisible(true);
            errLblZeitEnde.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.KURSORT)) {
            errLblKursort.setVisible(true);
            errLblKursort.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT1)) {
            errLblLehrkraft1.setVisible(true);
            errLblLehrkraft1.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT2)) {
            errLblLehrkraft2.setVisible(true);
            errLblLehrkraft2.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(true);
            errLblBemerkungen.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.KURSTYP)) {
            comboBoxKurstyp.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ALTERSBEREICH)) {
            txtAltersbereich.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STUFE)) {
            txtStufe.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.WOCHENTAG)) {
            comboBoxWochentag.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_BEGINN)) {
            txtZeitBeginn.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ZEIT_ENDE)) {
            txtZeitEnde.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.KURSORT)) {
            comboBoxKursort.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT1)) {
            comboBoxLehrkraft1.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.LEHRKRAFT2)) {
            comboBoxLehrkraft2.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.BEMERKUNGEN)) {
            txtBemerkungen.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.KURSTYP)) {
            errLblKurstyp.setVisible(false);
            comboBoxKurstyp.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ALTERSBEREICH)) {
            errLblAltersbereich.setVisible(false);
            txtAltersbereich.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STUFE)) {
            errLblStufe.setVisible(false);
            txtStufe.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENTAG)) {
            errLblWochentag.setVisible(false);
            comboBoxWochentag.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZEIT_BEGINN)) {
            errLblZeitBeginn.setVisible(false);
            txtZeitBeginn.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ZEIT_ENDE)) {
            errLblZeitEnde.setVisible(false);
            txtZeitEnde.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.KURSORT)) {
            errLblKursort.setVisible(false);
            comboBoxKursort.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT1)) {
            errLblLehrkraft1.setVisible(false);
            comboBoxLehrkraft1.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT2)) {
            errLblLehrkraft2.setVisible(false);
            comboBoxLehrkraft2.setToolTipText(null);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BEMERKUNGEN)) {
            errLblBemerkungen.setVisible(false);
            txtBemerkungen.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {}

}
