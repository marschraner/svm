package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.SchuelerBereitsInDatenbankResult;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.domain.model.SchuelerErfassenSaveResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SchuelerBereitsInDatenbankDialog extends SchuelerErfassenDialog {
    private final SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lblBeschreibung;
    private JLabel lblSchueler;
    private SchuelerErfassenSaveResult result;

    public SchuelerBereitsInDatenbankDialog(SchuelerBereitsInDatenbankResult schuelerBereitsInDatenbankResult, SchuelerErfassenModel schuelerErfassenModel) {
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        if (schuelerBereitsInDatenbankResult != null) {
            lblSchueler.setText(schuelerBereitsInDatenbankResult.getSchuelerToString());
            lblBeschreibung.setText(schuelerBereitsInDatenbankResult.getBeschreibung());
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        schuelerErfassenModel.abbrechen();
        result = null;
        dispose();
    }

    public SchuelerErfassenSaveResult getResult() {
        return result;
    }

    public static void main(String[] args) {
        SchuelerBereitsInDatenbankDialog dialog = new SchuelerBereitsInDatenbankDialog(null, null);
        dialog.lblSchueler.setText("söldfkjaö");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
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
        contentPane.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        buttonOK = new JButton();
        buttonOK.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        contentPane.add(panel3, gbc);
        lblBeschreibung = new JLabel();
        lblBeschreibung.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblBeschreibung, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer2, gbc);
        lblSchueler = new JLabel();
        lblSchueler.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblSchueler, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
