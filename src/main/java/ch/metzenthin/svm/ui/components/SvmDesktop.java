package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.domain.commands.CommandInvoker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * SVM Applikation
 */
public class SvmDesktop extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final static String TITLE_DEFAULT = "SVM Schüler Verwaltung Metzenthin";

    private final SvmContext svmContext;
    private JComponent activeComponent;

    public SvmDesktop(SvmContext svmContext) {
        super(TITLE_DEFAULT);
        this.svmContext = svmContext;

        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        // Set up the GUI.
        setJMenuBar(createMenuBar());

        // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // call quit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        setAndShowActivePanel(createSchuelerSuchenPanel().$$$getRootComponent$$$(), "Schüler suchen");

        // Display the window.
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Set up the lone menu.
        JMenu menuDatei = new JMenu("Datei");
        menuDatei.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menuDatei);

        JMenu menuSchueler = new JMenu("Schüler");
        menuSchueler.setMnemonic(KeyEvent.VK_C);
        menuBar.add(menuSchueler);

        // Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Neuen Schüler erfassen");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        menuItem.setActionCommand("schuelerErfassen");
        menuItem.addActionListener(this);
        menuSchueler.add(menuItem);

        menuItem = new JMenuItem("Schüler suchen");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
        menuItem.setActionCommand("schuelerSuchen");
        menuItem.addActionListener(this);
        menuSchueler.add(menuItem);

        menuItem = new JMenuItem("Monatsstatistik");
        menuItem.setMnemonic(KeyEvent.VK_M);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
        menuItem.setActionCommand("monatsstatistik");
        menuItem.addActionListener(this);
        menuSchueler.add(menuItem);

        // Set up the second menu item.
        menuItem = new JMenuItem("Beenden");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
        menuItem.setActionCommand("beenden");
        menuItem.addActionListener(this);
        menuDatei.add(menuItem);

        return menuBar;
    }

    // React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ((activeComponent != null) && !"beenden".equals(e.getActionCommand())) {
            CommandInvoker commandInvoker = svmContext.getCommandInvoker();
            commandInvoker.closeSession();
            activeComponent.setVisible(false);
        }

        if ("schuelerErfassen".equals(e.getActionCommand())) {
            SchuelerErfassenPanel schuelerErfassenPanel = new SchuelerErfassenPanel(svmContext);
            schuelerErfassenPanel.addCloseListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFrameAbbrechen();
                }
            });
            setAndShowActivePanel(schuelerErfassenPanel.$$$getRootComponent$$$(), "Neuen Schüler erfassen");

        } else if ("schuelerSuchen".equals(e.getActionCommand())) {
            setAndShowActivePanel(createSchuelerSuchenPanel().$$$getRootComponent$$$(), "Schüler suchen");

        } else if ("monatsstatistik".equals(e.getActionCommand())) {
            MonatsstatistikPanel anAbmeldestatistikPanel = new MonatsstatistikPanel(svmContext);
            anAbmeldestatistikPanel.addCloseListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onFrameAbbrechen();
                }
            });
            anAbmeldestatistikPanel.addNextPanelListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onNextPanelAvailable(e.getSource());
                }
            });
            setAndShowActivePanel(anAbmeldestatistikPanel.$$$getRootComponent$$$(), "Monatsstatistik");

        } else { // beenden
            quit();
        }
    }

    private SchuelerSuchenPanel createSchuelerSuchenPanel() {
        SchuelerSuchenPanel schuelerSuchenPanel = new SchuelerSuchenPanel(svmContext);
        schuelerSuchenPanel.addCloseListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onFrameAbbrechen();
            }
        });
        schuelerSuchenPanel.addNextPanelListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNextPanelAvailable(e.getSource());
            }
        });
        return schuelerSuchenPanel;
    }

    private void setAndShowActivePanel(JComponent component, String title) {
        activeComponent = component;
        getContentPane().add(activeComponent);
        activeComponent.setVisible(true);
        setTitle(title);
        revalidate();
    }

    private void onNextPanelAvailable(Object eventSource) {
        Object[] eventSourceArray = (Object[]) eventSource;
        JComponent nextComponent = (JComponent) eventSourceArray[0];
        activeComponent.setVisible(false);
        getContentPane().remove(activeComponent);
        setAndShowActivePanel(nextComponent, (String) eventSourceArray[1]);
    }

    private void onFrameAbbrechen() {
        CommandInvoker commandInvoker = svmContext.getCommandInvoker();
        commandInvoker.closeSession();
        activeComponent.setVisible(false);
        getContentPane().remove(activeComponent);
        activeComponent = null;
        setTitle(TITLE_DEFAULT);
    }

    // Quit the application.
    private void quit() {
        int n = 0; //default button title
        if (activeComponent != null) {
            Object[] options = {"Ja", "Nein"};
            n = JOptionPane.showOptionDialog(
                    this,
                    "Durch Drücken des Ja-Buttons wird die Applikation beendet. Allfällig erfasste Daten gehen verloren.",
                    "Soll die Applikation beendet werden?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //do not use a custom Icon
                    options,  //the titles of buttons
                    options[0]);
        }
        if (n == 0) {
            System.exit(0);
        }
    }

}