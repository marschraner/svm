package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.dataTypes.Anrede;
import ch.metzenthin.svm.domain.model.AngehoerigerEinEintragGleicherNameAndereAttributeResult;
import ch.metzenthin.svm.domain.model.SchuelerErfassenModel;
import ch.metzenthin.svm.persistence.entities.Angehoeriger;
import ch.metzenthin.svm.persistence.entities.Schueler;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class AngehoerigerEinEintragGleicherNameAndereAttributeDialog extends SchuelerErfassenDialog {
    
    private final AngehoerigerEinEintragGleicherNameAndereAttributeResult angehoerigerEinEintragGleicherNameAndereAttributeResult;
    private final SchuelerErfassenModel schuelerErfassenModel;
    private JPanel contentPane;
    private JButton buttonDbUebernehmen;
    private JButton buttonAbbrechen;
    private JButton buttonNeuErfasstenEintragVerwenden;
    private JLabel lblBeschreibung;
    private JLabel lblAngehoeriger;
    private JLabel lblKinder;
    private JLabel lblKinderValue;
    private JLabel lblAngehoerigerValue;
    private JLabel lblSchuelerRechnungsempfaenger1;
    private JLabel lblSchuelerRechnungsempfaenger2;
    private JLabel lblSchuelerRechnungsempfaengerValue;

    public AngehoerigerEinEintragGleicherNameAndereAttributeDialog(
            AngehoerigerEinEintragGleicherNameAndereAttributeResult angehoerigerEinEintragGleicherNameAndereAttributeResult,
            SchuelerErfassenModel schuelerErfassenModel
    ) {
        this.angehoerigerEinEintragGleicherNameAndereAttributeResult = angehoerigerEinEintragGleicherNameAndereAttributeResult;
        this.schuelerErfassenModel = schuelerErfassenModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonNeuErfasstenEintragVerwenden);

        setTitle("Teilweise übereinstimmender Datenbankeintrag für " + angehoerigerEinEintragGleicherNameAndereAttributeResult.getAngehoerigenArt());

        lblBeschreibung.setText("In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben für " + angehoerigerEinEintragGleicherNameAndereAttributeResult.getAngehoerigenArt());
        setLabels(angehoerigerEinEintragGleicherNameAndereAttributeResult.getAngehoerigerFoundInDatabase());

        buttonDbUebernehmen.addActionListener(e -> onDbUebernehmen());

        buttonNeuErfasstenEintragVerwenden.addActionListener(e -> onNeuErfasstenEintragVerwenden());

        buttonAbbrechen.addActionListener(e -> onAbbrechen());

        // call onAbbrechen() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAbbrechen();
            }
        });

        // call onAbbrechen() on ESCAPE
        contentPane.registerKeyboardAction(e -> onAbbrechen(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @SuppressWarnings("DuplicatedCode")
    private void setLabels(Angehoeriger angehoeriger) {
        lblBeschreibung.setText("In der Datenbank wurde ein Eintrag gefunden, der mit den erfassten Angaben für " + angehoerigerEinEintragGleicherNameAndereAttributeResult.getAngehoerigenArt() + " teilsweise übereinstimmt:");
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
        }
    }

    private void onDbUebernehmen() {
        setResult(schuelerErfassenModel.proceedUebernehmen(angehoerigerEinEintragGleicherNameAndereAttributeResult));
        dispose();
    }

    private void onNeuErfasstenEintragVerwenden() {
        setResult(schuelerErfassenModel.proceedWeiterfahren(angehoerigerEinEintragGleicherNameAndereAttributeResult));
        dispose();
    }

    private void onAbbrechen() {
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
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel1.add(panel2, gbc);
        panel2.setBorder(BorderFactory.createTitledBorder(null, "Teilweise übereinstimmender Datenbankeintag", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, Font.BOLD, -1, panel2.getFont()), new Color(-16777216)));
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer3, gbc);
        lblAngehoeriger = new JLabel();
        lblAngehoeriger.setText("Angehoeriger:\n");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAngehoeriger, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer4, gbc);
        lblAngehoerigerValue = new JLabel();
        lblAngehoerigerValue.setText("lblAngehoerigerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblAngehoerigerValue, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(spacer5, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        lblBeschreibung = new JLabel();
        lblBeschreibung.setText("lblBeschreibung");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(lblBeschreibung, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer6, gbc);
        lblKinder = new JLabel();
        lblKinder.setText("Erfasste Kinder:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(lblKinder, gbc);
        lblKinderValue = new JLabel();
        lblKinderValue.setText("lblKinderValue");
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
        panel2.add(lblSchuelerRechnungsempfaenger1, gbc);
        lblSchuelerRechnungsempfaenger2 = new JLabel();
        lblSchuelerRechnungsempfaenger2.setText("Rechnungsempfänger:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 10);
        panel2.add(lblSchuelerRechnungsempfaenger2, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel2.add(spacer7, gbc);
        lblSchuelerRechnungsempfaengerValue = new JLabel();
        lblSchuelerRechnungsempfaengerValue.setText("lblSchuelerRechnungsempfaengerValue");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(lblSchuelerRechnungsempfaengerValue, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        contentPane.add(panel4, BorderLayout.SOUTH);
        buttonDbUebernehmen = new JButton();
        buttonDbUebernehmen.setMaximumSize(new Dimension(288, 29));
        buttonDbUebernehmen.setMinimumSize(new Dimension(288, 29));
        buttonDbUebernehmen.setPreferredSize(new Dimension(288, 29));
        buttonDbUebernehmen.setText("Eintrag aus Datenbank übernehmen");
        buttonDbUebernehmen.setMnemonic('E');
        buttonDbUebernehmen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(buttonDbUebernehmen, gbc);
        buttonAbbrechen = new JButton();
        buttonAbbrechen.setMaximumSize(new Dimension(114, 29));
        buttonAbbrechen.setMinimumSize(new Dimension(114, 29));
        buttonAbbrechen.setPreferredSize(new Dimension(114, 29));
        buttonAbbrechen.setText("Abbrechen");
        buttonAbbrechen.setMnemonic('A');
        buttonAbbrechen.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(buttonAbbrechen, gbc);
        buttonNeuErfasstenEintragVerwenden = new JButton();
        buttonNeuErfasstenEintragVerwenden.setMaximumSize(new Dimension(288, 29));
        buttonNeuErfasstenEintragVerwenden.setMinimumSize(new Dimension(288, 29));
        buttonNeuErfasstenEintragVerwenden.setPreferredSize(new Dimension(288, 29));
        buttonNeuErfasstenEintragVerwenden.setSelected(true);
        buttonNeuErfasstenEintragVerwenden.setText("Neu erfassten Eintrag verwenden");
        buttonNeuErfasstenEintragVerwenden.setMnemonic('N');
        buttonNeuErfasstenEintragVerwenden.setDisplayedMnemonicIndex(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel4.add(buttonNeuErfasstenEintragVerwenden, gbc);
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
