package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenResult;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.Set;

import static ch.metzenthin.svm.common.utils.Converter.asString;
import static ch.metzenthin.svm.common.utils.SimpleValidator.equalsNullSafe;

/**
 * @author Martin Schraner
 */
public class SchuelerSuchenController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(SchuelerSuchenController.class);

    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasseHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
    private JTextField txtGeburtsdatum;
    private JTextField txtCodes;
    private JTextField txtLehrkraft;
    private JTextField txtVon;
    private JTextField txtBis;
    private JTextField txtAnAbmeldemonat;
    private JTextField txtStichtag;
    private JRadioButton radioBtnSchueler;
    private JRadioButton radioBtnEltern;
    private JRadioButton radioBtnRechnungsempfaenger;
    private JRadioButton radioBtnRolleAlle;
    private JRadioButton radioBtnAngemeldet;
    private JRadioButton radioBtnAbgemeldet;
    private JRadioButton radioBtnAnmeldestatusAlle;
    private JRadioButton radioBtnDispensiert;
    private JRadioButton radioBtnNichtDispensiert;
    private JRadioButton radioBtnDispensationAlle;
    private JRadioButton radioBtnWeiblich;
    private JRadioButton radioBtnMaennlich;
    private JRadioButton radioBtnGeschlechtAlle;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JCheckBox checkBoxStammdatenBeruecksichtigen;
    private JCheckBox checkBoxKursBeruecksichtigen;
    private JCheckBox checkBoxCodesBeruecksichtigen;
    private JCheckBox checkBoxAnAbmeldestatistikBeruecksichtigen;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblStrasseHausnummer;
    private JLabel errLblPlz;
    private JLabel errLblOrt;
    private JLabel errLblFestnetz;
    private JLabel errLblNatel;
    private JLabel errLblEmail;
    private JLabel errLblGeburtsdatum;
    private JLabel errLblStichtag;
    private JLabel errLblAnAbmeldemonat;
    private JButton btnSuchen;
    private JButton btnAbbrechen;
    private ActionListener closeListener;
    private ActionListener nextPanelListener;

    private SchuelerSuchenModel schuelerSuchenModel;

    public SchuelerSuchenController(SchuelerSuchenModel schuelerSuchenModel) {
        super(schuelerSuchenModel);
        this.schuelerSuchenModel = schuelerSuchenModel;
        this.schuelerSuchenModel.addPropertyChangeListener(this);
        this.schuelerSuchenModel.addDisableFieldsListener(this);
        this.schuelerSuchenModel.addMakeErrorLabelsInvisibleListener(this);
        this.schuelerSuchenModel.addCompletedListener(new CompletedListener() {
            @Override
            public void completed(boolean completed) {
                onSchuelerSuchenModelCompleted(completed);
            }
        });
    }

    private void onSchuelerSuchenModelCompleted(boolean completed) {
        LOGGER.trace("SchuelerSuchenModel completed=" + completed);
        if (completed) {
            btnSuchen.setToolTipText(null);
            btnSuchen.setEnabled(true);
        } else {
            btnSuchen.setToolTipText("Bitte Eingabedaten vervollständigen");
            btnSuchen.setEnabled(false);
        }
    }

    public void setTxtNachname(JTextField txtNachname) {
        this.txtNachname = txtNachname;
        this.txtNachname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNachnameEvent();
            }
        });
        this.txtNachname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNachnameEvent();
            }
        });
    }

    public void setTxtVorname(JTextField txtVorname) {
        this.txtVorname = txtVorname;
        this.txtVorname.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVornameEvent();
            }
        });
        this.txtVorname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onVornameEvent();
            }
        });
    }

    public void setTxtStrasseHausnummer(JTextField txtStrasseHausnummer) {
        this.txtStrasseHausnummer = txtStrasseHausnummer;
        this.txtStrasseHausnummer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStrasseHausnummerEvent();
            }
        });
        this.txtStrasseHausnummer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStrasseHausnummerEvent();
            }
        });
    }

    public void setTxtPlz(JTextField txtPlz) {
        this.txtPlz = txtPlz;
        this.txtPlz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPlzEvent();
            }
        });
        this.txtPlz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onPlzEvent();
            }
        });
    }

    public void setTxtOrt(JTextField txtOrt) {
        this.txtOrt = txtOrt;
        this.txtOrt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOrtEvent();
            }
        });
        this.txtOrt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onOrtEvent();
            }
        });
    }

    public void setTxtFestnetz(JTextField txtFestnetz) {
        this.txtFestnetz = txtFestnetz;
        this.txtFestnetz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFestnetzEvent();
            }
        });
        this.txtFestnetz.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onFestnetzEvent();
            }
        });
    }

    public void setTxtNatel(JTextField txtNatel) {
        this.txtNatel = txtNatel;
        this.txtNatel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNatelEvent();
            }
        });
        this.txtNatel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onNatelEvent();
            }
        });
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
        this.txtEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEmailEvent();
            }
        });
        this.txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onEmailEvent();
            }
        });
    }

    public void setTxtGeburtsdatum(JTextField txtGeburtsdatum) {
        this.txtGeburtsdatum = txtGeburtsdatum;
        this.txtGeburtsdatum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGeburtsdatumEvent();
            }
        });
        this.txtGeburtsdatum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onGeburtsdatumEvent();
            }
        });
    }

    public void setTxtLehrkraft(JTextField txtLehrkraft) {
        this.txtLehrkraft = txtLehrkraft;
    }

    public void setComboBoxWochentag(JComboBox<Wochentag> comboBoxWochentag) {
        this.comboBoxWochentag = comboBoxWochentag;
    }

    public void setTxtVon(JTextField txtVon) {
        this.txtVon = txtVon;
    }

    public void setTxtBis(JTextField txtBis) {
        this.txtBis = txtBis;
    }

    public void setTxtCodes(JTextField txtCodes) {
        this.txtCodes = txtCodes;
    }

    public void setTxtAnAbmeldemonat(JTextField txtAnAbmeldemonat) {
        this.txtAnAbmeldemonat = txtAnAbmeldemonat;
        this.txtAnAbmeldemonat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAnmeldemonatEvent();
            }
        });
        this.txtAnAbmeldemonat.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onAnmeldemonatEvent();
            }
        });
    }

    public void setTxtStichtag(JTextField txtStichtag) {
        this.txtStichtag = txtStichtag;
        this.txtStichtag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStichtagEvent();
            }
        });
        this.txtStichtag.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onStichtagEvent();
            }
        });
    }

    public void setRadioBtnGroupRolle(JRadioButton radioBtnSchueler, JRadioButton radioBtnEltern, JRadioButton radioBtnRechnungsempfaenger, JRadioButton radioBtnRolleAlle) {
        this.radioBtnSchueler = radioBtnSchueler;
        this.radioBtnEltern = radioBtnEltern;
        this.radioBtnRechnungsempfaenger = radioBtnRechnungsempfaenger;
        this.radioBtnRolleAlle = radioBtnRolleAlle;
        // Action Commands
        this.radioBtnSchueler.setActionCommand(SchuelerSuchenModel.RolleSelected.SCHUELER.toString());
        this.radioBtnEltern.setActionCommand(SchuelerSuchenModel.RolleSelected.ELTERN.toString());
        this.radioBtnRechnungsempfaenger.setActionCommand(SchuelerSuchenModel.RolleSelected.RECHNUNGSEMPFAENGER.toString());
        this.radioBtnRolleAlle.setActionCommand(SchuelerSuchenModel.RolleSelected.ALLE.toString());
        // Listener
        RadioBtnGroupRolleListener radioBtnGroupRolleListener = new RadioBtnGroupRolleListener();
        this.radioBtnSchueler.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnEltern.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRechnungsempfaenger.addActionListener(radioBtnGroupRolleListener);
        this.radioBtnRolleAlle.addActionListener(radioBtnGroupRolleListener);
    }

    public void setRadioBtnGroupAnmeldestatus(JRadioButton radioBtnAngemeldet, JRadioButton radioBtnAbgemeldet, JRadioButton radioBtnAnmeldestatusAlle) {
        this.radioBtnAngemeldet = radioBtnAngemeldet;
        this.radioBtnAbgemeldet = radioBtnAbgemeldet;
        this.radioBtnAnmeldestatusAlle = radioBtnAnmeldestatusAlle;
        // Action Commands
        this.radioBtnAngemeldet.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ANGEMELDET.toString());
        this.radioBtnAbgemeldet.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ABGEMELDET.toString());
        this.radioBtnAnmeldestatusAlle.setActionCommand(SchuelerSuchenModel.AnmeldestatusSelected.ALLE.toString());
        // Listener
        RadioBtnGroupAnmeldestatusListener radioBtnGroupAnmeldestatusListener = new RadioBtnGroupAnmeldestatusListener();
        this.radioBtnAngemeldet.addActionListener(radioBtnGroupAnmeldestatusListener);
        this.radioBtnAbgemeldet.addActionListener(radioBtnGroupAnmeldestatusListener);
        this.radioBtnAnmeldestatusAlle.addActionListener(radioBtnGroupAnmeldestatusListener);
    }

    public void setRadioBtnGroupDispensation(JRadioButton radioBtnDispensiert, JRadioButton radioBtnNichtDispensiert, JRadioButton radioBtnDispensationAlle) {
        this.radioBtnDispensiert = radioBtnDispensiert;
        this.radioBtnNichtDispensiert = radioBtnNichtDispensiert;
        this.radioBtnDispensationAlle = radioBtnDispensationAlle;
        // Action Commands
        this.radioBtnDispensiert.setActionCommand(SchuelerSuchenModel.DispensationSelected.DISPENSIERT.toString());
        this.radioBtnNichtDispensiert.setActionCommand(SchuelerSuchenModel.DispensationSelected.NICHT_DISPENSIERT.toString());
        this.radioBtnDispensationAlle.setActionCommand(SchuelerSuchenModel.DispensationSelected.ALLE.toString());
        // Listener
        RadioBtnGroupDispensationListener radioBtnGroupDispensationListener = new RadioBtnGroupDispensationListener();
        this.radioBtnDispensiert.addActionListener(radioBtnGroupDispensationListener);
        this.radioBtnNichtDispensiert.addActionListener(radioBtnGroupDispensationListener);
        this.radioBtnDispensationAlle.addActionListener(radioBtnGroupDispensationListener);
    }

    public void setRadioBtnGroupGeschlecht(JRadioButton radioBtnWeiblich, JRadioButton radioBtnMaennlich, JRadioButton radioBtnGeschlechtAlle) {
        this.radioBtnWeiblich = radioBtnWeiblich;
        this.radioBtnMaennlich = radioBtnMaennlich;
        this.radioBtnGeschlechtAlle = radioBtnGeschlechtAlle;
        // Action Commands
        this.radioBtnWeiblich.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.WEIBLICH.toString());
        this.radioBtnMaennlich.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.MAENNLICH.toString());
        this.radioBtnGeschlechtAlle.setActionCommand(SchuelerSuchenModel.GeschlechtSelected.ALLE.toString());
        // Listener
        RadioBtnGroupGeschlechtListener radioBtnGroupGeschlechtListener = new RadioBtnGroupGeschlechtListener();
        this.radioBtnWeiblich.addActionListener(radioBtnGroupGeschlechtListener);
        this.radioBtnMaennlich.addActionListener(radioBtnGroupGeschlechtListener);
        this.radioBtnGeschlechtAlle.addActionListener(radioBtnGroupGeschlechtListener);
    }

    public void setRadioBtnGroupAnAbmeldungen(JRadioButton radioBtnAnmeldungen, JRadioButton radioBtnAbmeldungen) {
        this.radioBtnAnmeldungen = radioBtnAnmeldungen;
        this.radioBtnAbmeldungen = radioBtnAbmeldungen;
        // Action Commands
        this.radioBtnAnmeldungen.setActionCommand(SchuelerSuchenModel.AnAbmeldungenSelected.ANMELDUNGEN.toString());
        this.radioBtnAbmeldungen.setActionCommand(SchuelerSuchenModel.AnAbmeldungenSelected.ABMELDUNGEN.toString());
        // Listener
        RadioBtnGroupAnAbmeldungenListener radioBtnGroupAnAbmeldungenListener = new RadioBtnGroupAnAbmeldungenListener();
        this.radioBtnAnmeldungen.addActionListener(radioBtnGroupAnAbmeldungenListener);
        this.radioBtnAbmeldungen.addActionListener(radioBtnGroupAnAbmeldungenListener);
    }

    public void setCheckBoxStammdatenBeruecksichtigen(JCheckBox checkBoxStammdatenBeruecksichtigen) {
        this.checkBoxStammdatenBeruecksichtigen = checkBoxStammdatenBeruecksichtigen;
        this.checkBoxStammdatenBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onStammdatenBeruecksichtigenEvent();
            }
        });
    }

    public void setCheckBoxKursBeruecksichtigen(JCheckBox checkBoxKursBeruecksichtigen) {
        this.checkBoxKursBeruecksichtigen = checkBoxKursBeruecksichtigen;
        this.checkBoxKursBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onKursBeruecksichtigenEvent();
            }
        });
    }

    public void setCheckBoxCodesBeruecksichtigen(JCheckBox checkBoxCodesBeruecksichtigen) {
        this.checkBoxCodesBeruecksichtigen = checkBoxCodesBeruecksichtigen;
        this.checkBoxCodesBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onCodesBeruecksichtigenEvent();
            }
        });
    }

    public void setCheckBoxAnAbmeldestatistikBeruecksichtigen(JCheckBox checkBoxAnAbmeldestatistikBeruecksichtigen) {
        this.checkBoxAnAbmeldestatistikBeruecksichtigen = checkBoxAnAbmeldestatistikBeruecksichtigen;
        this.checkBoxAnAbmeldestatistikBeruecksichtigen.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onAnAbmeldestatistikBeruecksichtigenEvent();
            }
        });
    }

    public void setBtnSuchen(JButton btnSuchen) {
        this.btnSuchen = btnSuchen;
        this.btnSuchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSuchen();
            }
        });
    }

    public void setBtnAbbrechen(JButton btnAbbrechen) {
        this.btnAbbrechen = btnAbbrechen;
        this.btnAbbrechen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAbbrechen();
            }
        });
    }

    public void setErrLblNachname(JLabel errLblNachname) {
        this.errLblNachname = errLblNachname;
    }

    public void setErrLblVorname(JLabel errLblVorname) {
        this.errLblVorname = errLblVorname;
    }

    public void setErrLblStrasseHausnummer(JLabel errLblStrasseHausnummer) {
        this.errLblStrasseHausnummer = errLblStrasseHausnummer;
    }

    public void setErrLblPlz(JLabel errLblPlz) {
        this.errLblPlz = errLblPlz;
    }

    public void setErrLblOrt(JLabel errLblOrt) {
        this.errLblOrt = errLblOrt;
    }

    public void setErrLblFestnetz(JLabel errLblFestnetz) {
        this.errLblFestnetz = errLblFestnetz;
    }

    public void setErrLblNatel(JLabel errLblNatel) {
        this.errLblNatel = errLblNatel;
    }

    public void setErrLblEmail(JLabel errLblEmail) {
        this.errLblEmail = errLblEmail;
    }

    public void setErrLblGeburtsdatum(JLabel errLblGeburtsdatum) {
        this.errLblGeburtsdatum = errLblGeburtsdatum;
    }

    public void setErrLblStichtag(JLabel errLblStichtag) {
        this.errLblStichtag = errLblStichtag;
    }

    public void setErrLblAnAbmeldemonat(JLabel errLblAnAbmeldemonat) {
        this.errLblAnAbmeldemonat = errLblAnAbmeldemonat;
    }

    public void addCloseListener(ActionListener closeListener) {
        this.closeListener = closeListener;
    }

    public void addNextPanelListener(ActionListener nextPanelListener) {
        this.nextPanelListener = nextPanelListener;
    }

    private void onNachnameEvent() {
        LOGGER.trace("SchuelerSuchenController Event Nachname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNachname.getText(), schuelerSuchenModel.getNachname());
        setModelNachname();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNachname() {
        errLblNachname.setVisible(false);
        try {
            schuelerSuchenModel.setNachname(txtNachname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelNachname Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onVornameEvent() {
        LOGGER.trace("SchuelerSuchenController Event Vorname");
        boolean equalFieldAndModelValue = equalsNullSafe(txtVorname.getText(), schuelerSuchenModel.getVorname());
        setModelVorname();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelVorname() {
        errLblVorname.setVisible(false);
        try {
            schuelerSuchenModel.setVorname(txtVorname.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelVorname Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onStrasseHausnummerEvent() {
        LOGGER.trace("SchuelerSuchenController Event StrasseHausnummer");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStrasseHausnummer.getText(), schuelerSuchenModel.getStrasseHausnummer());
        setModelStrasseHausnummer();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStrasseHausnummer() {
        errLblStrasseHausnummer.setVisible(false);
        try {
            schuelerSuchenModel.setStrasseHausnummer(txtStrasseHausnummer.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelStrasseHausnummer Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onPlzEvent() {
        LOGGER.trace("SchuelerSuchenController Event Plz");
        boolean equalFieldAndModelValue = equalsNullSafe(txtPlz.getText(), schuelerSuchenModel.getPlz());
        setModelPlz();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelPlz() {
        errLblPlz.setVisible(false);
        try {
            schuelerSuchenModel.setPlz(txtPlz.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelPlz Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onOrtEvent() {
        LOGGER.trace("SchuelerSuchenController Event Ort");
        boolean equalFieldAndModelValue = equalsNullSafe(txtOrt.getText(), schuelerSuchenModel.getOrt());
        setModelOrt();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelOrt() {
        errLblOrt.setVisible(false);
        try {
            schuelerSuchenModel.setOrt(txtOrt.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelOrt Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onFestnetzEvent() {
        LOGGER.trace("SchuelerSuchenController Event Festnetz");
        boolean equalFieldAndModelValue = equalsNullSafe(txtFestnetz.getText(), schuelerSuchenModel.getFestnetz());
        setModelFestnetz();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelFestnetz() {
        errLblFestnetz.setVisible(false);
        try {
            schuelerSuchenModel.setFestnetz(txtFestnetz.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelFestnetz Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onNatelEvent() {
        LOGGER.trace("SchuelerSuchenController Event Natel");
        boolean equalFieldAndModelValue = equalsNullSafe(txtNatel.getText(), schuelerSuchenModel.getNatel());
        setModelNatel();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelNatel() {
        errLblNatel.setVisible(false);
        try {
            schuelerSuchenModel.setNatel(txtNatel.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelNatel Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onEmailEvent() {
        LOGGER.trace("SchuelerSuchenController Event Email");
        boolean equalFieldAndModelValue = equalsNullSafe(txtEmail.getText(), schuelerSuchenModel.getEmail());
        setModelEmail();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelEmail() {
        errLblEmail.setVisible(false);
        try {
            schuelerSuchenModel.setEmail(txtEmail.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelEmail Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onGeburtsdatumEvent() {
        LOGGER.trace("SchuelerSuchenController Event Geburtsdatum");
        boolean equalFieldAndModelValue = equalsNullSafe(txtGeburtsdatum.getText(), schuelerSuchenModel.getGeburtsdatum());
        setModelGeburtsdatum();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelGeburtsdatum() {
        errLblGeburtsdatum.setVisible(false);
        try {
            schuelerSuchenModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelGeburtsdatum Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onAnmeldemonatEvent() {
        LOGGER.trace("SchuelerSuchenController Event An-/Abmeldemonat");
        boolean equalFieldAndModelValue = equalsNullSafe(txtAnAbmeldemonat.getText(), schuelerSuchenModel.getAnAbmeldemonat());
        setModelAnAbmeldemonat();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelAnAbmeldemonat() {
        errLblAnAbmeldemonat.setVisible(false);
        try {
            schuelerSuchenModel.setAnAbmeldemonat(txtAnAbmeldemonat.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelAnAbmeldemonat Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onStichtagEvent() {
        LOGGER.trace("SchuelerSuchenController Event Stichtag");
        boolean equalFieldAndModelValue = equalsNullSafe(txtStichtag.getText(), schuelerSuchenModel.getStichtag());
        setModelStichtag();
        if (equalFieldAndModelValue) {
            // Wenn Field und Model den gleichen Wert haben, erfolgt kein PropertyChangeEvent. Deshalb muss hier die Validierung angestossen werden.
            LOGGER.trace("Validierung wegen equalFieldAndModelValue");
            validate();
        }
    }

    private void setModelStichtag() {
        errLblStichtag.setVisible(false);
        try {
            schuelerSuchenModel.setStichtag(txtStichtag.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelStichtag Exception=" + e.getMessage());
            showErrMsg(e);
        }
    }

    private void onStammdatenBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event StammdatenBeruecksichtigen. Selected=" + checkBoxStammdatenBeruecksichtigen.isSelected());
        schuelerSuchenModel.setStammdatenBeruecksichtigen(checkBoxStammdatenBeruecksichtigen.isSelected());
    }

    private void onKursBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event KursBeruecksichtigen. Selected=" + checkBoxKursBeruecksichtigen.isSelected());
        schuelerSuchenModel.setKursBeruecksichtigen(checkBoxKursBeruecksichtigen.isSelected());
    }

    private void onCodesBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event CodesBeruecksichtigen. Selected=" + checkBoxCodesBeruecksichtigen.isSelected());
        schuelerSuchenModel.setCodesBeruecksichtigen(checkBoxCodesBeruecksichtigen.isSelected());
    }

    private void onAnAbmeldestatistikBeruecksichtigenEvent() {
        LOGGER.trace("SchuelerSuchenController Event AnAbmeldestatistikBeruecksichtigen. Selected=" + checkBoxAnAbmeldestatistikBeruecksichtigen.isSelected());
        schuelerSuchenModel.setAnAbmeldestatistikBeruecksichtigen(checkBoxAnAbmeldestatistikBeruecksichtigen.isSelected());
    }

    private void onAbbrechen() {
        LOGGER.trace("SchuelerSuchenPanel Abbrechen gedrückt");
        closeListener.actionPerformed(new ActionEvent(btnAbbrechen, ActionEvent.ACTION_PERFORMED, "Close nach Abbrechen"));
    }

    private void onSuchen() {
        LOGGER.trace("SchuelerSuchenPanel Suchen gedrückt");
        SchuelerSuchenResult schuelerSuchenResult = schuelerSuchenModel.suchen();
        SchuelerSuchenTableModel schuelerSuchenTableModel = new SchuelerSuchenTableModel(schuelerSuchenResult);
        SchuelerSuchenResultPanel schuelerSuchenResultPanel = new SchuelerSuchenResultPanel(schuelerSuchenTableModel);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {
        if (checkIsFieldChange(Field.NACHNAME, evt)) {
            txtNachname.setText(schuelerSuchenModel.getNachname());
        } else if (checkIsFieldChange(Field.VORNAME, evt)) {
            txtVorname.setText(schuelerSuchenModel.getVorname());
        } else if (checkIsFieldChange(Field.STRASSE_HAUSNUMMER, evt)) {
            txtStrasseHausnummer.setText(schuelerSuchenModel.getStrasseHausnummer());
        } else if (checkIsFieldChange(Field.PLZ, evt)) {
            txtPlz.setText(schuelerSuchenModel.getPlz());
        } else if (checkIsFieldChange(Field.ORT, evt)) {
            txtOrt.setText(schuelerSuchenModel.getOrt());
        } else if (checkIsFieldChange(Field.GEBURTSDATUM, evt)) {
            txtGeburtsdatum.setText(asString(schuelerSuchenModel.getGeburtsdatum()));
        } else if (checkIsFieldChange(Field.FESTNETZ, evt)) {
            txtFestnetz.setText(schuelerSuchenModel.getFestnetz());
        } else if (checkIsFieldChange(Field.NATEL, evt)) {
            txtNatel.setText(schuelerSuchenModel.getNatel());
        } else if (checkIsFieldChange(Field.EMAIL, evt)) {
            txtEmail.setText(schuelerSuchenModel.getEmail());
        } else if (checkIsFieldChange(Field.STICHTAG, evt)) {
            txtStichtag.setText(asString(schuelerSuchenModel.getStichtag()));
        } else if (checkIsFieldChange(Field.AN_ABMELDEMONAT, evt)) {
            txtAnAbmeldemonat.setText(asString(schuelerSuchenModel.getAnAbmeldemonat()));
        } else if (checkIsFieldChange(Field.STAMMDATEN_BERUECKSICHTIGEN, evt)) {
            checkBoxStammdatenBeruecksichtigen.setSelected(schuelerSuchenModel.isStammdatenBeruecksichtigen());
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                schuelerSuchenModel.enableFields(getStammdatenFields());
                schuelerSuchenModel.enableFields(getSucheMitStichtagFields());
                schuelerSuchenModel.disableFields(getAnAbmeldestatistikFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getAnAbmeldestatistikFields());
                schuelerSuchenModel.setAnAbmeldestatistikBeruecksichtigen(false);
            } else {
                schuelerSuchenModel.disableFields(getStammdatenFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getStammdatenFields());
            }
        } else if (checkIsFieldChange(Field.KURS_BERUECKSICHTIGEN, evt)) {
            checkBoxKursBeruecksichtigen.setSelected(schuelerSuchenModel.isKursBeruecksichtigen());
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                schuelerSuchenModel.enableFields(getKursFields());
                schuelerSuchenModel.enableFields(getSucheMitStichtagFields());
                schuelerSuchenModel.disableFields(getAnAbmeldestatistikFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getAnAbmeldestatistikFields());
                schuelerSuchenModel.setAnAbmeldestatistikBeruecksichtigen(false);
            } else {
                schuelerSuchenModel.disableFields(getKursFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getKursFields());
            }
        } else if (checkIsFieldChange(Field.CODES_BERUECKSICHTIGEN, evt)) {
            checkBoxCodesBeruecksichtigen.setSelected(schuelerSuchenModel.isCodesBeruecksichtigen());
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                schuelerSuchenModel.enableFields(getCodesFields());
                schuelerSuchenModel.enableFields(getSucheMitStichtagFields());
                schuelerSuchenModel.disableFields(getAnAbmeldestatistikFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getAnAbmeldestatistikFields());
                schuelerSuchenModel.setAnAbmeldestatistikBeruecksichtigen(false);
            } else {
                schuelerSuchenModel.disableFields(getCodesFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getCodesFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getCodesFields());
            }
        } else if (checkIsFieldChange(Field.AN_ABMELDESTATISTIK_BERUECKSICHTIGEN, evt)) {
            checkBoxAnAbmeldestatistikBeruecksichtigen.setSelected(schuelerSuchenModel.isAnAbmeldestatistikBeruecksichtigen());
            Boolean newValue = (Boolean) evt.getNewValue();
            if (isBooleanNewValuePropertyChecked(newValue)) {
                schuelerSuchenModel.enableFields(getAnAbmeldestatistikFields());
                schuelerSuchenModel.disableFields(getSucheMitStichtagFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getSucheMitStichtagFields());
                schuelerSuchenModel.disableFields(getStammdatenFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getStammdatenFields());
                schuelerSuchenModel.setStammdatenBeruecksichtigen(false);
                schuelerSuchenModel.disableFields(getKursFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getKursFields());
                schuelerSuchenModel.setKursBeruecksichtigen(false);
                schuelerSuchenModel.disableFields(getCodesFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getCodesFields());
                schuelerSuchenModel.setCodesBeruecksichtigen(false);

            } else {
                schuelerSuchenModel.disableFields(getAnAbmeldestatistikFields());
                schuelerSuchenModel.makeErrorLabelsInvisible(getAnAbmeldestatistikFields());
            }
        }
    }

    private boolean isBooleanNewValuePropertyChecked(Boolean newValue) {
        return (newValue != null) && newValue;
    }

    private Set<Field> getSucheMitStichtagFields() {
        Set<Field> sucheMitStichtagFields = new HashSet<>();
        sucheMitStichtagFields.add(Field.STICHTAG);
        sucheMitStichtagFields.add(Field.ANGEMELDET);
        sucheMitStichtagFields.add(Field.ABGEMELDET);
        sucheMitStichtagFields.add(Field.ANMELDESTATUS_ALLE);
        sucheMitStichtagFields.add(Field.DISPENSIERT);
        sucheMitStichtagFields.add(Field.NICHT_DISPENSIERT);
        sucheMitStichtagFields.add(Field.DISPENSATION_ALLE);
        return sucheMitStichtagFields;
    }

    private Set<Field> getStammdatenFields() {
        Set<Field> stammdatenFields = new HashSet<>();
        stammdatenFields.add(Field.NACHNAME);
        stammdatenFields.add(Field.VORNAME);
        stammdatenFields.add(Field.STRASSE_HAUSNUMMER);
        stammdatenFields.add(Field.PLZ);
        stammdatenFields.add(Field.ORT);
        stammdatenFields.add(Field.FESTNETZ);
        stammdatenFields.add(Field.NATEL);
        stammdatenFields.add(Field.EMAIL);
        stammdatenFields.add(Field.GEBURTSDATUM);
        stammdatenFields.add(Field.SCHUELER);
        stammdatenFields.add(Field.ELTERN);
        stammdatenFields.add(Field.RECHNUNGSEMPFAENGER);
        stammdatenFields.add(Field.ROLLE_ALLE);
        return stammdatenFields;
    }

    private Set<Field> getKursFields() {
        Set<Field> kursFields = new HashSet<>();
        kursFields.add(Field.LEHRKRAFT);
        kursFields.add(Field.WOCHENTAG);
        kursFields.add(Field.VON);
        kursFields.add(Field.BIS);
        return kursFields;
    }

    private Set<Field> getCodesFields() {
        Set<Field> codesFields = new HashSet<>();
        codesFields.add(Field.CODES);
        return codesFields;
    }

    private Set<Field> getAnAbmeldestatistikFields() {
        Set<Field> anAbmeldestatistikFields = new HashSet<>();
        anAbmeldestatistikFields.add(Field.AN_ABMELDEMONAT);
        anAbmeldestatistikFields.add(Field.ANMELDUNGEN);
        anAbmeldestatistikFields.add(Field.ABMELDUNGEN);
        return anAbmeldestatistikFields;
    }

    @Override
    void validateFields() throws SvmValidationException {
        if (txtNachname.isEnabled()) {
            LOGGER.trace("Validate field Nachname");
            setModelNachname();
        }
        if (txtVorname.isEnabled()) {
            LOGGER.trace("Validate field Vorname");
            setModelVorname();
        }
        if (txtStrasseHausnummer.isEnabled()) {
            LOGGER.trace("Validate field StrasseHausnummer");
            setModelStrasseHausnummer();
        }
        if (txtPlz.isEnabled()) {
            LOGGER.trace("Validate field Plz");
            setModelPlz();
        }
        if (txtOrt.isEnabled()) {
            LOGGER.trace("Validate field Ort");
            setModelOrt();
        }
        if (txtGeburtsdatum.isEnabled()) {
            LOGGER.trace("Validate field Geburtsdatum");
            setModelGeburtsdatum();
        }
        if (txtFestnetz.isEnabled()) {
            LOGGER.trace("Validate field Festnetz");
            setModelFestnetz();
        }
        if (txtNatel.isEnabled()) {
            LOGGER.trace("Validate field Natel");
            setModelNatel();
        }
        if (txtEmail.isEnabled()) {
            LOGGER.trace("Validate field Email");
            setModelEmail();
        }
        if (txtAnAbmeldemonat.isEnabled()) {
            LOGGER.trace("Validate field An-/Abmeldemonat");
            setModelAnAbmeldemonat();
        }
        if (txtStichtag.isEnabled()) {
            LOGGER.trace("Validate field Stichtag");
            setModelStichtag();
        }
    }

    @Override
    void showErrMsg(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            errLblVorname.setVisible(true);
            errLblVorname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            errLblNachname.setVisible(true);
            errLblNachname.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STRASSE_HAUSNUMMER)) {
            errLblStrasseHausnummer.setVisible(true);
            errLblStrasseHausnummer.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.PLZ)) {
            errLblPlz.setVisible(true);
            errLblPlz.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ORT)) {
            errLblOrt.setVisible(true);
            errLblOrt.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FESTNETZ)) {
            errLblFestnetz.setVisible(true);
            errLblFestnetz.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NATEL)) {
            errLblNatel.setVisible(true);
            errLblNatel.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.EMAIL)) {
            errLblEmail.setVisible(true);
            errLblEmail.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GEBURTSDATUM)) {
            errLblGeburtsdatum.setVisible(true);
            errLblGeburtsdatum.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(true);
            errLblStichtag.setText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.AN_ABMELDEMONAT)) {
            errLblAnAbmeldemonat.setVisible(true);
            errLblAnAbmeldemonat.setText(e.getMessage());
        }
    }

    @Override
    void showErrMsgAsToolTip(SvmValidationException e) {
        if (e.getAffectedFields().contains(Field.VORNAME)) {
            txtVorname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NACHNAME)) {
            txtNachname.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STRASSE_HAUSNUMMER)) {
            txtStrasseHausnummer.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.PLZ)) {
            txtPlz.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.ORT)) {
            txtOrt.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.FESTNETZ)) {
            txtFestnetz.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.NATEL)) {
            txtNatel.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.EMAIL)) {
            txtEmail.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.GEBURTSDATUM)) {
            txtGeburtsdatum.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.STICHTAG)) {
            txtStichtag.setToolTipText(e.getMessage());
        }
        if (e.getAffectedFields().contains(Field.AN_ABMELDEMONAT)) {
            txtAnAbmeldemonat.setToolTipText(e.getMessage());
        }
    }

    @Override
    public void makeErrorLabelsInvisible(Set<Field> fields) {
        if (fields.contains(Field.VORNAME)) {
            errLblVorname.setVisible(false);
            txtVorname.setToolTipText(null);
        }
        if (fields.contains(Field.NACHNAME)) {
            errLblNachname.setVisible(false);
            txtNachname.setToolTipText(null);
        }
        if (fields.contains(Field.STRASSE_HAUSNUMMER)) {
            errLblStrasseHausnummer.setVisible(false);
            txtStrasseHausnummer.setToolTipText(null);
        }
        if (fields.contains(Field.PLZ)) {
            errLblPlz.setVisible(false);
            txtPlz.setToolTipText(null);
        }
        if (fields.contains(Field.ORT)) {
            errLblOrt.setVisible(false);
            txtOrt.setToolTipText(null);
        }
        if (fields.contains(Field.FESTNETZ)) {
            errLblFestnetz.setVisible(false);
            txtFestnetz.setToolTipText(null);
        }
        if (fields.contains(Field.NATEL)) {
            errLblNatel.setVisible(false);
            txtNatel.setToolTipText(null);
        }
        if (fields.contains(Field.EMAIL)) {
            errLblEmail.setVisible(false);
            txtEmail.setToolTipText(null);
        }
        if (fields.contains(Field.GEBURTSDATUM)) {
            errLblGeburtsdatum.setVisible(false);
            txtGeburtsdatum.setToolTipText(null);
        }
        if (fields.contains(Field.AN_ABMELDEMONAT)) {
            errLblAnAbmeldemonat.setVisible(false);
            txtAnAbmeldemonat.setToolTipText(null);
        }
        if (fields.contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(false);
            txtStichtag.setToolTipText(null);
        }
    }

    @Override
    public void disableFields(boolean disable, Set<Field> fields) {
        if (fields.contains(Field.ALLE) || fields.contains(Field.NACHNAME)) {
            txtNachname.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VORNAME)) {
            txtVorname.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STRASSE_HAUSNUMMER)) {
            txtStrasseHausnummer.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.PLZ)) {
            txtPlz.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ORT)) {
            txtOrt.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.FESTNETZ)) {
            txtFestnetz.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NATEL)) {
            txtNatel.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.EMAIL)) {
            txtEmail.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.GEBURTSDATUM)) {
            txtGeburtsdatum.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.SCHUELER)) {
            radioBtnSchueler.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ELTERN)) {
            radioBtnEltern.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.RECHNUNGSEMPFAENGER)) {
            radioBtnRechnungsempfaenger.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ROLLE_ALLE)) {
            radioBtnRolleAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STAMMDATEN_BERUECKSICHTIGEN)) {
            checkBoxStammdatenBeruecksichtigen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANGEMELDET)) {
            radioBtnAngemeldet.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABGEMELDET)) {
            radioBtnAbgemeldet.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDESTATUS_ALLE)) {
            radioBtnAnmeldestatusAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSIERT)) {
            radioBtnDispensiert.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.NICHT_DISPENSIERT)) {
            radioBtnNichtDispensiert.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.DISPENSATION_ALLE)) {
            radioBtnDispensationAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WEIBLICH)) {
            radioBtnWeiblich.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.MAENNLICH)) {
            radioBtnMaennlich.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.GESCHLECHT_ALLE)) {
            radioBtnGeschlechtAlle.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.LEHRKRAFT)) {
            txtLehrkraft.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.WOCHENTAG)) {
            comboBoxWochentag.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.VON)) {
            txtVon.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.BIS)) {
            txtBis.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.KURS_BERUECKSICHTIGEN)) {
            checkBoxKursBeruecksichtigen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.CODES)) {
            txtCodes.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.CODES_BERUECKSICHTIGEN)) {
            checkBoxCodesBeruecksichtigen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.AN_ABMELDEMONAT)) {
            txtAnAbmeldemonat.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ANMELDUNGEN)) {
            radioBtnAnmeldungen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.ABMELDUNGEN)) {
            radioBtnAbmeldungen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.AN_ABMELDESTATISTIK_BERUECKSICHTIGEN)) {
            checkBoxAnAbmeldestatistikBeruecksichtigen.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.STICHTAG)) {
            txtStichtag.setEnabled(!disable);
        }
    }

    class RadioBtnGroupRolleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Rolle Event");
            schuelerSuchenModel.setRolle(SchuelerSuchenModel.RolleSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupAnmeldestatusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Anmeldestatus Event");
            schuelerSuchenModel.setAnmeldestatus(SchuelerSuchenModel.AnmeldestatusSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupDispensationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Dispensation Event");
            schuelerSuchenModel.setDispensation(SchuelerSuchenModel.DispensationSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupGeschlechtListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController Geschlecht Event");
            schuelerSuchenModel.setGeschlecht(SchuelerSuchenModel.GeschlechtSelected.valueOf(e.getActionCommand()));
        }
    }

    class RadioBtnGroupAnAbmeldungenListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LOGGER.trace("SchuelerSuchenController AnAbmeldungen Event");
            schuelerSuchenModel.setAnAbmeldungen(SchuelerSuchenModel.AnAbmeldungenSelected.valueOf(e.getActionCommand()));
        }
    }

}
