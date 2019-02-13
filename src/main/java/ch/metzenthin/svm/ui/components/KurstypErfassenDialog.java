package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.KurstypErfassenModel;
import ch.metzenthin.svm.domain.model.KurstypenModel;
import ch.metzenthin.svm.ui.componentmodel.KurstypenTableModel;
import ch.metzenthin.svm.ui.control.KurstypErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class KurstypErfassenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTextField txtBezeichnung;
    private JCheckBox checkBoxSelektierbar;
    private JLabel errLblBezeichnung;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;

    public KurstypErfassenDialog(SvmContext svmContext, KurstypenTableModel kurstypenTableModel, KurstypenModel kurstypenModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnSpeichern);
        }
        createKurstypErfassenController(svmContext, kurstypenTableModel, kurstypenModel, indexBearbeiten, isBearbeiten);
    }

    private void createKurstypErfassenController(SvmContext svmContext, KurstypenTableModel kurstypenTableModel, KurstypenModel kurstypenModel, int indexBearbeiten, boolean isBearbeiten) {
        KurstypErfassenModel kurstypErfassenModel = (isBearbeiten ? kurstypenModel.getKurstypErfassenModel(svmContext, indexBearbeiten) : svmContext.getModelFactory().createKurstypErfassenModel());
        KurstypErfassenController kurstypErfassenController = new KurstypErfassenController(svmContext, kurstypenTableModel, kurstypErfassenModel, isBearbeiten, DEFAULT_BUTTON_ENABLED);
        kurstypErfassenController.setKurstypErfassenDialog(this);
        kurstypErfassenController.setContentPane(contentPane);
        kurstypErfassenController.setTxtBezeichnung(txtBezeichnung);
        kurstypErfassenController.setCheckBoxSelektierbar(checkBoxSelektierbar);
        kurstypErfassenController.setBtnSpeichern(btnSpeichern);
        kurstypErfassenController.setBtnAbbrechen(btnAbbrechen);
        kurstypErfassenController.setErrLblBezeichnung(errLblBezeichnung);
        kurstypErfassenController.constructionDone();
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kurstyp", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), new Color(-16777216)));
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
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
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
        errLblBezeichnung.setText("errLblBezeichnung");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBezeichnung, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Selektierbar");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        checkBoxSelektierbar = new JCheckBox();
        checkBoxSelektierbar.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(checkBoxSelektierbar, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
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
        buttonPanel.add(btnSpeichern, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer9, gbc);
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
        buttonPanel.add(btnAbbrechen, gbc);
        label1.setLabelFor(txtBezeichnung);
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
