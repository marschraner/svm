package ch.metzenthin.svm.ui.components;


import ch.metzenthin.svm.common.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.ui.control.SchuelerController;

import javax.swing.*;
import java.awt.*;

/**
 * @author hans
 */
public class SchuelerPanel {
    private JPanel panel;
    private JPanel mainPanel;
    private JTextField txtNachname;
    private JTextField txtVorname;
    private JTextField txtStrasseHausnummer;
    private JTextField txtPlz;
    private JTextField txtOrt;
    private JTextField txtFestnetz;
    private JTextField txtNatel;
    private JTextField txtEmail;
    private JTextField txtGeburtsdatum;
    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextArea textAreaBemerkungen;
    private JComboBox<Geschlecht> comboBoxGeschlecht;
    private JLabel lblAbmeldedatum;
    private JLabel errLblNachname;
    private JLabel errLblVorname;
    private JLabel errLblStrasseHausnummer;
    private JLabel errLblOrt;
    private JLabel errLblPlz;
    private JLabel errLblFestnetz;
    private JLabel errLblNatel;
    private JLabel errLblEmail;
    private JLabel errLblGeschlecht;
    private JLabel errLblGeburtsdatum;
    private JLabel errLblAnmeldedatum;
    private JLabel errLblAbmeldedatum;
    private JLabel errLblBemerkungen;

    SchuelerPanel() {
        $$$setupUI$$$();
    }

    public SchuelerController setModel(SchuelerModel schuelerModel, boolean defaultButtonEnabled) {
        initializeErrLbls();
        return createSchuelerController(schuelerModel, defaultButtonEnabled);
    }

    private SchuelerController createSchuelerController(SchuelerModel schuelerModel, boolean defaultButtonEnabled) {
        SchuelerController schuelerController = new SchuelerController(schuelerModel, defaultButtonEnabled);
        schuelerController.setTxtNachname(txtNachname);
        schuelerController.setTxtVorname(txtVorname);
        schuelerController.setTxtStrasseHausnummer(txtStrasseHausnummer);
        schuelerController.setTxtPlz(txtPlz);
        schuelerController.setTxtOrt(txtOrt);
        schuelerController.setTxtFestnetz(txtFestnetz);
        schuelerController.setTxtNatel(txtNatel);
        schuelerController.setTxtEmail(txtEmail);
        schuelerController.setComboBoxGeschlecht(comboBoxGeschlecht);
        schuelerController.setTxtGeburtsdatum(txtGeburtsdatum);
        schuelerController.setTxtAnmeldedatum(txtAnmeldedatum);
        schuelerController.setTxtAbmeldedatum(txtAbmeldedatum);
        schuelerController.setTextAreaBemerkungen(textAreaBemerkungen);
        schuelerController.setErrLblNachname(errLblNachname);
        schuelerController.setErrLblVorname(errLblVorname);
        schuelerController.setErrLblStrasseHausnummer(errLblStrasseHausnummer);
        schuelerController.setErrLblPlz(errLblPlz);
        schuelerController.setErrLblOrt(errLblOrt);
        schuelerController.setErrLblFestnetz(errLblFestnetz);
        schuelerController.setErrLblNatel(errLblNatel);
        schuelerController.setErrLblEmail(errLblEmail);
        schuelerController.setErrLblGeschlecht(errLblGeschlecht);
        schuelerController.setErrLblGeburtsdatum(errLblGeburtsdatum);
        schuelerController.setErrLblAnmeldedatum(errLblAnmeldedatum);
        schuelerController.setErrLblAbmeldedatum(errLblAbmeldedatum);
        schuelerController.setErrLblBemerkungen(errLblBemerkungen);
        return schuelerController;
    }

    private void initializeErrLbls() {
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
        errLblGeschlecht.setVisible(false);
        errLblGeschlecht.setForeground(Color.RED);
        errLblGeburtsdatum.setVisible(false);
        errLblGeburtsdatum.setForeground(Color.RED);
        errLblAnmeldedatum.setVisible(false);
        errLblAnmeldedatum.setForeground(Color.RED);
        errLblAbmeldedatum.setVisible(false);
        errLblAbmeldedatum.setForeground(Color.RED);
        errLblBemerkungen.setVisible(false);
        errLblBemerkungen.setForeground(Color.RED);
    }

    private void createUIComponents() {
        comboBoxGeschlecht = new JComboBox<>();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JLabel getLblAbmeldedatum() {
        return lblAbmeldedatum;
    }

    public JTextField getTxtAbmeldedatum() {
        return txtAbmeldedatum;
    }

    public JLabel getErrLblAbmeldedatum() {
        return errLblAbmeldedatum;
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
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
        label1.setText("Vorname");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label1, gbc);
        txtVorname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtVorname, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Strasse/Nr.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer1, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("PLZ/Ort");
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panel1, gbc);
        txtStrasseHausnummer = new JTextField();
        txtStrasseHausnummer.setColumns(27);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtStrasseHausnummer, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Festnetz");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label4, gbc);
        txtFestnetz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtFestnetz, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Geburtsdatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label5, gbc);
        txtGeburtsdatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtGeburtsdatum, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Natel");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label6, gbc);
        txtNatel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNatel, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("E-Mail");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label7, gbc);
        txtEmail = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtEmail, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        lblAbmeldedatum = new JLabel();
        lblAbmeldedatum.setText("Abmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(lblAbmeldedatum, gbc);
        txtAbmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 21;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtAbmeldedatum, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer6, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Anmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label8, gbc);
        txtAnmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 19;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtAnmeldedatum, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer7, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 23;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 40;
        mainPanel.add(scrollPane1, gbc);
        textAreaBemerkungen = new JTextArea();
        textAreaBemerkungen.setLineWrap(true);
        textAreaBemerkungen.setWrapStyleWord(true);
        scrollPane1.setViewportView(textAreaBemerkungen);
        final JLabel label9 = new JLabel();
        label9.setText("Bemerkungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 23;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label9, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer8, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
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
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer9, gbc);
        txtOrt = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtOrt, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer11, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Geschlecht");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label10, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer12, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(comboBoxGeschlecht, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Nachname");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(label11, gbc);
        txtNachname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNachname, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer13, gbc);
        errLblVorname = new JLabel();
        errLblVorname.setText("errLblVorn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblVorname, gbc);
        errLblStrasseHausnummer = new JLabel();
        errLblStrasseHausnummer.setText("errLblStrHaus");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblStrasseHausnummer, gbc);
        errLblPlz = new JLabel();
        errLblPlz.setText("errLblPlz");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblPlz, gbc);
        errLblOrt = new JLabel();
        errLblOrt.setText("errLblOrt");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblOrt, gbc);
        errLblFestnetz = new JLabel();
        errLblFestnetz.setText("errLblFestnetz");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblFestnetz, gbc);
        errLblNatel = new JLabel();
        errLblNatel.setText("errLblNatel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblNatel, gbc);
        errLblEmail = new JLabel();
        errLblEmail.setText("errLblEmail");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblEmail, gbc);
        errLblGeschlecht = new JLabel();
        errLblGeschlecht.setText("errLblGeschl");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblGeschlecht, gbc);
        errLblGeburtsdatum = new JLabel();
        errLblGeburtsdatum.setText("errLblGeb");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblGeburtsdatum, gbc);
        errLblAnmeldedatum = new JLabel();
        errLblAnmeldedatum.setText("errLblAnm");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblAnmeldedatum, gbc);
        errLblAbmeldedatum = new JLabel();
        errLblAbmeldedatum.setText("errLblAbm");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblAbmeldedatum, gbc);
        errLblBemerkungen = new JLabel();
        errLblBemerkungen.setText("errLblBem");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblBemerkungen, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer14, gbc);
        errLblNachname = new JLabel();
        errLblNachname.setText("errLblNachn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(errLblNachname, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 200;
        mainPanel.add(spacer15, gbc);
        label1.setLabelFor(txtVorname);
        label3.setLabelFor(txtPlz);
        label4.setLabelFor(txtFestnetz);
        label5.setLabelFor(txtGeburtsdatum);
        label6.setLabelFor(txtNatel);
        label7.setLabelFor(txtEmail);
        lblAbmeldedatum.setLabelFor(txtAbmeldedatum);
        label8.setLabelFor(txtAnmeldedatum);
        label9.setLabelFor(textAreaBemerkungen);
        label10.setLabelFor(comboBoxGeschlecht);
        label11.setLabelFor(txtNachname);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
