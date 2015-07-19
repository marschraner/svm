package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.AngehoerigerModel;
import ch.metzenthin.svm.ui.control.AngehoerigerController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Hans Stamm
 */
public class AngehoerigerPanel {
    private JPanel panel;
    private JComboBox<Anrede> comboBoxAnrede;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasseHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
    private JCheckBox checkBoxRechnungsempfaenger;
    private JCheckBox checkBoxGleicheAdresseWieSchueler;
    private JLabel lblAnrede;
    private JPanel mainPanel;
    private JLabel lblRechnungsempfaenger;
    private JLabel lblGleicheAdresseWieSchueler;
    private JLabel errLblAnrede;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblStrasseHausnummer;
    private JLabel errLblPlz;
    private JLabel errLblOrt;
    private JLabel errLblFestnetz;
    private JLabel errLblNatel;
    private JLabel errLblEmail;
    private JLabel errLblRechnungsempfaenger;
    private JLabel errLblGleicheAdresseWieSchueler;

    public AngehoerigerPanel() {
        $$$setupUI$$$();
    }

    public AngehoerigerController setModel(AngehoerigerModel angehoerigerModel) {
        initializeErrLbls();
        return createAngehoerigerController(angehoerigerModel);
    }

    private AngehoerigerController createAngehoerigerController(AngehoerigerModel angehoerigerModel) {
        AngehoerigerController angehoerigerController = new AngehoerigerController(angehoerigerModel);
        angehoerigerController.setComboBoxAnrede(comboBoxAnrede);
        angehoerigerController.setTxtNachname(txtNachname);
        angehoerigerController.setTxtVorname(txtVorname);
        angehoerigerController.setTxtStrasseHausnummer(txtStrasseHausnummer);
        angehoerigerController.setTxtPlz(txtPlz);
        angehoerigerController.setTxtOrt(txtOrt);
        angehoerigerController.setTxtFestnetz(txtFestnetz);
        angehoerigerController.setTxtNatel(txtNatel);
        angehoerigerController.setTxtEmail(txtEmail);
        angehoerigerController.setCheckBoxGleicheAdresseWieSchueler(checkBoxGleicheAdresseWieSchueler);
        angehoerigerController.setCheckBoxRechnungsempfaenger(checkBoxRechnungsempfaenger);
        angehoerigerController.setErrLblAnrede(errLblAnrede);
        angehoerigerController.setErrLblNachname(errLblNachname);
        angehoerigerController.setErrLblVorname(errLblVorname);
        angehoerigerController.setErrLblStrasseHausnummer(errLblStrasseHausnummer);
        angehoerigerController.setErrLblPlz(errLblPlz);
        angehoerigerController.setErrLblOrt(errLblOrt);
        angehoerigerController.setErrLblFestnetz(errLblFestnetz);
        angehoerigerController.setErrLblNatel(errLblNatel);
        angehoerigerController.setErrLblEmail(errLblEmail);
        angehoerigerController.setErrLblRechnungsempfaenger(errLblRechnungsempfaenger);
        angehoerigerController.setErrLblGleicheAdresseWieSchueler(errLblGleicheAdresseWieSchueler);
        return angehoerigerController;
    }

    private void initializeErrLbls() {
        errLblAnrede.setVisible(false);
        errLblAnrede.setForeground(Color.RED);
        errLblNachname.setVisible(false);
        errLblNachname.setForeground(Color.RED);
        errLblVorname.setVisible(false);
        errLblVorname.setForeground(Color.RED);
        errLblStrasseHausnummer.setVisible(false);
        errLblStrasseHausnummer.setForeground(Color.RED);
        errLblOrt.setVisible(false);
        errLblOrt.setForeground(Color.RED);
        errLblPlz.setVisible(false);
        errLblPlz.setForeground(Color.RED);
        errLblFestnetz.setVisible(false);
        errLblFestnetz.setForeground(Color.RED);
        errLblNatel.setVisible(false);
        errLblNatel.setForeground(Color.RED);
        errLblEmail.setVisible(false);
        errLblEmail.setForeground(Color.RED);
        errLblRechnungsempfaenger.setVisible(false);
        errLblRechnungsempfaenger.setForeground(Color.RED);
        errLblGleicheAdresseWieSchueler.setVisible(false);
        errLblGleicheAdresseWieSchueler.setForeground(Color.RED);
    }

    private void createUIComponents() {
        comboBoxAnrede = new JComboBox<>();
    }

    public JLabel getLblAnrede() {
        return lblAnrede;
    }

    public JComboBox<Anrede> getComboBoxAnrede() {
        return comboBoxAnrede;
    }

    public JLabel getLblGleicheAdresseWieSchueler() {
        return lblGleicheAdresseWieSchueler;
    }

    public JCheckBox getCheckBoxGleicheAdresseWieSchueler() {
        return checkBoxGleicheAdresseWieSchueler;
    }

    public JLabel getLblRechnungsempfaenger() {
        return lblRechnungsempfaenger;
    }

    public JCheckBox getCheckBoxRechnungsempfaenger() {
        return checkBoxRechnungsempfaenger;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        panel.add(mainPanel, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Nachname");
        label1.setDisplayedMnemonic('C');
        label1.setDisplayedMnemonicIndex(2);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        txtNachname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNachname, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Vorname");
        label2.setDisplayedMnemonic('V');
        label2.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label2, gbc);
        txtVorname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtVorname, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Strasse/Nr.");
        label3.setDisplayedMnemonic('S');
        label3.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label3, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer2, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("PLZ/Ort");
        label4.setDisplayedMnemonic('P');
        label4.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label4, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer4, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panel1, gbc);
        txtStrasseHausnummer = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtStrasseHausnummer, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Festnetz");
        label5.setDisplayedMnemonic('F');
        label5.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label5, gbc);
        txtFestnetz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtFestnetz, gbc);
        lblAnrede = new JLabel();
        lblAnrede.setText("Anrede");
        lblAnrede.setDisplayedMnemonic('A');
        lblAnrede.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(lblAnrede, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(comboBoxAnrede, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Natel");
        label6.setDisplayedMnemonic('N');
        label6.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label6, gbc);
        txtNatel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNatel, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("E-Mail");
        label7.setDisplayedMnemonic('E');
        label7.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label7, gbc);
        txtEmail = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtEmail, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer6, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 5;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel2, gbc);
        txtPlz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtPlz, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer7, gbc);
        txtOrt = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtOrt, gbc);
        lblRechnungsempfaenger = new JLabel();
        lblRechnungsempfaenger.setText("Rechnungsempfänger");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(lblRechnungsempfaenger, gbc);
        checkBoxRechnungsempfaenger = new JCheckBox();
        checkBoxRechnungsempfaenger.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(checkBoxRechnungsempfaenger, gbc);
        lblGleicheAdresseWieSchueler = new JLabel();
        lblGleicheAdresseWieSchueler.setText("Gleiche Adresse wie Schüler");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(lblGleicheAdresseWieSchueler, gbc);
        checkBoxGleicheAdresseWieSchueler = new JCheckBox();
        checkBoxGleicheAdresseWieSchueler.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(checkBoxGleicheAdresseWieSchueler, gbc);
        errLblAnrede = new JLabel();
        errLblAnrede.setText("errLblAnr");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblAnrede, gbc);
        errLblNachname = new JLabel();
        errLblNachname.setText("errLblNach");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblNachname, gbc);
        errLblVorname = new JLabel();
        errLblVorname.setText("errLblVorn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblVorname, gbc);
        errLblStrasseHausnummer = new JLabel();
        errLblStrasseHausnummer.setText("errLblStrHa");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblStrasseHausnummer, gbc);
        errLblPlz = new JLabel();
        errLblPlz.setText("errLblPlz");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblPlz, gbc);
        errLblOrt = new JLabel();
        errLblOrt.setText("errLblOrt");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblOrt, gbc);
        errLblFestnetz = new JLabel();
        errLblFestnetz.setText("errLblFestn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblFestnetz, gbc);
        errLblNatel = new JLabel();
        errLblNatel.setText("errLblNatel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblNatel, gbc);
        errLblEmail = new JLabel();
        errLblEmail.setText("errLblEmail");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblEmail, gbc);
        errLblRechnungsempfaenger = new JLabel();
        errLblRechnungsempfaenger.setText("errLblRechn");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(errLblRechnungsempfaenger, gbc);
        errLblGleicheAdresseWieSchueler = new JLabel();
        errLblGleicheAdresseWieSchueler.setText("errLblGlAdrWieSchue");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(errLblGleicheAdresseWieSchueler, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 200;
        mainPanel.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer11, gbc);
        label1.setLabelFor(txtNachname);
        label2.setLabelFor(txtVorname);
        label4.setLabelFor(txtPlz);
        label5.setLabelFor(txtFestnetz);
        lblAnrede.setLabelFor(comboBoxAnrede);
        label6.setLabelFor(txtNatel);
        label7.setLabelFor(txtEmail);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
