package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenErfassenModel;
import ch.metzenthin.svm.domain.model.LektionsgebuehrenModel;
import ch.metzenthin.svm.ui.componentmodel.LektionsgebuehrenTableModel;
import ch.metzenthin.svm.ui.control.LektionsgebuehrenErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LektionsgebuehrenErfassenDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTextField txtLektionslaenge;
    private JTextField txtBetrag1Kind;
    private JTextField txtBetrag2Kinder;
    private JTextField txtBetrag3Kinder;
    private JTextField txtBetrag4Kinder;
    private JTextField txtBetrag5Kinder;
    private JTextField txtBetrag6Kinder;
    private JLabel errLblLektionslaenge;
    private JLabel errLblBetrag1Kind;
    private JLabel errLblBetrag2Kinder;
    private JLabel errLblBetrag3Kinder;
    private JLabel errLblBetrag4Kinder;
    private JLabel errLblBetrag5Kinder;
    private JLabel errLblBetrag6Kinder;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;

    public LektionsgebuehrenErfassenDialog(SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel, LektionsgebuehrenModel lektionsgebuehrenModel, int indexBearbeiten, boolean isBearbeiten, String title) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnSpeichern);
        }
        createLektionsgebuehrenErfassenController(svmContext, lektionsgebuehrenTableModel, lektionsgebuehrenModel, indexBearbeiten, isBearbeiten);
    }

    private void createLektionsgebuehrenErfassenController(SvmContext svmContext, LektionsgebuehrenTableModel lektionsgebuehrenTableModel, LektionsgebuehrenModel lektionsgebuehrenModel, int indexBearbeiten, boolean isBearbeiten) {
        LektionsgebuehrenErfassenModel lektionsgebuehrenErfassenModel = (isBearbeiten ? lektionsgebuehrenModel.getLektionsgebuehrenErfassenModel(svmContext, indexBearbeiten) : svmContext.getModelFactory().createLektionsgebuehrenErfassenModel());
        LektionsgebuehrenErfassenController lektionsgebuehrenErfassenController = new LektionsgebuehrenErfassenController(svmContext, lektionsgebuehrenTableModel, lektionsgebuehrenErfassenModel, isBearbeiten, DEFAULT_BUTTON_ENABLED);
        lektionsgebuehrenErfassenController.setLektionsgebuehrenErfassenDialog(this);
        lektionsgebuehrenErfassenController.setContentPane(contentPane);
        lektionsgebuehrenErfassenController.setTxtLektionslaenge(txtLektionslaenge);
        lektionsgebuehrenErfassenController.setTxtBetrag1Kind(txtBetrag1Kind);
        lektionsgebuehrenErfassenController.setTxtBetrag2Kinder(txtBetrag2Kinder);
        lektionsgebuehrenErfassenController.setTxtBetrag3Kinder(txtBetrag3Kinder);
        lektionsgebuehrenErfassenController.setTxtBetrag4Kinder(txtBetrag4Kinder);
        lektionsgebuehrenErfassenController.setTxtBetrag5Kinder(txtBetrag5Kinder);
        lektionsgebuehrenErfassenController.setTxtBetrag6Kinder(txtBetrag6Kinder);
        lektionsgebuehrenErfassenController.setBtnSpeichern(btnSpeichern);
        lektionsgebuehrenErfassenController.setBtnAbbrechen(btnAbbrechen);
        lektionsgebuehrenErfassenController.setErrLblLektionslaenge(errLblLektionslaenge);
        lektionsgebuehrenErfassenController.setErrLblBetrag1Kind(errLblBetrag1Kind);
        lektionsgebuehrenErfassenController.setErrLblBetrag2Kinder(errLblBetrag2Kinder);
        lektionsgebuehrenErfassenController.setErrLblBetrag3Kinder(errLblBetrag3Kinder);
        lektionsgebuehrenErfassenController.setErrLblBetrag4Kinder(errLblBetrag4Kinder);
        lektionsgebuehrenErfassenController.setErrLblBetrag5Kinder(errLblBetrag5Kinder);
        lektionsgebuehrenErfassenController.setErrLblBetrag6Kinder(errLblBetrag6Kinder);
        lektionsgebuehrenErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblLektionslaenge.setVisible(false);
        errLblLektionslaenge.setForeground(Color.RED);
        errLblBetrag1Kind.setVisible(false);
        errLblBetrag1Kind.setForeground(Color.RED);
        errLblBetrag2Kinder.setVisible(false);
        errLblBetrag2Kinder.setForeground(Color.RED);
        errLblBetrag3Kinder.setVisible(false);
        errLblBetrag3Kinder.setForeground(Color.RED);
        errLblBetrag4Kinder.setVisible(false);
        errLblBetrag4Kinder.setForeground(Color.RED);
        errLblBetrag5Kinder.setVisible(false);
        errLblBetrag5Kinder.setForeground(Color.RED);
        errLblBetrag6Kinder.setVisible(false);
        errLblBetrag6Kinder.setForeground(Color.RED);
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lektionsgebühren", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont())));
        final JLabel label1 = new JLabel();
        label1.setText("Lektionslänge");
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
        gbc.ipadx = 250;
        panel1.add(spacer5, gbc);
        txtLektionslaenge = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtLektionslaenge, gbc);
        errLblLektionslaenge = new JLabel();
        errLblLektionslaenge.setText("errLblLektionslaenge");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblLektionslaenge, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("1 Kind");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label2, gbc);
        txtBetrag1Kind = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag1Kind, gbc);
        errLblBetrag1Kind = new JLabel();
        errLblBetrag1Kind.setText("errLblBetrag1Kind");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag1Kind, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer6, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("2 Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        txtBetrag2Kinder = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag2Kinder, gbc);
        errLblBetrag2Kinder = new JLabel();
        errLblBetrag2Kinder.setText("errLblBetrag2Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag2Kinder, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer7, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("3 Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        txtBetrag3Kinder = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag3Kinder, gbc);
        errLblBetrag3Kinder = new JLabel();
        errLblBetrag3Kinder.setText("errLblBetrag3Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag3Kinder, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer8, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("4 Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label5, gbc);
        txtBetrag4Kinder = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag4Kinder, gbc);
        errLblBetrag4Kinder = new JLabel();
        errLblBetrag4Kinder.setText("errLblBetrag4Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag4Kinder, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer9, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("5 Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label6, gbc);
        txtBetrag5Kinder = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag5Kinder, gbc);
        errLblBetrag5Kinder = new JLabel();
        errLblBetrag5Kinder.setText("errLblBetrag5Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag5Kinder, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer10, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("6 Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label7, gbc);
        txtBetrag6Kinder = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 14;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtBetrag6Kinder, gbc);
        errLblBetrag6Kinder = new JLabel();
        errLblBetrag6Kinder.setText("errLblBetrag6Kinder");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblBetrag6Kinder, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer11, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        datenPanel.add(buttonPanel, gbc);
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
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer13, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer14, gbc);
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
        label1.setLabelFor(txtLektionslaenge);
        label2.setLabelFor(txtBetrag1Kind);
        label3.setLabelFor(txtBetrag2Kinder);
        label4.setLabelFor(txtBetrag3Kinder);
        label5.setLabelFor(txtBetrag4Kinder);
        label6.setLabelFor(txtBetrag5Kinder);
        label7.setLabelFor(txtBetrag6Kinder);
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
