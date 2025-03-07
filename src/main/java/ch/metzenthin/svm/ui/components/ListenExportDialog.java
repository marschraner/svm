package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.ListenExportTyp;
import ch.metzenthin.svm.common.datatypes.Listentyp;
import ch.metzenthin.svm.ui.componentmodel.KurseTableModel;
import ch.metzenthin.svm.ui.componentmodel.MitarbeitersTableModel;
import ch.metzenthin.svm.ui.componentmodel.SchuelerSuchenTableModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.control.ListenExportController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

@SuppressWarnings({"java:S100", "java:S1450"})
public class ListenExportDialog extends JDialog {

    // Schalter zur Aktivierung des Default-Button (nicht dynamisch)
    private static final boolean DEFAULT_BUTTON_ENABLED = false;

    private JPanel contentPane;
    private JPanel datenPanel;
    private JLabel txtListentyp;
    private JComboBox<Listentyp> comboBoxListentyp;
    private JLabel titel;
    private JTextField txtTitel;
    private JLabel errLblListentyp;
    private JLabel errLblTitel;
    private JPanel buttonPanel;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public ListenExportDialog(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel, ListenExportTyp listenExportTyp) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        setTitle("Als Liste exportieren");
        initializeErrLbls();
        if (DEFAULT_BUTTON_ENABLED) {
            getRootPane().setDefaultButton(btnOk);
        }
        createListenExportController(svmContext, schuelerSuchenTableModel, mitarbeitersTableModel, kurseTableModel, semesterrechnungenTableModel, listenExportTyp);
    }

    private void createListenExportController(SvmContext svmContext, SchuelerSuchenTableModel schuelerSuchenTableModel, MitarbeitersTableModel mitarbeitersTableModel, KurseTableModel kurseTableModel, SemesterrechnungenTableModel semesterrechnungenTableModel, ListenExportTyp listenExportTyp) {
        ListenExportController listenExportController = new ListenExportController(svmContext.getModelFactory().createListenExportModel(), schuelerSuchenTableModel, mitarbeitersTableModel, kurseTableModel, semesterrechnungenTableModel, listenExportTyp, DEFAULT_BUTTON_ENABLED);
        listenExportController.setListenExportDialog(this);
        listenExportController.setContentPane(contentPane);
        listenExportController.setComboBoxListentyp(comboBoxListentyp);
        listenExportController.setTxtTitel(txtTitel);
        listenExportController.setBtnOk(btnOk);
        listenExportController.setBtnAbbrechen(btnAbbrechen);
        listenExportController.setErrLblListentyp(errLblListentyp);
        listenExportController.setErrLblTitel(errLblTitel);
    }

    private void initializeErrLbls() {
        errLblListentyp.setVisible(false);
        errLblListentyp.setForeground(Color.RED);
        errLblTitel.setVisible(false);
        errLblTitel.setForeground(Color.RED);
    }

    private void createUIComponents() {
        comboBoxListentyp = new JComboBox<>();
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Liste", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel1.getFont()), null));
        txtListentyp = new JLabel();
        txtListentyp.setText("Listentyp");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 15);
        panel1.add(txtListentyp, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(comboBoxListentyp, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(spacer4, gbc);
        titel = new JLabel();
        titel.setText("Titel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(titel, gbc);
        txtTitel = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtTitel, gbc);
        errLblTitel = new JLabel();
        errLblTitel.setText("errLblTitel");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblTitel, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 450;
        panel1.add(spacer6, gbc);
        errLblListentyp = new JLabel();
        errLblListentyp.setText("errLblListentyp");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblListentyp, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        datenPanel.add(buttonPanel, gbc);
        btnOk = new JButton();
        btnOk.setMaximumSize(new Dimension(114, 29));
        btnOk.setMinimumSize(new Dimension(114, 29));
        btnOk.setOpaque(false);
        btnOk.setPreferredSize(new Dimension(114, 29));
        btnOk.setText("OK");
        btnOk.setMnemonic('O');
        btnOk.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnOk, gbc);
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
        titel.setLabelFor(txtTitel);
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
