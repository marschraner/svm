package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Wochentag;
import ch.metzenthin.svm.domain.model.KursanmeldungErfassenModel;
import ch.metzenthin.svm.domain.model.KursanmeldungenModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Mitarbeiter;
import ch.metzenthin.svm.ui.componentmodel.KursanmeldungenTableModel;
import ch.metzenthin.svm.ui.control.KursanmeldungErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

@SuppressWarnings({"java:S100", "java:S1450"})
public class KursanmeldungErfassenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTextField txtZeitBeginn;
    private JTextField txtAnmeldedatum;
    private JTextField txtAbmeldedatum;
    private JTextField txtBemerkungen;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblLehrkraft;
    private JLabel errLblAnmeldedatum;
    private JLabel errLblAbmeldedatum;
    private JLabel errLblBemerkungen;
    private JSpinner spinnerSemester;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Mitarbeiter> comboBoxLehrkraft;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public KursanmeldungErfassenDialog(SvmContext svmContext, KursanmeldungenModel kursanmeldungenModel, KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnOk);
        }
        createKursanmeldungErfassenController(svmContext, kursanmeldungenModel, kursanmeldungenTableModel, schuelerDatenblattModel, indexBearbeiten, isBearbeiten);
        initializeErrLbls();
    }

    private void createKursanmeldungErfassenController(SvmContext svmContext, KursanmeldungenModel kursanmeldungenModel, KursanmeldungenTableModel kursanmeldungenTableModel, SchuelerDatenblattModel schuelerDatenblattModel, int indexBearbeiten, boolean isBearbeiten) {
        KursanmeldungErfassenModel kursanmeldungErfassenModel = (isBearbeiten ? kursanmeldungenModel.getKursanmeldungErfassenModel(svmContext, kursanmeldungenTableModel, indexBearbeiten) : svmContext.getModelFactory().createKursanmeldungErfassenModel());
        KursanmeldungErfassenController kursanmeldungErfassenController = new KursanmeldungErfassenController(svmContext, kursanmeldungErfassenModel, kursanmeldungenTableModel, schuelerDatenblattModel, isBearbeiten, DEFAULT_BUTTON_ENABLED);
        kursanmeldungErfassenController.setKursanmeldungErfassenDialog(this);
        kursanmeldungErfassenController.setContentPane(contentPane);
        kursanmeldungErfassenController.setErrLblWochentag(errLblWochentag);
        kursanmeldungErfassenController.setErrLblZeitBeginn(errLblZeitBeginn);
        kursanmeldungErfassenController.setErrLblLehrkraft(errLblLehrkraft);
        kursanmeldungErfassenController.setErrLblAnmeldedatum(errLblAnmeldedatum);
        kursanmeldungErfassenController.setErrLblAbmeldedatum(errLblAbmeldedatum);
        kursanmeldungErfassenController.setErrLblBemerkungen(errLblBemerkungen);
        kursanmeldungErfassenController.setComboBoxWochentag(comboBoxWochentag);
        kursanmeldungErfassenController.setTxtZeitBeginn(txtZeitBeginn);
        kursanmeldungErfassenController.setComboBoxLehrkraft(comboBoxLehrkraft);
        kursanmeldungErfassenController.setTxtAnmeldedatum(txtAnmeldedatum);
        kursanmeldungErfassenController.setTxtAbmeldedatum(txtAbmeldedatum);
        kursanmeldungErfassenController.setTxtBemerkungen(txtBemerkungen);
        kursanmeldungErfassenController.setSpinnerSemester(spinnerSemester);
        kursanmeldungErfassenController.setBtnOk(btnOk);
        kursanmeldungErfassenController.setBtnAbbrechen(btnAbbrechen);
        kursanmeldungErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblWochentag.setVisible(false);
        errLblWochentag.setForeground(Color.RED);
        errLblZeitBeginn.setVisible(false);
        errLblZeitBeginn.setForeground(Color.RED);
        errLblLehrkraft.setVisible(false);
        errLblLehrkraft.setForeground(Color.RED);
        errLblAnmeldedatum.setVisible(false);
        errLblAnmeldedatum.setForeground(Color.RED);
        errLblAbmeldedatum.setVisible(false);
        errLblAbmeldedatum.setForeground(Color.RED);
        errLblBemerkungen.setVisible(false);
        errLblBemerkungen.setForeground(Color.RED);
    }

    private void createUIComponents() {
        spinnerSemester = new JSpinner();
        comboBoxWochentag = new JComboBox<>();
        comboBoxLehrkraft = new JComboBox<>();
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kursanmeldung", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), null));
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Schuljahr, Semester");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Tag");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxWochentag, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Zeit Beginn");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label3, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        txtZeitBeginn = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(txtZeitBeginn, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel2.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        errLblZeitBeginn = new JLabel();
        errLblZeitBeginn.setText("errLblZeitBeginn");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblZeitBeginn, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Lehrkraft");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxLehrkraft, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer8, gbc);
        errLblWochentag = new JLabel();
        errLblWochentag.setText("errLblWochentag");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblWochentag, gbc);
        errLblLehrkraft = new JLabel();
        errLblLehrkraft.setText("errLblLehrkraft");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblLehrkraft, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spinnerSemester, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer9, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Anmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label5, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer10, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Bemerkungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label6, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer11, gbc);
        txtBemerkungen = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBemerkungen, gbc);
        errLblBemerkungen = new JLabel();
        errLblBemerkungen.setText("errLblBemerkungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBemerkungen, gbc);
        txtAnmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAnmeldedatum, gbc);
        errLblAnmeldedatum = new JLabel();
        errLblAnmeldedatum.setText("errLblAnmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblAnmeldedatum, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer12, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Abmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label7, gbc);
        txtAbmeldedatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAbmeldedatum, gbc);
        errLblAbmeldedatum = new JLabel();
        errLblAbmeldedatum.setText("errLblAbmeldedatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblAbmeldedatum, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        datenPanel.add(buttonPanel, gbc);
        btnOk = new JButton();
        btnOk.setMaximumSize(new Dimension(115, 29));
        btnOk.setMinimumSize(new Dimension(115, 29));
        btnOk.setPreferredSize(new Dimension(115, 29));
        btnOk.setText("OK");
        btnOk.setMnemonic('O');
        btnOk.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnOk, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer14, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer15, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(115, 29));
        btnAbbrechen.setMinimumSize(new Dimension(115, 29));
        btnAbbrechen.setPreferredSize(new Dimension(115, 29));
        btnAbbrechen.setText("Abbrechen");
        btnAbbrechen.setMnemonic('A');
        btnAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(spinnerSemester);
        label2.setLabelFor(comboBoxWochentag);
        label3.setLabelFor(txtZeitBeginn);
        label4.setLabelFor(comboBoxLehrkraft);
        label5.setLabelFor(txtAnmeldedatum);
        label6.setLabelFor(txtBemerkungen);
        label7.setLabelFor(txtAbmeldedatum);
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
