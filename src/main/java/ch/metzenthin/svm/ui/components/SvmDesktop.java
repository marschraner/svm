package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * SVM Applikation
 */
public class SvmDesktop extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private final static String TITLE_DEFAULT = "SVM Schüler Verwaltung Metzenthin";

    private final SvmContext svmContext;
    private SchuelerErfassenPanel schuelerErfassenPanel;

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

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
        menuSchueler.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menuSchueler);

        // Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Neuen Schüler erfassen");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        menuItem.setActionCommand("schuelerErfassen");
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
        if ("schuelerErfassen".equals(e.getActionCommand())) {
            schuelerErfassenPanel = new SchuelerErfassenPanel(svmContext);
            schuelerErfassenPanel.addCloseListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSchuelerErfassenFrameAbbrechen();
                }
            });
            getContentPane().add(schuelerErfassenPanel.$$$getRootComponent$$$());
            schuelerErfassenPanel.$$$getRootComponent$$$().setVisible(true);
            setTitle("Neuen Schüler erfassen");
            revalidate();
        } else { // beenden
            quit();
        }
    }

    private void onSchuelerErfassenFrameAbbrechen() {
        schuelerErfassenPanel.$$$getRootComponent$$$().setVisible(false);
        getContentPane().remove(schuelerErfassenPanel.$$$getRootComponent$$$());
        setTitle(TITLE_DEFAULT);
    }

    // Quit the application.
    private void quit() {
        System.exit(0);
    }

}