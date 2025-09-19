package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Anrede;
import ch.metzenthin.svm.domain.model.MitarbeiterErfassenModel;
import ch.metzenthin.svm.domain.model.MitarbeitersModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.control.MitarbeiterErfassenController;
import java.awt.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;

@SuppressWarnings({"java:S100", "java:S1450"})
public class MitarbeiterErfassenDialog extends JDialog {

  // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
  private static final boolean DEFAULT_BUTTON_ENABLED = false;

  private JPanel contentPane;
  private JPanel datenPanel;
  private JComboBox<Anrede> comboBoxAnrede;
  private JTextField txtNachname;
  private JTextField txtVorname;
  private JTextField txtStrasseHausnummer;
  private JTextField txtPlz;
  private JTextField txtOrt;
  private JTextField txtFestnetz;
  private JTextField txtNatel;
  private JTextField txtEmail;
  private JTextField txtGeburtsdatum;
  private JTextField txtAhvNummer;
  private JTextField txtIbanNummer;
  private JTextArea textAreaVertretungsmoeglichkeiten;
  private JTextArea textAreaBemerkungen;
  private JLabel errLblAnrede;
  private JLabel errLblNachname;
  private JLabel errLblVorname;
  private JLabel errLblStrasseHausnummer;
  private JLabel errLblPlz;
  private JLabel errLblOrt;
  private JLabel errLblFestnetz;
  private JLabel errLblNatel;
  private JLabel errLblEmail;
  private JLabel errLblGeburtsdatum;
  private JLabel errLblAhvNummer;
  private JLabel errLblIbanNummer;
  private JLabel errLblVertretungsmoeglichkeiten;
  private JLabel errLblBemerkungen;
  private JCheckBox checkBoxLehrkraft;
  private JCheckBox checkBoxAktiv;
  private JLabel lblCodes;
  private JButton btnCodesBearbeiten;
  private JButton btnSpeichern;
  private JButton btnAbbrechen;

  public MitarbeiterErfassenDialog(
      SvmContext svmContext,
      MitarbeitersTableModel mitarbeitersTableModel,
      MitarbeitersModel mitarbeitersModel,
      int indexBearbeiten,
      boolean isBearbeiten,
      String title) {
    $$$setupUI$$$();
    setContentPane(contentPane);
    setModal(true);
    setTitle(title);
    if (DEFAULT_BUTTON_ENABLED) {
      getRootPane().setDefaultButton(btnSpeichern);
    }
    createLehrkraftErfassenController(
        svmContext, mitarbeitersTableModel, mitarbeitersModel, indexBearbeiten, isBearbeiten);
    initializeErrLbls();
  }

  @SuppressWarnings("DuplicatedCode")
  private void createLehrkraftErfassenController(
      SvmContext svmContext,
      MitarbeitersTableModel mitarbeitersTableModel,
      MitarbeitersModel mitarbeitersModel,
      int indexBearbeiten,
      boolean isBearbeiten) {
    MitarbeiterErfassenModel mitarbeiterErfassenModel =
        (isBearbeiten
            ? mitarbeitersModel.getMitarbeiterErfassenModel(
                svmContext, mitarbeitersTableModel, indexBearbeiten)
            : svmContext.getModelFactory().createMitarbeiterErfassenModel());
    MitarbeiterErfassenController mitarbeiterErfassenController =
        new MitarbeiterErfassenController(
            svmContext,
            mitarbeitersTableModel,
            mitarbeiterErfassenModel,
            isBearbeiten,
            DEFAULT_BUTTON_ENABLED);
    mitarbeiterErfassenController.setMitarbeiterErfassenDialog(this);
    mitarbeiterErfassenController.setContentPane(contentPane);
    mitarbeiterErfassenController.setComboBoxAnrede(comboBoxAnrede);
    mitarbeiterErfassenController.setTxtNachname(txtNachname);
    mitarbeiterErfassenController.setTxtVorname(txtVorname);
    mitarbeiterErfassenController.setTxtStrasseHausnummer(txtStrasseHausnummer);
    mitarbeiterErfassenController.setTxtPlz(txtPlz);
    mitarbeiterErfassenController.setTxtOrt(txtOrt);
    mitarbeiterErfassenController.setTxtFestnetz(txtFestnetz);
    mitarbeiterErfassenController.setTxtNatel(txtNatel);
    mitarbeiterErfassenController.setTxtEmail(txtEmail);
    mitarbeiterErfassenController.setTxtGeburtsdatum(txtGeburtsdatum);
    mitarbeiterErfassenController.setTxtAhvNummer(txtAhvNummer);
    mitarbeiterErfassenController.setTxtIbanNummer(txtIbanNummer);
    mitarbeiterErfassenController.setTextAreaVertretungsmoeglichkeiten(
        textAreaVertretungsmoeglichkeiten);
    mitarbeiterErfassenController.setTextAreaBemerkungen(textAreaBemerkungen);
    mitarbeiterErfassenController.setCheckBoxLehrkraft(checkBoxLehrkraft);
    mitarbeiterErfassenController.setCheckBoxAktiv(checkBoxAktiv);
    mitarbeiterErfassenController.setLblCodes(lblCodes);
    mitarbeiterErfassenController.setBtnCodesBearbeiten(btnCodesBearbeiten);
    mitarbeiterErfassenController.setBtnSpeichern(btnSpeichern);
    mitarbeiterErfassenController.setBtnAbbrechen(btnAbbrechen);
    mitarbeiterErfassenController.setErrLblAnrede(errLblAnrede);
    mitarbeiterErfassenController.setErrLblNachname(errLblNachname);
    mitarbeiterErfassenController.setErrLblVorname(errLblVorname);
    mitarbeiterErfassenController.setErrLblStrasseHausnummer(errLblStrasseHausnummer);
    mitarbeiterErfassenController.setErrLblPlz(errLblPlz);
    mitarbeiterErfassenController.setErrLblOrt(errLblOrt);
    mitarbeiterErfassenController.setErrLblFestnetz(errLblFestnetz);
    mitarbeiterErfassenController.setErrLblNatel(errLblNatel);
    mitarbeiterErfassenController.setErrLblEmail(errLblEmail);
    mitarbeiterErfassenController.setErrLblGeburtsdatum(errLblGeburtsdatum);
    mitarbeiterErfassenController.setErrLblAhvNummer(errLblAhvNummer);
    mitarbeiterErfassenController.setErrLblIbanNummer(errLblIbanNummer);
    mitarbeiterErfassenController.setErrLblVertretungsmoeglichkeiten(
        errLblVertretungsmoeglichkeiten);
    mitarbeiterErfassenController.setErrLblBemerkungen(errLblBemerkungen);
    mitarbeiterErfassenController.constructionDone();
  }

  @SuppressWarnings("DuplicatedCode")
  private void initializeErrLbls() {
    errLblAnrede.setVisible(false);
    errLblAnrede.setForeground(Color.RED);
    errLblNachname.setVisible(false);
    errLblNachname.setForeground(Color.RED);
    errLblVorname.setVisible(false);
    errLblVorname.setForeground(Color.RED);
    errLblStrasseHausnummer.setVisible(false);
    errLblStrasseHausnummer.setForeground(Color.RED);
    errLblPlz.setVisible(false);
    errLblPlz.setForeground(Color.RED);
    errLblOrt.setVisible(false);
    errLblOrt.setForeground(Color.RED);
    errLblFestnetz.setVisible(false);
    errLblFestnetz.setForeground(Color.RED);
    errLblNatel.setVisible(false);
    errLblNatel.setForeground(Color.RED);
    errLblEmail.setVisible(false);
    errLblEmail.setForeground(Color.RED);
    errLblGeburtsdatum.setVisible(false);
    errLblGeburtsdatum.setForeground(Color.RED);
    errLblAhvNummer.setVisible(false);
    errLblAhvNummer.setForeground(Color.RED);
    errLblIbanNummer.setVisible(false);
    errLblIbanNummer.setForeground(Color.RED);
    errLblVertretungsmoeglichkeiten.setVisible(false);
    errLblVertretungsmoeglichkeiten.setForeground(Color.RED);
    errLblBemerkungen.setVisible(false);
    errLblBemerkungen.setForeground(Color.RED);
  }

  private void createUIComponents() {
    comboBoxAnrede = new JComboBox<>();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout(0, 0));
    datenPanel = new JPanel();
    datenPanel.setLayout(new GridBagLayout());
    contentPane.add(datenPanel, BorderLayout.CENTER);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(10, 10, 10, 10);
    datenPanel.add(panel1, gbc);
    panel1.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Mitarbeiter",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()),
            new Color(-16777216)));
    final JLabel label1 = new JLabel();
    label1.setText("Nachname");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label1, gbc);
    txtNachname = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 3;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtNachname, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer2, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer3, gbc);
    errLblNachname = new JLabel();
    errLblNachname.setText("errLblNachname");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 2;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblNachname, gbc);
    final JLabel label2 = new JLabel();
    label2.setText("Vorname");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 5;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label2, gbc);
    txtVorname = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 5;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtVorname, gbc);
    errLblVorname = new JLabel();
    errLblVorname.setText("errLblVorname");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblVorname, gbc);
    final JPanel spacer4 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer4, gbc);
    final JPanel spacer5 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 6;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer5, gbc);
    final JLabel label3 = new JLabel();
    label3.setText("Strasse/Nr.");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 7;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label3, gbc);
    txtStrasseHausnummer = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 7;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtStrasseHausnummer, gbc);
    errLblStrasseHausnummer = new JLabel();
    errLblStrasseHausnummer.setText("errLblStrasseHausnummer");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 6;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblStrasseHausnummer, gbc);
    final JPanel spacer6 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 8;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer6, gbc);
    final JLabel label4 = new JLabel();
    label4.setText("PLZ/Ort");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 9;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label4, gbc);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 9;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    panel1.add(panel2, gbc);
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
    errLblPlz = new JLabel();
    errLblPlz.setText("errLblPlz");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 8;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblPlz, gbc);
    final JPanel spacer8 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 8;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(spacer8, gbc);
    errLblOrt = new JLabel();
    errLblOrt.setText("errLblOrt");
    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 8;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblOrt, gbc);
    final JPanel spacer9 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 10;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer9, gbc);
    final JLabel label5 = new JLabel();
    label5.setText("Festnetz");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 11;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label5, gbc);
    txtFestnetz = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 11;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtFestnetz, gbc);
    errLblFestnetz = new JLabel();
    errLblFestnetz.setText("errLblFestnetz");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 10;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblFestnetz, gbc);
    final JPanel spacer10 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 12;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer10, gbc);
    final JLabel label6 = new JLabel();
    label6.setText("Natel");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 13;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label6, gbc);
    txtNatel = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 13;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtNatel, gbc);
    errLblNatel = new JLabel();
    errLblNatel.setText("errLblNatel");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 12;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblNatel, gbc);
    final JPanel spacer11 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 14;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer11, gbc);
    final JLabel label7 = new JLabel();
    label7.setText("E-Mail");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 15;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label7, gbc);
    txtEmail = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 15;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtEmail, gbc);
    errLblEmail = new JLabel();
    errLblEmail.setText("errLblEmail");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 14;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblEmail, gbc);
    final JPanel spacer12 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 16;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer12, gbc);
    final JLabel label8 = new JLabel();
    label8.setText("Geburtsdatum");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 17;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label8, gbc);
    txtGeburtsdatum = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 17;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtGeburtsdatum, gbc);
    errLblGeburtsdatum = new JLabel();
    errLblGeburtsdatum.setText("errLblGeburtsdatum");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 16;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblGeburtsdatum, gbc);
    final JPanel spacer13 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 18;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer13, gbc);
    final JLabel label9 = new JLabel();
    label9.setText("AHV-Nummer");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 19;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label9, gbc);
    txtAhvNummer = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 19;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtAhvNummer, gbc);
    errLblAhvNummer = new JLabel();
    errLblAhvNummer.setText("errLblAhvNummer");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 18;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblAhvNummer, gbc);
    final JPanel spacer14 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 22;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer14, gbc);
    final JLabel label10 = new JLabel();
    label10.setText("Vertretungsmöglichkeiten");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 23;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(0, 0, 0, 10);
    panel1.add(label10, gbc);
    errLblVertretungsmoeglichkeiten = new JLabel();
    errLblVertretungsmoeglichkeiten.setText("errLblVertretungsmoeglichkeiten");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 22;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblVertretungsmoeglichkeiten, gbc);
    final JPanel spacer15 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 26;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer15, gbc);
    checkBoxAktiv = new JCheckBox();
    checkBoxAktiv.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 29;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(checkBoxAktiv, gbc);
    final JLabel label11 = new JLabel();
    label11.setText("Aktiv");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 29;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label11, gbc);
    final JPanel spacer16 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 32;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer16, gbc);
    final JLabel label12 = new JLabel();
    label12.setText("Anrede");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label12, gbc);
    final JPanel spacer17 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer17, gbc);
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(comboBoxAnrede, gbc);
    final JPanel spacer18 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 32;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipadx = 300;
    panel1.add(spacer18, gbc);
    errLblAnrede = new JLabel();
    errLblAnrede.setText("errLblAnrede");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblAnrede, gbc);
    final JPanel spacer19 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 24;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer19, gbc);
    final JScrollPane scrollPane1 = new JScrollPane();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 23;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipady = 20;
    panel1.add(scrollPane1, gbc);
    textAreaVertretungsmoeglichkeiten = new JTextArea();
    textAreaVertretungsmoeglichkeiten.setLineWrap(true);
    textAreaVertretungsmoeglichkeiten.setWrapStyleWord(true);
    scrollPane1.setViewportView(textAreaVertretungsmoeglichkeiten);
    final JLabel label13 = new JLabel();
    label13.setText("Bemerkungen");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 25;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label13, gbc);
    final JScrollPane scrollPane2 = new JScrollPane();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 25;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.ipady = 20;
    panel1.add(scrollPane2, gbc);
    textAreaBemerkungen = new JTextArea();
    textAreaBemerkungen.setLineWrap(true);
    textAreaBemerkungen.setWrapStyleWord(true);
    scrollPane2.setViewportView(textAreaBemerkungen);
    errLblBemerkungen = new JLabel();
    errLblBemerkungen.setText("errLblBemerkungen");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 24;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblBemerkungen, gbc);
    final JLabel label14 = new JLabel();
    label14.setText("Lehrkraft");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 27;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label14, gbc);
    checkBoxLehrkraft = new JCheckBox();
    checkBoxLehrkraft.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 27;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(checkBoxLehrkraft, gbc);
    final JPanel spacer20 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 28;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer20, gbc);
    final JLabel label15 = new JLabel();
    label15.setText("Codes");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 31;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label15, gbc);
    final JPanel spacer21 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 30;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer21, gbc);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 31;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.BOTH;
    panel1.add(panel3, gbc);
    lblCodes = new JLabel();
    lblCodes.setText("lblCodes");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    panel3.add(lblCodes, gbc);
    final JPanel spacer22 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel3.add(spacer22, gbc);
    btnCodesBearbeiten = new JButton();
    btnCodesBearbeiten.setMaximumSize(new Dimension(162, 29));
    btnCodesBearbeiten.setMinimumSize(new Dimension(162, 29));
    btnCodesBearbeiten.setPreferredSize(new Dimension(162, 29));
    btnCodesBearbeiten.setText("Codes bearbeiten");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel3.add(btnCodesBearbeiten, gbc);
    errLblIbanNummer = new JLabel();
    errLblIbanNummer.setText("errLblIbanNummer");
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 20;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(errLblIbanNummer, gbc);
    final JPanel spacer23 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 20;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer23, gbc);
    final JLabel label16 = new JLabel();
    label16.setText("IBAN-Nummer");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 21;
    gbc.anchor = GridBagConstraints.WEST;
    panel1.add(label16, gbc);
    txtIbanNummer = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 21;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(txtIbanNummer, gbc);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    datenPanel.add(panel4, gbc);
    btnSpeichern = new JButton();
    btnSpeichern.setMaximumSize(new Dimension(114, 29));
    btnSpeichern.setMinimumSize(new Dimension(114, 29));
    btnSpeichern.setPreferredSize(new Dimension(114, 29));
    btnSpeichern.setText("Speichern");
    btnSpeichern.setMnemonic('S');
    btnSpeichern.setDisplayedMnemonicIndex(0);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel4.add(btnSpeichern, gbc);
    final JPanel spacer24 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel4.add(spacer24, gbc);
    final JPanel spacer25 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel4.add(spacer25, gbc);
    final JPanel spacer26 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel4.add(spacer26, gbc);
    btnAbbrechen = new JButton();
    btnAbbrechen.setMaximumSize(new Dimension(114, 29));
    btnAbbrechen.setMinimumSize(new Dimension(114, 29));
    btnAbbrechen.setPreferredSize(new Dimension(114, 29));
    btnAbbrechen.setText("Abbrechen");
    btnAbbrechen.setMnemonic('A');
    btnAbbrechen.setDisplayedMnemonicIndex(0);
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel4.add(btnAbbrechen, gbc);
    label1.setLabelFor(txtNachname);
    label2.setLabelFor(txtVorname);
    label3.setLabelFor(txtStrasseHausnummer);
    label4.setLabelFor(txtPlz);
    label5.setLabelFor(txtFestnetz);
    label6.setLabelFor(txtNatel);
    label7.setLabelFor(txtEmail);
    label8.setLabelFor(txtGeburtsdatum);
    label9.setLabelFor(txtAhvNummer);
    label13.setLabelFor(scrollPane2);
    label16.setLabelFor(txtIbanNummer);
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
    Font font =
        new Font(
            resultName,
            style >= 0 ? style : currentFont.getStyle(),
            size >= 0 ? size : currentFont.getSize());
    boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
    Font fontWithFallback =
        isMac
            ? new Font(font.getFamily(), font.getStyle(), font.getSize())
            : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
    return fontWithFallback instanceof FontUIResource
        ? fontWithFallback
        : new FontUIResource(fontWithFallback);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }
}
