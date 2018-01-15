package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.AngehoerigerEinEintragPasstResult;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AngehoerigerEinEintragPasstDialog extends SchuelerErfassenDialog {
    private final AngehoerigerEinEintragPasstResult angehoerigerEinEintragPasstResult;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonUebernehmen;
    private JButton buttonKorrigieren;
    private JLabel lblBeschreibung;
    private JLabel lblAngehoerigerValue;
    private JLabel lblKinderValue;
    private JLabel lblKinder;
    private JLabel lblSchuelerRechnungsempfaenger1;
    private JLabel lblSchuelerRechnungsempfaengerValue;
    private JLabel lblAngehoeriger;
    private JLabel lblSchuelerRechnungsempfaenger2;

    public AngehoerigerEinEintragPasstDialog(
            AngehoerigerEinEintragPasstResult angehoerigerEinEintragPasstResult,
            SchuelerErfassenModel schuelerErfassenModel) {
        this.angehoerigerEinEintragPasstResult = angehoerigerEinEintragPasstResult;
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonUebernehmen);

        setTitle(angehoerigerEinEintragPasstResult.getAngehoerigenArt() + " bereits in Datenbank");
        setLabels(angehoerigerEinEintragPasstResult.getAngehoerigerFound());

        buttonUebernehmen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onUebernehmen();
            }
        });

        buttonKorrigieren.addActionListener(new ActionListener() {
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

    private void setLabels(Angehoeriger angehoeriger) {
        lblBeschreibung.setText("In der Datenbank wurde ein Eintrag gefunden, der auf die erfassten Angaben von " + angehoerigerEinEintragPasstResult.getAngehoerigenArt() + " passt:");
        lblAngehoerigerValue.setText(angehoeriger.toString());
        Set<Schueler> schuelerList = new HashSet<>();
        if (angehoeriger.getKinderMutter().size() > 0) {
            lblAngehoeriger.setText("Mutter:");
            schuelerList = angehoeriger.getKinderMutter();
        } else if (angehoeriger.getKinderVater().size() > 0) {
            lblAngehoeriger.setText("Vater:");
            schuelerList = angehoeriger.getKinderVater();
        } else if (angehoeriger.getSchuelerRechnungsempfaenger().size() > 0) {
            if (angehoeriger.getAnrede() == Anrede.FRAU) {
                lblAngehoeriger.setText("Rechnungsempfängerin:");
                lblSchuelerRechnungsempfaenger1.setText("Schüler mit dieser");
                lblSchuelerRechnungsempfaenger2.setText("Rechnungsempfängerin:");
            } else {
                lblAngehoeriger.setText("Rechnungsempfänger:");
                lblSchuelerRechnungsempfaenger1.setText("Schüler mit diesem");
                lblSchuelerRechnungsempfaenger2.setText("Rechnungsempfänger:");
            }
            schuelerList = angehoeriger.getSchuelerRechnungsempfaenger();
        }
        String schuelerAsStr = "-";
        if (schuelerList.size() > 0) {
            StringBuilder schuelerStb = new StringBuilder("<html>");
            for (Schueler schueler : schuelerList) {
                if (schuelerStb.length() > 6) {
                    schuelerStb.append("<br>");
                }
                schuelerStb.append(schueler.toString());
            }
            schuelerStb.append("</html>");
            schuelerAsStr = schuelerStb.toString();
        }
        if (angehoeriger.getKinderMutter().size() > 0 || angehoeriger.getKinderVater().size() > 0) {
            lblKinderValue.setText(schuelerAsStr);
            lblSchuelerRechnungsempfaenger1.setVisible(false);
            lblSchuelerRechnungsempfaenger2.setVisible(false);
            lblSchuelerRechnungsempfaengerValue.setVisible(false);
        } else if (angehoeriger.getSchuelerRechnungsempfaenger().size() > 0) {
            lblSchuelerRechnungsempfaengerValue.setText(schuelerAsStr);
            lblKinder.setVisible(false);
            lblKinderValue.setVisible(false);
        } else {
            lblKinderValue.setText("-");
            lblSchuelerRechnungsempfaengerValue.setText("-");
        }
    }

    private void onUebernehmen() {
        setResult(schuelerErfassenModel.proceedUebernehmen(angehoerigerEinEintragPasstResult));
        dispose();
    }

    private void onZurueck() {
        schuelerErfassenModel.abbrechen();
        abbrechen();
        dispose();
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        contentPane.add(panel1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel1.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Datenbankeintrag gefunden", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel2.getFont()), new Color(-16777216)));
        lblAngehoerigerValue = new JLabel();
        lblAngehoerigerValue.setText("AngehörigerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAngehoerigerValue, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        lblAngehoeriger = new JLabel();
        lblAngehoeriger.setText("Angehöriger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAngehoeriger, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        lblBeschreibung = new JLabel();
        lblBeschreibung.setText("Beschreibung");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblBeschreibung, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer6, gbc);
        lblKinder = new JLabel();
        lblKinder.setText("Angemeldete Kinder:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(lblKinder, gbc);
        lblKinderValue = new JLabel();
        lblKinderValue.setText("KinderValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblKinderValue, gbc);
        lblSchuelerRechnungsempfaenger1 = new JLabel();
        lblSchuelerRechnungsempfaenger1.setText("Schüler mit diesem");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(lblSchuelerRechnungsempfaenger1, gbc);
        lblSchuelerRechnungsempfaenger2 = new JLabel();
        lblSchuelerRechnungsempfaenger2.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(lblSchuelerRechnungsempfaenger2, gbc);
        lblSchuelerRechnungsempfaengerValue = new JLabel();
        lblSchuelerRechnungsempfaengerValue.setText("SchuelerRechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblSchuelerRechnungsempfaengerValue, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer7, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        contentPane.add(panel4, BorderLayout.SOUTH);
        buttonUebernehmen = new JButton();
        buttonUebernehmen.setText("Eintrag übernehmen");
        buttonUebernehmen.setMnemonic('E');
        buttonUebernehmen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(buttonUebernehmen, gbc);
        buttonKorrigieren = new JButton();
        buttonKorrigieren.setText("Eingabe korrigieren");
        buttonKorrigieren.setMnemonic('K');
        buttonKorrigieren.setDisplayedMnemonicIndex(8);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(buttonKorrigieren, gbc);
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
