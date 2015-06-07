package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.domain.model.AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog extends SchuelerErfassenDialog {
    private final AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonWeiterfahren;
    private JButton buttonZurueck;

    public AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog(
            AngehoerigerMehrereEintraegeGleicherNameAndereAttributeResult angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult,
            SchuelerErfassenModel schuelerErfassenModel
    ) {
        this.angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult = angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult;
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonWeiterfahren);

        setTitle("AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog " + angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult.getAngehoerigenArt());

        buttonWeiterfahren.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onWeiterfahren();
            }
        });

        buttonZurueck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        });

        // call onZurueck() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onZurueck();
            }
        });

        // call onZurueck() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onZurueck();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onWeiterfahren() {
        setResult(schuelerErfassenModel.proceedWeiterfahren(angehoerigerMehrereEintraegeGleicherNameAndereAttributeResult));
        dispose();
    }

    private void onZurueck() {
        schuelerErfassenModel.abbrechen();
        abbrechen();
        dispose();
    }

    public static void main(String[] args) {
        AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog dialog = new AngehoerigerMehrereEintraegeGleicherNameAndereAttributeDialog(null, null);
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
        buttonWeiterfahren = new JButton();
        buttonWeiterfahren.setText("Weiterfahren");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonWeiterfahren, gbc);
        buttonZurueck = new JButton();
        buttonZurueck.setText("Zurück");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonZurueck, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel3, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
