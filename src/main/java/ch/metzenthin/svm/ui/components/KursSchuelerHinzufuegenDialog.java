package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.dataTypes.Wochentag;
import ch.metzenthin.svm.domain.model.KursSchuelerHinzufuegenModel;
import ch.metzenthin.svm.domain.model.KurseModel;
import ch.metzenthin.svm.domain.model.SchuelerDatenblattModel;
import ch.metzenthin.svm.persistence.entities.Lehrkraft;
import ch.metzenthin.svm.persistence.entities.Semester;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.control.KursSchuelerHinzufuegenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class KursSchuelerHinzufuegenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JComboBox<Semester> comboBoxSemester;
    private JComboBox<Wochentag> comboBoxWochentag;
    private JComboBox<Lehrkraft> comboBoxLehrkraft;
    private JLabel errLblWochentag;
    private JLabel errLblZeitBeginn;
    private JLabel errLblLehrkraft;
    private JTextField txtZeitBeginn;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public KursSchuelerHinzufuegenDialog(SvmContext svmContext, KurseTableModel kurseTableModel, KurseModel kurseModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle("Kurs hinzufügen");
        initializeErrLbls();
        createKurseSchuelerHinzufuegenController(svmContext, kurseTableModel, kurseModel, schuelerDatenblattModel);
    }

    private void createKurseSchuelerHinzufuegenController(SvmContext svmContext, KurseTableModel kurseTableModel, KurseModel kurseModel, SchuelerDatenblattModel schuelerDatenblattModel) {
        KursSchuelerHinzufuegenModel kursSchuelerHinzufuegenModel = svmContext.getModelFactory().createKursSchuelerHinzufuegenModel();
        KursSchuelerHinzufuegenController kursSchuelerHinzufuegenController = new KursSchuelerHinzufuegenController(svmContext, kurseTableModel, kursSchuelerHinzufuegenModel, kurseModel, schuelerDatenblattModel);
        kursSchuelerHinzufuegenController.setKursSchuelerHinzufuegenDialog(this);
        kursSchuelerHinzufuegenController.setContentPane(contentPane);
        kursSchuelerHinzufuegenController.setComboBoxSemester(comboBoxSemester);
        kursSchuelerHinzufuegenController.setComboBoxWochentag(comboBoxWochentag);
        kursSchuelerHinzufuegenController.setTxtZeitBeginn(txtZeitBeginn);
        kursSchuelerHinzufuegenController.setComboBoxLehrkraft(comboBoxLehrkraft);
        kursSchuelerHinzufuegenController.setBtnOk(btnOk);
        kursSchuelerHinzufuegenController.setBtnAbbrechen(btnAbbrechen);
        kursSchuelerHinzufuegenController.setErrLblWochentag(errLblWochentag);
        kursSchuelerHinzufuegenController.setErrLblZeitBeginn(errLblZeitBeginn);
        kursSchuelerHinzufuegenController.setErrLblLehrkraft(errLblLehrkraft);
    }

    private void initializeErrLbls() {
        errLblWochentag.setVisible(false);
        errLblWochentag.setForeground(Color.RED);
        errLblZeitBeginn.setVisible(false);
        errLblZeitBeginn.setForeground(Color.RED);
        errLblLehrkraft.setVisible(false);
        errLblLehrkraft.setForeground(Color.RED);
    }

    private void createUIComponents() {
        comboBoxSemester = new JComboBox<>();
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kurs", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize())));
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Semester");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label1, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxSemester, gbc);
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
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer9, gbc);
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
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnOk, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer12, gbc);
        btnAbbrechen = new JButton();
        btnAbbrechen.setMaximumSize(new Dimension(115, 29));
        btnAbbrechen.setMinimumSize(new Dimension(115, 29));
        btnAbbrechen.setPreferredSize(new Dimension(115, 29));
        btnAbbrechen.setText("Abbrechen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(comboBoxSemester);
        label2.setLabelFor(comboBoxWochentag);
        label4.setLabelFor(comboBoxLehrkraft);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
