package ch.metzenthin.svm.ui.components;


import ch.metzenthin.svm.dataTypes.Geschlecht;
import ch.metzenthin.svm.domain.model.SchuelerModel;
import ch.metzenthin.svm.ui.control.SchuelerController;

import javax.swing.*;
import java.awt.*;

/**
 * @author hans
 */
public class SchuelerPanel {
    private JPanel panel;
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
    private JPanel mainPanel;

    private SchuelerModel schuelerModel;

    public SchuelerPanel() {
        $$$setupUI$$$();
    }

    public void setModel(SchuelerModel schuelerModel) {
        this.schuelerModel = schuelerModel;
        createSchuelerController();
    }

    private void createSchuelerController() {
        SchuelerController schuelerController = new SchuelerController(schuelerModel);
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
    }

    private void createUIComponents() {
        comboBoxGeschlecht = new JComboBox<>();
    }

    public JPanel getMainPanel() {
        return mainPanel;
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
        mainPanel.setMinimumSize(new Dimension(550, 528));
        mainPanel.setPreferredSize(new Dimension(550, 528));
        panel.add(mainPanel, BorderLayout.CENTER);
        final JPanel spacer1 = new JPanel();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Vorname");
        label1.setDisplayedMnemonic('V');
        label1.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label1, gbc);
        txtVorname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtVorname, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Strasse/Nr.");
        label2.setDisplayedMnemonic('S');
        label2.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label2, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer2, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("PLZ/Ort");
        label3.setDisplayedMnemonic('P');
        label3.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label3, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer3, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
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
        label4.setDisplayedMnemonic('F');
        label4.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label4, gbc);
        txtFestnetz = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtFestnetz, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Geburtsdatum");
        label5.setDisplayedMnemonic('G');
        label5.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label5, gbc);
        txtGeburtsdatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtGeburtsdatum, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Natel");
        label6.setDisplayedMnemonic('N');
        label6.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label6, gbc);
        txtNatel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNatel, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer4, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("E-Mail");
        label7.setDisplayedMnemonic('E');
        label7.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label7, gbc);
        txtEmail = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtEmail, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 19;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer7, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Abmeldedatum");
        label8.setDisplayedMnemonic('B');
        label8.setDisplayedMnemonicIndex(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label8, gbc);
        txtAbmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtAbmeldedatum, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 23;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer8, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Anmeldedatum");
        label9.setDisplayedMnemonic('M');
        label9.setDisplayedMnemonicIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label9, gbc);
        txtAnmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 20;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtAnmeldedatum, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 21;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer9, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 40;
        mainPanel.add(scrollPane1, gbc);
        textAreaBemerkungen = new JTextArea();
        scrollPane1.setViewportView(textAreaBemerkungen);
        final JLabel label10 = new JLabel();
        label10.setText("Bemerkungen");
        label10.setDisplayedMnemonic('R');
        label10.setDisplayedMnemonicIndex(4);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label10, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 25;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer10, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
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
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer11, gbc);
        txtOrt = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtOrt, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer13, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Geschlecht");
        label11.setDisplayedMnemonic('H');
        label11.setDisplayedMnemonicIndex(4);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label11, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer14, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(comboBoxGeschlecht, gbc);
        final JLabel label12 = new JLabel();
        label12.setText("Nachname");
        label12.setDisplayedMnemonic('C');
        label12.setDisplayedMnemonicIndex(2);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label12, gbc);
        txtNachname = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNachname, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainPanel.add(spacer15, gbc);
        label1.setLabelFor(txtVorname);
        label3.setLabelFor(txtPlz);
        label4.setLabelFor(txtFestnetz);
        label5.setLabelFor(txtGeburtsdatum);
        label6.setLabelFor(txtNatel);
        label7.setLabelFor(txtEmail);
        label8.setLabelFor(txtAbmeldedatum);
        label9.setLabelFor(txtAnmeldedatum);
        label10.setLabelFor(textAreaBemerkungen);
        label11.setLabelFor(comboBoxGeschlecht);
        label12.setLabelFor(txtNachname);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
