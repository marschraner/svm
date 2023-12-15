package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Field;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SemesterrechnungBearbeitenModel;
import ch.metzenthin.svm.domain.model.SemesterrechnungenModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.components.SemesterrechnungenPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SemesterrechnungBearbeitenController extends SemesterrechnungController {

    private static final Logger LOGGER = LogManager.getLogger(SemesterrechnungBearbeitenController.class);

    // Möglichkeit zum Umschalten des validation modes (nicht dynamisch)
    private static final boolean MODEL_VALIDATION_MODE = false;

    private ActionListener closeListener;
    private ActionListener nextPanelListener;
    private final SvmContext svmContext;
    private SemesterrechnungBearbeitenModel semesterrechnungBearbeitenModel;
    private final SemesterrechnungenModel semesterrechnungenModel;
    private final SemesterrechnungenTableModel semesterrechnungenTableModel;
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
        disableVorrechnung(!semesterrechnungBearbeitenModel.isVorrechnungEnabled());
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
        // ACHTUNG: Vor Zuweisung des neuen Models invalidate auf altem Model aufrufen, damit Txt-Einträge alle auf null gesetzt werden.
        //          Sonst bleiben Txt-Werte, die null sein sollten, aber vor dem Blättern einen Wert hatten, mit dem alten Wert behaftet!
        semesterrechnungBearbeitenModel.invalidateAll();
        semesterrechnungBearbeitenModel.makeErrorLabelsInvisible(new HashSet<>(List.of(Field.ALLE)));
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
        lblMalRabattFaktorVorrechnung.setVisible(!semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty());
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
        lblMalRabattFaktorNachrechnung.setVisible(!semesterrechnungBearbeitenModel.getRabattFaktor().isEmpty());
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

    // Key-Listeners zum Enablen des Speichern-Buttons nach dem ersten Tastendruck
    @Override
    public void setTxtRechnungsdatumVorrechnung(JTextField txtRechnungsdatumVorrechnung) {
        super.setTxtRechnungsdatumVorrechnung(txtRechnungsdatumVorrechnung);
        txtRechnungsdatumVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtErmaessigungVorrechnung(JTextField txtErmaessigungVorrechnung) {
        super.setTxtErmaessigungVorrechnung(txtErmaessigungVorrechnung);
        txtErmaessigungVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtErmaessigungsgrundVorrechnung(JTextField txtErmaessigungsgrundVorrechnung) {
        super.setTxtErmaessigungsgrundVorrechnung(txtErmaessigungsgrundVorrechnung);
        txtErmaessigungsgrundVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtZuschlagVorrechnung(JTextField txtZuschlagVorrechnung) {
        super.setTxtZuschlagVorrechnung(txtZuschlagVorrechnung);
        txtZuschlagVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtZuschlagsgrundVorrechnung(JTextField txtZuschlagsgrundVorrechnung) {
        super.setTxtZuschlagsgrundVorrechnung(txtZuschlagsgrundVorrechnung);
        txtZuschlagsgrundVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtAnzahlWochenVorrechnung(JTextField txtAnzahlWochenVorrechnung) {
        super.setTxtAnzahlWochenVorrechnung(txtAnzahlWochenVorrechnung);
        txtAnzahlWochenVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtWochenbetragVorrechnung(JTextField txtWochenbetragVorrechnung) {
        super.setTxtWochenbetragVorrechnung(txtWochenbetragVorrechnung);
        txtWochenbetragVorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung1Vorrechnung(JTextField txtDatumZahlung1Vorrechnung) {
        super.setTxtDatumZahlung1Vorrechnung(txtDatumZahlung1Vorrechnung);
        txtDatumZahlung1Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung1Vorrechnung(JTextField txtBetragZahlung1Vorrechnung) {
        super.setTxtBetragZahlung1Vorrechnung(txtBetragZahlung1Vorrechnung);
        txtBetragZahlung1Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung2Vorrechnung(JTextField txtDatumZahlung2Vorrechnung) {
        super.setTxtDatumZahlung2Vorrechnung(txtDatumZahlung2Vorrechnung);
        txtDatumZahlung2Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung2Vorrechnung(JTextField txtBetragZahlung2Vorrechnung) {
        super.setTxtBetragZahlung2Vorrechnung(txtBetragZahlung2Vorrechnung);
        txtBetragZahlung2Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung3Vorrechnung(JTextField txtDatumZahlung3Vorrechnung) {
        super.setTxtDatumZahlung3Vorrechnung(txtDatumZahlung3Vorrechnung);
        txtDatumZahlung3Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung3Vorrechnung(JTextField txtBetragZahlung3Vorrechnung) {
        super.setTxtBetragZahlung3Vorrechnung(txtBetragZahlung3Vorrechnung);
        txtBetragZahlung3Vorrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtRechnungsdatumNachrechnung(JTextField txtRechnungsdatumNachrechnung) {
        super.setTxtRechnungsdatumNachrechnung(txtRechnungsdatumNachrechnung);
        txtRechnungsdatumNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtErmaessigungNachrechnung(JTextField txtErmaessigungNachrechnung) {
        super.setTxtErmaessigungNachrechnung(txtErmaessigungNachrechnung);
        txtErmaessigungNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtErmaessigungsgrundNachrechnung(JTextField txtErmaessigungsgrundNachrechnung) {
        super.setTxtErmaessigungsgrundNachrechnung(txtErmaessigungsgrundNachrechnung);
        txtErmaessigungsgrundNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtZuschlagNachrechnung(JTextField txtZuschlagNachrechnung) {
        super.setTxtZuschlagNachrechnung(txtZuschlagNachrechnung);
        txtZuschlagNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtZuschlagsgrundNachrechnung(JTextField txtZuschlagsgrundNachrechnung) {
        super.setTxtZuschlagsgrundNachrechnung(txtZuschlagsgrundNachrechnung);
        txtZuschlagsgrundNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtAnzahlWochenNachrechnung(JTextField txtAnzahlWochenNachrechnung) {
        super.setTxtAnzahlWochenNachrechnung(txtAnzahlWochenNachrechnung);
        txtAnzahlWochenNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtWochenbetragNachrechnung(JTextField txtWochenbetragNachrechnung) {
        super.setTxtWochenbetragNachrechnung(txtWochenbetragNachrechnung);
        txtWochenbetragNachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung1Nachrechnung(JTextField txtDatumZahlung1Nachrechnung) {
        super.setTxtDatumZahlung1Nachrechnung(txtDatumZahlung1Nachrechnung);
        txtDatumZahlung1Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung1Nachrechnung(JTextField txtBetragZahlung1Nachrechnung) {
        super.setTxtBetragZahlung1Nachrechnung(txtBetragZahlung1Nachrechnung);
        txtBetragZahlung1Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung2Nachrechnung(JTextField txtDatumZahlung2Nachrechnung) {
        super.setTxtDatumZahlung2Nachrechnung(txtDatumZahlung2Nachrechnung);
        txtDatumZahlung2Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung2Nachrechnung(JTextField txtBetragZahlung2Nachrechnung) {
        super.setTxtBetragZahlung2Nachrechnung(txtBetragZahlung2Nachrechnung);
        txtBetragZahlung2Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtDatumZahlung3Nachrechnung(JTextField txtDatumZahlung3Nachrechnung) {
        super.setTxtDatumZahlung3Nachrechnung(txtDatumZahlung3Nachrechnung);
        txtDatumZahlung3Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTxtBetragZahlung3Nachrechnung(JTextField txtBetragZahlung3Nachrechnung) {
        super.setTxtBetragZahlung3Nachrechnung(txtBetragZahlung3Nachrechnung);
        txtBetragZahlung3Nachrechnung.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    @Override
    public void setTextAreaBemerkungen(JTextArea textAreaBemerkungen) {
        super.setTextAreaBemerkungen(textAreaBemerkungen);
        textAreaBemerkungen.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableSpeichernDisableNavigation();
            }
        });
    }

    public void setBtnEmail(JButton btnEmail) {
        this.btnEmail = btnEmail;
        setEmailEnabledDisabled();
        btnEmail.addActionListener(e -> onEmail());
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
        btnWochenbetragVorrechnung.addActionListener(e -> onWochenbetragVorrechnung());
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
        btnWochenbetragNachrechnung.addActionListener(e -> onWochenbetragNachrechnung());
    }

    private void onWochenbetragNachrechnung() {
        btnWochenbetragNachrechnung.setFocusPainted(true);
        semesterrechnungBearbeitenModel.calculateWochenbetrag(Rechnungstyp.NACHRECHNUNG);
        btnWochenbetragNachrechnung.setFocusPainted(false);
    }

    public void setBtnZurueck(JButton btnZurueck) {
        this.btnZurueck = btnZurueck;
        btnZurueck.addActionListener(e -> onZurueck());
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
        btnErster.addActionListener(e -> onErster());
        enableBtnErster();
    }

    private void onErster() {
        scroll(0);
    }

    public void setBtnLetzter(JButton btnLetzter) {
        this.btnLetzter = btnLetzter;
        btnLetzter.addActionListener(e -> onLetzter());
        enableBtnLetzter();
    }

    private void onLetzter() {
        scroll(semesterrechnungenTableModel.getRowCount() - 1);
    }

    public void setBtnNachfolgender(JButton btnNachfolgender) {
        this.btnNachfolgender = btnNachfolgender;
        btnNachfolgender.addActionListener(e -> onNachfolgender());
        enableBtnNachfolgender();
    }

    private void onNachfolgender() {
        scroll(selectedRow + 1);
    }

    public void setBtnVorheriger(JButton btnVorheriger) {
        this.btnVorheriger = btnVorheriger;
        btnVorheriger.addActionListener(e -> onVorheriger());
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
        this.btnSpeichern.addActionListener(e -> onSpeichern());
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
        this.btnVerwerfen.addActionListener(e -> onVerwerfen());
    }

    private void onVerwerfen() {

        constructionDone();

        // Wenn beim Editieren ein ungültiger Wert eingegeben wurde, wurde dieser nicht ins Model geschrieben,
        // d.h. im Model befindet sich immer noch der Wert von SemesterOrigin.
        // In einem solchen Fall wird durch initializeModelWithSemesterOrigin kein PropertyChangeEvent ausgelöst,
        // und die Felder werden in der View nicht aktualisiert, d.h. sie zeigen immer noch die fehlerhafte Eingabe
        // an. Die View der entsprechenden Felder muss daher manuell aktualisiert werden.
        txtRechnungsdatumVorrechnung.setText(asString(semesterrechnungModel.getRechnungsdatumVorrechnung()));
        txtErmaessigungVorrechnung.setText(semesterrechnungModel.getErmaessigungVorrechnung() == null ? null : semesterrechnungModel.getErmaessigungVorrechnung().toString());
        txtErmaessigungsgrundVorrechnung.setText(semesterrechnungModel.getErmaessigungsgrundVorrechnung());
        txtZuschlagVorrechnung.setText(semesterrechnungModel.getZuschlagVorrechnung() == null ? null : semesterrechnungModel.getZuschlagVorrechnung().toString());
        txtZuschlagsgrundVorrechnung.setText(semesterrechnungModel.getZuschlagsgrundVorrechnung());
        txtAnzahlWochenVorrechnung.setText(semesterrechnungModel.getAnzahlWochenVorrechnung() == null ? null : Integer.toString(semesterrechnungModel.getAnzahlWochenVorrechnung()));
        txtWochenbetragVorrechnung.setText(semesterrechnungModel.getWochenbetragVorrechnung() == null ? null : semesterrechnungModel.getWochenbetragVorrechnung().toString());
        txtDatumZahlung1Vorrechnung.setText(asString(semesterrechnungModel.getDatumZahlung1Vorrechnung()));
        txtBetragZahlung1Vorrechnung.setText(semesterrechnungModel.getBetragZahlung1Vorrechnung() == null ? null : semesterrechnungModel.getBetragZahlung1Vorrechnung().toString());
        txtDatumZahlung2Vorrechnung.setText(asString(semesterrechnungModel.getDatumZahlung2Vorrechnung()));
        txtBetragZahlung2Vorrechnung.setText(semesterrechnungModel.getBetragZahlung2Vorrechnung() == null ? null : semesterrechnungModel.getBetragZahlung2Vorrechnung().toString());
        txtDatumZahlung3Vorrechnung.setText(asString(semesterrechnungModel.getDatumZahlung3Vorrechnung()));
        txtBetragZahlung3Vorrechnung.setText(semesterrechnungModel.getBetragZahlung3Vorrechnung() == null ? null : semesterrechnungModel.getBetragZahlung3Vorrechnung().toString());
        txtRechnungsdatumNachrechnung.setText(asString(semesterrechnungModel.getRechnungsdatumNachrechnung()));
        txtErmaessigungNachrechnung.setText(semesterrechnungModel.getErmaessigungNachrechnung() == null ? null : semesterrechnungModel.getErmaessigungNachrechnung().toString());
        txtErmaessigungsgrundNachrechnung.setText(semesterrechnungModel.getErmaessigungsgrundNachrechnung());
        txtZuschlagNachrechnung.setText(semesterrechnungModel.getZuschlagNachrechnung() == null ? null : semesterrechnungModel.getZuschlagNachrechnung().toString());
        txtZuschlagsgrundNachrechnung.setText(semesterrechnungModel.getZuschlagsgrundNachrechnung());
        txtAnzahlWochenNachrechnung.setText(semesterrechnungModel.getAnzahlWochenNachrechnung() == null ? null : Integer.toString(semesterrechnungModel.getAnzahlWochenNachrechnung()));
        txtWochenbetragNachrechnung.setText(semesterrechnungModel.getWochenbetragNachrechnung() == null ? null : semesterrechnungModel.getWochenbetragNachrechnung().toString());
        txtDatumZahlung1Nachrechnung.setText(asString(semesterrechnungModel.getDatumZahlung1Nachrechnung()));
        txtBetragZahlung1Nachrechnung.setText(semesterrechnungModel.getBetragZahlung1Nachrechnung() == null ? null : semesterrechnungModel.getBetragZahlung1Nachrechnung().toString());
        txtDatumZahlung2Nachrechnung.setText(asString(semesterrechnungModel.getDatumZahlung2Nachrechnung()));
        txtBetragZahlung2Nachrechnung.setText(semesterrechnungModel.getBetragZahlung2Nachrechnung() == null ? null : semesterrechnungModel.getBetragZahlung2Nachrechnung().toString());
        txtDatumZahlung3Nachrechnung.setText(asString(semesterrechnungModel.getDatumZahlung3Nachrechnung()));
        txtBetragZahlung3Nachrechnung.setText(semesterrechnungModel.getBetragZahlung3Nachrechnung() == null ? null : semesterrechnungModel.getBetragZahlung3Nachrechnung().toString());
        textAreaBemerkungen.setText(semesterrechnungModel.getBemerkungen());

        semesterrechnungBearbeitenModel.makeErrorLabelsInvisible(new HashSet<>(List.of(Field.ALLE)));

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
