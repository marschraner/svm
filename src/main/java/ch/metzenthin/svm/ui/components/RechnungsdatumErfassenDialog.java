package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.dataTypes.Rechnungstyp;
import ch.metzenthin.svm.domain.model.RechnungsdatumErfassenModel;
import ch.metzenthin.svm.ui.componentmodel.SemesterrechnungenTableModel;
import ch.metzenthin.svm.ui.control.RechnungsdatumErfassenController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class RechnungsdatumErfassenDialog extends JDialog {
    private JPanel contentPane;
    private JPanel datenPanel;
    private JPanel buttonPanel;
    private JTextField txtRechnungsdatum;
    private JLabel errLblRechnungsdatum;
    private JButton btnOk;
    private JButton btnAbbrechen;

    public RechnungsdatumErfassenDialog(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, Rechnungstyp rechnungstyp) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Rechnungsdatum");
        initializeErrLbls();
        createRechnungsdatumErfassenController(svmContext, semesterrechnungenTableModel, rechnungstyp);
    }

    private void createRechnungsdatumErfassenController(SvmContext svmContext, SemesterrechnungenTableModel semesterrechnungenTableModel, Rechnungstyp rechnungstyp) {
        RechnungsdatumErfassenModel rechnungsdatumErfassenModel = svmContext.getModelFactory().createRechnungsdatumErfassenModel();
        RechnungsdatumErfassenController rechnungsdatumErfassenController = new RechnungsdatumErfassenController(svmContext, semesterrechnungenTableModel, rechnungsdatumErfassenModel, rechnungstyp);
        rechnungsdatumErfassenController.setRechnungsdatumErfassenDialog(this);
        rechnungsdatumErfassenController.setContentPane(contentPane);
        rechnungsdatumErfassenController.setTxtRechnungsdatum(txtRechnungsdatum);
        rechnungsdatumErfassenController.setBtnOk(btnOk);
        rechnungsdatumErfassenController.setBtnAbbrechen(btnAbbrechen);
        rechnungsdatumErfassenController.setErrLblRechnungsdatum(errLblRechnungsdatum);
        rechnungsdatumErfassenController.constructionDone();
    }

    private void initializeErrLbls() {
        errLblRechnungsdatum.setVisible(false);
        errLblRechnungsdatum.setForeground(Color.RED);
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
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Rechnungsdatum", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), Font.BOLD, panel1.getFont().getSize())));
        final JLabel label1 = new JLabel();
        label1.setText("Rechnungsdatum");
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel1.add(spacer5, gbc);
        txtRechnungsdatum = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(txtRechnungsdatum, gbc);
        errLblRechnungsdatum = new JLabel();
        errLblRechnungsdatum.setText("errLblRechnungsdatum");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(errLblRechnungsdatum, gbc);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        btnOk = new JButton();
        btnOk.setMaximumSize(new Dimension(114, 29));
        btnOk.setMinimumSize(new Dimension(114, 29));
        btnOk.setPreferredSize(new Dimension(114, 29));
        btnOk.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(btnOk, gbc);
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
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        buttonPanel.add(spacer8, gbc);
        errLblRechnungsdatum.setLabelFor(txtRechnungsdatum);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
