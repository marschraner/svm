package ch.metzenthin.svm.ui.control;

import ch.metzenthin.svm.dataTypes.Field;
import ch.metzenthin.svm.domain.SvmValidationException;
import ch.metzenthin.svm.domain.model.SchuelerSuchenModel;
import ch.metzenthin.svm.domain.model.SchuelerSuchenResult;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.components.SchuelerSuchenResultPanel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.util.Set;

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
    private JRadioButton radioBtnAngemeldet;
    private JRadioButton radioBtnAbgemeldet;
    private JRadioButton radioBtnAnmeldestatusAlle;
    private JRadioButton radioBtnDispensiert;
    private JRadioButton radioBtnNichtDispensiert;
    private JRadioButton radioBtnDispensationAlle;
    private JRadioButton radioBtnWeiblich;
    private JRadioButton radioBtnMaennlich;
    private JRadioButton radioBtnGeschlechtAlle;
    private JRadioButton radioBtnSchueler;
    private JRadioButton radioBtnEltern;
    private JRadioButton radioBtnRechnungsempfaenger;
    private JRadioButton radioBtnRolleAlle;
    private JRadioButton radioBtnAnmeldungen;
    private JRadioButton radioBtnAbmeldungen;
    private JPanel stichtagPanel;
    private JTextField txtStichtag;
    private JPanel anAbmeldeStatistikPanel;
    private JTextField txtAnAbmeldemonat;
    private JTextField txtAbmeldemonat;
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
        schuelerSuchenModel.setNachname(txtNachname.getText());
    }

    private void onVornameEvent() {
        LOGGER.trace("SchuelerSuchenController Event Vorname");
        schuelerSuchenModel.setVorname(txtVorname.getText());
    }

    private void onStrasseHausnummerEvent() {
        LOGGER.trace("SchuelerSuchenController Event StrasseHausnummer");
        schuelerSuchenModel.setStrasseHausnummer(txtStrasseHausnummer.getText());
    }

    private void onPlzEvent() {
        LOGGER.trace("SchuelerSuchenController Event Plz");
        schuelerSuchenModel.setPlz(txtPlz.getText());
    }

    private void onOrtEvent() {
        LOGGER.trace("SchuelerSuchenController Event Ort");
        schuelerSuchenModel.setOrt(txtOrt.getText());
    }

    private void onFestnetzEvent() {
        LOGGER.trace("SchuelerSuchenController Event Festnetz");
        schuelerSuchenModel.setFestnetz(txtFestnetz.getText());
    }

    private void onNatelEvent() {
        LOGGER.trace("SchuelerSuchenController Event Natel");
        schuelerSuchenModel.setNatel(txtNatel.getText());
    }

    private void onEmailEvent() {
        LOGGER.trace("SchuelerSuchenController Event Email");
        schuelerSuchenModel.setEmail(txtEmail.getText());
    }

    private void onGeburtsdatumEvent() {
        makeErrorLabelInvisible(Field.GEBURTSDATUM);
        try {
            schuelerSuchenModel.setGeburtsdatum(txtGeburtsdatum.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelGeburtsdatum Exception=" + e.getMessage());
            showErrMsg(e);
            return;
        }
    }

    private void onStichtagEvent() {
        makeErrorLabelInvisible(Field.STICHTAG);
        try {
            schuelerSuchenModel.setStichtag(txtStichtag.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelStichtag Exception=" + e.getMessage());
            showErrMsg(e);
            return;
        }
    }

    private void onAnmeldemonatEvent() {
        makeErrorLabelInvisible(Field.AN_ABMELDEMONAT);
        try {
            schuelerSuchenModel.setAnmeldemonat(txtAnAbmeldemonat.getText());
        } catch (SvmValidationException e) {
            LOGGER.trace("SchuelerSuchenController setModelAnAbmeldemonat Exception=" + e.getMessage());
            showErrMsg(e);
            return;
        }
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
        schuelerSuchenResultPanel.addNextPanelListener(nextPanelListener);
        nextPanelListener.actionPerformed(new ActionEvent(new Object[]{schuelerSuchenResultPanel.$$$getRootComponent$$$(), "Suchresultat"}, ActionEvent.ACTION_PERFORMED, "Suchresultat verfügbar"));
    }

    @Override
    void doPropertyChange(PropertyChangeEvent evt) {}

    @Override
    void validateFields() throws SvmValidationException {}

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
        if (fields.contains(Field.STICHTAG)) {
            errLblStichtag.setVisible(false);
            txtStichtag.setToolTipText(null);
        }
        if (fields.contains(Field.AN_ABMELDEMONAT)) {
            errLblAnAbmeldemonat.setVisible(false);
            txtAnAbmeldemonat.setToolTipText(null);
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
            radioBtnAnmeldestatusAlle.setEnabled(!disable);
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
        if (fields.contains(Field.ALLE) || fields.contains(Field.STICHTAG)) {
            txtStichtag.setEnabled(!disable);
        }
        if (fields.contains(Field.ALLE) || fields.contains(Field.AN_ABMELDEMONAT)) {
            txtAnAbmeldemonat.setEnabled(!disable);
        }
    }
}
