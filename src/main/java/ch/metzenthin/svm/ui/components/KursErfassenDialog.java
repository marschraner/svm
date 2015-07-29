package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.KursErfassenModel;
import ch.metzenthin.svm.domain.model.KurseSemesterwahlModel;
import ch.metzenthin.svm.persistence.entities.Kursort;
import ch.metzenthin.svm.persistence.entities.Kurstyp;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.control.KursErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class KursErfassenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTextField txtAltersbereich;
    private JTextField txtStufe;
    private JTextField txtZeitBeginn;
    private JTextField txtZeitEnde;
    private JTextField txtBemerkungen;
    private JComboBox<Kurstyp> comboBoxKurstyp;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Kursort> comboBoxKursort;
    private JComboBox<Lehrkraft> comboBoxLehrkraft1;
    private JComboBox<Lehrkraft> comboBoxLehrkraft2;
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
    private JButton btnAbbrechen;

    public KursErfassenDialog(SvmContext svmContext, KurseModel kurseModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        createKursErfassenController(svmContext, kurseModel, kurseSemesterwahlModel, kurseTableModel, indexBearbeiten, isBearbeiten);
        initializeErrLbls();
    }

    private void createKursErfassenController(SvmContext svmContext, KurseModel kurseModel, KurseSemesterwahlModel kurseSemesterwahlModel, KurseTableModel kurseTableModel, int indexBearbeiten, boolean isBearbeiten) {
        KursErfassenModel kursErfassenModel = (isBearbeiten ? kurseModel.getKursErfassenModel(svmContext, kurseTableModel, indexBearbeiten) : svmContext.getModelFactory().createKursErfassenModel());
        KursErfassenController kursErfassenController = new KursErfassenController(svmContext, kursErfassenModel, kurseSemesterwahlModel, kurseTableModel);
        kursErfassenController.setKursErfassenDialog(this);
        kursErfassenController.setContentPane(contentPane);
        kursErfassenController.setComboBoxKurstyp(comboBoxKurstyp);
        kursErfassenController.setTxtAltersbereich(txtAltersbereich);
        kursErfassenController.setTxtStufe(txtStufe);
        kursErfassenController.setComboBoxWochentag(comboBoxWochentag);
        kursErfassenController.setTxtZeitBeginn(txtZeitBeginn);
        kursErfassenController.setTxtZeitEnde(txtZeitEnde);
        kursErfassenController.setComboBoxKursort(comboBoxKursort);
        kursErfassenController.setComboBoxLehrkraft1(comboBoxLehrkraft1);
        kursErfassenController.setComboBoxLehrkraft2(comboBoxLehrkraft2);
        kursErfassenController.setTxtBemerkungen(txtBemerkungen);
        kursErfassenController.setBtnSpeichern(btnSpeichern);
        kursErfassenController.setBtnAbbrechen(btnAbbrechen);
        kursErfassenController.setErrLblKurstyp(errLblKurstyp);
        kursErfassenController.setErrLblAltersbereich(errLblAltersbereich);
        kursErfassenController.setErrLblStufe(errLblStufe);
        kursErfassenController.setErrLblWochentag(errLblWochentag);
        kursErfassenController.setErrLblZeitBeginn(errLblZeitBeginn);
        kursErfassenController.setErrLblZeitEnde(errLblZeitEnde);
        kursErfassenController.setErrLblKursort(errLblKursort);
        kursErfassenController.setErrLblLehrkraft1(errLblLehrkraft1);
        kursErfassenController.setErrLblLehrkraft2(errLblLehrkraft2);
        kursErfassenController.setErrLblBemerkungen(errLblBemerkungen);
        kursErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblKurstyp.setVisible(false);
        errLblKurstyp.setForeground(Color.RED);
        errLblAltersbereich.setVisible(false);
        errLblAltersbereich.setForeground(Color.RED);
        errLblStufe.setVisible(false);
        errLblStufe.setForeground(Color.RED);
        errLblWochentag.setVisible(false);
        errLblWochentag.setForeground(Color.RED);
        errLblZeitBeginn.setVisible(false);
        errLblZeitBeginn.setForeground(Color.RED);
        errLblZeitEnde.setVisible(false);
        errLblZeitEnde.setForeground(Color.RED);
        errLblKursort.setVisible(false);
        errLblKursort.setForeground(Color.RED);
        errLblLehrkraft1.setVisible(false);
        errLblLehrkraft1.setForeground(Color.RED);
        errLblLehrkraft2.setVisible(false);
        errLblLehrkraft2.setForeground(Color.RED);
        errLblBemerkungen.setVisible(false);
        errLblBemerkungen.setForeground(Color.RED);
    }

    private void createUIComponents() {
        comboBoxKurstyp = new JComboBox<>();
        comboBoxWochentag = new JComboBox<>();
        comboBoxKursort = new JComboBox<>();
        comboBoxLehrkraft1 = new JComboBox<>();
        comboBoxLehrkraft2 = new JComboBox<>();
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kurs", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize()), new Color(-16777216)));
        final JLabel label1 = new JLabel();
        label1.setText("Kurstyp");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxKurstyp, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Alter");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        txtAltersbereich = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtAltersbereich, gbc);
        errLblAltersbereich = new JLabel();
        errLblAltersbereich.setText("errLblAlter");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblAltersbereich, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Stufe");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        txtStufe = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtStufe, gbc);
        errLblStufe = new JLabel();
        errLblStufe.setText("errLblStufe");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblStufe, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Tag");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer6, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxWochentag, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Zeit");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label5, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("von");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label6, gbc);
        txtZeitBeginn = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel2.add(txtZeitBeginn, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("bis");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label7, gbc);
        txtZeitEnde = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel2.add(txtZeitEnde, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        errLblZeitBeginn = new JLabel();
        errLblZeitBeginn.setText("errLblVon");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(errLblZeitBeginn, gbc);
        errLblZeitEnde = new JLabel();
        errLblZeitEnde.setText("errLblBis");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(errLblZeitEnde, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer9, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer10, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Ort");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label8, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxKursort, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer11, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Lehrkraft 1");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label9, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxLehrkraft1, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer12, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Lehrkraft 2");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label10, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxLehrkraft2, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer13, gbc);
        errLblLehrkraft1 = new JLabel();
        errLblLehrkraft1.setText("errLblLehrkraft1");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblLehrkraft1, gbc);
        errLblLehrkraft2 = new JLabel();
        errLblLehrkraft2.setText("errLblLehrkraft2");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblLehrkraft2, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Bemerkungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label11, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer14, gbc);
        txtBemerkungen = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBemerkungen, gbc);
        errLblBemerkungen = new JLabel();
        errLblBemerkungen.setText("errLblBemerkungen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBemerkungen, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 18;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer15, gbc);
        errLblKurstyp = new JLabel();
        errLblKurstyp.setText("errLblKurstyp");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblKurstyp, gbc);
        errLblWochentag = new JLabel();
        errLblWochentag.setText("errLblWochentag");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblWochentag, gbc);
        errLblKursort = new JLabel();
        errLblKursort.setText("errLblKursort");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblKursort, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnSpeichern = new JButton();
        btnSpeichern.setMaximumSize(new Dimension(114, 29));
        btnSpeichern.setMinimumSize(new Dimension(114, 29));
        btnSpeichern.setPreferredSize(new Dimension(114, 29));
        btnSpeichern.setText("Speichern");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnSpeichern, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer16, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer17, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer18, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(114, 29));
        btnAbbrechen.setMinimumSize(new Dimension(114, 29));
        btnAbbrechen.setPreferredSize(new Dimension(114, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(comboBoxKurstyp);
        label2.setLabelFor(txtAltersbereich);
        label3.setLabelFor(txtStufe);
        label4.setLabelFor(comboBoxWochentag);
        label6.setLabelFor(txtZeitBeginn);
        label7.setLabelFor(txtZeitEnde);
        label8.setLabelFor(comboBoxKursort);
        label9.setLabelFor(comboBoxLehrkraft1);
        label10.setLabelFor(comboBoxLehrkraft2);
        label11.setLabelFor(txtBemerkungen);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
