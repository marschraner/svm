package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.KursortErfassenModel;
import ch.metzenthin.svm.domain.model.KursorteModel;
import ch.metzenthin.svm.ui.control.KursortErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class KursortErfassenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel datenPanel;
    private JTextField txtBezeichnung;
    private JPanel buttonPanel;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    private JLabel errLblBezeichnung;

    public KursortErfassenDialog(SvmContext svmContext, KursorteModel kursorteModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        createKursortErfassenController(svmContext, kursorteModel, indexBearbeiten, isBearbeiten);
    }

    private void createKursortErfassenController(SvmContext svmContext, KursorteModel kursorteModel, int indexBearbeiten, boolean isBearbeiten) {
        KursortErfassenModel kursortErfassenModel = (isBearbeiten ? kursorteModel.getKursortErfassenModel(svmContext, indexBearbeiten) : svmContext.getModelFactory().createKursortErfassenModel());
        KursortErfassenController kursortErfassenController = new KursortErfassenController(svmContext, kursortErfassenModel);
        kursortErfassenController.setKursortErfassenDialog(this);
        kursortErfassenController.setContentPane(contentPane);
        kursortErfassenController.setTxtBezeichnung(txtBezeichnung);
        kursortErfassenController.setBtnSpeichern(btnSpeichern);
        kursortErfassenController.setBtnAbbrechen(btnAbbrechen);
        kursortErfassenController.setErrLblBezeichnung(errLblBezeichnung);
        kursortErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblBezeichnung.setVisible(false);
        errLblBezeichnung.setForeground(Color.RED);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kursort", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize())));
        final JLabel label1 = new JLabel();
        label1.setText("Bezeichnung");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 300;
        panel1.add(spacer5, gbc);
        txtBezeichnung = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBezeichnung, gbc);
        errLblBezeichnung = new JLabel();
        errLblBezeichnung.setText("errLblKursort");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBezeichnung, gbc);
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
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer8, gbc);
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
        label1.setLabelFor(txtBezeichnung);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}