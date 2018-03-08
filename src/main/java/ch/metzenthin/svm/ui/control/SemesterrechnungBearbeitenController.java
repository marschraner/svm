package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SemesterrechnungBearbeitenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.SemesterrechnungenPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungBearbeitenController extends SemesterrechnungController {

    private static final Logger LOGGER = Logger.getLogger(SemesterrechnungBearbeitenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private final SvmContext svmContext;
    private SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel;
    private SemesterrechnungenModel semesterrechnungenModel;
    private SemesterrechnungenTableModel semesterrechnungenTableModel;
    private final JTable semesterrechnungenTable;
    private ActionListener zurueckZuSemesterrechnungSuchenListener;
    private int selectedRow;
    private JLabel lblNachnameVorname;
    private JLabel lblName;
    private JLabel lblStrasseNr;
    private JLabel lblPlzOrt;
    private JLabel lblFestnetz;
    private JLabel lblNatel;
    private JLabel lblEmail;
    private JLabel lblSchuelersVorrechnung;
    private JLabel lblKurseVorrechnung;
    private JLabel lblSechsJahresRabattVorrechnung;
    private JLabel lblErmaessigungVorrechnung;
    private JLabel lblZuschlagVorrechnung;
    private JLabel lblMalRabattFaktorVorrechnung;
    private JLabel lblRabattFaktorVorrechnung;
    private JLabel lblRechnungsbetragVorrechnung;
    private JLabel lblRestbetragVorrechnung;
    private JLabel lblSchuelersNachrechnung;
    private JLabel lblKurseNachrechnung;
    private JLabel lblSechsJahresRabattNachrechnung;
    private JLabel lblErmaessigungNachrechnung;
    private JLabel lblZuschlagNachrechnung;
    private JLabel lblMalRabattFaktorNachrechnung;
    private JLabel lblRabattFaktorNachrechnung;
    private JLabel lblRechnungsbetragNachrechnung;
    private JLabel lblRestbetragNachrechnung;
    private JLabel lblScrollPosition;
    private JButton btnEmail;
    private JButton btnWochenbetragVorrechnung;
    private JButton btnWochenbetragNachrechnung;
    private JButton btnErster;
    private JButton btnLetzter;
    private JButton btnNachfolgender;
    private JButton btnVorheriger;
    private JButton btnZurueck;
    private JButton btnSpeichern;
    private JButton btnVerwerfen;

    public SemesterrechnungBearbeitenController(SvmContext svmContext, SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel, SemesterrechnungenModel semesterrechnungenModel, SemesterrechnungenTableModel semesterrechnungenTableModel, JTable semesterrechnungenTable, int selectedRow, boolean defaultButtonEnabled) {
        super(svmContext, semesterrechnungBearbeitenModel, defaultButtonEnabled);
        this.svmContext = svmContext;
        this.semesterrechnungBearbeitenModel = semesterrechnungBearbeitenModel;
        this.semesterrechnungenModel = semesterrechnungenModel;
        this.semesterrechnungenTableModel = semesterrechnungenTableModel;
        this.semesterrechnungenTable = semesterrechnungenTable;
        this.selectedRow = selectedRow;
        this.semesterrechnungBearbeitenModel.addPropertyChangeListener(this);
        this.semesterrechnungBearbeitenModel.addDisableFieldsListener(this);
        this.semesterrechnungBearbeitenModel.addMakeErrorLabelsInvisibleListener(this);
        this.setModelValidationMode(MODEL_VALIDATION_MODE);
    }

    public void constructionDone() {
        semesterrechnungBearbeitenModel.initializeCompleted();
        enableNavigationDisableSpeichern();
        if (!semesterrechnungBearbeitenModel.isVorrechnungEnabled()) {
            disableVorrechnung(true);
        } else {
            disableVorrechnung(false);
        }
    }

    private void disableVorrechnung(boolean disable) {
        txtRechnungsdatumVorrechnung.setEnabled(!disable);
        txtErmaessigungVorrechnung.setEnabled(!disable);
        txtErmaessigungsgrundVorrechnung.setEnabled(!disable);
        txtZuschlagVorrechnung.setEnabled(!disable);
        txtZuschlagsgrundVorrechnung.setEnabled(!disable);
        txtAnzahlWochenVorrechnung.setEnabled(!disable);
        txtWochenbetragVorrechnung.setEnabled(!disable);
    }

    private void scroll(int selectedRow) {
        if ((this.selectedRow == selectedRow) || (selectedRow < 0) || (selectedRow >= semesterrechnungenTableModel.getRowCount())) {
            return;
        }
        this.selectedRow = selectedRow;
        setNewModel();
    }

    private void setNewModel() {
        semesterrechnungBearbeitenModel.removePropertyChangeListener(this);
        semesterrechnungBearbeitenModel.removeDisableFieldsListener(this);
        semesterrechnungBearbeitenModel.removeMakeErrorLabelsInvisibleListener(this);
        // ACHTUNG: Vor Zuweisung des neuen Model invalidate auf altem Model aufrufen, damit Txt-Einträge alle auf null gesetzt werden.
        //          Sonst bleiben Txt-Werte, die null sein sollten, aber vor dem Blättern einen Wert hatten, mit dem alten Wert behaftet!
        semesterrechnungBearbeitenModel.invalidateAll();
        semesterrechnungBearbeitenModel.makeErrorLabelsInvisible(new HashSet<>(Arrays.asList(new Field[]{Field.ALLE})));
        semesterrechnungBearbeitenModel = semesterrechnungenModel.getSemesterrechnungBearbeitenModel(svmContext, semesterrechnungenTableModel, convertRowIndexToModel());
        // ACHTUNG: Eltern-Model muss auch geändert werden!!! (-> gleiches Vorgehen wie in Konstruktor!)
        super.setSemesterrechnungModel(semesterrechnungBearbeitenModel);
        semesterrechnungBearbeitenModel.addPropertyChangeListener(this);
        semesterrechnungBearbeitenModel.addDisableFieldsListener(this);
        semesterrechnungBearbeitenModel.addMakeErrorLabelsInvisibleListener(this);
        setLblNachnameVorname();
        setLblName();
        setLblStrasseNr();
        setLblPlzOrt();
        setLblFestnetz();
        setLblNatel();
        setLblEmail();
        setLblSchuelersVorrechnung();
        setLblKurseVorrechnung();
        setLblSechsJahresRabattVorrechnung();
        setLblErmaessigungVorrechnung();
        setLblZuschlagVorrechnung();
        setLblMalRabattFaktorVorrechnung();
        setLblRabattFaktorVorrechnung();
        setLblRechnungsbetragVorrechnung();
        setLblRestbetragVorrechnung();
        setLblSchuelersNachrechnung();
        setLblKurseNachrechnung();
        setLblSechsJahresRabattNachrechnung();
        setLblErmaessigungNachrechnung();
        setLblZuschlagNachrechnung();
        setLblMalRabattFaktorNachrechnung();
        setLblRabattFaktorNachrechnung();
        setLblRechnungsbetragNachrechnung();
        setLblRestbetragNachrechnung();
        setLblScrollPosition();
        setEmailEnabledDisabled();
        constructionDone();
    }

    private int convertRowIndexToModel() {
        if (semesterrechnungenTable == null) {
            return selectedRow;
        }
        return semesterrechnungenTable.convertRowIndexToModel(selectedRow);
    }

    private void enableScrollButtons() {
        enableBtnErster();
        enableBtnLetzter();
        enableBtnVorheriger();
        enableBtnNachfolgender();
    }

    private void enableBtnNachfolgender() {
        btnNachfolgender.setEnabled(semesterrechnungenTableModel != null && selectedRow != (semesterrechnungenTableModel.getRowCount() - 1));
    }

    private void enableBtnVorheriger() {
        btnVorheriger.setEnabled(selectedRow != 0);
    }

    private void enableBtnLetzter() {
        btnLetzter.setEnabled(semesterrechnungenTableModel != null && selectedRow != (semesterrechnungenTableModel.getRowCount() - 1));
    }

    private void enableBtnErster() {
        btnErster.setEnabled(selectedRow != 0);
    }

    public void setLblNachnameVorname(JLabel lblNachnameVorname) {
        this.lblNachnameVorname = lblNachnameVorname;
        setLblNachnameVorname();
    }

    private void setLblNachnameVorname() {
        lblNachnameVorname.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerNachname() + " " + semesterrechnungBearbeitenModel.getRechnungsempfaengerVorname());
    }

    public void setLblName(JLabel lblNachname) {
        this.lblName = lblNachname;
        setLblName();
    }

    private void setLblName() {
        lblName.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerAnrede() + " " + semesterrechnungBearbeitenModel.getRechnungsempfaengerVorname() + " " + semesterrechnungBearbeitenModel.getRechnungsempfaengerNachname());
    }

    public void setLblStrasseNr(JLabel lblStrasseNr) {
        this.lblStrasseNr = lblStrasseNr;
        setLblStrasseNr();
    }

    private void setLblStrasseNr() {
        lblStrasseNr.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerStrasseNr());
    }

    public void setLblPlzOrt(JLabel lblPlzOrt) {
        this.lblPlzOrt = lblPlzOrt;
        setLblPlzOrt();
    }

    private void setLblPlzOrt() {
        lblPlzOrt.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerPlz() + " " + semesterrechnungBearbeitenModel.getRechnungsempfaengerOrt());
    }

    public void setLblFestnetz(JLabel lblFestnetz) {
        this.lblFestnetz = lblFestnetz;
        setLblFestnetz();
    }

    private void setLblFestnetz() {
        lblFestnetz.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerFestnetz());
    }

    public void setLblNatel(JLabel lblNatel) {
        this.lblNatel = lblNatel;
        setLblNatel();
    }

    private void setLblNatel() {
        lblNatel.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerNatel());
    }

    public void setLblEmail(JLabel lblEmail) {
        this.lblEmail = lblEmail;
        setLblEmail();
    }

    private void setLblEmail() {
        lblEmail.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerEmail());
    }

    public void setLblSchuelersVorrechnung(JLabel lblSchuelersVorrechnung) {
        this.lblSchuelersVorrechnung = lblSchuelersVorrechnung;
        setLblSchuelersVorrechnung();
    }

    private void setLblSchuelersVorrechnung() {
        lblSchuelersVorrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerSchuelersVorrechnung());
    }

    public void setLblKurseVorrechnung(JLabel lblKurseVorrechnung) {
        this.lblKurseVorrechnung = lblKurseVorrechnung;
        setLblKurseVorrechnung();
    }

    private void setLblKurseVorrechnung() {
        lblKurseVorrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerKurseVorrechnung());
    }

    public void setLblSechsJahresRabattVorrechnung(JLabel lblSechsJahresRabattVorrechnung) {
        this.lblSechsJahresRabattVorrechnung = lblSechsJahresRabattVorrechnung;
        setLblSechsJahresRabattVorrechnung();
    }

    private void setLblSechsJahresRabattVorrechnung() {
        lblSechsJahresRabattVorrechnung.setText(semesterrechnungBearbeitenModel.getSechsJahresRabattVorrechnung());
    }

    public void setLblErmaessigungVorrechnung(JLabel lblErmaessigungVorrechnung) {
        this.lblErmaessigungVorrechnung = lblErmaessigungVorrechnung;
        setLblErmaessigungVorrechnung();
    }

    private void setLblErmaessigungVorrechnung() {
        if (semesterrechnungBearbeitenModel.getErmaessigungVorrechnung() != null) {
            lblErmaessigungVorrechnung.setText(semesterrechnungBearbeitenModel.getErmaessigungVorrechnung().toString());
        }
    }

    public void setLblZuschlagVorrechnung(JLabel lblZuschlagVorrechnung) {
        this.lblZuschlagVorrechnung = lblZuschlagVorrechnung;
        setLblZuschlagVorrechnung();
    }

    private void setLblZuschlagVorrechnung() {
        if (semesterrechnungBearbeitenModel.getZuschlagVorrechnung() != null) {
            lblZuschlagVorrechnung.setText(semesterrechnungBearbeitenModel.getZuschlagVorrechnung().toString());
        }
    }

    public void setLblMalRabattFaktorVorrechnung(JLabel lblMalRabattFaktorVorrechnung) {
        this.lblMalRabattFaktorVorrechnung = lblMalRabattFaktorVorrechnung;
        setLblMalRabattFaktorVorrechnung();
    }

    private void setLblMalRabattFaktorVorrechnung() {
        if (semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty()) {
            lblMalRabattFaktorVorrechnung.setVisible(false);
        } else {
            lblMalRabattFaktorVorrechnung.setVisible(true);
        }
    }

    public void setLblRabattFaktorVorrechnung(JLabel lblRabattFaktorVorrechnung) {
        this.lblRabattFaktorVorrechnung = lblRabattFaktorVorrechnung;
        setLblRabattFaktorVorrechnung();
    }

    private void setLblRabattFaktorVorrechnung() {
        if (semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty()) {
            lblRabattFaktorVorrechnung.setVisible(false);
        } else {
            lblRabattFaktorVorrechnung.setVisible(true);
            lblRabattFaktorVorrechnung.setText(semesterrechnungBearbeitenModel.getRabattFaktor());
        }
    }

    public void setLblRechnungsbetragVorrechnung(JLabel lblRechnungsbetragVorrechnung) {
        this.lblRechnungsbetragVorrechnung = lblRechnungsbetragVorrechnung;
        setLblRechnungsbetragVorrechnung();
    }

    private void setLblRechnungsbetragVorrechnung() {
        lblRechnungsbetragVorrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsbetragVorrechnung());
    }

    public void setLblRestbetragVorrechnung(JLabel lblRestbetragVorrechnung) {
        this.lblRestbetragVorrechnung = lblRestbetragVorrechnung;
        setLblRestbetragVorrechnung();
    }

    private void setLblRestbetragVorrechnung() {
        lblRestbetragVorrechnung.setText(semesterrechnungBearbeitenModel.getRestbetragVorrechnung());
    }

    public void setLblSchuelersNachrechnung(JLabel lblSchuelersNachrechnung) {
        this.lblSchuelersNachrechnung = lblSchuelersNachrechnung;
        setLblSchuelersNachrechnung();
    }

    private void setLblSchuelersNachrechnung() {
        lblSchuelersNachrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerSchuelersNachrechnung());
    }

    public void setLblKurseNachrechnung(JLabel lblKurseNachrechnung) {
        this.lblKurseNachrechnung = lblKurseNachrechnung;
        setLblKurseNachrechnung();
    }

    private void setLblKurseNachrechnung() {
        lblKurseNachrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsempfaengerKurseNachrechnung());
    }

    public void setLblSechsJahresRabattNachrechnung(JLabel lblSechsJahresRabattNachrechnung) {
        this.lblSechsJahresRabattNachrechnung = lblSechsJahresRabattNachrechnung;
        setLblSechsJahresRabattNachrechnung();
    }

    private void setLblSechsJahresRabattNachrechnung() {
        lblSechsJahresRabattNachrechnung.setText(semesterrechnungBearbeitenModel.getSechsJahresRabattNachrechnung());
    }

    public void setLblErmaessigungNachrechnung(JLabel lblErmaessigungNachrechnung) {
        this.lblErmaessigungNachrechnung = lblErmaessigungNachrechnung;
        setLblErmaessigungNachrechnung();
    }

    private void setLblErmaessigungNachrechnung() {
        if (semesterrechnungBearbeitenModel.getErmaessigungNachrechnung() != null) {
            lblErmaessigungNachrechnung.setText(semesterrechnungBearbeitenModel.getErmaessigungNachrechnung().toString());
        }
    }

    public void setLblZuschlagNachrechnung(JLabel lblZuschlagNachrechnung) {
        this.lblZuschlagNachrechnung = lblZuschlagNachrechnung;
        setLblZuschlagNachrechnung();
    }

    private void setLblZuschlagNachrechnung() {
        if (semesterrechnungBearbeitenModel.getZuschlagNachrechnung() != null) {
            lblZuschlagNachrechnung.setText(semesterrechnungBearbeitenModel.getZuschlagNachrechnung().toString());
        }
    }

    public void setLblMalRabattFaktorNachrechnung(JLabel lblMalRabattFaktorNachrechnung) {
        this.lblMalRabattFaktorNachrechnung = lblMalRabattFaktorNachrechnung;
        setLblMalRabattFaktorNachrechnung();
    }

    private void setLblMalRabattFaktorNachrechnung() {
        if (semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty()) {
            lblMalRabattFaktorNachrechnung.setVisible(false);
        } else {
            lblMalRabattFaktorNachrechnung.setVisible(true);
        }
    }

    public void setLblRabattFaktorNachrechnung(JLabel lblRabattFaktorNachrechnung) {
        this.lblRabattFaktorNachrechnung = lblRabattFaktorNachrechnung;
        setLblRabattFaktorNachrechnung();
    }

    private void setLblRabattFaktorNachrechnung() {
        if (semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty()) {
            lblRabattFaktorNachrechnung.setVisible(false);
        } else {
            lblRabattFaktorNachrechnung.setVisible(true);
            lblRabattFaktorNachrechnung.setText(semesterrechnungBearbeitenModel.getRabattFaktor());
        }
    }

    public void setLblRechnungsbetragNachrechnung(JLabel lblRechnungsbetragNachrechnung) {
        this.lblRechnungsbetragNachrechnung = lblRechnungsbetragNachrechnung;
        setLblRechnungsbetragNachrechnung();
    }

    private void setLblRechnungsbetragNachrechnung() {
        lblRechnungsbetragNachrechnung.setText(semesterrechnungBearbeitenModel.getRechnungsbetragNachrechnung());
    }

    public void setLblRestbetragNachrechnung(JLabel lblRestbetragNachrechnung) {
        this.lblRestbetragNachrechnung = lblRestbetragNachrechnung;
        setLblRestbetragNachrechnung();
    }

    private void setLblRestbetragNachrechnung() {
        lblRestbetragNachrechnung.setText(semesterrechnungBearbeitenModel.getRestbetragNachrechnung());
    }

    public void setLblScrollPosition(JLabel lblScrollPosition) {
        this.lblScrollPosition = lblScrollPosition;
        setLblScrollPosition();
    }

    private void setLblScrollPosition() {
        lblScrollPosition.setText((selectedRow + 1) + " / " + semesterrechnungenTableModel.getRowCount());
    }

    public void setBtnEmail(JButton btnEmail) {
        this.btnEmail = btnEmail;
        setEmailEnabledDisabled();
        btnEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmail();
            }
        });
    }

    private void setEmailEnabledDisabled() {
        if (!semesterrechnungBearbeitenModel.checkIfRechnungsempfaengerHasEmail()) {
            this.btnEmail.setEnabled(false);
            return;
        }
        this.btnEmail.setEnabled(true);
    }

    private void onEmail() {
        btnEmail.setFocusPainted(true);
        semesterrechnungBearbeitenModel.callEmailClient();
        btnEmail.setFocusPainted(false);
    }

    public void setBtnWochenbetragVorrechnung(JButton btnWochenbetragVorrechnung) {
        this.btnWochenbetragVorrechnung = btnWochenbetragVorrechnung;
        // Nicht sichtbar, da verwirrend und nicht benötigt. Ggf später ganz löschen.
        btnWochenbetragVorrechnung.setVisible(false);
        btnWochenbetragVorrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochenbetragVorrechnung();
            }
        });
    }

    private void onWochenbetragVorrechnung() {
        btnWochenbetragVorrechnung.setFocusPainted(true);
        semesterrechnungBearbeitenModel.calculateWochenbetrag(Rechnungstyp.VORRECHNUNG);
        btnWochenbetragVorrechnung.setFocusPainted(false);
    }

    @Override
    void onRechnungsdatumNachrechnungEvent() {
        LOGGER.trace("SemesterrechnungBearbeitenController Event RechnungsdatumNachrechnung");
        boolean equalFieldAndModelValue = equalsNullSafe(txtRechnungsdatumNachrechnung.getText(), semesterrechnungBearbeitenModel.getRechnungsdatumNachrechnung());
        try {
            setModelRechnungsdatumNachrechnung();
            // Beim Setzen des Rechnungsdatums der Nachrechnung die Zahlungen der Vorrechnung einmalig in die Nachrechnung kopieren
            semesterrechnungBearbeitenModel.copyZahlungenVorrechnungToZahlungenNachrechnung();
        } catch (SvmValidationException e) {
            return;
        }
        if (equalFieldAndModelValue && isModelValidationMode()) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    public void setBtnWochenbetragNachrechnung(JButton btnWochenbetragNachrechnung) {
        this.btnWochenbetragNachrechnung = btnWochenbetragNachrechnung;
        // Nicht sichtbar, da verwirrend und nicht benötigt. Ggf später ganz löschen.
        btnWochenbetragNachrechnung.setVisible(false);
        btnWochenbetragNachrechnung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onWochenbetragNachrechnung();
            }
        });
    }

    private void onWochenbetragNachrechnung() {
        btnWochenbetragNachrechnung.setFocusPainted(true);
        semesterrechnungBearbeitenModel.calculateWochenbetrag(Rechnungstyp.NACHRECHNUNG);
        btnWochenbetragNachrechnung.setFocusPainted(false);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        this.btnZurueck = btnZurueck;
        btnZurueck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });
    }

    private void onZurueck() {
        if (semesterrechnungenTableModel != null && semesterrechnungenTableModel.getRowCount() > 1) {
            SemesterrechnungenPanel semesterrechnungenPanel = new SemesterrechnungenPanel(svmContext, semesterrechnungenTableModel, false);
            semesterrechnungenPanel.addNextPanelListener(nextPanelListener);
            semesterrechnungenPanel.addCloseListener(closeListener);
            semesterrechnungenPanel.addZurueckListener(zurueckZuSemesterrechnungSuchenListener);
            nextPanelListener.actionPerformed(new ActionEvent(new Object[]{semesterrechnungenPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat"));
        } else {
            zurueckZuSemesterrechnungSuchenListener.actionPerformed(new ActionEvent(btnZurueck, ActionEvent.ACTION_PERFORMED, "Zurück zu Semesterrechnungen suchen"));
        }
    }

    public void setBtnErster(JButton btnErster) {
        this.btnErster = btnErster;
        btnErster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onErster();
            }
        });
        enableBtnErster();
    }

    private void onErster() {
        scroll(0);
    }

    public void setBtnLetzter(JButton btnLetzter) {
        this.btnLetzter = btnLetzter;
        btnLetzter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLetzter();
            }
        });
        enableBtnLetzter();
    }

    private void onLetzter() {
        scroll(semesterrechnungenTableModel.getRowCount() - 1);
    }

    public void setBtnNachfolgender(JButton btnNachfolgender) {
        this.btnNachfolgender = btnNachfolgender;
        btnNachfolgender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachfolgender();
            }
        });
        enableBtnNachfolgender();
    }

    private void onNachfolgender() {
        scroll(selectedRow + 1);
    }

    public void setBtnVorheriger(JButton btnVorheriger) {
        this.btnVorheriger = btnVorheriger;
        btnVorheriger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVorheriger();
            }
        });
        enableBtnVorheriger();
    }

    private void onVorheriger() {
        scroll(selectedRow - 1);
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
        semesterrechnungBearbeitenModel.speichern(semesterrechnungenTableModel);
        btnSpeichern.setFocusPainted(false);
        enableNavigationDisableSpeichern();
    }

    public void setBtnVerwerfen(JButton btnVerwerfen) {
        this.btnVerwerfen = btnVerwerfen;
        if (isModelValidationMode()) {
            btnVerwerfen.setEnabled(false);
        }
        this.btnVerwerfen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVerwerfen();
            }
        });
    }

    private void onVerwerfen() {
        semesterrechnungBearbeitenModel.makeErrorLabelsInvisible(new HashSet<>(Arrays.asList(new Field[]{Field.ALLE})));
        constructionDone();
        btnVerwerfen.setFocusPainted(false);
        enableNavigationDisableSpeichern();
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addZurueckZuSemesterrechnungSuchenListener(ActionListener zurueckZuSemesterrechnungSuchenListener) {
        this.zurueckZuSemesterrechnungSuchenListener = zurueckZuSemesterrechnungSuchenListener;
    }

    private void updateLabels() {
        setLblErmaessigungVorrechnung();
        setLblZuschlagVorrechnung();
        setLblMalRabattFaktorVorrechnung();
        setLblRabattFaktorVorrechnung();
        setLblRechnungsbetragVorrechnung();
        setLblRestbetragVorrechnung();
        setLblErmaessigungNachrechnung();
        setLblZuschlagNachrechnung();
        setLblMalRabattFaktorNachrechnung();
        setLblRabattFaktorNachrechnung();
        setLblRechnungsbetragNachrechnung();
        setLblRestbetragNachrechnung();
    }

    private void enableDisableFields() {
        if (semesterrechnungBearbeitenModel.getRechnungsdatumVorrechnung() == null) {
            disableZahlungenVorrechnung();
        } else {
            enableZahlungenVorrechnung();
        }
        if (semesterrechnungBearbeitenModel.getRechnungsdatumNachrechnung() == null) {
            disableZahlungenNachrechnung();
        } else {
            enableZahlungenNachrechnung();
        }
    }

    private void enableZahlungenVorrechnung() {
        semesterrechnungBearbeitenModel.enableFields(getZahlungenVorrechnungenFields());
    }

    private void enableZahlungenNachrechnung() {
        semesterrechnungBearbeitenModel.enableFields(getZahlungenNachrechnungenFields());
    }

    private void disableZahlungenVorrechnung() {
        semesterrechnungBearbeitenModel.disableFields(getZahlungenVorrechnungenFields());
    }

    private void disableZahlungenNachrechnung() {
        semesterrechnungBearbeitenModel.disableFields(getZahlungenNachrechnungenFields());
    }

    private Set<Field> getZahlungenVorrechnungenFields() {
        Set<Field> zahlungenVorrechnungenFields = new HashSet<>();
        zahlungenVorrechnungenFields.add(Field.BETRAG_ZAHLUNG_1_VORRECHNUNG);
        zahlungenVorrechnungenFields.add(Field.DATUM_ZAHLUNG_1_VORRECHNUNG);
        zahlungenVorrechnungenFields.add(Field.BETRAG_ZAHLUNG_2_VORRECHNUNG);
        zahlungenVorrechnungenFields.add(Field.DATUM_ZAHLUNG_2_VORRECHNUNG);
        zahlungenVorrechnungenFields.add(Field.BETRAG_ZAHLUNG_3_VORRECHNUNG);
        zahlungenVorrechnungenFields.add(Field.DATUM_ZAHLUNG_3_VORRECHNUNG);
        return zahlungenVorrechnungenFields;
    }

    private Set<Field> getZahlungenNachrechnungenFields() {
        Set<Field> zahlungenNachrechnungenFields = new HashSet<>();
        zahlungenNachrechnungenFields.add(Field.BETRAG_ZAHLUNG_1_NACHRECHNUNG);
        zahlungenNachrechnungenFields.add(Field.DATUM_ZAHLUNG_1_NACHRECHNUNG);
        zahlungenNachrechnungenFields.add(Field.BETRAG_ZAHLUNG_2_NACHRECHNUNG);
        zahlungenNachrechnungenFields.add(Field.DATUM_ZAHLUNG_2_NACHRECHNUNG);
        zahlungenNachrechnungenFields.add(Field.BETRAG_ZAHLUNG_3_NACHRECHNUNG);
        zahlungenNachrechnungenFields.add(Field.DATUM_ZAHLUNG_3_NACHRECHNUNG);
        return zahlungenNachrechnungenFields;
    }

    private void enableNavigationDisableSpeichern() {
        if (btnErster == null || btnVorheriger == null || btnNachfolgender == null || btnLetzter == null || btnZurueck == null || btnSpeichern == null || btnVerwerfen == null) {
            return;
        }
        enableScrollButtons();
        btnZurueck.setEnabled(true);
        btnSpeichern.setEnabled(false);
        btnVerwerfen.setEnabled(false);
    }

    private void enableSpeichernDisableNavigation() {
        if (btnErster == null || btnVorheriger == null || btnNachfolgender == null || btnLetzter == null || btnZurueck == null || btnSpeichern == null || btnVerwerfen == null) {
            return;
        }
        btnErster.setEnabled(false);
        btnVorheriger.setEnabled(false);
        btnNachfolgender.setEnabled(false);
        btnLetzter.setEnabled(false);
        btnZurueck.setEnabled(false);
        btnSpeichern.setEnabled(true);
        btnVerwerfen.setEnabled(true);
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        super.doPropertyChange(evt);
        updateLabels();
        enableDisableFields();
        enableSpeichernDisableNavigation();
    }

}
